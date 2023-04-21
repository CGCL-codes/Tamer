package org.nargila.treexml.component;

import javax.swing.*;
import java.awt.*;
import org.w3c.dom.Node;

public class ElementComponent extends JButton {

    public ElementComponent(String text) {
        super(text);
        setColors();
    }

    public ElementComponent(Node n) {
        super(n.getNodeName());
        setColors();
    }

    void setColors() {
        setBackground(new Color(0, 100, 0));
        setForeground(Color.white);
    }
}
