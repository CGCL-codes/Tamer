package org.colorvision;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import org.colorvision.Preview.Orientation;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Activity for calibrating the position of the target.
 * @author Adam Crume
 */
public class CalibrateTarget extends Activity {

    /** Key for the target offset preference. */
    static final String PREF = "targetOffset";

    private static final String TAG = "ColorVision";

    private Preview preview;

    private Alerter alerter;

    private float[] portraitCalibration;

    private boolean portrait = true;

    private boolean dialogDismissed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "CalibrateTarget.onCreate");
        if (savedInstanceState != null) {
            portrait = savedInstanceState.getBoolean("portrait");
            dialogDismissed = savedInstanceState.getBoolean("dialogDismissed");
            portraitCalibration = savedInstanceState.getFloatArray("portraitCalibration");
        }
        alerter = new Alerter(this);
        if (!dialogDismissed) {
            displayInstructions();
        }
    }

    private void displayInstructions() {
        dialogDismissed = false;
        String msg = getString(portrait ? R.string.calibrate_target_instructions : R.string.calibrate_target_instructions_landscape);
        alerter.alert(msg, new OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialogDismissed = true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.calibrate_target_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.cancel:
                finish();
                return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "CalibrateTarget.onResume");
        preview = new Preview(this);
        preview.setOrientation(portrait ? Orientation.PORTRAIT : Orientation.LANDSCAPE);
        preview.setPreviewCallback(new PreviewCallback() {

            public boolean onPictureTaken(byte[] data, Camera camera) {
                Bitmap img = BitmapFactory.decodeByteArray(data, 0, data.length);
                int[] dot = findDot(img);
                img.recycle();
                if (dot == null) {
                    alerter.alert("Unable to find dot.  Please try again.");
                    return true;
                }
                dot[0] = img.getWidth() / 2 - dot[0];
                dot[1] = img.getHeight() / 2 - dot[1];
                float[] offset = new float[2];
                offset[0] = dot[0] * 1.0f / img.getWidth();
                offset[1] = dot[1] * 1.0f / img.getHeight();
                if (portrait) {
                    float tmp = offset[0];
                    offset[0] = -offset[1];
                    offset[1] = tmp;
                    portrait = false;
                    portraitCalibration = offset;
                    preview.setOrientation(Orientation.LANDSCAPE);
                    displayInstructions();
                    return true;
                } else {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    Editor editor = prefs.edit();
                    float[] old = preview.decodeOffsets(prefs.getString(PREF, "0.0,0.0;0.0,0.0"));
                    old[0] += portraitCalibration[0];
                    old[1] += portraitCalibration[1];
                    old[2] += offset[0];
                    old[3] += offset[1];
                    editor.putString(PREF, old[0] + ", " + old[1] + "; " + old[2] + ", " + old[3]);
                    editor.commit();
                    finish();
                    return false;
                }
            }
        });
        setContentView(preview);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("portrait", portrait);
        outState.putBoolean("dialogDismissed", dialogDismissed);
        outState.putFloatArray("portraitCalibration", portraitCalibration);
    }

    @Override
    protected void onPause() {
        if (preview != null) {
            preview.release();
            preview = null;
        }
        super.onPause();
    }

    /**
	 * Locates a black dot near the center of the image.
	 * @param img mostly white image containing a black dot near the center
	 * @return coordinates of the black dot, or null if not found
	 */
    private int[] findDot(Bitmap img) {
        int w = img.getWidth();
        int h = img.getHeight();
        int cx = w / 2;
        int cy = h / 2;
        int xx = -1;
        int yy = -1;
        outer: for (int dist = 0; dist < w / 4; dist++) {
            for (int x = cx - dist; x <= cx + dist; x++) {
                if (isBlack(img, x, cy - dist)) {
                    xx = x;
                    yy = cy - dist;
                    break outer;
                }
                if (isBlack(img, x, cy + dist)) {
                    xx = x;
                    yy = cy + dist;
                    break outer;
                }
            }
            for (int y = cy - dist; y <= cy + dist; y++) {
                if (isBlack(img, cx - dist, y)) {
                    xx = cx - dist;
                    yy = y;
                    break outer;
                }
                if (isBlack(img, cx + dist, y)) {
                    xx = cx + dist;
                    yy = y;
                    break outer;
                }
            }
        }
        if (xx == -1) {
            return null;
        }
        BitSet2D filled = new BitSet2D();
        List<int[]> queue = new LinkedList<int[]>();
        queue.add(new int[] { xx, yy });
        int sumx = 0;
        int sumy = 0;
        int count = 0;
        while (!queue.isEmpty()) {
            int[] node = queue.remove(0);
            int x = node[0];
            int y = node[1];
            if (isBlackish(img, x, y) && !filled.get(x - cx, y - cy)) {
                count++;
                sumx += x;
                sumy += y;
                filled.set(x - cx, y - cy, true);
                int west = x;
                int east = x;
                do {
                    west--;
                } while (west > 0 && isBlackish(img, west, y) && !filled.get(west - cx, y - cy));
                do {
                    east++;
                } while (east < w - 1 && isBlackish(img, east, y) && !filled.get(east - cx, y - cy));
                for (int i = west + 1; i < east; i++) {
                    if (y > 0 && isBlackish(img, i, y - 1) && !filled.get(i - cx, y - 1 - cy)) {
                        queue.add(new int[] { i, y - 1 });
                    }
                    if (y < h - 1 && isBlackish(img, i, y + 1) && !filled.get(i - cx, y + 1 - cy)) {
                        queue.add(new int[] { i, y + 1 });
                    }
                }
            }
        }
        return new int[] { sumx / count, sumy / count };
    }

    private boolean isBlack(Bitmap img, int x, int y) {
        int data = img.getPixel(x, y);
        return ((data & 0xff) + ((data >> 8) & 0x0ff) + ((data >> 16) & 0xff)) / 3 < 80;
    }

    private boolean isBlackish(Bitmap img, int x, int y) {
        int data = img.getPixel(x, y);
        return ((data & 0xff) + ((data >> 8) & 0x0ff) + ((data >> 16) & 0xff)) / 3 < 90;
    }

    /**
	 * Similar to a 2D boolean array, except that this is automatically growable and allows negative indices.
	 * Setting values at indices far from (0,0) uses more memory than indices close to (0,0).
	 * @author Adam Crume
	 */
    private static class BitSet2D {

        private BitSet data = new BitSet();

        public boolean get(int x, int y) {
            return data.get(index(x, y));
        }

        public void set(int x, int y, boolean value) {
            data.set(index(x, y), value);
        }

        private int index(int x, int y) {
            x = x < 0 ? -2 * x - 1 : 2 * x;
            y = y < 0 ? -2 * y - 1 : 2 * y;
            return (x + y) * (x + y + 1) / 2 + x;
        }
    }
}
