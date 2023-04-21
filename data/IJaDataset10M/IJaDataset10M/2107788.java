package org.zkoss.gwt.client.zul.wgt;

import org.zkoss.gwt.client.zul.Widget;
import java.util.*;
import com.google.gwt.core.client.JavaScriptObject;

public class Include extends org.zkoss.gwt.client.zul.Widget {

    protected native JavaScriptObject create();

    public native void setContent(String content);

    public native String getContent();

    public native void setComment(boolean comment);

    public native boolean isComment();
}
