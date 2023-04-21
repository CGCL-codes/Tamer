package com.qbrowser.property;

import com.qbrowser.QBrowserV2;
import com.qbrowser.container.MessageContainer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author takemura
 */
public class PropertyTable extends DefaultTableModel {

    static ResourceBundle resources = QBrowserV2.resources;

    final String[] columnNames = { resources.getString("qkey.proptable.header.key"), resources.getString("qkey.proptable.header.prop_type"), resources.getString("qkey.proptable.header.prop_value") };

    LinkedList list = null;

    private javax.jms.Message innermessage = null;

    ArrayList type_selection = null;

    public PropertyTable() {
        init();
        add_one_row(new Property());
    }

    public PropertyTable(int number_of_initial_rows) {
        init();
        for (int i = 0; i < number_of_initial_rows; i++) {
            add_one_row(new Property());
        }
    }

    @Override
    public int getRowCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch(column) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return Object.class;
        }
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    public Class<?> getCellClassAt(int row, int column) {
        return getColumnClass(column);
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (list == null) {
            return;
        }
        if (list.size() < row) {
            return;
        }
        Property p = (Property) list.get(row);
        if (p == null) {
            return;
        }
        try {
            switch(column) {
                case 0:
                    if (value instanceof String) {
                        p.setKey((String) value);
                    }
                    return;
                case 1:
                    if (value instanceof String) {
                        p.setProperty_type((String) value);
                    }
                    return;
                case 2:
                    p.setProperty_value(value);
                    return;
                default:
                    return;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (list == null) {
            return null;
        }
        Property p = (Property) list.get(row);
        if (p == null) {
            return "";
        }
        try {
            switch(column) {
                case 0:
                    return p.getKey();
                case 1:
                    return p.getProperty_type();
                case 2:
                    return p.getProperty_value();
                default:
                    return "Bad column value: " + column;
            }
        } catch (Exception e) {
            return ("Error: " + e);
        }
    }

    /**
         * Load and enumeration of messages into the table
         */
    public int load(Enumeration e) {
        if (e == null) {
            return 0;
        }
        list = new LinkedList();
        while (e.hasMoreElements()) {
            list.add(e.nextElement());
        }
        fireTableDataChanged();
        return list.size();
    }

    public Message getInnerMessage() {
        return innermessage;
    }

    public int load(MessageContainer mc) {
        if (mc == null) {
            return 0;
        }
        Message message = mc.getMessage();
        list = new LinkedList();
        try {
            for (Enumeration enu = message.getPropertyNames(); enu.hasMoreElements(); ) {
                String name = (enu.nextElement()).toString();
                Object propvalueobj = message.getObjectProperty(name);
                Property prop = new Property();
                prop.setKey(name);
                if (propvalueobj instanceof String) {
                    JTextArea jobj = new JTextArea(1, 10);
                    jobj.setText((String) propvalueobj);
                    jobj.setToolTipText(resources.getString("qkey.msg.msg230"));
                    prop.setProperty_value(jobj);
                } else {
                    prop.setProperty_value(propvalueobj);
                }
                prop.autoComplementTypeNme();
                list.add(prop);
            }
        } catch (JMSException jmse) {
        }
        fireTableDataChanged();
        this.innermessage = message;
        return list.size();
    }

    void init() {
        list = new LinkedList();
    }

    public void add_one_row(Property value) {
        list.add(value);
        fireTableDataChanged();
    }

    public void add_one_empty_row() {
        list.add(new Property());
        fireTableDataChanged();
    }

    public void deletePropertyAtRow(int row) {
        if (list == null) {
            return;
        }
        list.remove(row);
        fireTableDataChanged();
    }

    public Property getPropertyAtRow(int row) {
        if (list == null) {
            return null;
        }
        return ((Property) list.get(row));
    }
}
