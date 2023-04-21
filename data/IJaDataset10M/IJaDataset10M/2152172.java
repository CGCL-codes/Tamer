package com.cosylab.vdct.application.components;

import com.cosylab.vdct.model.property.Property;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Janez Golob
 */
public class PropertiesTableCellRenderer extends JLabel implements TableCellRenderer {

    public PropertiesTableCellRenderer() {
        super();
        this.setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
        NodePropertiesTableModel model = (NodePropertiesTableModel) table.getModel();
        NodePropertiesRow row = (NodePropertiesRow) model.getRow(rowIndex);
        Property property = row.getProperty(toModel(table, vColIndex));
        if (property != null && property.validate() == false) {
            setBackground(Color.RED);
        } else {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
        }
        setFont(table.getFont());
        if (value != null) {
            setText(value.toString());
        } else {
            setText("");
        }
        if (property != null) {
            setToolTipText(property.getTooltip());
        }
        return this;
    }

    /**
         * Converts a visible column index to a column index in the model.
         * @param table
         * @param vColIndex
         * @return -1 if the index does not exist
         */
    public int toModel(JTable table, int vColIndex) {
        if (vColIndex >= table.getColumnCount()) {
            return -1;
        }
        return table.getColumnModel().getColumn(vColIndex).getModelIndex();
    }

    @Override
    public void invalidate() {
    }

    @Override
    public void validate() {
    }

    @Override
    public void revalidate() {
    }

    public void repaint(long tm, int x, int y, int width, int height) {
    }

    public void repaint(Rectangle r) {
    }

    public void repaint() {
    }

    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    }
}
