package net.redlightning.redtorrent.common;

import java.text.DecimalFormat;
import java.util.List;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * All of the common code between the main Activity for Red Torrent and Red
 * Torrent Lite. Handles management of settings, adding/removing torrents, etc.
 * 
 * @author Michael Isaacson
 * @version 11.7.22
 */
public class TorrentActivity extends ListActivity {

    public static final GradientDrawable GRADIENT = new GradientDrawable(Orientation.RIGHT_LEFT, new int[] { 0xFF110000, 0xFFFF0000, 0xFF110000 });

    public static final GradientDrawable BACK_GRADIENT = new GradientDrawable(Orientation.TOP_BOTTOM, new int[] { 0x00000000, 0x00000000, 0xFF500000 });

    public static final String ACTION_START = "net.redlightning.redtorrent.START_ACTIVITY";

    protected static final String TAG = TorrentActivity.class.getSimpleName();

    protected static final int MENU_EXIT = 0;

    protected static final int MENU_SETTINGS = 1;

    protected static final int MENU_OPEN = 2;

    protected static final int MENU_SEARCH = 3;

    protected static final int MENU_STOP_ALL = 4;

    public static final int FILE_BROWSER = 10;

    private static DownloadListAdapter adapter;

    public static Handler handler;

    public static List<Downloader> downloaders;

    public AlertDialog pickerDialog;

    /**
	 * @return the adapter
	 */
    public static DownloadListAdapter getAdapter() {
        return adapter;
    }

    /**
	 * Reads the version name from the Manifest
	 */
    public String getVersionName(Context context) {
        try {
            return getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            Log.i(TAG, "Null version");
            return null;
        }
    }

    /**
	 * @param adapter the adapter to set
	 */
    public static synchronized void setAdapter(final DownloadListAdapter adapter) {
        TorrentActivity.adapter = adapter;
    }

    @Override
    public synchronized void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (FILE_BROWSER == requestCode && resultCode == RESULT_OK) {
            pickerDialog = new TorrentFilePickerDialog(this, data.getStringExtra("file")).build();
            pickerDialog.show();
            pickerDialog.getButton(Dialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if ("Clear All".equals(((Button) v).getText())) {
                        ((Button) v).setText("Select All");
                        TorrentFilePickerListener.selectAll(pickerDialog, false);
                    } else if ("Select All".equals(((Button) v).getText())) {
                        ((Button) v).setText("Clear All");
                        TorrentFilePickerListener.selectAll(pickerDialog, true);
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        TorrentService.setUiHandler(null);
        super.onDestroy();
    }

    /**
	 * Update the free memory display
	 * 
	 * @param context the current context
	 * @param memoryView the TextView to display the memory message in
	 */
    public static synchronized void updateMemory(final Context context, final TextView memoryView, final TextView sdcardView, final TextView dhtView) {
        final long memory = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory();
        final float kilobytes = memory / 1024;
        final float megabytes = kilobytes / 1024;
        if (megabytes > 2) {
            memoryView.setText("RAM: " + new DecimalFormat("##0.00").format(megabytes) + " MB");
        } else if (kilobytes > 1000) {
            memoryView.setText("RAM: " + new DecimalFormat("##0.00").format(kilobytes) + " KB");
        } else {
            memoryView.setText("RAM: " + memory + " bytes");
        }
        if (megabytes < 1) {
            memoryView.setTextColor(Color.RED);
        } else {
            memoryView.setTextColor(Color.WHITE);
        }
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        stat.restat(Environment.getExternalStorageDirectory().getAbsolutePath());
        long available = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize()) / 1024 / 1024;
        if (available > 1024) {
            sdcardView.setText("SD: " + new DecimalFormat("##0.00").format(available / 1024.0) + " GB");
        } else {
            sdcardView.setText("SD: " + available + " MB");
        }
        if (available < 5) {
            sdcardView.setTextColor(Color.RED);
        } else {
            sdcardView.setTextColor(Color.WHITE);
        }
    }

    /**
	 * @return the handler
	 */
    public static synchronized Handler getHandler() {
        return handler;
    }

    /**
	 * @param intent
	 */
    protected synchronized void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.e(TAG, "Search: " + query);
        }
    }
}
