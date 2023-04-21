package gwtmodule1.client.gui.utils;

import com.google.gwt.user.client.ui.SimplePanel;

public class HSpacer extends SimplePanel {

    public HSpacer() {
        this("24px");
    }

    public HSpacer(String height) {
        super();
        setHeight(height);
    }
}
