package org.grailrtls.client.gui.swing.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import org.grailrtls.libworldmodel.client.protocol.messages.DataResponseMessage.Attribute;
import org.grailrtls.util.FieldDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldInfoTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = { "Name", "Origin", "Data", "Created", "Expires" };

    private static final Logger log = LoggerFactory.getLogger(FieldInfoTableModel.class);

    private Attribute[] fields;

    public String getColumnName(int col) {
        if (col < 0 || col >= COLUMN_NAMES.length) {
            return null;
        }
        return COLUMN_NAMES[col];
    }

    public int getColumnCount() {
        return 5;
    }

    public int getRowCount() {
        if (fields == null) {
            return 0;
        }
        return fields.length;
    }

    public Object getValueAt(int row, int col) {
        if (fields == null) {
            return null;
        }
        if (row >= fields.length || row < 0) {
            log.warn("Row {} is out of bounds for {}.", row, fields.length);
            return null;
        }
        if (col >= 5 || col < 0) {
            log.warn("Col {} is out of bounds.", col);
            return null;
        }
        Attribute field = this.fields[row];
        switch(col) {
            case 0:
                return field.getAttributeName();
            case 1:
                return field.getOriginName();
            case 2:
                return FieldDecoder.decodeField(field.getAttributeName(), field.getData());
            case 3:
                return new Date(field.getCreationDate());
            case 4:
                return new Date(field.getExpirationDate());
        }
        return null;
    }

    public Attribute[] getFields() {
        return fields;
    }

    public void setFields(Attribute[] fields) {
        this.fields = fields;
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    fireTableDataChanged();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
