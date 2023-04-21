package com.manning.gwtip.bookstore.client.dnd;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LoadListener;
import com.google.gwt.user.client.ui.Widget;

public class ReflectedImage extends Image {

    public ReflectedImage() {
        super();
        addLoadListener(new LoadListener() {

            public void onLoad(Widget widget) {
                reflect(getElement(), (float) 0.25, (float) 0.5, 188, 150);
            }

            public void onError(Widget widget) {
            }
        });
    }

    public native void unreflect(Element image);

    public native void reflect(Element image, float height, float opacity, int offsetHeight, int offsetWidth);
}
