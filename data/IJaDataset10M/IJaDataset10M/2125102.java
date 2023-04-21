package android.support.v4.view;

import android.view.MenuItem;
import android.view.View;

/**
 * Implementation of menu compatibility that can call Honeycomb APIs.
 */
class MenuItemCompatHoneycomb {

    public static void setShowAsAction(MenuItem item, int actionEnum) {
        item.setShowAsAction(actionEnum);
    }

    public static MenuItem setActionView(MenuItem item, View view) {
        return item.setActionView(view);
    }
}
