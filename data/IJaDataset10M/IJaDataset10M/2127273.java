package com.sparkit.extracta.examples.models.javalobby;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import org.w3c.dom.*;

/**
 * This is a special class for rendering the Dom tree nodes.
 * Depending on the value's instance it returns a JLabel with
 * appropriate content.
 *
 * @version 1.0
 * @author Bostjan Vester
 * @author Dejan Pazin
 * @author Dominik Roblek
 */
public class DomTreeCellRenderer implements TreeCellRenderer {

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value != null) {
            if (value instanceof Text) {
                return new JLabel(((Text) value).getNodeValue());
            } else if (value instanceof Node) {
                return new JLabel("JavaLobby");
            } else {
                return new JLabel(value.toString());
            }
        }
        return new JLabel();
    }
}
