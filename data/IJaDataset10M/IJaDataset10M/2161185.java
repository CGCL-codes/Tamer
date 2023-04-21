package com.jgoodies.looks.plastic.theme;

import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;
import com.jgoodies.looks.plastic.PlasticScrollBarUI;

/**
 * A theme with low saturated yellow primary colors and a light brown
 * window background.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.5 $
 */
public class DesertYellow extends DesertBlue {

    public String getName() {
        return "Desert Yellow";
    }

    protected ColorUIResource getPrimary2() {
        return Colors.YELLOW_LOW_MEDIUM;
    }

    protected ColorUIResource getPrimary3() {
        return Colors.YELLOW_LOW_LIGHTEST;
    }

    public ColorUIResource getTitleTextColor() {
        return Colors.GRAY_DARKER;
    }

    public ColorUIResource getMenuItemSelectedBackground() {
        return Colors.YELLOW_LOW_MEDIUMDARK;
    }

    public void addCustomEntriesToTable(UIDefaults table) {
        super.addCustomEntriesToTable(table);
        Object[] uiDefaults = { "ScrollBar.is3DEnabled", Boolean.TRUE, PlasticScrollBarUI.MAX_BUMPS_WIDTH_KEY, new Integer(30) };
        table.putDefaults(uiDefaults);
    }
}
