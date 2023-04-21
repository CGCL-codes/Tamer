package fr.fg.client.data;

import com.google.gwt.core.client.JavaScriptObject;

public class TradeCenterData extends JavaScriptObject {

    public static final String CLASS_NAME = "TradeCenterData";

    public static final double TRADE_CENTER_RADIUS = 4.5;

    public static final String FIELD_ID = "a", FIELD_X = "b", FIELD_Y = "c";

    protected TradeCenterData() {
    }

    public final native int getId();

    public final native int getX();

    public final native int getY();
}
