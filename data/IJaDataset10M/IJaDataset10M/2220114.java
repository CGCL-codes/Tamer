package org.zkoss.gwt.client.zul.sel;

import org.zkoss.gwt.client.zul.Widget;
import java.util.*;
import com.google.gwt.core.client.JavaScriptObject;

public class Option extends org.zkoss.gwt.client.zul.Widget {

    protected native JavaScriptObject create();

    public native void setDisabled(boolean disabled);

    public native boolean isDisabled();

    public native void setValue(String value);

    public native String getValue();

    public native void setSelected(boolean selected);

    public native boolean isSelected();

    public native String getLabel();

    public native int getMaxlength();
}
