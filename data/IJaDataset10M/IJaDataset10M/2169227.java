package com.gapp.king.web.client;

import com.google.gwt.core.client.JavaScriptObject;

public class StockData extends JavaScriptObject {

    protected StockData() {
    }

    public final native String getSymbol();

    public final native double getPrice();

    public final native double getChange();

    public final double getChangePercent() {
        return 100.0 * getChange() / getPrice();
    }
}
