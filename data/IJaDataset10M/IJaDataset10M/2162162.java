package spamwatch.base.store;

import java.util.BitSet;
import java.util.Date;
import java.util.Iterator;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import spamwatch.base.account.Account.FIELD;
import spamwatch.filter.Classification;
import spamwatch.filter.FilterRating;

public class FolderTableModel extends AbstractTableModel {

    private EMailFolder emailFolder;

    private FIELD[] fields;

    private BitSet checkBits;

    public FolderTableModel(EMailFolder emailFolder, FIELD[] fields) {
        this.emailFolder = emailFolder;
        this.fields = fields;
        checkBits = new BitSet();
        addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                int first = e.getFirstRow();
                int last = e.getLastRow();
                if (first < 0 && last < 0) {
                    return;
                }
                first = Math.max(first, 0);
                if (e.getType() == TableModelEvent.DELETE) {
                    deleteRangeFromCheckBits(first, last);
                } else if (e.getType() == TableModelEvent.INSERT) {
                    insertRangeToCheckBits(first, last);
                } else if (e.getType() == TableModelEvent.UPDATE) {
                    if (last > FolderTableModel.this.emailFolder.getNumberOfMessages()) {
                        if (checkBits.cardinality() > 0) {
                            checkBits.clear();
                            fireTableDataChanged();
                        }
                    }
                }
            }
        });
    }

    @Override
    public String getColumnName(int column) {
        return fields[column].toString();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(fields[columnIndex]) {
            case SELECTED:
                return Boolean.class;
            case CLASSIFICATION:
                return Classification.class;
            case SENDER:
                return AddressInfo.class;
            case SUBJECT:
                return String.class;
            case SIZE:
                return Integer.class;
            case DATE:
                return Date.class;
            case RATING:
                return FilterRating.class;
        }
        return null;
    }

    public int getColumnCount() {
        return fields.length;
    }

    public int getRowCount() {
        return emailFolder.getNumberOfMessages();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return fields[column].equals(FIELD.SELECTED);
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (isCellEditable(row, column)) {
            setCheck((Boolean) value, row);
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        EMailStore store = getEMailStore(rowIndex);
        switch(fields[columnIndex]) {
            case SELECTED:
                return checkBits.get(rowIndex);
            case CLASSIFICATION:
                return store.getFlagClassification();
            case SENDER:
                Iterator<AddressInfo> senders = store.getSender();
                if (senders.hasNext()) {
                    return senders.next();
                } else {
                    return "";
                }
            case SUBJECT:
                return store.getSubject();
            case SIZE:
                return store.getSize();
            case DATE:
                return store.getDate();
            case RATING:
                return store.getFilterResult().getDecidingRating();
        }
        return null;
    }

    public EMailStore getEMailStore(int row) {
        EMailStore store = emailFolder.getEMailStore(row);
        return store;
    }

    public void setCheck(boolean newState, int... rows) {
        for (int i : rows) {
            checkBits.set(i, newState);
        }
        fireTableRowsUpdated(rows);
    }

    public void checkAll(boolean newState) {
        checkBits.set(0, getRowCount(), newState);
        fireTableRowsUpdated(0, getRowCount() - 1);
    }

    public int[] getCheckedRows() {
        int[] result = new int[checkBits.cardinality()];
        int count = 0;
        for (int i = checkBits.nextSetBit(0); i >= 0; i = checkBits.nextSetBit(i + 1)) {
            result[count] = i;
            count++;
        }
        return result;
    }

    public boolean areAllChecked() {
        return checkBits.cardinality() == getRowCount();
    }

    private void fireTableRowsUpdated(int[] rows) {
        if (rows.length == 0) {
            return;
        }
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i : rows) {
            min = Math.min(min, i);
            max = Math.max(max, i);
        }
        fireTableRowsUpdated(min, max);
    }

    private void deleteRangeFromCheckBits(int first, int last) {
        int oldLength = checkBits.length();
        BitSet tail = null;
        if (oldLength > last) {
            tail = checkBits.get(last + 1, oldLength);
        }
        if (oldLength >= first) {
            checkBits.clear(first, oldLength);
        }
        if (tail != null) {
            for (int i = tail.nextSetBit(0); i >= 0; i = tail.nextSetBit(i + 1)) {
                checkBits.set(first + i);
            }
        }
    }

    private void insertRangeToCheckBits(int first, int last) {
        int oldLength = checkBits.length();
        BitSet tail = null;
        if (oldLength >= first) {
            tail = checkBits.get(first, oldLength);
            checkBits.clear(first, oldLength);
        }
        if (tail != null) {
            for (int i = tail.nextSetBit(0); i >= 0; i = tail.nextSetBit(i + 1)) {
                checkBits.set(last + 1 + i);
            }
        }
    }

    public boolean areAllChecked(int[] rows) {
        for (int i : rows) {
            if (!checkBits.get(i)) {
                return false;
            }
        }
        return true;
    }
}
