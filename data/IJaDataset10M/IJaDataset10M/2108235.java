package org.argouml.ui;

import java.awt.Font;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalTheme;

/**
 * This class defines a variation on the default Metal Theme.
 */
public class JasonsBigTheme extends MetalTheme {

    private final ColorUIResource primary1 = new ColorUIResource(102, 102, 153);

    private final ColorUIResource primary2 = new ColorUIResource(153, 153, 204);

    private final ColorUIResource primary3 = new ColorUIResource(204, 204, 255);

    private final ColorUIResource secondary1 = new ColorUIResource(102, 102, 102);

    private final ColorUIResource secondary2 = new ColorUIResource(153, 153, 153);

    private final ColorUIResource secondary3 = new ColorUIResource(204, 204, 204);

    private final FontUIResource controlFont = new FontUIResource("SansSerif", Font.PLAIN, 14);

    private final FontUIResource systemFont = new FontUIResource("Dialog", Font.PLAIN, 14);

    private final FontUIResource windowTitleFont = new FontUIResource("SansSerif", Font.BOLD, 14);

    private final FontUIResource userFont = new FontUIResource("SansSerif", Font.PLAIN, 14);

    private final FontUIResource smallFont = new FontUIResource("Dialog", Font.PLAIN, 12);

    public String getName() {
        return "JasonsBig";
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

    protected ColorUIResource getSecondary1() {
        return secondary1;
    }

    protected ColorUIResource getSecondary2() {
        return secondary2;
    }

    protected ColorUIResource getSecondary3() {
        return secondary3;
    }

    public FontUIResource getControlTextFont() {
        return controlFont;
    }

    public FontUIResource getSystemTextFont() {
        return systemFont;
    }

    public FontUIResource getUserTextFont() {
        return userFont;
    }

    public FontUIResource getMenuTextFont() {
        return controlFont;
    }

    public FontUIResource getEmphasisTextFont() {
        return windowTitleFont;
    }

    public FontUIResource getSubTextFont() {
        return smallFont;
    }

    public FontUIResource getWindowTitleFont() {
        return windowTitleFont;
    }
}
