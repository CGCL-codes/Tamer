package com.sen.menu;

import com.sen.imageviewer.R;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class CropImageTask extends AsyncTask<Context, Integer, Integer> {

    ImageCropper imageCropper;

    @Override
    protected Integer doInBackground(Context... arg0) {
        imageCropper = (ImageCropper) arg0[0];
        imageCropper.cropImageAndSet();
        return null;
    }

    @Override
    protected void onPostExecute(Integer result) {
        imageCropper.dismissDialog(ImageCropper.CROPPING_IMAGE_DIALOG);
        switch(imageCropper.imageType) {
            case ImageCropper.TYPE_WALLPAPER:
                Toast.makeText(imageCropper, R.string.set_as_wallpaper, 1000).show();
                break;
            case ImageCropper.TYPE_CONTACT_PICTURE:
                Toast.makeText(imageCropper, R.string.set_as_contact_picture, 1000).show();
                break;
        }
        imageCropper.finish();
        super.onPostExecute(result);
    }
}
