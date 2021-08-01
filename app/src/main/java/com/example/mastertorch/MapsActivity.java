package com.example.mastertorch;



// Tải các thành phần cần cho phần mềm
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



// TODO Chỉnh lại theo tên của ứng dụng đang viết
// Xem trong build.gradle (Module): applicationId "com.example.mastertorch"
// import com.example.googlemap.databinding.ActivityMapsBinding;
import com.example.mastertorch.databinding.ActivityMapsBinding;

import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;



// Tập hợp các lệnh để chạy phần mềm
public class MapsActivity

        // Nạp các yếu tố cần thiết cho class
        extends FragmentActivity
        implements OnMapReadyCallback {

        // Định nghĩa GoogleMap
        private GoogleMap mMap;
        private ActivityMapsBinding binding;

        // Định nghĩa FusedLocationProviderClient
        FusedLocationProviderClient fusedLocationClient;

        // Định nghĩa các địa điểm String cordinates
        String[] cordinates=
                {
                "-34, 151",
                "-31.083332, 150.916672",
                "-32.916668, 151.750000",
                "-27.470125, 153.021072"
                };



        // Thiết lập method: onCreate
        @Override
        protected void onCreate (Bundle savedInstanceState) {

            // Định nghĩa onCreate
            super.onCreate(savedInstanceState);

            // Định nghĩa binding
            binding = ActivityMapsBinding.inflate(getLayoutInflater());

            // Định nghĩa setContentView
            setContentView(binding.getRoot());

            // Định nghĩa SupportMapFragment
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment)
                    getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            if (mapFragment != null)
                {
                mapFragment.getMapAsync(this);
                }

        } // Kết thúc method: onCreate



        /**
        * Manipulates the map once available.
        * This callback is triggered when the map is ready to be used.
        * This is where we can add markers or lines, add listeners or move the camera. In this case,
        * we just add a marker near Sydney, Australia.
        * If Google Play services is not installed on the device, the user will be prompted to install
        * it inside the SupportMapFragment. This method will only be triggered once the user has
        * installed Google Play services and returned to the app.
        */



        // Định nghĩa method: onMapReady
        @Override
        public void onMapReady(GoogleMap googleMap) {

            // Định nghĩa mMap
            mMap = googleMap;

            // Kiểm tra location permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                    checkPermission();
                    }

            // Định nghĩa setMyLocationEnabled
            mMap.setMyLocationEnabled(true);

            // Định nghĩa getFusedLocationProviderClient để định vị bằng GPS
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());



            // Định nghĩa và chạy getLastLocation
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {



                        // Định nghĩa method: onSuccess
                        @Override
                        public void onSuccess(Location location) {
                            // Tìm kiếm định vị bằng GPS
                            // If 1
                            if (location != null)

                                // If 2
                                { if (mMap != null)
                                    { for (int i = 0; i < cordinates.length; i++)
                                        { String[] result=cordinates[i].split(",");
                                        System.out.println("Lat: "+result[0]);
                                        System.out.println("Lng: "+result[1]);

                                        String mLocation=getLocationName(Double.parseDouble(result[0]),
                                                Double.parseDouble(result[1]));

                                        LatLng mylocation = new LatLng(Double.parseDouble(result[0]),
                                                Double.parseDouble(result[1]));

                                        // Add a marker in Sydney and move the camera
                                        // LatLng sydney = new LatLng(-34, 151);
                                        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                                        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                        mMap.addMarker(new MarkerOptions().position(mylocation).title(mLocation));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));
                                        }
                                    } else {
                                            Toast.makeText(getApplicationContext(), "Map Not Ready", Toast.LENGTH_LONG).show();
                                            } // Kết thúc If 2

                                Toast.makeText(getApplicationContext(), "Longitude: " + location.getLongitude() + " Latitude: " + location.getLatitude(), Toast.LENGTH_LONG).show();
                                } else {
                                        Toast.makeText(getApplicationContext(), "No Location", Toast.LENGTH_LONG).show();
                                        } // Kết thúc If 1
                        } // Kết thúc method: onSuccess
                    }); // Kết thúc Chạy getLastLocation
        } // Kết thúc method: onMapReady



        // Định nghĩa method: checkPermission
        public void checkPermission(){

                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getApplicationContext(),
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        {
                        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},123);
                        }
        } // Kết thúc method: checkPermission



        // Định nghĩa method: getLocationName
        public String getLocationName(Double latitude,Double longitude) {

            // Nạp các yếu tố cho method
            String cityName;
            String stateName;
            String countryName;

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException e)
                        {
                        e.printStackTrace();
                        }

            if (!addresses.isEmpty())
                {
                cityName = addresses.get(0).getAddressLine(0);
                stateName = addresses.get(0).getAddressLine(1);
                countryName = addresses.get(0).getAddressLine(2);
                } else {
                        cityName="Marker";
                        }

            return cityName;
        } // Kết thúc method: getLocationName



} // Kết thúc các lệnh để chạy phần mềm