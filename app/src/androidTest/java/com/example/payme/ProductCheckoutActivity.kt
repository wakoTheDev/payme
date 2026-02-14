package com.example.payme



import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton


class ProductCheckoutActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var totalTextView: TextView
    private lateinit var checkoutButton: Button

    // Shopping cart to track selected products and quantities
    private val cart = mutableMapOf<Product, Int>()

    // Sample product list
    private val products = listOf(
        Product(1, "Laptop", 899.99),
        Product(2, "Wireless Mouse", 29.99),
        Product(3, "Keyboard", 79.99),
        Product(4, "Monitor", 299.99),
        Product(5, "USB Cable", 9.99),
        Product(6, "Headphones", 149.99),
        Product(7, "Webcam", 89.99),
        Product(8, "Phone Case", 19.99),
        Product(9, "Power Bank", 39.99),
        Product(10, "SD Card 128GB", 24.99)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_checkout)

        initializeViews()
        setupRecyclerView()
        setupCheckoutButton()
        updateTotal()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewProducts)
        totalTextView = findViewById(R.id.textViewTotal)
        checkoutButton = findViewById(R.id.buttonCheckout)
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(products) { product ->
            addToCart(product)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProductCheckoutActivity)
            adapter = productAdapter
        }
    }

    private fun setupCheckoutButton() {
        checkoutButton.setOnClickListener {
            if (cart.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
            } else {
                processCheckout()
            }
        }
    }

    private fun addToCart(product: Product) {
        val currentQuantity = cart[product] ?: 0
        cart[product] = currentQuantity + 1

        Toast.makeText(
            this,
            "${product.name} added to cart",
            Toast.LENGTH_SHORT
        ).show()

        updateTotal()
    }

    private fun updateTotal() {
        val total = cart.entries.sumOf { (product, quantity) ->
            product.price * quantity
        }

        totalTextView.text = String.format("Total: $%.2f", total)
    }

    private fun processCheckout() {
        val total = cart.entries.sumOf { (product, quantity) ->
            product.price * quantity
        }

        val itemCount = cart.values.sum()

        Toast.makeText(
            this,
            "Checkout successful! Total: $${String.format("%.2f", total)} for $itemCount items",
            Toast.LENGTH_LONG
        ).show()

        // Clear cart after checkout
        cart.clear()
        updateTotal()
    }
}


data class Product(
    val id: Int,
    val name: String,
    val price: Double
)


class ProductAdapter(
    private val products: List<Product>,
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.textViewProductName)
        val priceTextView: TextView = view.findViewById(R.id.textViewProductPrice)
        val addButton: ImageButton = view.findViewById(R.id.buttonAddProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.nameTextView.text = product.name
        holder.priceTextView.text = String.format("$%.2f", product.price)

        holder.addButton.setOnClickListener {
            onProductClick(product)
        }

        // Optional: Make the whole item clickable
        holder.itemView.setOnClickListener {
            onProductClick(product)
        }
    }

    override fun getItemCount() = products.size
}