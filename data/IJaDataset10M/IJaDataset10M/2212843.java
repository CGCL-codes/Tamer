package com.metanology.mde.utils;

/**
 * @author drykovan
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MarkerBlankFactory {

    private static MarkerBlankCollection blanks = null;

    public static void reset() {
        blanks = null;
    }

    public static MarkerBlankCollection getBlanks() {
        if (blanks == null) blanks = new MarkerBlankCollection();
        return blanks;
    }

    public static void addBlank(MarkerBlank blank) {
        MarkerBlankCollection cBlanks = getBlanks();
        cBlanks.add(blank);
    }

    public static void delBlank(MarkerBlank blank) {
        MarkerBlankCollection cBlanks = getBlanks();
        blank.setAction(MarkerBlank.ACTION_DEL);
        cBlanks.add(blank);
    }
}
