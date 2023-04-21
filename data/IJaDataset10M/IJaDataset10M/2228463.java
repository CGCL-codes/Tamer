package com.android.wheretogo.activity;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.android.wheretogo.adapter.DbAdapter;
import com.android.wheretogo.constant.Constant;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

public class ActivityTakepic extends Activity {

    private ImageView imageView = null;

    private String photoPath = Environment.getExternalStorageDirectory() + "/WhereToGO" + "/Camera";

    private String photoName = null;

    private String photoFullPath = null;

    private DbAdapter dbAdapter = null;

    private String photoCreatedTime = null;

    private SharedPreferences gps = null;

    private String lati = null;

    private String longi = null;

    private int picId = 0;

    public void onCreate(Bundle savedInstanceState) {
        Log.v("pic1", "inonCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameraimageshow);
        File cameraPath = new File(photoPath);
        if (!cameraPath.exists()) {
            cameraPath.mkdirs();
        }
        dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        this.photoName = this.getPhotoName();
        this.photoFullPath = this.photoPath + "/" + this.photoName;
        File f = new File(this.photoPath, this.photoName);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent it) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                this.getLongi();
                this.getLati();
                picId = (int) dbAdapter.insertPicture(this.photoFullPath, Integer.parseInt(Constant.useId), this.longi, this.lati, this.photoCreatedTime);
                Intent intent = new Intent(this, ActivityCameraImageShow.class);
                intent.putExtra("picId", picId);
                startActivity(intent);
            }
        }
        super.onActivityResult(requestCode, resultCode, it);
        this.finish();
    }

    private String getPhotoName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        this.photoCreatedTime = dateFormat2.format(date);
        return Constant.useId + "@" + System.currentTimeMillis() + ".jpg";
    }

    public void getLati() {
        gps = getSharedPreferences("gps", 0);
        if (gps != null) {
            String temp = gps.getString("lati", null);
            if (temp != null) {
                lati = temp.substring(0, temp.length() - 2) + "50";
            }
        }
    }

    public void getLongi() {
        gps = getSharedPreferences("gps", 0);
        if (gps != null) {
            String temp = gps.getString("longi", null);
            if (temp != null) {
                longi = temp.substring(0, temp.length() - 2) + "50";
            }
        }
    }

    public void onStart() {
        Log.v("pic1", "takepic activity has been onStart");
        super.onStart();
    }

    public void onStop() {
        Log.v("pic1", "takepic activity has been onStop");
        super.onStop();
    }

    public void onPause() {
        Log.v("pic1", "takepic activity has been onPause");
        super.onPause();
    }

    public void onRestart() {
        Log.v("pic1", "takepic activity has been onRestart");
        super.onRestart();
    }

    public void onResume() {
        Log.v("pic1", "takepic activity has been onResume");
        super.onResume();
    }

    public void onDestroy() {
        Log.v("pic1", "takepic activity has been destroyed");
        dbAdapter.close();
        super.onDestroy();
    }
}
