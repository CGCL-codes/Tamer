package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.Popup;
import javax.swing.PopupFactory;

public class VsMenuDesignerPopupFactory extends PopupFactory {

    public Popup getPopup(Component owner, Component contents, int x, int y) throws IllegalArgumentException {
        if (contents == null) {
            throw new IllegalArgumentException("Popup.getPopup must be passed non-null contents");
        }
        if (contents instanceof JPopupMenu && isVsDesigner(owner)) {
            LightWeightPopup popup = new LightWeightPopup();
            popup.resetPopup(owner, contents, x, y);
            return popup;
        } else {
            return super.getPopup(owner, contents, x, y);
        }
    }

    private boolean isVsDesigner(Component owner) {
        Container parent = null;
        if (owner != null) {
            parent = (owner instanceof Container ? (Container) owner : owner.getParent());
        }
        for (Container p = parent; p != null; p = p.getParent()) {
            if (p instanceof JComponent && ((JComponent) p).getClientProperty("popup.layer") != null) {
                return true;
            } else if (p instanceof JRootPane) {
                if (p.getParent() instanceof JInternalFrame) {
                    continue;
                }
                parent = ((JRootPane) p).getLayeredPane();
            } else if (p instanceof Window) {
                if (parent == null) {
                    parent = p;
                }
                return false;
            } else if (p instanceof JApplet) {
                return false;
            }
        }
        return false;
    }
}
