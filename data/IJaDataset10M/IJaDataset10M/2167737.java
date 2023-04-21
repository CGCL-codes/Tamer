package com.tensegrity.palowebviewer.modules.util.client.browsertypes;

public class IE60Browser extends AbstractBrowserType {

    public String getName() {
        return "Explorer";
    }

    public int getTypeID() {
        return TYPE_ID_IE_6_0;
    }

    public String getVersion() {
        return "6";
    }
}
