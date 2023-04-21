package com.memoire.vainstall.builder.util;

import java.awt.Color;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 *
 * @see
 *
 * @author Henrik Falk
 * @version $Id: VAInstallTheme.java,v 1.1 2001/09/28 19:41:42 hfalk Exp $
 */
public class VAInstallTheme extends DefaultMetalTheme {

    private final ColorUIResource primary1 = new ColorUIResource(102, 153, 153);

    private final ColorUIResource primary2 = new ColorUIResource(128, 192, 192);

    private final ColorUIResource primary3 = new ColorUIResource(159, 235, 235);

    private static FontUIResource normalPlainFont = new FontUIResource(new java.awt.Font("TimesRoman", java.awt.Font.PLAIN, 14));

    private static FontUIResource normalBoldFont = new FontUIResource(new java.awt.Font("TimesRoman", java.awt.Font.BOLD, 12));

    public VAInstallTheme() {
        super();
    }

    /**
     * Name of this Theme
     */
    public String getName() {
        return "VAInstall";
    }

    protected ColorUIResource getPrimary1() {
        return primary1;
    }

    protected ColorUIResource getPrimary2() {
        return primary2;
    }

    protected ColorUIResource getPrimary3() {
        return primary3;
    }

    public FontUIResource getControlTextFont() {
        return normalPlainFont;
    }

    public FontUIResource getSystemTextFont() {
        return normalBoldFont;
    }

    public FontUIResource getUserTextFont() {
        return normalBoldFont;
    }

    public FontUIResource getMenuTextFont() {
        return normalBoldFont;
    }

    public FontUIResource getWindowTitleFont() {
        return normalBoldFont;
    }

    public FontUIResource getSubTextFont() {
        return normalBoldFont;
    }
}
