package org.client.a.ui;

import com.google.gwt.user.client.ui.*;

public class MenuItem extends com.google.gwt.user.client.ui.MenuItem {

    public MenuItem(com.google.gwt.user.client.ui.MenuBar menubar) {
        super("No label", menubar);
    }

    public void setLabel(String label) {
        setText(label);
    }
}
