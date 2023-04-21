package org.jbrix.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class DefaultTreeListLabelRenderer extends JLabel implements TreeListLabelRenderer {

    public Component getTreeListLabelRendererComponent(TreeList treeList, JList list, Object value, int listIndex) {
        if (getBorder() == null) {
            setBorder(new EtchedBorder());
        }
        setHorizontalAlignment(JLabel.CENTER);
        setText(value.toString());
        return this;
    }
}
