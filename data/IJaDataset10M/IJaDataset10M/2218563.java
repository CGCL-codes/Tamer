package org.apache.harmony.awt.state;

import com.google.code.appengine.awt.Dimension;
import com.google.code.appengine.awt.Rectangle;

/**
 * State of menu item
 */
public interface MenuItemState {

    String getText();

    Rectangle getTextBounds();

    void setTextBounds(int x, int y, int w, int h);

    String getShortcut();

    Rectangle getShortcutBounds();

    void setShortcutBounds(int x, int y, int w, int h);

    Rectangle getItemBounds();

    void setItemBounds(int x, int y, int w, int h);

    boolean isMenu();

    boolean isChecked();

    boolean isEnabled();

    boolean isCheckBox();

    boolean isSeparator();

    Dimension getMenuSize();
}
