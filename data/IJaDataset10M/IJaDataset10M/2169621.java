package org.mitre.rt.client.ui.cchecks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.mitre.rt.rtclient.ValueType;

public class ComplexValueTableModel extends AbstractTableModel {

    private static final Logger logger = Logger.getLogger(ComplexValueTableModel.class.getPackage().getName());

    public static final int VALUE = 0;

    private static final List<String> columnHeaders = new ArrayList<String>() {

        {
            add("Value");
        }
    };

    private List<String> data;

    public ComplexValueTableModel() {
        super();
        data = new ArrayList<String>();
    }

    public ComplexValueTableModel(ValueType parent) {
        List<String> dataList = parent.getItemList();
        this.data = dataList;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    public void processData(XmlObject parent, XmlObject dataContainer) {
        List<String> myData = null;
        if (parent != null) {
            myData = ((ValueType) parent).getItemList();
        } else {
            myData = Collections.emptyList();
        }
        this.data = myData;
    }

    @Override
    public String getColumnName(int index) {
        return columnHeaders.get(index);
    }

    @Override
    public int getRowCount() {
        return (this.data == null) ? 0 : this.data.size();
    }

    @Override
    public int getColumnCount() {
        return (columnHeaders == null) ? 0 : columnHeaders.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return (this.data == null) ? null : this.data.get(rowIndex);
    }

    @Override
    public void setValueAt(Object obj, int row, int column) {
        if ((obj != null) && (row <= this.data.size())) {
            this.data.set(row, obj.toString());
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return (data == null || getRowCount() == 0) ? Object.class : getValueAt(0, columnIndex).getClass();
    }

    public List<String> getData() {
        return this.data;
    }

    public void updateTableDisplay() {
        this.fireTableDataChanged();
    }
}
