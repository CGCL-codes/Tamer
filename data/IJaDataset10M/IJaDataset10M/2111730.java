package com.gft.larozanam.client.componentes;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public class CMenuItem extends MenuItem {

    public static final String STYLE = "cmenuitem";

    {
        setStyleName(STYLE);
    }

    public CMenuItem(SafeHtml html, Command command) {
        super(html, command);
    }

    public CMenuItem(String text, boolean asHtml, Command command) {
        super(text, asHtml, command);
    }

    public CMenuItem(String html, Command command) {
        this(html, true, command);
    }
}
