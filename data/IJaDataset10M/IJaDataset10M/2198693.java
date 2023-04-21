package activities;

import java.io.File;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

public class PictureCaptureActivity extends Activity {

    private ProgressDialog progressDialog;

    private static final int IMAGE_CAPTURE = 0;

    private Uri imageUri;

    private ImageView imageView;

    /**
	 * Called when the activity is first created. sets the content and gets the
	 * references to the basic widgets on the screen like {@code Button} or
	 * {@link ImageView}
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startCamera();
    }

    public void startCamera() {
        Log.d("ANDRO_CAMERA", "Starting camera on the phone...");
        String fileName = Environment.getExternalStorageDirectory() + MainActivity.FILE_PATH;
        File imageFile = new File(fileName);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        imageUri = Uri.fromFile(imageFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, IMAGE_CAPTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Log.d("ANDRO_CAMERA", "Picture taken!!!");
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }
}
