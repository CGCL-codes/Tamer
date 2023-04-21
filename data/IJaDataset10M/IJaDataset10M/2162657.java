package org.rapla.mobile.android.app;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * This class provides functionality to show a consistent progress dialog to the
 * user while data are being retrieved.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class LoadDataProgressDialog extends ProgressDialog {

    /**
	 * @param context Current context
	 */
    public LoadDataProgressDialog(Context context) {
        super(context);
        this.setMessage(context.getString(org.rapla.mobile.android.R.string.progress_dialog_loading_data));
        this.setIndeterminate(true);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }
}
