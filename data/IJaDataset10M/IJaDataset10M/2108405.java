package org.rapla.mobile.android.test.mock;

import org.rapla.mobile.android.app.LoadDataProgressDialog;
import android.content.Context;

/**
 * MockLoadDataProgressDialog
 * 
 * Mocked load data progress dialog
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class MockLoadDataProgressDialog extends LoadDataProgressDialog {

    public boolean shown = false;

    public MockLoadDataProgressDialog(Context context) {
        super(context);
    }

    /**
	 * Overwrite show to work in unit test environment
	 */
    public void show() {
        this.shown = true;
    }
}
