package com.gwtext.client.dd;

import com.gwtext.client.core.JsObject;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.widgets.Layer;
import com.google.gwt.core.client.JavaScriptObject;

/**
 *
 * @author Sanjiv Jivan
 */
public class StatusProxy extends JsObject {

    public StatusProxy(JavaScriptObject jsObj) {
        super(jsObj);
    }

    public StatusProxy() {
        jsObj = create(null);
    }

    protected native JavaScriptObject create(JavaScriptObject config);

    private static StatusProxy instance(JavaScriptObject jsObj) {
        return new StatusProxy(jsObj);
    }

    /**
     * Returns the underlying proxy {@link Layer}.
     *
     * @return the underlying proxy Layer
     */
    public native Layer getEl();

    /**
     * Returns the ghost element.
     *
     * @return the ghost element
     */
    public native ExtElement getGhost();

    public native void hide();

    public native void hide(boolean clear);

    public native void reset();

    public native void reset(boolean clearGhost);

    public native void setStatus(String cssClass);

    public native void show();

    public native void stop();

    public native void sync();

    public native void update(String html);
}
