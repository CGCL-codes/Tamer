package com.ivis.xprocess.app;

import org.eclipse.osgi.util.NLS;

public class XProcessMessages extends NLS {

    private static final String BUNDLE_NAME = "com.ivis.xprocess.app.messages";

    public static String Switch_Datasource;

    public static String Reset_Perspective;

    public static String Global_FileMenu;

    public static String Global_FileMenu_New;

    public static String Global_EditMenu;

    public static String Global_NavigateMenu;

    public static String Global_WindowMenu;

    public static String Global_HelpMenu;

    public static String Global_HowToMenu;

    public static String WindowMenu_Show_View;

    public static String WindowMenu_Switch_Perspective;

    static {
        NLS.initializeMessages(BUNDLE_NAME, XProcessMessages.class);
    }
}
