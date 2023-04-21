package com.jgoodies.plaf.plastic.theme;

import javax.swing.plaf.ColorUIResource;

/**
 * A theme with low saturated red primary colors and a light gray 
 * window background.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.1.1.1 $
 */
public class SkyRed extends AbstractSkyTheme {

    public String getName() {
        return "Sky Red";
    }

    protected ColorUIResource getPrimary1() {
        return Colors.RED_LOW_DARK;
    }

    protected ColorUIResource getPrimary2() {
        return Colors.RED_LOW_MEDIUM;
    }

    protected ColorUIResource getPrimary3() {
        return Colors.RED_LOW_LIGHTER;
    }
}
