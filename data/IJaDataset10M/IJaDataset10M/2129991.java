package com.tensegrity.palowebviewer.modules.util.client.browsertypes;

public interface IBrowserType {

    public static final int TYPE_ID_UNKNOWN = 0;

    public static final int TYPE_ID_FIREFOX = 1;

    public static final int TYPE_ID_IE_6_0 = 2;

    public static final int TYPE_ID_IE_7_0 = 3;

    public static final int TYPE_ID_MOZILLA = 4;

    public static final IBrowserType TYPE_UNKNOWN = new UnknownBrowser();

    public static final IBrowserType TYPE_FIREFOX = new FirefoxBrowser();

    public static final IBrowserType TYPE_IE_6_0 = new IE60Browser();

    public static final IBrowserType TYPE_IE_7_0 = new IE70Browser();

    public static final IBrowserType TYPE_MOZILLA = new MozillaBrowser();

    public static final IBrowserType[] BROWSER_TYPES = new IBrowserType[] { TYPE_FIREFOX, TYPE_IE_6_0, TYPE_IE_7_0, TYPE_MOZILLA, TYPE_UNKNOWN };

    public int getTypeID();

    public String getName();

    public String getVersion();

    public boolean verifyVersion(String name, String version);
}
