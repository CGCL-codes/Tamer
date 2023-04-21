package gomule.dropCalc.gui;

import gomule.dropCalc.monsters.MonsterTuple;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class DCTableModel extends AbstractTableModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6556329099541788418L;

    public ArrayList tmRows;

    public ArrayList iTableModelListeners = new ArrayList();

    public int type = 0;

    public int iSelected = 2;

    public boolean dec = true;

    public DCTableModel() {
    }

    public Class getColumnClass(int c) {
        return String.class;
    }

    public int getColumnCount() {
        return 3;
    }

    public void refresh(HashMap iItems, int nDiff, int classKey, boolean dec) {
        this.dec = dec;
        type = 1;
        tmRows = new ArrayList();
        Iterator it = iItems.keySet().iterator();
        while (it.hasNext()) {
            MonsterTuple tSelected = (MonsterTuple) it.next();
            switch(nDiff) {
                case 0:
                    if (tSelected.getParent().getClassOfMon() == classKey) {
                        tmRows.add(new OutputRow(tSelected.getParent().getRealName() + " (" + tSelected.getParent().getMonDiff() + ")", tSelected.getArLvlName(), iItems.get(tSelected)));
                    }
                    break;
                case 1:
                    if (tSelected.getParent().getClassOfMon() == classKey && tSelected.getParent().getMonDiff().equals("N")) {
                        tmRows.add(new OutputRow(tSelected.getParent().getRealName() + " (" + tSelected.getParent().getMonDiff() + ")", tSelected.getArLvlName(), iItems.get(tSelected)));
                    }
                    break;
                case 2:
                    if (tSelected.getParent().getClassOfMon() == classKey && tSelected.getParent().getMonDiff().equals("NM")) {
                        tmRows.add(new OutputRow(tSelected.getParent().getRealName() + " (" + tSelected.getParent().getMonDiff() + ")", tSelected.getArLvlName(), iItems.get(tSelected)));
                    }
                    break;
                case 3:
                    if (tSelected.getParent().getClassOfMon() == classKey && tSelected.getParent().getMonDiff().equals("H")) {
                        tmRows.add(new OutputRow(tSelected.getParent().getRealName() + " (" + tSelected.getParent().getMonDiff() + ")", tSelected.getArLvlName(), iItems.get(tSelected)));
                    }
                    break;
            }
        }
        fireTableStructureChanged();
        fireTableChanged();
        sortCol(iSelected);
    }

    public void refresh(ArrayList mTuples, boolean dec) {
        this.dec = dec;
        type = 0;
        tmRows = new ArrayList();
        for (int x = 0; x < mTuples.size(); x = x + 1) {
            MonsterTuple tSelected = ((MonsterTuple) mTuples.get(x));
            Iterator TCIt = tSelected.getFinalTCs().keySet().iterator();
            while (TCIt.hasNext()) {
                String tcArr = (String) TCIt.next();
                tmRows.add(new OutputRow(tcArr, tSelected.getArLvlName(), tSelected.getFinalTCs().get(tcArr)));
            }
        }
        fireTableStructureChanged();
        fireTableChanged();
        sortCol(iSelected);
    }

    public String getColumnName(int arg0) {
        switch(type) {
            case 0:
                switch(arg0) {
                    case 0:
                        return "TC";
                    case 1:
                        return "Area";
                    case 2:
                        return "Probability";
                    default:
                        return "";
                }
            case 1:
                switch(arg0) {
                    case 0:
                        return "Name";
                    case 1:
                        return "Area";
                    case 2:
                        return "Probability";
                    default:
                        return "";
                }
        }
        return "";
    }

    public int getRowCount() {
        if (tmRows == null) {
            return 0;
        }
        return tmRows.size();
    }

    public Object getValueAt(int row, int col) {
        switch(col) {
            case 0:
                return ((OutputRow) tmRows.get(row)).getC0();
            case 1:
                return ((OutputRow) tmRows.get(row)).getC1();
            case 2:
                return ((OutputRow) tmRows.get(row)).getStrC2(dec);
            default:
                return new String("");
        }
    }

    public boolean isCellEditable(int arg0, int arg1) {
        return false;
    }

    public void addTableModelListener(TableModelListener pListener) {
        iTableModelListeners.add(pListener);
    }

    public void removeTableModelListener(TableModelListener pListener) {
        iTableModelListeners.remove(pListener);
    }

    public void fireTableChanged() {
        fireTableChanged(new TableModelEvent(this));
    }

    public void fireTableChanged(TableModelEvent pEvent) {
        for (int i = 0; i < iTableModelListeners.size(); i++) {
            ((TableModelListener) iTableModelListeners.get(i)).tableChanged(pEvent);
        }
    }

    public void setValueAt(Object value, int row, int col) {
    }

    public void reset() {
        tmRows = new ArrayList();
        fireTableChanged();
        this.fireTableStructureChanged();
    }

    public void sortCol(int headerCol) {
        iSelected = headerCol;
        Collections.sort(tmRows, new Comparator() {

            public int compare(Object pObj1, Object pObj2) {
                OutputRow lItem1 = (OutputRow) pObj1;
                OutputRow lItem2 = (OutputRow) pObj2;
                switch(iSelected) {
                    case 0:
                        return (lItem1.getC0().compareTo(lItem2.getC0()));
                    case 1:
                        return (lItem1.getC1().compareTo(lItem2.getC1()));
                    case 2:
                        return (lItem2.getObjC2().compareTo(lItem1.getObjC2()));
                }
                return 0;
            }
        });
        fireTableChanged();
    }
}
