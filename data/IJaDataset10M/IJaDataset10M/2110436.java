package com.awarix.gwt.dom.stylesheets;

import com.awarix.gwt.dom.DOMObject;
import com.google.gwt.core.client.JavaScriptObject;
import org.w3c.dom.stylesheets.MediaList;

public class MediaListImpl extends DOMObject implements MediaList {

    protected MediaListImpl(JavaScriptObject jso) {
        super(jso);
    }

    public native void appendMedium(String medium);

    public native void deleteMedium(String medium);

    public native int getLength();

    public native String getMediaText();

    public native String item(int index);

    public native void setMediaText(String mediaText);

    public static MediaList wrapMediaList(JavaScriptObject jso) {
        return jso == null ? null : new MediaListImpl(jso);
    }
}
