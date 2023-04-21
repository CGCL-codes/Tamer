package org.armedbear.j;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

public final class DefaultLookAndFeel extends DefaultMetalTheme {

    private static final Preferences preferences = Editor.preferences();

    private final ColorUIResource primary1 = new ColorUIResource(0, 0, 0);

    private FontUIResource plainFont;

    public static void setLookAndFeel() {
        String lookAndFeelClassName = "javax.swing.plaf.metal.MetalLookAndFeel";
        Editor.lookAndFeel = preferences.getStringProperty(Property.LOOK_AND_FEEL);
        if (Editor.lookAndFeel == null) {
            if (Platform.isPlatformMacOSX()) Editor.lookAndFeel = "Aqua";
        }
        if (Editor.lookAndFeel != null) {
            if (Editor.lookAndFeel.equals("System")) {
                lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
            } else if (Editor.lookAndFeel.equals("Metal")) {
                ;
            } else if (Editor.lookAndFeel.equals("Motif")) {
                lookAndFeelClassName = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            } else if (Editor.lookAndFeel.equals("Windows")) {
                lookAndFeelClassName = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            } else if (Editor.lookAndFeel.equals("Aqua")) {
                lookAndFeelClassName = "com.apple.mrj.swing.MacLookAndFeel";
            } else if (Editor.lookAndFeel.equals("Nimbus")) {
                lookAndFeelClassName = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
            } else if (Editor.lookAndFeel.equals("GTK+")) {
                lookAndFeelClassName = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            } else {
                Editor.lookAndFeel = null;
            }
        }
        if (Editor.lookAndFeel == null) {
            MetalLookAndFeel.setCurrentTheme(new DefaultLookAndFeel());
            UIManager.put("Tree.collapsedIcon", Utilities.getIconFromFile("collapsed.png"));
            UIManager.put("Tree.expandedIcon", Utilities.getIconFromFile("expanded.png"));
        } else {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
        }
        try {
            UIManager.setLookAndFeel(lookAndFeelClassName);
        } catch (Exception e) {
        }
        UIManager.put("ToolBarUI", "org.armedbear.j.ToolBarUI");
        UIManager.put("ButtonUI", "org.armedbear.j.ButtonUI");
        UIManager.put("LabelUI", "org.armedbear.j.LabelUI");
    }

    private DefaultLookAndFeel() {
        String name = preferences.getStringProperty(Property.DIALOG_FONT_NAME);
        int size = preferences.getIntegerProperty(Property.DIALOG_FONT_SIZE);
        Font font = new Font(name, Font.PLAIN, size);
        plainFont = new FontUIResource(font);
    }

    public void addCustomEntriesToTable(UIDefaults table) {
        table.put("Button.border", BorderFactory.createRaisedBevelBorder());
        table.put("TextField.border", BorderFactory.createLoweredBevelBorder());
        table.put("SplitPaneUI", "javax.swing.plaf.basic.BasicSplitPaneUI");
        table.put("ScrollBarUI", "org.armedbear.j.ScrollBarUI");
        table.put("TreeUI", "javax.swing.plaf.basic.BasicTreeUI");
        table.put("SplitPane.dividerSize", new Integer(3));
        table.put("ScrollBar.background", new Color(0xe0e0e0));
        table.put("ScrollBar.foreground", new Color(0xc0c0c0));
        table.put("ScrollBar.track", new Color(0xe0e0e0));
        table.put("ScrollBar.trackHighlight", Color.black);
        table.put("ScrollBar.thumb", new Color(0xc0c0c0));
        table.put("ScrollBar.thumbHighlight", Color.white);
        table.put("ScrollBar.thumbDarkShadow", Color.black);
        table.put("ScrollBar.thumbShadow", new Color(0x808080));
        table.put("ScrollBar.width", new Integer(16));
        table.put("Button.textIconGap", new Integer(1));
        table.put("ToolTipUI", "org.armedbear.j.ToolTipUI");
    }

    protected ColorUIResource getPrimary1() {
        return primary1;
    }

    public FontUIResource getControlTextFont() {
        return plainFont;
    }

    public FontUIResource getSystemTextFont() {
        return plainFont;
    }

    public FontUIResource getUserTextFont() {
        return plainFont;
    }

    public FontUIResource getMenuTextFont() {
        return plainFont;
    }

    public FontUIResource getWindowTitleFont() {
        return plainFont;
    }

    public FontUIResource getSubTextFont() {
        return plainFont;
    }
}
