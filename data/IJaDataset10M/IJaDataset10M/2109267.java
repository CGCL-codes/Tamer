package au.gov.qld.dnr.dss.v1.ranking;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.tree.*;
import javax.swing.border.*;

/**
 * Custom layered pane, which, when resized, resizes all its children to
 * its size.  Sort of like having a BorderLayout.CENTER for each of the
 * layers.
 */
class CustomLayeredPane extends JLayeredPane {

    Component _sizeRef;

    CustomLayeredPane(Component sizeRef) {
        _sizeRef = sizeRef;
        this.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent ev) {
                fillErUp(ev);
            }
        });
    }

    void fillErUp(ComponentEvent ev) {
        Rectangle bounds = getBounds();
        Border b = getBorder();
        Insets is = new Insets(0, 0, 0, 0);
        if (b != null) is = b.getBorderInsets(this);
        bounds.width = bounds.width - is.left - is.right;
        bounds.height = bounds.height - is.top - is.bottom;
        bounds.x = is.left;
        bounds.y = is.top;
        Component[] comps = getComponents();
        for (int i = 0; i < comps.length; i++) {
            Component c = comps[i];
            c.setBounds(bounds);
        }
        revalidate();
    }

    public Dimension getPreferredSize() {
        Dimension size = _sizeRef.getPreferredSize();
        return size;
    }
}
