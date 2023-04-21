package com.nathanson.chipublib;

import android.content.Intent;
import android.net.Uri;
import java.util.List;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager;
import android.content.Context;
import android.app.AlertDialog;
import android.app.Dialog;
import java.util.ArrayList;
import android.app.ProgressDialog;
import android.os.Handler;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.csv.CSVParser;
import java.net.MalformedURLException;
import java.io.IOException;
import java.util.HashSet;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;

/**
 * Utility class for providing UPC and ISBN bar code scanning.
 *
 * @author Elliot Nathanson
 */
public class ChiPubLibBarcode {

    private static final ArrayList<String> UPC_FORMATS = new ArrayList<String>() {

        {
            add("UPC_A");
            add("UPC_E ");
            add("UPC_EAN_EXTENSION");
        }
    };

    private static final ArrayList<String> ISBN_FORMATS = new ArrayList<String>() {

        {
            add("EAN_8");
            add("EAN_13");
        }
    };

    private static final String UPC_LOOKUP_URL = "http://www.searchupc.com/handlers/upcsearch.ashx?request_type=1";

    private static final String UPC_DESC_COLUMN_HEADING = "productname";

    private static final String CLEAN_UPC_INFO_REGEXP = "[^a-zA-Z0-9 ]";

    public static final int UNKNOWN = -1;

    public static final int UPC = 0;

    public static final int ISBN = 1;

    private static final String ZXING_PACKAGE = "com.google.zxing.client.android";

    private static final String SCAN_INTENT = ZXING_PACKAGE + ".SCAN";

    public static Intent getScanIntent() {
        Uri uri = Uri.parse(SCAN_INTENT);
        Intent scanIntent = new Intent(SCAN_INTENT);
        scanIntent.putExtra("SCAN_MODE", "Produce_MODE");
        return scanIntent;
    }

    /**
     * Shows a dialog to the user indicating we are looking
     * up the upc code.
     *
     * @param context calling activity
     * @return Dialog
     */
    public static Dialog showFetchDialog(Context context) {
        return ProgressDialog.show(context, null, context.getResources().getString(R.string.progress_action_upc_fetch));
    }

    /**
     * Shows a dialog to the user to see if he/she would like to
     * install ZXing. Code mostly hijacked from  IntentIntegrator.java
     * in ZXing.
     *
     * @param context calling activity
     * @return Dialog
     */
    public static Dialog showInstallDialog(final Context context) {
        Resources r = context.getResources();
        String title = r.getString(R.string.zxing_install_dialog_title);
        String msg = r.getString(R.string.zxing_install_dialog_msg);
        String yes = r.getString(R.string.zxing_install_dialog_yes);
        String no = r.getString(R.string.zxing_install_dialog_no);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setCancelable(true);
        dialog.setPositiveButton(yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i) {
                context.startActivity(getInstallIntent());
            }
        });
        dialog.setNegativeButton(no, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return dialog.show();
    }

    /**
     * Describe Displays error dialog.
     *
     * @param context calling activity.
     * @param errorMsg message to display to user.
     * @return the displayed dialog
     */
    public static Dialog showErrorDialog(final Context context, final String errorMsg) {
        String title = context.getResources().getString(R.string.error_dialog_title_error);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(errorMsg);
        return dialog.show();
    }

    /**
     * Provides intent for installing ZXing via the market.
     *
     * @return ZXing install Intent.
     */
    private static Intent getInstallIntent() {
        Uri uri = Uri.parse("market://search?q=pname:" + ZXING_PACKAGE);
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    /**
     * Determines if a given intent is available; mostly hijacked from google.
     *
     * @param context associated activity calling method.
     * @param intent lookup this intent
     * @return true if available, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * Determines the format of the scanned barcode.
     *
     * @param intent contains barcode scanning results.
     * @return UPC, ISBN, or UNKNOWN.
     */
    public static int formatType(Intent intent) {
        String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
        if (UPC_FORMATS.contains(format)) {
            return UPC;
        }
        if (ISBN_FORMATS.contains(format)) {
            return ISBN;
        }
        return UNKNOWN;
    }

    /**
     * Determines the ISBN from the scanned barcode.
     *
     * @param intent contains barcode scanning results.
     * @return the ISBN
     */
    public static String getISBN(Intent intent) {
        return intent.getStringExtra("SCAN_RESULT");
    }

    /**
     * Since UPC lookup is remote and slow, functionality put
     * into this thread.
     *
     * @param context calling activity
     * @param handler who to notify when thread complete.
     * @param runnable what function to call within handler.
     * @param intent contains barcode scanning results.
     * @return UPCLookupThread to perform UPC lookup.
     */
    public static UPCLookupThread getUPCFetchThread(Context context, Handler handler, Runnable runnable, Intent intent, ArrayList<String> results) {
        String upcKey = context.getResources().getString(R.string.search_upc_key);
        return new ChiPubLibBarcode.UPCLookupThread(context, handler, runnable, upcKey, intent, results);
    }

    /**
     * Thread to do upc lookup.
     */
    public static class UPCLookupThread extends Thread {

        private Context context_;

        private Handler handler_;

        private Runnable runnable_;

        private Intent intent_;

        private String upcKey_;

        private String errorMsg_;

        private ArrayList<String> results_;

        public UPCLookupThread(Context context, Handler handler, Runnable runnable, String upcKey, Intent intent, ArrayList<String> results) {
            context_ = context;
            handler_ = handler;
            runnable_ = runnable;
            results_ = results;
            intent_ = intent;
            upcKey_ = upcKey;
        }

        public void run() {
            try {
                processUPC();
            } catch (ChiPubLibBarcodeException e) {
            } finally {
                handler_.post(runnable_);
            }
        }

        public String getErrorMsg() {
            return errorMsg_;
        }

        /**
	 * Requests a UPC lookup from remote site and stores the unique
	 * descriptions in results_.
	 */
        private void processUPC() throws ChiPubLibBarcodeException {
            HttpURLConnection httpConnection = null;
            try {
                String upc = intent_.getStringExtra("SCAN_RESULT");
                URL url = new URL(UPC_LOOKUP_URL + "&access_token=" + upcKey_ + "&upc=" + upc);
                httpConnection = (HttpURLConnection) url.openConnection();
                int responseCode = httpConnection.getResponseCode();
                HashSet uniqueResuts = new HashSet<String>();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    CSVParser csvParser = new CSVParser(reader);
                    int descCol = getDescCol(csvParser.getLine());
                    String[] line;
                    String cleanStr;
                    while ((line = csvParser.getLine()) != null) {
                        cleanStr = line[descCol].replaceAll(CLEAN_UPC_INFO_REGEXP, "");
                        if (uniqueResuts.add(cleanStr)) {
                            results_.add(cleanStr);
                        }
                    }
                }
            } catch (MalformedURLException e) {
                Resources r = context_.getResources();
                errorMsg_ = r.getString(R.string.upc_error_msg) + r.getString(R.string.unknown_error_msg);
                throw new ChiPubLibBarcodeException(errorMsg_);
            } catch (IOException e) {
                Resources r = context_.getResources();
                errorMsg_ = r.getString(R.string.upc_error_msg) + r.getString(R.string.network_error_msg);
                throw new ChiPubLibBarcodeException(errorMsg_);
            } finally {
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
            }
        }

        /**
	 * Determines which column number has the desired data.
	 *
	 * @param headings array of the various upc lookup headings 
	 * @return column number of desired data.
	 * @exception ChiPubLibBarcodeException if column does not exist.
	 */
        private int getDescCol(String[] headings) throws ChiPubLibBarcodeException {
            if (headings.length > 0) {
                for (int lv = 0; lv < headings.length; lv++) {
                    if (headings[lv].equals(UPC_DESC_COLUMN_HEADING)) {
                        return lv;
                    }
                }
            }
            errorMsg_ = context_.getResources().getString(R.string.invalid_data_error_msg);
            throw new ChiPubLibBarcodeException(errorMsg_);
        }
    }
}
