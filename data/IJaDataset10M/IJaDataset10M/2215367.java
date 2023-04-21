package com.ynhenc.gis.ui.viewer_02.style_editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Stroke;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import com.ynhenc.gis.ui.resource.GetTextInterface;

public abstract class Renderer_00_Object implements ListCellRenderer, TableCellRenderer {

    public final void configureRenderer(JLabel renderer, Object value) {
        if (value != null) {
            Icon icon = this.getIcon(value);
            if (icon != null) {
                renderer.setIcon(icon);
                renderer.setText("");
            } else {
                renderer.setIcon(null);
                renderer.setText("" + value);
            }
            if (value instanceof GetTextInterface) {
                GetTextInterface gt = (GetTextInterface) value;
                renderer.setText(gt.getText());
            }
        } else {
            renderer.setIcon(null);
            renderer.setText("��");
        }
    }

    public final Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        listRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        this.configureRenderer(listRenderer, value);
        return listRenderer;
    }

    public final Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        tableRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        this.configureRenderer(tableRenderer, value);
        return tableRenderer;
    }

    public abstract Icon getIcon(Object vlaue);

    private static final DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();

    private static final DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer();
}
