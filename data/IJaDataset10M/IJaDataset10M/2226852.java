package com.makeabyte.jhosting.client.session;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

@Name("PreferencesBean")
public class PreferencesBean implements Preferences {

    @Logger
    private Log log;

    private String iconTheme;

    public PreferencesBean() {
    }

    public void setSelectedIconTheme(String iconTheme) {
        this.iconTheme = iconTheme;
        log.info("Setting iconTheme to {0}", iconTheme);
    }

    public String getSelectedIconTheme() {
        return iconTheme;
    }
}
