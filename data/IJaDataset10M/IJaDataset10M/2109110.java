package net.infonode.gui.laf.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.6 $
 */
public class SlimComboBoxUI extends MetalComboBoxUI {

    public static Border FOCUS_BORDER = new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(0, 3, 0, 3));

    public static Border NORMAL_BORDER = new EmptyBorder(1, 4, 1, 4);

    protected ListCellRenderer createRenderer() {
        return new BasicComboBoxRenderer() {

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(index == -1 ? noFocusBorder : cellHasFocus ? FOCUS_BORDER : NORMAL_BORDER);
                return label;
            }
        };
    }
}
