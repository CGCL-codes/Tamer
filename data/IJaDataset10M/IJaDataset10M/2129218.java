package com.mebigfatguy.patchanim.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Utils {

    public enum Sizing {

        Width, Height, Both
    }

    private Utils() {
    }

    public static JPanel createFormPanel(JLabel label, JComponent component) {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout(4, 4));
        label.setLabelFor(component);
        p.add(label, BorderLayout.WEST);
        p.add(component, BorderLayout.CENTER);
        return p;
    }

    public static void limitPanelHeight(JPanel p, JComponent limitingComponent) {
        Dimension ps = limitingComponent.getPreferredSize();
        Dimension ms = p.getMaximumSize();
        ms.height = ps.height;
        p.setMaximumSize(ms);
    }

    public static void sizeUniformly(Sizing sizing, JComponent... components) {
        int width = 0;
        int height = 0;
        for (JComponent comp : components) {
            Dimension d = comp.getPreferredSize();
            if (d.width > width) width = d.width;
            if (d.height > height) height = d.height;
        }
        for (JComponent comp : components) {
            Dimension minD = comp.getMinimumSize();
            if (sizing != Sizing.Height) minD.width = width;
            if (sizing != Sizing.Width) minD.height = height;
            comp.setMinimumSize(minD);
            Dimension maxD = comp.getMaximumSize();
            if (sizing != Sizing.Height) maxD.width = width;
            if (sizing != Sizing.Width) maxD.height = height;
            comp.setMaximumSize(maxD);
            Dimension prefD = comp.getPreferredSize();
            if (sizing != Sizing.Height) prefD.width = width;
            if (sizing != Sizing.Width) prefD.height = height;
            comp.setPreferredSize(prefD);
        }
    }
}
