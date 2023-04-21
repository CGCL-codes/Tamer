package com.elibera.m.xml.proc;

import com.elibera.m.xml.display.DisplayElement;
import com.elibera.m.xml.display.VierEckElement;

public abstract class ProcVierEckElement extends ProcDisplayElement {

    public int getTopLeftX(DisplayElement el) {
        return ((VierEckElement) el).eckpunkte[0];
    }

    public int getTopLeftY(DisplayElement el) {
        return ((VierEckElement) el).eckpunkte[1];
    }

    public int getBottomRightX(DisplayElement el) {
        return ((VierEckElement) el).eckpunkte[2];
    }

    public int getBottomRightY(DisplayElement el) {
        return ((VierEckElement) el).eckpunkte[3];
    }

    public int getLastLineHeight(DisplayElement el) {
        return ((VierEckElement) el).eckpunkte[3] - ((VierEckElement) el).eckpunkte[1];
    }

    public void resetLastLineHeight(DisplayElement el, int height) {
        ((VierEckElement) el).eckpunkte[3] = height + ((VierEckElement) el).eckpunkte[1];
    }

    public int getLastLineWidth(DisplayElement el) {
        return ((VierEckElement) el).eckpunkte[2] - ((VierEckElement) el).eckpunkte[0];
    }

    public void resetXPositionForAlign(DisplayElement el2, int canvasWidth, int XFromRight) {
        VierEckElement el = (VierEckElement) el2;
        int w = getLastLineWidth(el);
        el.eckpunkte[2] = canvasWidth - XFromRight;
        el.eckpunkte[0] = el.eckpunkte[2] - w;
    }

    /**
	 * verschiebt die Y Position des Elements
	 * @param el
	 * @param newY
	 */
    public void resetYPosition(DisplayElement el2, int newY) {
        VierEckElement el = (VierEckElement) el2;
        el.eckpunkte[3] = newY + (el.eckpunkte[3] - el.eckpunkte[1]);
        el.eckpunkte[1] = newY;
    }
}
