package org.columba.core.gui.logdisplay;

import java.awt.Color;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * A list cell renderer for the LogRecordList.
 *
 * @author redsolo
 */
public class LogRecordListRenderer extends JLabel implements ListCellRenderer {

    /**
     * Creates the list cell renderer.
     */
    public LogRecordListRenderer() {
        super();
        setOpaque(true);
    }

    /** {@inheritDoc} */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        LogRecord record = (LogRecord) value;
        if (record.getLevel() == Level.SEVERE) {
            setForeground(Color.RED);
        } else if (record.getLevel() == Level.WARNING) {
            setForeground(Color.ORANGE);
        } else {
            setForeground(list.getForeground());
        }
        if (isSelected) {
            setBackground(list.getSelectionBackground());
        } else {
            setBackground(list.getBackground());
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(record.getMessage());
        setText(buffer.toString());
        return this;
    }

    /** {@inheritDoc} */
    public void validate() {
    }

    /** {@inheritDoc} */
    public void revalidate() {
    }

    /** {@inheritDoc} */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    }

    /** {@inheritDoc} */
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    }
}
