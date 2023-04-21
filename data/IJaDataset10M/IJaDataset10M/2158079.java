package com.xiaolei.android.ui;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.xiaolei.android.BizTracker.R;
import com.xiaolei.android.listener.OnCameraTakedPhotoListener;

/**
 * @author xiaolei
 * 
 */
public class CameraSupportableActivity extends Activity {

    private Uri imageUri;

    private String fileName = "";

    public final int REQUEST_CODE = 1024;

    private OnCameraTakedPhotoListener onCameraTakedPhotoListener;

    protected String photoPath = "";

    protected String packageName = "BizTracker";

    protected Boolean photoPathExists = false;

    public void AddCameraTakedPhotoListener(OnCameraTakedPhotoListener listener) {
        if (listener != null) {
            onCameraTakedPhotoListener = listener;
        }
    }

    public void RemoveCameraTakedPhotoListener() {
        onCameraTakedPhotoListener = null;
    }

    public CameraSupportableActivity() {
        super();
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            photoPath = Environment.getExternalStorageDirectory() + "/" + packageName + "/photo";
            File folder = new File(photoPath);
            if (!folder.exists()) {
                photoPathExists = folder.mkdirs();
                if (photoPathExists == false) {
                    Toast.makeText(this, getString(R.string.create_photo_folder_failed), Toast.LENGTH_SHORT).show();
                }
            } else {
                photoPathExists = true;
            }
        }
    }

    protected void OnCameraTakedPhoto(String fileName, Exception error) {
        if (onCameraTakedPhotoListener != null) {
            onCameraTakedPhotoListener.OnTakedPhoto(fileName, error);
        }
    }

    public void takePhoto(String preferredFileName) {
        if (TextUtils.isEmpty(photoPath)) {
            OnCameraTakedPhoto(null, new IllegalArgumentException("External storage is not available."));
        }
        if (TextUtils.isEmpty(preferredFileName)) {
            throw new IllegalArgumentException("preferredFileName cannot be empty.");
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        String shortFileName = preferredFileName + ".jpg";
        fileName = photoPath + File.separator + shortFileName;
        File photo = new File(photoPath, shortFileName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getContentResolver().notifyChange(selectedImage, null);
                    OnCameraTakedPhoto(fileName, null);
                } else {
                    fileName = "";
                }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("photoFileName", fileName);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i("DEBUG", "onRestoreInstanceState(): " + fileName);
        fileName = savedInstanceState.getString("photoFileName");
        OnCameraTakedPhoto(fileName, null);
    }
}
