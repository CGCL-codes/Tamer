package org.argouml.uml;

import java.beans.PropertyChangeEvent;
import java.util.Vector;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import org.argouml.kernel.DelayedChangeNotify;
import org.tigris.gef.util.Predicate;
import org.tigris.gef.util.PredicateTrue;

public class TableModelComposite extends AbstractTableModel implements TableModelTarget {

    Vector _rowObjects = new Vector();

    Vector _colDescs = new Vector();

    boolean _allowAddition = false;

    boolean _allowRemoval = false;

    Predicate _pred = PredicateTrue.theInstance();

    public TableModelComposite() {
        initColumns();
    }

    public void initColumns() {
    }

    public void addColumn(ColumnDescriptor cd) {
        _colDescs.addElement(cd);
    }

    public void setAllowAddition(boolean b) {
        _allowAddition = b;
    }

    public void setAllowRemoval(boolean b) {
        _allowRemoval = b;
    }

    public void setTarget(Object target) {
        Vector rowObjects = rowObjectsFor(target);
        _rowObjects = rowObjects;
        fireTableStructureChanged();
    }

    public Vector rowObjectsFor(Object t) {
        System.out.println("default rowObjectsFor called. bad!");
        return new Vector();
    }

    public Vector getRowObjects() {
        return _rowObjects;
    }

    /** this is  at
   * @param p
   */
    public void setFilter(Predicate p) {
        _pred = p;
    }

    public int getColumnCount() {
        return _colDescs.size();
    }

    public String getColumnName(int c) {
        if (c < _colDescs.size()) {
            ColumnDescriptor cd = (ColumnDescriptor) _colDescs.elementAt(c);
            return cd.getName();
        }
        return "XXX";
    }

    public Class getColumnClass(int c) {
        if (c < _colDescs.size()) {
            ColumnDescriptor cd = (ColumnDescriptor) _colDescs.elementAt(c);
            return cd.getColumnClass();
        }
        System.out.println("asdwasd");
        return String.class;
    }

    public boolean isCellEditable(int row, int col) {
        if (row < 0 || row >= _rowObjects.size()) return false;
        if (col < 0 || col >= _colDescs.size()) return false;
        Object rowObj = _rowObjects.elementAt(row);
        ColumnDescriptor cd = (ColumnDescriptor) _colDescs.elementAt(col);
        return cd.isEditable(rowObj);
    }

    public int getRowCount() {
        int numRows = _rowObjects.size();
        if (_allowAddition) numRows++;
        return numRows;
    }

    public Object getValueAt(int row, int col) {
        if (row >= 0 && row < _rowObjects.size()) {
            if (col >= 0 && col < _colDescs.size()) {
                Object rowObj = _rowObjects.elementAt(row);
                ColumnDescriptor cd = (ColumnDescriptor) _colDescs.elementAt(col);
                return cd.getValueFor(rowObj);
            }
        }
        return "TC-" + row + "," + col;
    }

    public void setValueAt(Object val, int row, int col) {
        if (row >= 0 && row < _rowObjects.size()) {
            if (col >= 0 && col < _colDescs.size()) {
                Object rowObj = _rowObjects.elementAt(row);
                ColumnDescriptor cd = (ColumnDescriptor) _colDescs.elementAt(col);
                cd.setValueFor(rowObj, val);
                return;
            }
        }
        if (_allowAddition && row >= _rowObjects.size()) {
            fireTableStructureChanged();
        } else if (_allowRemoval && val.equals("")) {
            _rowObjects.removeElementAt(row);
            fireTableStructureChanged();
        }
    }

    public void vetoableChange(PropertyChangeEvent pce) {
        DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
        SwingUtilities.invokeLater(delayedNotify);
    }

    public void delayedVetoableChange(PropertyChangeEvent pce) {
        fireTableStructureChanged();
    }
}
