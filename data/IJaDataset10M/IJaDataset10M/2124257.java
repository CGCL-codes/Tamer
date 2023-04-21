package com.swingarchitect.layout;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class WrapPanel extends JPanel {

    public WrapPanel(JComponent component) {
        setBorder(BorderFactory.createEmptyBorder());
        EasyLayout layout = new EasyLayout(this);
        setLayout(layout);
        add(component, new Location(0, 1));
    }
}
