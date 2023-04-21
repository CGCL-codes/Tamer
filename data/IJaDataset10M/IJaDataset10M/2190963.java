package it.mobistego;

import it.mobistego.alg.LSB2bit;
import it.mobistego.dialog.MobiProgressBar;
import it.mobistego.handler.ProgressHandler;
import it.mobistego.mms.MmsIntent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author Paolo Manzo
 * 
 */
public class EncodeActivity extends Activity {

    private Context context;

    private final Handler handler = new Handler();

    private Bitmap sourceBitmap;

    public static final int PICK_IMAGE = 1;

    public static final int PICK_CONTACT = 2;

    public static final int MESSAGE = 3;

    public static String BYTEIMAGE = "BYTEIMAGE";

    public static String TEXT = "TEXT";

    private String absoluteFilePathSource;

    private MobiProgressBar progressBar;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encoder);
        context = this;
        Button buttonSelectImage = (Button) findViewById(R.id.ButtonSelect);
        buttonSelectImage.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
            }
        });
        Button buttonSend = (Button) findViewById(R.id.send);
        buttonSend.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                closeContextMenu();
                closeOptionsMenu();
                progressBar = new MobiProgressBar(EncodeActivity.this);
                progressBar.setMax(100);
                progressBar.setMessage(context.getString(R.string.encoding));
                progressBar.show();
                Thread tt = new Thread(new Runnable() {

                    public void run() {
                        Uri uri = encode();
                        MmsIntent mms = new MmsIntent(uri, EncodeActivity.this);
                        progressBar.dismiss();
                        mms.send();
                    }
                });
                tt.start();
            }
        });
        Button buttonSave = (Button) findViewById(R.id.save);
        buttonSave.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                closeContextMenu();
                closeOptionsMenu();
                progressBar = new MobiProgressBar(EncodeActivity.this);
                progressBar.setMax(100);
                progressBar.setMessage(context.getString(R.string.encoding));
                progressBar.show();
                Thread tt = new Thread(new Runnable() {

                    public void run() {
                        encode();
                        handler.post(mShowAlert);
                    }
                });
                tt.start();
            }
        });
    }

    final Runnable mShowAlert = new Runnable() {

        public void run() {
            progressBar.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(context.getText(R.string.saved)).setCancelable(false).setPositiveButton(context.getText(R.string.ok), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    EncodeActivity.this.finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch(requestCode) {
            case (PICK_IMAGE):
                if (resultCode == RESULT_OK) {
                    Uri photoUri = intent.getData();
                    if (photoUri != null) {
                        try {
                            TextView t = (TextView) this.findViewById(R.id.EditTextImage);
                            Cursor cursor = getContentResolver().query(photoUri, null, null, null, null);
                            cursor.moveToFirst();
                            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                            absoluteFilePathSource = cursor.getString(idx);
                            t.setText(absoluteFilePathSource);
                            BitmapFactory.Options opt = new BitmapFactory.Options();
                            opt.inDither = false;
                            opt.inScaled = false;
                            opt.inDensity = 0;
                            opt.inJustDecodeBounds = false;
                            opt.inPurgeable = false;
                            opt.inSampleSize = 1;
                            opt.inScreenDensity = 0;
                            opt.inTargetDensity = 0;
                            sourceBitmap = BitmapFactory.decodeFile(absoluteFilePathSource, opt);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    final Runnable mIncrementProgress = new Runnable() {

        public void run() {
            progressBar.incrementProgressBy(1);
        }
    };

    final Runnable mInitializeProgress = new Runnable() {

        public void run() {
            progressBar.setMax(100);
        }
    };

    final Runnable mSetInderminate = new Runnable() {

        public void run() {
            progressBar.setMessage(context.getString(R.string.saving));
            progressBar.setIndeterminate(true);
        }
    };

    private Uri encode() {
        Uri result = null;
        EditText text = (EditText) findViewById(R.id.message);
        String s = text.getText().toString();
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();
        int[] oneD = new int[width * height];
        sourceBitmap.getPixels(oneD, 0, width, 0, 0, width, height);
        int density = sourceBitmap.getDensity();
        sourceBitmap.recycle();
        byte[] byteImage = LSB2bit.encodeMessage(oneD, width, height, s, new ProgressHandler() {

            private int mysize;

            private int actualSize;

            public void increment(final int inc) {
                actualSize += inc;
                if (actualSize % mysize == 0) handler.post(mIncrementProgress);
            }

            public void setTotal(final int tot) {
                mysize = tot / 50;
                handler.post(mInitializeProgress);
            }

            public void finished() {
            }
        });
        oneD = null;
        sourceBitmap = null;
        int[] oneDMod = LSB2bit.byteArrayToIntArray(byteImage);
        byteImage = null;
        Log.v("Encode", "" + oneDMod[0]);
        Log.v("Encode Alpha", "" + (oneDMod[0] >> 24 & 0xFF));
        Log.v("Encode Red", "" + (oneDMod[0] >> 16 & 0xFF));
        Log.v("Encode Green", "" + (oneDMod[0] >> 8 & 0xFF));
        Log.v("Encode Blue", "" + (oneDMod[0] & 0xFF));
        System.gc();
        Log.v("Free memory", Runtime.getRuntime().freeMemory() + "");
        Log.v("Image mesure", (width * height * 32 / 8) + "");
        Bitmap destBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        destBitmap.setDensity(density);
        int partialProgr = height * width / 50;
        int masterIndex = 0;
        for (int j = 0; j < height; j++) for (int i = 0; i < width; i++) {
            destBitmap.setPixel(i, j, Color.argb(0xFF, oneDMod[masterIndex] >> 16 & 0xFF, oneDMod[masterIndex] >> 8 & 0xFF, oneDMod[masterIndex++] & 0xFF));
            if (masterIndex % partialProgr == 0) handler.post(mIncrementProgress);
        }
        handler.post(mSetInderminate);
        Log.v("Encode", "" + destBitmap.getPixel(0, 0));
        Log.v("Encode Alpha", "" + (destBitmap.getPixel(0, 0) >> 24 & 0xFF));
        Log.v("Encode Red", "" + (destBitmap.getPixel(0, 0) >> 16 & 0xFF));
        Log.v("Encode Green", "" + (destBitmap.getPixel(0, 0) >> 8 & 0xFF));
        Log.v("Encode Blue", "" + (destBitmap.getPixel(0, 0) & 0xFF));
        String sdcardState = android.os.Environment.getExternalStorageState();
        String destPath = null;
        int indexSepar = absoluteFilePathSource.lastIndexOf(File.separator);
        int indexPoint = absoluteFilePathSource.lastIndexOf(".");
        if (indexPoint <= 1) indexPoint = absoluteFilePathSource.length();
        String fileNameDest = absoluteFilePathSource.substring(indexSepar + 1, indexPoint);
        fileNameDest += "_mobistego";
        if (sdcardState.contentEquals(android.os.Environment.MEDIA_MOUNTED)) destPath = android.os.Environment.getExternalStorageDirectory() + File.separator + fileNameDest + ".png";
        OutputStream fout = null;
        try {
            Log.v("Path", destPath);
            fout = new FileOutputStream(destPath);
            destBitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            result = Uri.parse("file://" + destPath);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result));
            fout.flush();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        destBitmap.recycle();
        return result;
    }
}
