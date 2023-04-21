package com.tensegrity.palowebviewer.modules.util.client.browsertypes;

public class IE70Browser extends AbstractBrowserType {

    public String getName() {
        return "Explorer";
    }

    public int getTypeID() {
        return TYPE_ID_IE_7_0;
    }

    public String getVersion() {
        return "7";
    }
}
