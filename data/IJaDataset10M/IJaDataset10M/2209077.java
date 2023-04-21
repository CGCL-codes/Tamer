package gov.nih.niaid.bcbb.nexplorer3.client.widgets;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Fade-in / fade-out animation effect for Google Web Toolkit
 * 
 * @author Copyright (c) Ulrich Hilger, http://sites.google.com/site/uhilger, all rights reserved
 */
public class Fader extends Animation {

    private Element we;

    private int mode;

    public static final String OPACITY_STYLE_FF = "opacity";

    public static final String OPACITY_STYLE_IE = "filter";

    public static final String IE_OPACITY_ATTR = "alpha(opacity=";

    public static final String IE_OPACITY_ATTR_TERMINATOR = ")";

    public static final int MODE_FADE_IN = 0;

    public static final int MODE_FADE_OUT = 1;

    /**
   * create a new Fader object
   * @param w  the Widget to apply a fade in or fade out effect on
   * @param mode  one of MODE_FADE_IN or MODE_FADE_OUT
   */
    public Fader(Widget w, int mode) {
        super();
        this.we = w.getElement();
        this.mode = mode;
    }

    /**
   * Immediately hide an element by setting its transparency to 100%
   * @param e  the element to hide
   */
    public static void hide(Widget w) {
        Element e = w.getElement();
        DOM.setStyleAttribute(e, Fader.OPACITY_STYLE_FF, Float.toString(0f));
        DOM.setStyleAttribute(e, Fader.OPACITY_STYLE_IE, Fader.IE_OPACITY_ATTR + Integer.toString(0) + Fader.IE_OPACITY_ATTR_TERMINATOR);
    }

    /**
   * apply the effect (implicitly called)
   * @param  progress  the internal progress value to which the animation 
   * needs to adjust its state
   */
    protected void onUpdate(double progress) {
        if (MODE_FADE_OUT == mode) {
            progress = 1.0 - progress;
        }
        DOM.setStyleAttribute(we, OPACITY_STYLE_FF, Double.toString(progress));
        DOM.setStyleAttribute(we, OPACITY_STYLE_IE, IE_OPACITY_ATTR + Double.toString(progress * 100.0) + IE_OPACITY_ATTR_TERMINATOR);
    }
}
