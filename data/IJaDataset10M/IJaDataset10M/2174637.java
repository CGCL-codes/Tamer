package com.gwtext.client.widgets.chart.yui;

import com.google.gwt.core.client.JavaScriptObject;
import com.gwtext.client.core.JsObject;
import com.gwtext.client.util.JavaScriptObjectHelper;

/**
 * Series class for the YUI Charts widget.
 *
 * @author Sanjiv Jivan
 */
public class Series extends JsObject {

    public Series() {
        jsObj = create();
    }

    protected native JavaScriptObject create();

    /**
     * The human-readable name of the series.
     *
     * @param displayName the human-readable name of the series.
     */
    public void setDisplayName(String displayName) {
        JavaScriptObjectHelper.setAttribute(jsObj, "displayName", displayName);
    }

    /**
     * The type of series.
     *
     * @param type the type of series
     */
    public void setType(ChartType type) {
        JavaScriptObjectHelper.setAttribute(jsObj, "type", type.getType());
    }
}
