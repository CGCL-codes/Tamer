package be.vds.jtb.basics.client.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RootPaneContainer;
import javax.swing.UIManager;

/**
 * 
 * @author gautier
 */
public class WindowUtils {

    public static void centerWindow(Window window) {
        window.setLocationRelativeTo(null);
    }

    public static void maximizeJFrame(JFrame frame) {
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    public static void setSystemLAF() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            Logger.getLogger(WindowUtils.class.getName()).warning("Unable to load Nimbus LaF");
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e1) {
                Logger.getLogger(WindowUtils.class.getName()).severe(e1.getMessage());
            }
        }
    }

    public static void setNimbusLAF() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            Logger.getLogger(WindowUtils.class.getName()).warning("Unable to load Nimbus LaF");
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e1) {
                Logger.getLogger(WindowUtils.class.getName()).severe(e1.getMessage());
            }
        }
    }

    public static double getScreenHeight() {
        return Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    }

    public static double getScreenWidth() {
        return Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    }

    public static void resizeToHeight(Window window, double height) {
        double proportion = height / window.getHeight();
        window.setSize((int) (window.getWidth() * proportion), (int) height);
    }

    public static void resizeA4Portrait(Window window, double height) {
        window.setSize((int) (height * (Math.pow(2, (-0.5)))), (int) height);
    }

    public static void resizeA4LandScape(Window window, double width) {
        window.setSize((int) width, (int) (width * (Math.pow(2, (-0.5)))));
    }

    public static void centerWindow(Window w, Window parent) {
        w.setLocationRelativeTo(parent);
    }

    public static void waitCursor(RootPaneContainer container, boolean bSet) {
        Component gp = container.getGlassPane();
        if (bSet) {
            gp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (!gp.isVisible()) gp.setVisible(true);
        } else {
            gp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            if (gp.isVisible()) gp.setVisible(false);
        }
    }

    public static void errorDialog(Component parent, String msg, String title) {
        JOptionPane.showMessageDialog(parent, msg, (title == null ? "Error" : title), JOptionPane.ERROR_MESSAGE);
    }

    public static void warnDialog(Component parent, String msg, String title) {
        JOptionPane.showMessageDialog(parent, msg, (title == null ? "Warning" : title), JOptionPane.WARNING_MESSAGE);
    }

    public static void beep() {
        Runnable r = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
        if (r != null) r.run();
    }

    public static Window getTopLevelWindow(Component c) {
        Window w = null;
        Container container = null;
        do {
            container = c.getParent();
            if (container instanceof Window) {
                w = (Window) container;
                break;
            }
            c = container;
        } while (c != null);
        return w;
    }

    public static JFrame getParentFrame(Component c) {
        JFrame frame = null;
        Container container = null;
        do {
            container = c.getParent();
            if (container instanceof JFrame) {
                frame = (JFrame) container;
                break;
            }
            c = container;
        } while (c != null);
        return frame;
    }

    public static void dumpComponentsBounds(Container container) {
        dumpComponentsBounds(container, 0);
    }

    public static void dumpComponentsBounds(Component c, int level) {
        for (int i = 0; i < level; i++) System.out.print(" ");
        System.out.print(c);
        System.out.print(", width: ");
        System.out.print(c.getWidth());
        System.out.print(", height: ");
        System.out.print(c.getHeight());
        System.out.print(", bounds: ");
        System.out.println(c.getBounds());
        if (c instanceof Container) {
            Component[] children = ((Container) c).getComponents();
            for (int j = 0; j < children.length; j++) dumpComponentsBounds(children[j], level + 1);
        }
    }
}
