package ie.setu.placemark.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import ie.setu.placemark.R
import ie.setu.placemark.databinding.ActivityPlacemarkBinding
import ie.setu.placemark.main.MainApp
import ie.setu.placemark.models.PlacemarkModel
import timber.log.Timber.i

class PlacemarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlacemarkBinding
    var placemark = PlacemarkModel()
    //val placemarks = ArrayList<PlacemarkModel>()
    lateinit var app: MainApp
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlacemarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("Placemark Activity started...")

        if (intent.hasExtra("placemark_edit")) {
            edit = true
            placemark = intent.extras?.getParcelable("placemark_edit")!!
            binding.placemarkTitle.setText(placemark.title)
            binding.description.setText(placemark.description)
            binding.btnAdd.setText(R.string.button_savePlacemark)
        }

        binding.btnAdd.setOnClickListener() {
            placemark.title = binding.placemarkTitle.text.toString()
            placemark.description = binding.description.text.toString()
            if (placemark.title.isEmpty()) {
                Snackbar.make(it,R.string.snackbar_Text, Snackbar.LENGTH_LONG)
                    .show()

            } else {
                if (edit) {
                    app.placemarks.update(placemark.copy())
                    i("edit Button Pressed: $placemark")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    app.placemarks.create(placemark.copy())
                    i("add Button Pressed: $placemark")
                    setResult(RESULT_OK)
                    finish()
                }
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}