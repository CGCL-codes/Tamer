package com.cosmos.swingb;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Miro
 */
public class CheckBoxListCellRenderer extends DefaultListCellRenderer {

    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JComponent cell;
        if (value instanceof JCheckBox) cell = (JCheckBox) value; else cell = this;
        cell.setComponentOrientation(list.getComponentOrientation());
        Color bg = null;
        Color fg = null;
        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index) {
            bg = UIManager.getColor("List.dropCellBackground");
            fg = UIManager.getColor("List.dropCellForeground");
            isSelected = true;
        }
        if (isSelected) {
            cell.setBackground(bg == null ? list.getSelectionBackground() : bg);
            cell.setForeground(fg == null ? list.getSelectionForeground() : fg);
        } else {
            cell.setBackground(list.getBackground());
            cell.setForeground(list.getForeground());
        }
        if (value instanceof Icon) {
            ((JLabel) cell).setIcon((Icon) value);
            ((JLabel) cell).setText("");
        } else if (value instanceof JCheckBox) {
        } else {
            ((JLabel) cell).setIcon(null);
            ((JLabel) cell).setText((value == null) ? "" : value.toString());
        }
        cell.setEnabled(list.isEnabled());
        cell.setFont(list.getFont());
        Border border = null;
        if (cellHasFocus) {
            if (isSelected) {
                border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = UIManager.getBorder("List.focusCellHighlightBorder");
            }
        } else {
            border = getNoFocusBorder();
        }
        cell.setBorder(border);
        return cell;
    }

    private Border getNoFocusBorder() {
        Border border = UIManager.getBorder("List.cellNoFocusBorder");
        if (System.getSecurityManager() != null) {
            if (border != null) {
                return border;
            }
            return SAFE_NO_FOCUS_BORDER;
        } else {
            if (border != null && (noFocusBorder == null || noFocusBorder == DEFAULT_NO_FOCUS_BORDER)) {
                return border;
            }
            return noFocusBorder;
        }
    }
}
