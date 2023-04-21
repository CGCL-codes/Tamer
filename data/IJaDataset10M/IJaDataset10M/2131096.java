package gatchan.jedit.hyperlinks;

import org.gjt.sp.jedit.Mode;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.ServiceManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.*;
import java.util.Vector;
import java.util.Collections;
import java.util.Comparator;
import java.awt.*;

/**
 * @author Matthieu Casanova
 * @version $Id: Buffer.java 8190 2006-12-07 07:58:34Z kpouer $
 */
public class ModeServiceAssociationTableModel extends AbstractTableModel {

    private final Vector<Entry> modes;

    private final String col1;

    private final String col2;

    private final String defaultOption;

    private final String propertySuffix;

    private ModeServiceAssociationTableModel(String col1, String col2, String defaultOption, String propertySuffix) {
        this.col1 = col1;
        this.col2 = col2;
        this.defaultOption = defaultOption;
        this.propertySuffix = propertySuffix;
        Mode[] modes = jEdit.getModes();
        this.modes = new Vector<Entry>(modes.length);
        for (int i = 0; i < modes.length; i++) {
            this.modes.add(new Entry(modes[i].getName()));
        }
        Collections.sort(this.modes);
    }

    public static JTable getTable(String serviceName, String col2, String defaultValue, String propertySuffix) {
        ModeServiceAssociationTableModel tableModel = new ModeServiceAssociationTableModel("Mode", col2, defaultValue, propertySuffix);
        JTable table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        String[] serviceNames = ServiceManager.getServiceNames(serviceName);
        Vector<String> serviceVector = new Vector<String>(serviceNames.length + 2);
        serviceVector.add(null);
        serviceVector.add(defaultValue);
        for (int i = 0; i < serviceNames.length; i++) {
            serviceVector.add(serviceNames[i]);
        }
        Collections.sort(serviceVector, new Comparator<String>() {

            public int compare(String a, String b) {
                a = a == null ? "" : a;
                b = b == null ? "" : b;
                return a.compareToIgnoreCase(b);
            }
        });
        MyCellRenderer comboBox = new MyCellRenderer(serviceVector);
        table.setRowHeight(comboBox.getPreferredSize().height);
        TableColumn column = table.getColumnModel().getColumn(1);
        column.setCellRenderer(comboBox);
        column.setCellEditor(new DefaultCellEditor(new MyCellRenderer(serviceVector)));
        return table;
    }

    public int getColumnCount() {
        return 2;
    }

    public int getRowCount() {
        return modes.size();
    }

    @Override
    public Class getColumnClass(int col) {
        switch(col) {
            case 0:
            case 1:
                return String.class;
            default:
                throw new InternalError();
        }
    }

    public Object getValueAt(int row, int col) {
        Entry entry = modes.elementAt(row);
        switch(col) {
            case 0:
                return entry.mode;
            case 1:
                return entry.serviceName;
            default:
                throw new InternalError();
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 1;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (col == 0) return;
        Entry entry = modes.elementAt(row);
        switch(col) {
            case 1:
                entry.serviceName = (String) value;
                break;
            default:
                throw new InternalError();
        }
        fireTableRowsUpdated(row, row);
    }

    @Override
    public String getColumnName(int index) {
        switch(index) {
            case 0:
                return col1;
            case 1:
                return col2;
            default:
                throw new InternalError();
        }
    }

    public void save() {
        for (int i = 0; i < modes.size(); i++) {
            modes.get(i).save();
        }
    }

    class Entry implements Comparable<Entry> {

        private final String mode;

        private String serviceName;

        Entry(String mode) {
            this.mode = mode;
            serviceName = jEdit.getProperty("mode." + this.mode + '.' + propertySuffix);
        }

        void save() {
            if (defaultOption.equals(serviceName)) jEdit.resetProperty("mode." + mode + '.' + propertySuffix); else jEdit.setProperty("mode." + mode + '.' + propertySuffix, serviceName);
        }

        public int compareTo(Entry a) {
            return mode.compareToIgnoreCase(a.mode);
        }
    }

    private static class MyCellRenderer extends JComboBox implements TableCellRenderer {

        MyCellRenderer(Vector vector) {
            super(vector);
            setRequestFocusEnabled(false);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setSelectedItem(value);
            return this;
        }
    }
}
