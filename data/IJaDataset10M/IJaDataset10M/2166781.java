package com.billing.member;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author javamaniac < mrt.itnewbies@gmail.com >
 */
public class TableModelViewMember extends AbstractTableModel {

    private static final int nmMember = 0;

    private static final int password = 1;

    private static final int tipeMember = 2;

    private static final int status = 3;

    private static final int idTipeMember = 4;

    private static final int idMember = 5;

    private final ArrayList<Object> recordMember;

    private String[] headerTable;

    public TableModelViewMember(String[] titleTable) {
        super();
        setTitleTable(titleTable);
        recordMember = new ArrayList<Object>();
    }

    private void setTitleTable(String[] titleTable) {
        this.headerTable = titleTable;
    }

    @Override
    public int getRowCount() {
        return recordMember.size();
    }

    @Override
    public int getColumnCount() {
        return headerTable.length;
    }

    /** Method getColumnName ini berfungsi untuk mendapatkan nama kolom pada table 
     * header
     * @param kolom yang ingin ditampilkan nama kolomnya pada table header
     *
     * @return nama kolom pada table header
     */
    @Override
    public String getColumnName(final int kolom) {
        return headerTable[kolom];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final DomainTblModelMember tblModel = (DomainTblModelMember) recordMember.get(rowIndex);
        switch(columnIndex) {
            case nmMember:
                return tblModel.getNmMember();
            case password:
                return tblModel.getPassword();
            case tipeMember:
                return tblModel.getTipeMember();
            case status:
                return tblModel.getStatus();
            case idMember:
                return tblModel.getIdMember();
            case idTipeMember:
                return tblModel.getIdTipeMember();
            default:
                return new Object();
        }
    }

    public void addMember(final DomainTblModelMember listMember) {
        recordMember.add(listMember);
        fireTableRowsInserted(recordMember.size() - 1, recordMember.size() - 1);
    }

    public void clearTable() {
        recordMember.clear();
    }
}
