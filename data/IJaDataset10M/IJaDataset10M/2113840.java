package com.javamonks.jmmconvert;

import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author jsutton
 */
public class ConversionProgressCellRenderer extends JProgressBar implements TableCellRenderer {

    private static final long serialVersionUID = -3077768611240933028L;

    public ConversionProgressCellRenderer() {
        super();
        this.setStringPainted(true);
        this.setMinimum(0);
        this.setMaximum(100);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        if (value == null) {
            this.setString("Not Run Yet");
        } else if (((Double) value).doubleValue() == 1.0) {
            this.setString("Done");
        } else {
            this.setString("Running");
            this.setValue((int) (((Double) value).doubleValue() * 100));
            this.setToolTipText(Integer.toString((int) (((Double) value).doubleValue() * 100)));
        }
        return this;
    }

    @Override
    public void validate() {
    }

    @Override
    public void revalidate() {
    }

    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    }
}
