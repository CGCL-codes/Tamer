package com.google.gwt.user.client.ui.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

/**
 * Implementation interface for creating and manipulating focusable elements
 * that aren't naturally focusable in all browsers, such as DIVs.
 */
public class FocusImpl {

    private static FocusImpl implPanel = (FocusImpl) GWT.create(FocusImpl.class);

    /**
   * This instance may not be a {@link FocusImplOld}, because that special case
   * is only needed for things that aren't naturally focusable on some browsers,
   * such as DIVs. This exact class works for truly focusable widgets on those
   * browsers.
   * 
   * The compiler will optimize out the conditional.
   */
    private static FocusImpl implWidget = (implPanel instanceof FocusImplOld) ? new FocusImpl() : implPanel;

    /**
   * Returns the focus implementation class for creating and manipulating
   * focusable elements that aren't naturally focusable in all browsers, such as
   * DIVs.
   */
    public static FocusImpl getFocusImplForPanel() {
        return implPanel;
    }

    /**
   * Returns the focus implementation class for manipulating focusable elements
   * that are naturally focusable in all browsers, such as text boxes.
   */
    public static FocusImpl getFocusImplForWidget() {
        return implWidget;
    }

    /**
   * Not externally instantiable or extensible. 
   */
    FocusImpl() {
    }

    public native void blur(Element elem);

    public native Element createFocusable();

    public native void focus(Element elem);

    public native int getTabIndex(Element elem);

    public native void setAccessKey(Element elem, char key);

    public native void setTabIndex(Element elem, int index);
}
