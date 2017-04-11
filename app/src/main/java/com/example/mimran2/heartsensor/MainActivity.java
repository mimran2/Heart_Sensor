package com.example.mimran2.heartsensor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener  {
    TextView box;
    Button b;
    int permissionCheck;
    Context thisActivity;
    SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         box=(TextView)findViewById(R.id.hr);
         b = (Button) findViewById(R.id.button);
        permissionCheck= ContextCompat.checkSelfPermission(this,
                Manifest.permission.BODY_SENSORS);

        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getBeat();
            }
        });
        thisActivity=getApplicationContext();






        //check for permissions
        int MY_PERMISSIONS_REQUEST_BODY_SENSORS= 1;
        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BODY_SENSORS},
                        MY_PERMISSIONS_REQUEST_BODY_SENSORS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }




    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            String msg = "" + (int) event.values[0];
            box.setText(msg);
            // Log.d(TAG, msg);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //dont really need to do anything
    }
//    SensorEventListener sEventListener=   new SensorEventListener() {
//        public void onSensorChanged(SensorEvent event) {
//            if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
//                String msg = "" + (int) event.values[0];
//                box.setText(msg);
//                // Log.d(TAG, msg);
//            }
//        }
//
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int accuracy) {
//            //dont really need to do anything
//        }
//    };

    private void getBeat() {
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        Sensor mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        boolean reg=mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //Log.d("Sensor Status:", " Sensor registered: " + (reg ? "yes" : "no"));

        //box.setText();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //stop measure after 30 seconds
                String beat=(box.getText().toString());
                box.setText(beat);
                stopMeasure();

            }

        }, 35000L);
    }
    private void stopMeasure() {
        mSensorManager.unregisterListener(this);
    }
}
