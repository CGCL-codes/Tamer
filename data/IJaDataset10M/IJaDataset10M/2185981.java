package org.magicdroid.app.ui.fremdcode;

import org.magicdroid.app.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoFormActivity extends Activity implements OnClickListener, Runnable {

    private final String TAG = "reportForm";

    private ProgressDialog progressDialog = null;

    private String currentPhotoPath = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPhotoPath = this.getIntent().getStringExtra("photofile");
        setContentView(R.layout.reportphotoform);
        ((Button) findViewById(R.id.btnTakePicture)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnReportFormSubmit)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnReportFormCancel)).setOnClickListener(this);
        if (currentPhotoPath != null) {
            Toast.makeText(getBaseContext(), "Ready to send photo: " + currentPhotoPath, Toast.LENGTH_LONG).show();
            ((ImageView) findViewById(R.id.previewphoto)).setImageURI(Uri.parse(currentPhotoPath));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnReportFormSubmit) {
            progressDialog = ProgressDialog.show(PhotoFormActivity.this, "Submitting Report", "Please wait...", true, true);
            Handler handler = new Handler();
            handler.postDelayed(this, 1000);
        } else if (v.getId() == R.id.btnReportFormCancel) {
            showMain();
        } else if (v.getId() == R.id.btnTakePicture) {
            takePicture();
        }
    }

    public void run() {
        submitForm();
    }

    private void takePicture() {
        Intent iMain = new Intent(this, ImageCaptureActivity.class);
        startActivity(iMain);
    }

    private void submitForm() {
        if (currentPhotoPath == null) {
            Toast.makeText(getBaseContext(), "Please take a picture first!", Toast.LENGTH_LONG).show();
            if (progressDialog != null) progressDialog.dismiss();
        } else {
            try {
                Log.i(TAG, "submitting form...");
                String caption = ((TextView) findViewById(R.id.entryCaption)).getText().toString();
                String photoPath = currentPhotoPath;
                if (progressDialog != null) progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showMain() {
    }
}
