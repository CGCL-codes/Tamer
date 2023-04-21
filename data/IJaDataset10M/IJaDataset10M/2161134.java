package net.sourceforge.contactmanager.uitutils;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class ParentSearch {

    public static JDesktopPane getDesktop(Component component) {
        if (component instanceof JDesktopPane) {
            return (JDesktopPane) component;
        }
        Container parent = component.getParent();
        while (parent != null) {
            if (parent instanceof JDesktopPane) {
                return (JDesktopPane) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    public static JInternalFrame getInternalFrame(Component component) {
        if (component instanceof JInternalFrame) {
            return (JInternalFrame) component;
        }
        Container parent = component.getParent();
        while (parent != null) {
            if (parent instanceof JInternalFrame) {
                return (JInternalFrame) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }
}
