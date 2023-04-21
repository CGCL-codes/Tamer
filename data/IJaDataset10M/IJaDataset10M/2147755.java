package com.squareshoot.picCheckin;

import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class FullscreenPicture extends Activity {

    private String uri;

    private Bitmap bm = null;

    private GetPhotoTask photoTask;

    private ImageView maVue;

    /**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Common.HIGHDEBUG) Log.v(Common.TAG, "FullScreenPicture : OnCreate");
        uri = (String) getIntent().getExtras().get("uri");
        if (Common.HIGHDEBUG) Log.v(Common.TAG, "ShowPicture : uri : " + uri);
        setContentView(R.layout.fullscreenpicture);
        maVue = (ImageView) findViewById(R.id.showvenuepicture);
        if (savedInstanceState == null) {
            if (uri != null) {
                photoTask = new GetPhotoTask();
                photoTask.setActivity(this);
                photoTask.execute(uri);
            }
        } else {
            bm = savedInstanceState.getParcelable("photo");
            if (bm != null) {
                maVue.setImageBitmap(bm);
                findViewById(R.id.fullscreenLoadingLinear).setVisibility(View.GONE);
            } else {
                if (getLastNonConfigurationInstance() != null) {
                    if (Common.HIGHDEBUG) Log.w(Common.TAG, "getting photoTask");
                    photoTask = (GetPhotoTask) getLastNonConfigurationInstance();
                    photoTask.setActivity(this);
                }
            }
        }
        maVue.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(uri));
                startActivity(i);
            }
        });
    }

    public Object onRetainNonConfigurationInstance() {
        if (photoTask != null) {
            photoTask.setActivity(null);
        }
        return photoTask;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("photo", bm);
    }

    private void onThreadCompleted(Bitmap bitmap, Bundle data) {
        findViewById(R.id.fullscreenLoadingLinear).setVisibility(View.GONE);
        if (bitmap != null) {
            this.bm = bitmap;
            maVue.setImageBitmap(bm);
        } else {
            Toast erreurToast = Toast.makeText(FullscreenPicture.this, data.getString("eMessage"), Toast.LENGTH_LONG);
            erreurToast.setGravity(Gravity.CENTER, 0, 0);
            erreurToast.show();
            finish();
        }
    }

    private class GetPhotoTask extends AsyncTask<String, String, Bitmap> {

        private FullscreenPicture activity;

        private boolean completed = false;

        private Bitmap bitmap = null;

        Bundle data = new Bundle();

        protected Bitmap doInBackground(String... parameters) {
            String url = parameters[0];
            try {
                bitmap = Common.downloadImage(url);
            } catch (IOException e) {
                if (Common.HIGHDEBUG) Log.d(Common.TAG, "ShowVenuePicture: " + e.getMessage());
                data.putString("eMessage", e.getMessage());
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            completed = true;
            if (activity != null) endTask();
        }

        private void setActivity(FullscreenPicture activity) {
            this.activity = activity;
            if (completed) {
                endTask();
            }
        }

        private void endTask() {
            activity.photoTask = null;
            activity.onThreadCompleted(bitmap, data);
        }
    }
}
