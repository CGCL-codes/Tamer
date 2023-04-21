package de.itar.swing.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import de.itar.resources.Const;
import de.itar.exceptions.BiboExceptionHelper;
import de.itar.logic.HjDesc;
import de.itar.logic.MediumDesc;
import de.itar.swing.MainFrame;
import de.itar.swing.dialogs.MediumEditDialog;
import hm.core.utils.StringHelper;

public class MediumTableModel extends AbstractTableModel {

    private List<MediumDesc> lstMediumDescs;

    private HjDesc hjDesk;

    private int sortedBy = -1;

    private boolean aufsteigend = true;

    public MediumTableModel(HjDesc hjDesk) {
        this.hjDesk = hjDesk;
        this.lstMediumDescs = new ArrayList<MediumDesc>();
        for (int i = 0; i < hjDesk.getBookCount(); i++) {
            this.lstMediumDescs.add(hjDesk.getBuchDesc(i));
        }
        Collections.sort(lstMediumDescs, new MediumTableComperator(0, aufsteigend));
        this.fireTableStructureChanged();
    }

    public int getColumnCount() {
        return hjDesk.getAttributeCount() + 1;
    }

    public int getRowCount() {
        return lstMediumDescs.size();
    }

    public String getColumnName(int column) {
        if (column < hjDesk.getAttributeCount()) {
            String result = hjDesk.getAttributes()[column];
            return StringHelper.firstCharToUpperCase(result);
        } else {
            return "Status";
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex < hjDesk.getAttributeCount()) {
            return lstMediumDescs.get(rowIndex).getAttributeValue(columnIndex);
        } else {
            MediumDesc md = lstMediumDescs.get(rowIndex);
            if (md.isLend()) {
                StringBuffer result = new StringBuffer();
                result.append(Const.TABLE_ISLENDSINCE_1);
                result.append(md.getLendName());
                result.append(Const.TABLE_ISLENDSINCE_2);
                result.append(md.getLendDays());
                result.append(Const.TABLE_ISLENDSINCE_3);
                return result.toString();
            } else {
                return "-";
            }
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public List<MediumDesc> getViewList() {
        return lstMediumDescs;
    }

    public MediumDesc getMediumDesc(int row) {
        return (MediumDesc) lstMediumDescs.get(row);
    }

    public int isSortedBy() {
        return sortedBy;
    }

    public void setSortedBy(int sortedBy) {
        this.sortedBy = sortedBy;
    }

    public boolean isAufsteigend() {
        return aufsteigend;
    }

    public void setAufsteigend(boolean aufsteigend) {
        this.aufsteigend = aufsteigend;
    }

    public void showDialog(int row, boolean editable) throws Exception {
        try {
            if (editable) {
                new MediumEditDialog(MainFrame.getInstance(), lstMediumDescs.get(row), MediumEditDialog.EDIT_ENTRY).setVisible(true);
            } else {
                new MediumEditDialog(MainFrame.getInstance(), lstMediumDescs.get(row), MediumEditDialog.SHOW_ENTRY).setVisible(true);
            }
            MainFrame.getInstance().refreshTable();
        } catch (Throwable t) {
            BiboExceptionHelper.showErrorDialog(t);
        }
    }
}
