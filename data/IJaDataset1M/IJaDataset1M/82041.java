package com.nullfish.app.jfd2.command.embed.rename;

import javax.swing.table.AbstractTableModel;
import com.nullfish.app.jfd2.resource.JFDResource;
import com.nullfish.lib.vfs.VFile;

public class LumpSumRenameTableModel extends AbstractTableModel {

    private LumpSumRenamer renamer;

    public LumpSumRenameTableModel(LumpSumRenamer renamer) {
        this.renamer = renamer;
    }

    public int getRowCount() {
        return renamer.getCounts();
    }

    public int getColumnCount() {
        return 2;
    }

    /**
	 * セルの値を取得する。
	 */
    public Object getValueAt(int row, int column) {
        if (column == 0) {
            return renamer.getFrom(row);
        }
        if (column == 1) {
            return renamer.getDest(row);
        }
        return null;
    }

    /**
	 * セルの値を設定する。
	 */
    public void setValueAt(Object aValue, int row, int column) {
        if (column == 1) {
            renamer.setDest((VFile) aValue, row);
            renamer.updateStatus(row);
            fireTableRowsUpdated(row, row);
            fireTableDataChanged();
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }

    public String getColumnName(int column) {
        switch(column) {
            case 0:
                return JFDResource.LABELS.getString("orig_filename");
            case 1:
                return JFDResource.LABELS.getString("new_filename");
            default:
                return "";
        }
    }
}
