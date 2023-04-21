package org.openintents.shopping.ui.widget;

import org.openintents.shopping.library.provider.ShoppingContract;
import org.openintents.shopping.library.provider.ShoppingContract.Contains;
import org.openintents.shopping.ui.widget.QuickSelectMenu.OnItemSelectedListener;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v2.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

public class QuickSelectMenu {

    android.widget.PopupMenu mImplPlatform = null;

    org.openintents.shopping.ui.widget.backport.PopupMenu mImplBackport = null;

    private enum ImplMode {

        PLATFORM, BACKPORT, NONE
    }

    ;

    ImplMode mMode = ImplMode.NONE;

    private OnItemSelectedListener mItemSelectedListener;

    public QuickSelectMenu(Context context, View anchor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mMode = ImplMode.PLATFORM;
            mImplPlatform = new android.widget.PopupMenu(context, anchor);
            mImplPlatform.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onMenuItemClickImpl(item);
                }
            });
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mMode = ImplMode.BACKPORT;
            mImplBackport = new org.openintents.shopping.ui.widget.backport.PopupMenu(context, anchor);
            mImplBackport.setOnMenuItemClickListener(new org.openintents.shopping.ui.widget.backport.PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onMenuItemClickImpl(item);
                }
            });
        }
    }

    public Menu getMenu() {
        if (mMode == ImplMode.PLATFORM) {
            Menu menu = mImplPlatform.getMenu();
            return menu;
        }
        if (mMode == ImplMode.BACKPORT) {
            Menu menu = mImplBackport.getMenu();
            return menu;
        }
        return null;
    }

    /**
     * Interface responsible for receiving menu item click events if the items themselves
     * do not have individual item click listeners.
     */
    public interface OnItemSelectedListener {

        /**
         * This method will be invoked when an item is selected.
         *
         * @param item {@link CharSequence} that was selected
         * @param id 
         */
        public void onItemSelected(CharSequence item, int id);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        mItemSelectedListener = listener;
    }

    public void show() {
        if (mMode == ImplMode.PLATFORM) {
            mImplPlatform.show();
        }
        if (mMode == ImplMode.BACKPORT) {
            mImplBackport.show();
        }
    }

    public boolean onMenuItemClickImpl(MenuItem item) {
        CharSequence name = item.getTitle();
        int id = item.getItemId();
        this.mItemSelectedListener.onItemSelected(name, id);
        return true;
    }
}
