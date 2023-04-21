package org.jmove.zui.util;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ColorDecoratorTableCellRenderer implements TableCellRenderer {

    private TableCellRenderer myDecoratedRenderer;

    private TableCellColorChooser myColorChooser;

    public ColorDecoratorTableCellRenderer(TableCellRenderer decoratedRenderer, TableCellColorChooser colorChooser) {
        myDecoratedRenderer = decoratedRenderer;
        myColorChooser = colorChooser;
    }

    public TableCellRenderer getDecoratedRenderer() {
        return myDecoratedRenderer;
    }

    public void setDecoratedRenderer(TableCellRenderer decoratedRenderer) {
        myDecoratedRenderer = decoratedRenderer;
    }

    public TableCellColorChooser getColorChooser() {
        return myColorChooser;
    }

    public void setColorChooser(TableCellColorChooser colorChooser) {
        myColorChooser = colorChooser;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component comp = myDecoratedRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (comp instanceof JComponent && myColorChooser != null) {
            Color color = myColorChooser.getTableCellColor(table, value, isSelected, hasFocus, row, column);
            JComponent jComp = (JComponent) comp;
            if (color != null) {
                jComp.setBackground(color);
            }
        }
        return comp;
    }

    public static interface TableCellColorChooser {

        Color getTableCellColor(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column);
    }
}
