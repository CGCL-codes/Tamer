package net.micode.notes.ui;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import net.micode.notes.R;

public class DropdownMenu {

    private Button mButton;

    private PopupMenu mPopupMenu;

    private Menu mMenu;

    public DropdownMenu(Context context, Button button, int menuId) {
        mButton = button;
        mButton.setBackgroundResource(R.drawable.dropdown_icon);
        mPopupMenu = new PopupMenu(context, mButton);
        mMenu = mPopupMenu.getMenu();
        mPopupMenu.getMenuInflater().inflate(menuId, mMenu);
        mButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                mPopupMenu.show();
            }
        });
    }

    public void setOnDropdownMenuItemClickListener(OnMenuItemClickListener listener) {
        if (mPopupMenu != null) {
            mPopupMenu.setOnMenuItemClickListener(listener);
        }
    }

    public MenuItem findItem(int id) {
        return mMenu.findItem(id);
    }

    public void setTitle(CharSequence title) {
        mButton.setText(title);
    }
}
