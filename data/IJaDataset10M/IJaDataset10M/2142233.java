package org.zkoss.gwt.client.zul.grid;

import org.zkoss.gwt.client.zul.Widget;
import java.util.*;
import com.google.gwt.core.client.JavaScriptObject;

public class Row extends org.zkoss.gwt.client.zul.Widget {

    protected native JavaScriptObject create();

    public native void setAlign(String align);

    public native String getAlign();

    public native void setNowrap(boolean nowrap);

    public native boolean isNowrap();

    public native void setValign(String valign);

    public native String getValign();

    public native org.zkoss.gwt.client.zul.grid.Grid getGrid();

    public native String getSpans();

    public native void setSpans(String spans);

    public native org.zkoss.gwt.client.zkex.grid.Group getGroup();
}
