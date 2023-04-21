package org.jmeld.ui.swing;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class MultiLineHeaderRenderer extends JLabel implements TableCellRenderer {

    private Icon icon;

    public MultiLineHeaderRenderer() {
        ListCellRenderer renderer;
        LookAndFeel.installColorsAndFont(this, "TableHeader.background", "TableHeader.foreground", "TableHeader.font");
        LookAndFeel.installBorder(this, "TableHeader.cellBorder");
        setOpaque(false);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String str;
        str = (value == null) ? "" : value.toString();
        setText(str);
        setHorizontalAlignment(JLabel.CENTER);
        return this;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    private static final long serialVersionUID = 101783804743496189L;
}
