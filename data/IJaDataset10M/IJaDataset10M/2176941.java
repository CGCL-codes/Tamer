package ui.table.model;

import java.util.Observable;
import java.util.Observer;
import javax.swing.table.AbstractTableModel;
import ui.Messages;
import base.InternetCafeManager;
import base.backup.Backup;

@SuppressWarnings("serial")
public class BackupTableModel extends AbstractTableModel implements Observer {

    String[] columnNames = { Messages.getString("common.id"), Messages.getString("common.name"), Messages.getString("common.date"), Messages.getString("common.description"), Messages.getString("common.size") + " " + Messages.getString("common.unit.kylobyte") };

    public static final int BACKUP_ID_COLUMN = 0;

    public static final int BACKUP_NAME_COLUMN = 1;

    public static final int BACKUP_DATE_COLUMN = 2;

    public static final int BACKUP_DESCRIPTION_COLUMN = 3;

    public static final int BACKUP_SIZE_COLUMN = 4;

    public BackupTableModel() {
        super();
        InternetCafeManager.getInstance().addObserver(this);
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return InternetCafeManager.getInstance().getBackup().length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int column) {
        if (row < 0 || column < 0) return null;
        Backup[] backup = InternetCafeManager.getInstance().getBackup();
        Object result = null;
        switch(column) {
            case BACKUP_ID_COLUMN:
                result = backup[row].getId();
                break;
            case BACKUP_NAME_COLUMN:
                result = backup[row].getName();
                break;
            case BACKUP_DATE_COLUMN:
                result = backup[row].getDate().toString();
                break;
            case BACKUP_DESCRIPTION_COLUMN:
                result = backup[row].getDescription();
                break;
            case BACKUP_SIZE_COLUMN:
                result = backup[row].getSize() > 0 ? backup[row].getSize() : Messages.getString("backuptablemodel.message1");
                break;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Class getColumnClass(int column) {
        if (column < 0) return null;
        Class result = null;
        switch(column) {
            case BACKUP_ID_COLUMN:
                result = Integer.class;
                break;
            case BACKUP_NAME_COLUMN:
                result = String.class;
                break;
            case BACKUP_DATE_COLUMN:
                result = String.class;
                break;
            case BACKUP_DESCRIPTION_COLUMN:
                result = String.class;
                break;
            case BACKUP_SIZE_COLUMN:
                result = Long.class;
                break;
        }
        return result;
    }

    public boolean isCellEditable(int row, int column) {
        if (row < 0 || column < 0) return false;
        boolean result = false;
        switch(column) {
            case BACKUP_ID_COLUMN:
                result = false;
                break;
            case BACKUP_NAME_COLUMN:
                result = false;
                break;
            case BACKUP_DATE_COLUMN:
                result = false;
                break;
            case BACKUP_DESCRIPTION_COLUMN:
                result = true;
                break;
            case BACKUP_SIZE_COLUMN:
                result = false;
                break;
        }
        return result;
    }

    public void setValueAt(Object value, int row, int column) {
        if (row < 0 || column < 0) return;
        fireTableCellUpdated(row, column);
    }

    public void update(Observable observable, Object changedObject) {
        if (changedObject instanceof Backup) this.fireTableDataChanged();
    }
}
