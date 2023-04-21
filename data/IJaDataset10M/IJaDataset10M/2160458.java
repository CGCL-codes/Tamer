package com.google.code.gtkjfilechooser.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import com.google.code.gtkjfilechooser.GtkStockIcon;
import com.google.code.gtkjfilechooser.Path;
import com.google.code.gtkjfilechooser.BookmarkManager.GtkBookmark;
import com.google.code.gtkjfilechooser.GtkStockIcon.Size;

public class FileComboBoxRenderer extends JLabel implements ListCellRenderer, UIResource {

    /**
	 * An empty <code>Border</code>. This field might not be used. To change the
	 * <code>Border</code> used by this renderer override the
	 * <code>getListCellRendererComponent</code> method and set the border of
	 * the returned component directly.
	 */
    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(LowerBorder.INSETS);

    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(LowerBorder.INSETS);

    protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;

    private JComboBox comboBox;

    public FileComboBoxRenderer(JComboBox comboBox) {
        super();
        this.comboBox = comboBox;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setName("ComboBox.listRenderer");
        Dimension size = getPreferredSize();
        size.height = 29;
        setPreferredSize(size);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getSelectionForeground());
            setForeground(list.getForeground());
        }
        setFont(list.getFont());
        if (value instanceof Path) {
            Path path = (Path) value;
            setText(path.getName());
            setIcon(GtkStockIcon.get(path.getIconName(), Size.GTK_ICON_SIZE_MENU));
        }
        if (comboBox != null) {
            setEnabled(comboBox.isEnabled());
            setComponentOrientation(comboBox.getComponentOrientation());
        }
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
        setBorder(border);
        if ((index + 1) < list.getModel().getSize()) {
            Object nextValue = list.getModel().getElementAt(index + 1);
            if (!(value instanceof GtkBookmark) && nextValue instanceof GtkBookmark) {
                setBorder(new LowerBorder(Color.GRAY, 1));
            }
        }
        return this;
    }

    private Border getNoFocusBorder() {
        Border border = UIManager.getBorder("List.cellNoFocusBorder");
        if (System.getSecurityManager() != null) {
            if (border != null) return border;
            return SAFE_NO_FOCUS_BORDER;
        } else {
            if (border != null && (noFocusBorder == null || noFocusBorder == DEFAULT_NO_FOCUS_BORDER)) {
                return border;
            }
            return noFocusBorder;
        }
    }
}
