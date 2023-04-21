package jp.co.cia.feelsketchbrowser;

import java.io.OutputStream;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class FeelSketchBrowser extends Activity {

    static final String TAG = "FeelSketchBrowser";

    private FinderView mFinderView;

    private Preview mPreview;

    private OverlayView mOverlayView;

    private ProgressDialog mProgressDialog;

    private Button mShutterButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveIconFile();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        mFinderView = (FinderView) findViewById(R.id.viewfinder_view);
        mPreview = (Preview) findViewById(R.id.preview_surface);
        mPreview.setFinder(mFinderView);
        mOverlayView = new OverlayView(this);
        addContentView(mOverlayView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mPreview.setOverlayView(mOverlayView);
        mShutterButton = (Button) findViewById(R.id.shutter_button);
        mShutterButton.setOnClickListener(mButtonListener);
        mShutterButton.requestFocus();
    }

    void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Decoding PMCode.");
        mProgressDialog.show();
    }

    void stopProgressDialog() {
        mProgressDialog.dismiss();
        mProgressDialog = null;
    }

    void hideShutterButton() {
        mShutterButton.setVisibility(View.INVISIBLE);
    }

    void requestAutoFocus() {
        mPreview.requestAutoFocus();
    }

    void requestPreview(boolean isCallback) {
        mPreview.requestPreview(isCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPreview.closeCamera();
    }

    void showIncorrectDialog(int code) {
        showDialog(code);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case 0:
                return new AlertDialog.Builder(FeelSketchBrowser.this).setIcon(R.drawable.alert_dialog_icon).setTitle("Decode Error").setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        mPreview.requestPreview(false);
                    }
                }).create();
        }
        return null;
    }

    private View.OnClickListener mButtonListener = new View.OnClickListener() {

        public void onClick(View v) {
            requestAutoFocus();
        }
    };

    private void saveIconFile() {
        try {
            int iconID[] = { R.drawable.icon0, R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5, R.drawable.icon6, R.drawable.icon7, R.drawable.icon8, R.drawable.icon9, R.drawable.icon10, R.drawable.icon11, R.drawable.icon12, R.drawable.icon13, R.drawable.icon14, R.drawable.icon15 };
            for (int i = 0; i < 16; i++) {
                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), iconID[i]);
                int pixels[] = new int[100 * 100];
                bitmap.getPixels(pixels, 0, 100, 0, 0, 100, 100);
                byte imageData[] = new byte[100 * 100 * 3];
                for (int j = 0; j < pixels.length; j++) {
                    if (pixels[j] == 0) {
                        imageData[3 * j] = 1;
                        imageData[3 * j + 1] = 1;
                        imageData[3 * j + 2] = 1;
                    } else {
                        imageData[3 * j] = (byte) (pixels[j] & 0xFF);
                        imageData[3 * j + 1] = (byte) (pixels[j] >> 8 & 0xFF);
                        imageData[3 * j + 2] = (byte) (pixels[j] >> 16 & 0xFF);
                    }
                }
                OutputStream out = this.openFileOutput(String.valueOf(i), Context.MODE_WORLD_READABLE);
                out.write(imageData);
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
