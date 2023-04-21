package org.lindenb.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.JComponent;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.lindenb.lang.RunnableObject;

/**
 * @author pierre
 *
 */
public class SwingUtils implements SwingConstants {

    protected SwingUtils() {
    }

    public static void center(Window window) {
        Dimension dim = window.getPreferredSize();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        window.setBounds(Math.max(0, (screen.width - dim.width) / 2), Math.max(0, (screen.height - dim.height) / 2), dim.width, dim.height);
    }

    public static void center(Window window, int marginH, int marginV) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        window.setBounds(Math.max(0, marginH / 2), Math.max(0, marginV / 2), screen.width - marginH, screen.height - marginV);
    }

    public static void center(Window window, int margin) {
        center(window, margin, margin);
    }

    public static void packAndCenter(Window window) {
        window.pack();
        center(window);
    }

    public static void setFontDeep(Component root, Font font) {
        root.setFont(font);
        if (root instanceof JComponent) {
            JComponent c = JComponent.class.cast(root);
            for (int i = 0; i < c.getComponentCount(); ++i) {
                setFontDeep(c.getComponent(i), font);
            }
        }
    }

    public static void setFontSize(Component root, int fontSize) {
        Font f = root.getFont();
        if (f != null) {
            root.setFont(new Font(f.getName(), f.getStyle(), fontSize));
        }
        if (root instanceof JComponent) {
            JComponent c = JComponent.class.cast(root);
            for (int i = 0; i < c.getComponentCount(); ++i) {
                setFontSize(c.getComponent(i), fontSize);
            }
        }
    }

    public static void setFontSize(Component root, float fontSize) {
        Font f = root.getFont();
        root.setFont(new Font(f.getName(), f.getStyle(), (int) Math.ceil(f.getSize() * fontSize)));
        if (root instanceof JComponent) {
            JComponent c = JComponent.class.cast(root);
            for (int i = 0; i < c.getComponentCount(); ++i) {
                setFontSize(c.getComponent(i), fontSize);
            }
        }
    }

    public static <T extends Component> T findComponentByName(Component root, String name, Class<T> clazz) {
        if (clazz.isInstance(root) && name.equals(root.getName())) {
            return clazz.cast(root);
        } else if (root instanceof JComponent) {
            JComponent c = JComponent.class.cast(root);
            for (int i = 0; i < c.getComponentCount(); ++i) {
                T t = findComponentByName(c.getComponent(i), name, clazz);
                if (t != null) return t;
            }
        }
        return null;
    }

    public static JComponent withFont(JComponent c, String fontName, int fontFace, int fontSize) {
        return withFont(c, new Font(fontName, fontFace, fontSize));
    }

    public static JComponent withFont(JComponent c, Font font) {
        c.setFont(font);
        return c;
    }

    public static JComponent withToolTipText(JComponent c, String tooltip) {
        c.setToolTipText(tooltip);
        return c;
    }

    public static JComponent withName(JComponent c, String name) {
        c.setName(name);
        return c;
    }

    public static JComponent withPreferredSize(JComponent c, Dimension preferredSize) {
        c.setPreferredSize(preferredSize);
        return c;
    }

    public static JComponent withPreferredSize(JComponent c, int width, int height) {
        return withPreferredSize(c, new Dimension(width, height));
    }

    /** show a window in the SWING thread
 * @param  w the window to be shown
 */
    public static void show(Window w) {
        SwingUtilities.invokeLater(new RunnableObject<Window>(w) {

            @Override
            public void run() {
                getObject().setVisible(true);
            }
        });
    }
}
