package org.knopflerfish.bundle.desktop.swing;

import java.awt.Font;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class KnopflerfishTheme extends DefaultMetalTheme {

    final ColorUIResource primary1 = new ColorUIResource(160, 133, 95);

    final ColorUIResource primary2 = new ColorUIResource(213, 185, 145);

    final ColorUIResource primary3 = new ColorUIResource(255, 238, 185);

    final ColorUIResource secondary1 = new ColorUIResource(112, 112, 112);

    final ColorUIResource secondary2 = new ColorUIResource(163, 163, 163);

    final ColorUIResource secondary3 = new ColorUIResource(229, 229, 220);

    FontUIResource controlFont;

    FontUIResource systemFont;

    FontUIResource userFont;

    FontUIResource smallFont;

    public String getName() {
        return "Knopflerfish";
    }

    public KnopflerfishTheme() {
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

    public ColorUIResource getSystemTextColor() {
        return MetalLookAndFeel.getBlack();
    }

    public FontUIResource getControlTextFont() {
        if (controlFont == null) {
            try {
                controlFont = new FontUIResource(Font.getFont("swing.plaf.metal.controlFont", new Font("Dialog", Font.PLAIN, 12)));
            } catch (Exception e) {
                controlFont = new FontUIResource("Dialog", Font.PLAIN, 12);
            }
        }
        return controlFont;
    }
}
