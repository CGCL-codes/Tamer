package org.schwiet.LincolnLog.ui.utils;

import info.clearthought.layout.TableLayout;

/**
 *
 * @author sethschwiethale
 */
public class LayoutUtility {

    private static final double[][] MAIN_DIVS = { { -1 }, { 45, -1, 24 } };

    private static final double[][] APP_AREA_DIVS = { { 220, -1 }, { -1 } };

    private static final double[][] VIEW_DIVS = { { 5, -1, 5 }, { 5, -1, 5, 24 } };

    private static final double[][] TRANS_DIVS = { { -1 }, { 35, -1 } };

    private static final TableLayout MAIN_LAYOUT = new TableLayout(MAIN_DIVS);

    private static final TableLayout APP_AREA_LAYOUT = new TableLayout(APP_AREA_DIVS);

    private static final TableLayout VIEW_LAYOUT = new TableLayout(VIEW_DIVS);

    private static final TableLayout VIEW_TABLE = new TableLayout(TRANS_DIVS);

    public static final String MAIN_HEADER = "0,0";

    public static final String MAIN_BOTTOM_BAR = "0,2";

    public static final String MAIN_APP_AREA = "0,1";

    public static final String APP_AREA_DIVVY_LIST = "0,0";

    public static final String APP_AREA_CONTENT = "1,0";

    public static final String VIEW_CONTENT = "1,1";

    public static final String VIEW_CONFIRMATION = "0,3,2,3";

    public static final String VIEW_TABLE_LOC = "0, 1";

    public static final String VIEW_NEW_TRANS = "0, 0";

    public static TableLayout getMainWindowLayout() {
        return MAIN_LAYOUT;
    }

    public static TableLayout getAppAreaLayout() {
        return APP_AREA_LAYOUT;
    }

    public static TableLayout getViewLayout() {
        return VIEW_LAYOUT;
    }

    public static TableLayout getTransactionLayout() {
        return VIEW_TABLE;
    }
}
