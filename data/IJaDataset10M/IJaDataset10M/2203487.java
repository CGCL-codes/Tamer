package eu.more.measurementservicegui.gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.lang.time.FastDateFormat;
import eu.more.JXTAErrorTransmissionService.generated.jaxb.impl.MessageImpl;
import eu.more.measurementservicegui.resources.LogMessages;

public class LogMessageTableModel extends AbstractTableModel {

    /**
   *
   */
    private static final long serialVersionUID = 3011071433966266014L;

    private List<String> columnNames = new ArrayList<String>();

    private Vector<MessageImpl> messages = new Vector<MessageImpl>();

    public LogMessageTableModel() {
        columnNames.add(LogMessages.getString("LogMessages.Title.Column.Date"));
        columnNames.add(LogMessages.getString("LogMessages.Title.Column.Nodename"));
        columnNames.add(LogMessages.getString("LogMessages.Title.Column.ErrorCode"));
        columnNames.add(LogMessages.getString("LogMessages.Title.Column.Message"));
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return Integer.class;
            case 3:
                return String.class;
            default:
                return null;
        }
    }

    public int getColumnCount() {
        return columnNames.size();
    }

    public String getColumnName(int columnIndex) {
        if (columnIndex < 0 || columnIndex > getColumnCount()) return null; else return columnNames.get(columnIndex);
    }

    public int getRowCount() {
        return messages.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex > messages.size()) return "";
        switch(columnIndex) {
            case 0:
                FastDateFormat instance = FastDateFormat.getInstance();
                MessageImpl messageImpl = messages.get(rowIndex);
                Calendar date = messageImpl.getDate();
                String format = instance.format(date);
                return format;
            case 1:
                return messages.get(rowIndex).getService();
            case 2:
                return new Integer(messages.get(rowIndex).getID());
            case 3:
                return messages.get(rowIndex).getMessage();
            default:
                return "";
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
    }

    public void setData(List<MessageImpl> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
        fireTableRowsUpdated(0, messages.size());
    }
}
