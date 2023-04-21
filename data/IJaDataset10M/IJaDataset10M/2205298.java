package de.renier.jkeepass.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;
import de.renier.jkeepass.Messages;
import de.renier.jkeepass.elements.BasicElement;
import de.renier.jkeepass.elements.BasicElementList;
import de.renier.jkeepass.util.IconPool;

/**
 * BasicElementTableModel
 *
 * @author <a href="mailto:software@renier.de">Renier Roth</a>
 */
public class BasicElementTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -5608210091952738397L;

    private BasicElementList basicElements = null;

    private static final String[] COLUMN_NAMES = { Messages.getString("BasicElementTableModel.0"), Messages.getString("BasicElementTableModel.1"), Messages.getString("BasicElementTableModel.2"), Messages.getString("BasicElementTableModel.3") };

    /**
   * setBasicElements
   *
   * @param elementList
   */
    public synchronized void setBasicElements(BasicElementList elementList) {
        basicElements = elementList;
        fireTableDataChanged();
    }

    /**
   * removeRow
   *
   * @param row
   */
    public synchronized void removeRow(int row) {
        if (basicElements != null && row < getRowCount()) {
            basicElements.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }

    /**
   * removeBasicElement
   *
   * @param element
   */
    public synchronized void removeBasicElement(BasicElement element) {
        if (basicElements != null && element != null) {
            int row = basicElements.indexOf(element);
            if (row >= 0) {
                basicElements.remove(row);
                fireTableRowsDeleted(row, row);
            }
        }
    }

    /**
   * insertBasicElement
   *
   * @param element
   * @param row
   */
    public synchronized void insertBasicElement(BasicElement element, int row) {
        if (basicElements != null && element != null) {
            if (row >= 0 && row < getRowCount()) {
                basicElements.add(row, element);
                fireTableRowsInserted(row, row);
            }
        }
    }

    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    public int getRowCount() {
        if (basicElements == null) return 0;
        return basicElements.size();
    }

    public synchronized Object getValueAt(int row, int col) {
        if ((row >= getRowCount()) || (col >= getColumnCount())) return null;
        if (basicElements == null) return null;
        BasicElement element = (BasicElement) basicElements.get(row);
        switch(col) {
            case 0:
                JLabel name = new JLabel(element.getName());
                name.setIcon(IconPool.getInstance().getIconForId(element.getSymbol()));
                return name;
            case 1:
                return element.getUser();
            case 2:
                return element.getPassword();
            case 3:
                return element.getExpire();
            default:
                return null;
        }
    }

    public Class getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return JLabel.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return Date.class;
            default:
                return super.getColumnClass(columnIndex);
        }
    }

    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    /**
   * getBasicElement
   *
   * @param row
   * @return
   */
    public synchronized BasicElement getBasicElement(int row) {
        if (basicElements == null) return null;
        return (BasicElement) basicElements.get(row);
    }

    /**
   * isSortable
   *
   * @param col
   * @return
   */
    public boolean isSortable(int col) {
        return true;
    }

    /**
   * sortColumn
   *
   * @param col
   * @param ascending
   */
    public void sortColumn(int col, boolean ascending) {
        if (basicElements != null) {
            Collections.sort(basicElements, new ColumnComparator(col, ascending));
        }
    }

    /**
   * ColumnComparator
   */
    private static class ColumnComparator implements Comparator {

        protected int index;

        protected boolean ascending;

        public ColumnComparator(int index, boolean ascending) {
            this.index = index;
            this.ascending = ascending;
        }

        public int compare(Object one, Object two) {
            if (one instanceof BasicElement && two instanceof BasicElement) {
                Object oOne = null;
                Object oTwo = null;
                switch(index) {
                    case 0:
                        oOne = ((BasicElement) one).getName();
                        oTwo = ((BasicElement) two).getName();
                        break;
                    case 1:
                        oOne = ((BasicElement) one).getUser();
                        oTwo = ((BasicElement) two).getUser();
                        break;
                    case 2:
                        oOne = ((BasicElement) one).getPassword();
                        oTwo = ((BasicElement) two).getPassword();
                        break;
                    case 3:
                        oOne = ((BasicElement) one).getExpire();
                        oTwo = ((BasicElement) two).getExpire();
                        break;
                    default:
                        break;
                }
                if (oOne instanceof Comparable && oTwo instanceof Comparable) {
                    Comparable cOne = (Comparable) oOne;
                    Comparable cTwo = (Comparable) oTwo;
                    if (ascending) {
                        return cOne.compareTo(cTwo);
                    }
                    return cTwo.compareTo(cOne);
                }
            }
            return 1;
        }
    }
}
