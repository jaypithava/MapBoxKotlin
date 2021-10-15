package com.c.mapboxkotlin

import android.Manifest
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.annotations.PolygonOptions
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import com.vmadalin.easypermissions.EasyPermissions
import androidx.core.app.ActivityCompat.requestPermissions
import android.content.pm.PackageManager
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    companion object{
        private const val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            checkMultiplePermissions();
        }

        val mapView = findViewById<MapView>(R.id.mapView)


        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { mapboxMap ->

            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
            mapboxMap.cameraPosition.zoom
                val polygonLatLngList = ArrayList<LatLng>()

                polygonLatLngList.add(LatLng(45.522585, -122.685699))
                polygonLatLngList.add(LatLng(45.534611, -122.708873))
                polygonLatLngList.add(LatLng(45.530883, -122.678833))
                polygonLatLngList.add(LatLng(45.547115, -122.667503))
                polygonLatLngList.add(LatLng(45.530643, -122.660121))
                polygonLatLngList.add(LatLng(45.533529, -122.636260))
                polygonLatLngList.add(LatLng(45.521743, -122.659091))
                polygonLatLngList.add(LatLng(45.510677, -122.648792))
                polygonLatLngList.add(LatLng(45.515008, -122.664070))
                polygonLatLngList.add(LatLng(45.502496, -122.669048))
                polygonLatLngList.add(LatLng(45.515369, -122.678489))
                polygonLatLngList.add(LatLng(45.506346, -122.702007))
                polygonLatLngList.add(LatLng(45.522585, -122.685699))

                mapboxMap.addPolygon(
                    PolygonOptions()
                    .addAll(polygonLatLngList)
                    .fillColor(Color.parseColor("#3bb2d0")))

            }
        }
    }
    private fun checkMultiplePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val permissionsNeeded: MutableList<String> = ArrayList()
            val permissionsList: List<String> = ArrayList()
            if (!addPermission(permissionsList as MutableList<String>, Manifest.permission.ACCESS_FINE_LOCATION)) {
                permissionsNeeded.add("GPS")
            }
            if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE)) {
                permissionsNeeded.add("READ PHONE STATE")
            }
            if (permissionsList.isNotEmpty()) {
                requestPermissions(
                    permissionsList.toTypedArray(),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
                )
                return
            }
        }
    }

    private fun addPermission(permissionsList: MutableList<String>, permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission)

            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission)) return false
        }
        return true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                val perms: MutableMap<String, Int> = HashMap()
                // Initial
                perms[Manifest.permission.ACCESS_FINE_LOCATION] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.READ_PHONE_STATE] = PackageManager.PERMISSION_GRANTED

                // Fill with results
                var i = 0
                while (i < permissions.size) {
                    perms[permissions[i]] = grantResults[i]
                    i++
                }
                if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED
                    && perms[Manifest.permission.READ_PHONE_STATE] == PackageManager.PERMISSION_GRANTED
                ) {
                    // All Permissions Granted
                    return
                } else {
                    // Permission Denied
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        Toast.makeText(
                            applicationContext,
                            """
                            My App cannot run without Location and Read Phone State Permissions.
                            Relaunch My App or allow permissions in Applications Settings
                            """.trimIndent(),
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}