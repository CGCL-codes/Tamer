package pck_tap.Userinterface.rpe.frmRecipe.JPanels.JMashPanel.jPanelMash.model;

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import pck_tap.alg.Util;
import pck_tap.Userinterface.rpe.frmRecipe.JPanels.JMashPanel.jPanelMash.data.MashTableRowData;
import pck_tap.beerxml.recipe.Mash_Step;

public class TableAbstractTableModel extends AbstractTableModel {

    private Boolean debug = false;

    protected Vector vectorData;

    public TableAbstractTableModel() {
        vectorData = new Vector();
        rowsFillWithDefaults();
    }

    public void rowsFillWithDefaults() {
        this.rowsDelete();
        this.rowAddEmpty();
        TableModelEvent event = new TableModelEvent(this);
        fireTableChanged(event);
    }

    public void data_del_row(Integer index) {
        if (index < vectorData.size() && index > 0) {
            vectorData.removeElementAt(index);
            TableModelEvent event = new TableModelEvent(this);
            fireTableChanged(event);
        }
    }

    public void data_add_empty_row() {
        vectorData.addElement(new MashTableRowData(new Mash_Step()));
        TableModelEvent event = new TableModelEvent(this);
        fireTableChanged(event);
    }

    public boolean isthere_an_empty_row() {
        boolean b = false;
        Enumeration eNum = vectorData.elements();
        while (eNum.hasMoreElements()) {
            MashTableRowData t = (MashTableRowData) eNum.nextElement();
            if (Util.isNull(t.getMash_Step().getName())) {
                b = true;
                break;
            } else {
                b = false;
            }
        }
        return b;
    }

    public void removeAllRecords() {
        vectorData.removeAllElements();
    }

    public void setDefaultData() {
        vectorData.removeAllElements();
        vectorData.addElement(new MashTableRowData());
        TableModelEvent event = new TableModelEvent(this);
        fireTableChanged(event);
    }

    public int getRowCount() {
        return (vectorData == null) ? 0 : vectorData.size();
    }

    public int getColumnCount() {
        return 4;
    }

    public boolean isCellEditable(int nRow, int nCol) {
        boolean isEditable = false;
        if (nCol == 0) {
            isEditable = true;
        } else if (nCol == 1) {
            isEditable = true;
        } else if (nCol == 2) {
            isEditable = true;
        } else if (nCol == 3) {
            isEditable = true;
        }
        return isEditable;
    }

    public void setValueAt(Object p_value, int p_row, int p_col) {
        if (!((p_row < 0)) && !((p_row >= getRowCount()))) {
            MashTableRowData vTableRowDataMash = (MashTableRowData) vectorData.elementAt(p_row);
            Mash_Step ms = vTableRowDataMash.getMash_Step();
            if (p_col == 0) {
                ms.setName((String) p_value);
            } else if (p_col == 1) {
                ms.setStep_Time((Double) p_value);
            } else if (p_col == 2) {
                ms.setStep_Temp((Double) p_value);
            } else if (p_col == 3) {
                ms.setRamp_Time((Double) p_value);
            }
            vectorData.setElementAt(vTableRowDataMash, p_row);
            fireTableCellUpdated(p_row, p_col);
        }
    }

    public Object getValueAt(int nRow, int nCol) {
        if ((nRow < 0) || (nRow >= getRowCount())) {
            return "";
        }
        try {
            MashTableRowData vTableRowDataMash = (MashTableRowData) vectorData.elementAt(nRow);
            Mash_Step ms = vTableRowDataMash.getMash_Step();
            switch(nCol) {
                case 0:
                    return ms.getName();
                case 1:
                    return ms.getStep_Time();
                case 2:
                    return ms.getStep_Temp();
                case 3:
                    return ms.getRamp_Time();
            }
        } catch (Exception e) {
            e = null;
        }
        return "";
    }

    public Class getColumnClass(int column) {
        try {
            Class returnValue;
            if ((column >= 0) && (column < getColumnCount())) {
                returnValue = getValueAt(0, column).getClass();
            } else {
                returnValue = Object.class;
            }
            return returnValue;
        } catch (Exception e) {
            return null;
        }
    }

    public void setVector(Vector vector) {
        this.vectorData = vector;
    }

    public Vector getVector() {
        return vectorData;
    }

    public boolean isNull(Object obj) {
        return obj == null || obj.toString().trim().length() == 0;
    }

    public void rowsDelete() {
        pl(".rowsDelete");
        vectorData = new Vector();
        TableModelEvent event = new TableModelEvent(this);
        fireTableChanged(event);
    }

    public void rowAdd(MashTableRowData MashTableRowData) {
        pl(".rowAdd :" + MashTableRowData.toString());
        vectorData.addElement(MashTableRowData);
        TableModelEvent event = new TableModelEvent(this);
        fireTableChanged(event);
    }

    private void pl(String p) {
        if (debug) {
            System.out.println(this.getClass().getName() + p);
        }
    }

    public Vector rows() {
        return vectorData;
    }

    public void rowAddEmpty() {
        pl(".rowAddEmpty");
        vectorData.addElement(new MashTableRowData());
        TableModelEvent event = new TableModelEvent(this);
        fireTableChanged(event);
    }

    public void rowAddEmpty(Mash_Step ms) {
        pl(".rowAddEmpty");
        vectorData.addElement(new MashTableRowData(ms));
        TableModelEvent event = new TableModelEvent(this);
        fireTableChanged(event);
    }

    public Double sumTime() {
        try {
            Enumeration eNum = vectorData.elements();
            Double total = new Double(0);
            while (eNum.hasMoreElements()) {
                MashTableRowData vMashTableRowData = (MashTableRowData) eNum.nextElement();
                total = total + Util.nvl(vMashTableRowData.getMash_Step().getStep_Time());
            }
            return total;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Double sumRamp() {
        try {
            Enumeration eNum = vectorData.elements();
            Double total = new Double(0);
            while (eNum.hasMoreElements()) {
                MashTableRowData vMashTableRowData = (MashTableRowData) eNum.nextElement();
                total = total + Util.nvl(vMashTableRowData.getMash_Step().getRamp_Time());
            }
            return total;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
