package com.jiajun.ui;

import java.math.BigDecimal;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import com.jiajun.model.Color;
import com.jiajun.model.PeiLiao;
import com.jiajun.util.Status;

public class PeiliaoTableModel extends AbstractTableModel {

    private Vector<String> columnNames;

    private Vector<PeiLiao> data;

    private int count = 0;

    private BigDecimal all = BigDecimal.ZERO;

    public PeiliaoTableModel(Color color, int type1, int type2) {
        data = Status.getDefault().getPeiliaos(color, type1, type2);
        String name = (type1 == 0 ? "����" : "����") + (type2 == 0 ? "��֬" : "ɫ��");
        columnNames = new Vector<String>();
        columnNames.add(name);
        columnNames.add("����");
        for (int i = 0; i < data.size(); i++) {
            all = all.add(new BigDecimal(data.get(i).getRatio()));
        }
        calc();
    }

    private void calc() {
        for (PeiLiao pl : data) {
            BigDecimal ratio = new BigDecimal(pl.getRatio());
            BigDecimal result = BigDecimal.valueOf(count).multiply(ratio.divide(all, 2, BigDecimal.ROUND_HALF_EVEN));
            pl.setCount(result.toPlainString());
        }
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PeiLiao rowdata = data.get(rowIndex);
        if (columnIndex == 0) {
            return rowdata.getName();
        } else if (columnIndex == 1 && !all.equals(BigDecimal.ZERO)) {
            return rowdata.getCount();
        } else {
            return "";
        }
    }

    public void setCount(int count) {
        this.count = count;
        calc();
        fireTableDataChanged();
    }

    public Vector<PeiLiao> get() {
        return data;
    }
}
