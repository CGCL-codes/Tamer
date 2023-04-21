package com.sandboxpix.sbp;

import java.awt.Color;
import java.util.Vector;
import java.util.Enumeration;

public class SBP_ColorPick implements SBP_ColorPick_Interface, SBP_ColorEventCaster {

    private Color currentColor;

    private ColorEventSource colorEventSource = new ColorEventSource();

    private class ColorEventSource implements SBP_ColorListener, SBP_ColorEventCaster {

        private Vector colorEventListeners = new Vector();

        public void sbp_color(SBP_ColorEvent ce) {
            castSBP_ColorEvent(ce);
        }

        public void addSBP_ColorListener(SBP_ColorListener aListener) {
            colorEventListeners.addElement(aListener);
        }

        public void castSBP_ColorEvent(SBP_ColorEvent ce) {
            Vector castList = (Vector) colorEventListeners.clone();
            Enumeration anIterator = castList.elements();
            while (anIterator.hasMoreElements()) {
                SBP_ColorListener listener = (SBP_ColorListener) anIterator.nextElement();
                switch(ce.getEventID()) {
                    case SBP_ColorEvent.SBP_COLOR:
                        listener.sbp_color(ce);
                        break;
                }
            }
        }

        public void removeSBP_ColorListener(SBP_ColorListener aListener) {
            colorEventListeners.removeElement(aListener);
        }
    }

    public SBP_ColorPick() {
        currentColor = Color.black;
        colorEventSource.addSBP_ColorListener(this);
    }

    public SBP_ColorPick(Color aColor) {
        this();
        currentColor = new Color(aColor.getRGB());
    }

    public void addSBP_ColorEventSource(SBP_ColorEventCaster cec) {
        cec.addSBP_ColorListener(colorEventSource);
    }

    public void removeSBP_ColorEventSource(SBP_ColorEventCaster cec) {
        cec.removeSBP_ColorListener(colorEventSource);
    }

    public void addSBP_ColorListener(SBP_ColorListener aListener) {
        colorEventSource.addSBP_ColorListener(aListener);
    }

    public void castSBP_ColorEvent(SBP_ColorEvent ce) {
        colorEventSource.castSBP_ColorEvent(ce);
    }

    public void removeSBP_ColorListener(SBP_ColorListener aListener) {
        colorEventSource.removeSBP_ColorListener(aListener);
    }

    /**
 * Get the current color.
 * @return java.awt.Color
 */
    public Color getColor() {
        return currentColor;
    }

    public void sbp_color(SBP_ColorEvent ce) {
        currentColor = ce.getColor();
    }

    public void pickDefault() {
        selfPicked(Color.black);
    }

    public void selfPicked(Color c) {
        SBP_ColorEvent ce = new SBP_ColorEvent(this, SBP_ColorEvent.SBP_COLOR, c);
        castSBP_ColorEvent(ce);
    }
}
