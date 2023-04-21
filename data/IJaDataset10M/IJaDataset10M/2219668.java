package data.swing;

import data.*;
import data.events.*;
import util.*;
import util.swing.*;
import java.beans.*;
import java.util.*;
import java.io.*;

/**
  * A {@link javax.swing.table.TableModel} that models the contents of a {@link CountingStock}.
  *
  * @author Steffen Zschaler
  * @version 2.0 23/08/1999
  * @since v2.0
  */
public class CountingStockTableModel extends AbstractTableModel implements HelpableListener, StockChangeListener, CatalogChangeListener, PropertyChangeListener, Serializable {

    /**
    * The DataBasket used to determine visibility.
    *
    * @serial
    */
    protected DataBasket m_dbBasket;

    /**
    * The CountingStock being modelled. May be {@link data.filters.CountingStockFilter filtered}.
    *
    * @serial
    */
    protected CountingStock m_csModel;

    /**
    * The Comparator that defines the sorting order of records in the model. It compares the keys of the
    * actual items.
    *
    * @serial
    */
    protected Comparator m_cmpComparator = new NaturalComparator();

    /**
    * If true, show lines informing about a zero amount of objects.
    *
    * @serial
    */
    protected boolean m_fShowZeros;

    /**
    * The internal model. A list of the items' keys.
    *
    * @serial
    */
    protected List m_lKeys;

    /**
    * Create a new CountingStockTableModel.
    *
    * @param cs the Stock to be modelled.
    * @param db the DataBasket to be used to determine visibility.
    * @param cmp the Comparator defining the sorting order. If <code>null</code> the records will be sorted
    * according to the natural ordering of their keys.
    * @param fShowZeros if true, lines informing about a zero amount of objects will be shown.
    * @param ted a TableEntryDescriptor that can split a {@link Record} into a table's cells.
    */
    public CountingStockTableModel(CountingStock cs, DataBasket db, Comparator cmp, boolean fShowZeros, TableEntryDescriptor ted) {
        super(ted);
        m_dbBasket = db;
        m_csModel = cs;
        if (cmp != null) {
            m_cmpComparator = cmp;
        }
        m_fShowZeros = fShowZeros;
        listenerList = new ListenerHelper(this);
        updateModel();
    }

    /**
    * A {@link CountingStockTableModel}'s record.
    *
    * <p>The record is basically a combination of a {@link CatalogItem} and a number indicating the number of
    * objects available.</p>
    *
    * @author Steffen Zschaler
    * @version 2.0 23/08/1999
    * @since v2.0
    */
    public static class Record implements Comparable {

        private CatalogItem m_ciDescriptor;

        private int m_nCount;

        /**
      * Create a new Record.
      */
        public Record(CatalogItem ci, int nCount) {
            super();
            m_ciDescriptor = ci;
            m_nCount = nCount;
        }

        /**
      * Compare by descriptor.
      */
        public int compareTo(Object o) {
            return m_ciDescriptor.compareTo(((Record) o).getDescriptor());
        }

        /**
      * Get the CatalogItem describing the items represented by this record.
      */
        public CatalogItem getDescriptor() {
            return m_ciDescriptor;
        }

        /** 
      * Get the number of items in this record.
      */
        public int getCount() {
            return m_nCount;
        }
    }

    /**
    * Get the record at the given index.
    *
    * @param row the index for which to retrieve the record. Element of [0, {@link #getRowCount}).
    * @return the {@link Record} to be displayed at the given index. May return <code>null</code> if
    * either there is no record at the indicated position or an exception occurs.
    *
    * @override Never
    */
    public Object getRecord(int row) {
        ((ListenerHelper) listenerList).needModelUpdate();
        try {
            if ((row > -1) && (row < getRowCount())) {
                String sKey = (String) m_lKeys.get(row);
                return new Record(m_csModel.getCatalog().get(sKey, m_dbBasket, false), m_csModel.countItems(sKey, m_dbBasket));
            } else {
                return null;
            }
        } catch (VetoException ve) {
            return null;
        }
    }

    /**
    * Get the number of records in this model.
    *
    * @override Never
    */
    public int getRowCount() {
        ((ListenerHelper) listenerList).needModelUpdate();
        return m_lKeys.size();
    }

    /**
    * Subscribe as a listener to the model. If the modelled {@link Catalog} is a {@link ListenableCatalog},
    * subscribe as a listener. If the modelled {@link CountingStock} is a {@link ListenableStock}, subscribe as
    * a listener.
    *
    * @override Never
    */
    public void subscribe() {
        if (m_csModel instanceof ListenableStock) {
            ((ListenableStock) m_csModel).addStockChangeListener(this);
        }
        if (m_csModel.getCatalog() instanceof ListenableCatalog) {
            ((ListenableCatalog) m_csModel.getCatalog()).addCatalogChangeListener(this);
        }
    }

    /**
    * Un-Subscribe as a listener from the model. If the modelled {@link Catalog} is a {@link ListenableCatalog},
    * un-subscribe as a listener. If the modelled {@link CountingStock} is a {@link ListenableStock},
    * un-subscribe as a listener.
    *
    * @override Never
    */
    public void unsubscribe() {
        if (m_csModel instanceof ListenableStock) {
            ((ListenableStock) m_csModel).removeStockChangeListener(this);
        }
        if (m_csModel.getCatalog() instanceof ListenableCatalog) {
            ((ListenableCatalog) m_csModel.getCatalog()).removeCatalogChangeListener(this);
        }
    }

    /**
    * Update the internal model based on the modelled {@link CountingStock}.
    *
    * @override Never
    */
    public synchronized void updateModel() {
        List lKeys = new LinkedList(m_csModel.getCatalog().keySet(m_dbBasket));
        Collections.sort(lKeys, m_cmpComparator);
        if (!m_fShowZeros) {
            for (Iterator i = lKeys.iterator(); i.hasNext(); ) {
                String sKey = (String) i.next();
                if (m_csModel.countItems(sKey, m_dbBasket) == 0) {
                    i.remove();
                }
            }
        }
        m_lKeys = lKeys;
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void addedStockItems(StockChangeEvent e) {
        if ((e.getBasket() == null) || (e.getBasket() == m_dbBasket)) {
            checkUpdate(e.getAffectedKey());
        }
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void commitAddStockItems(StockChangeEvent e) {
        checkUpdate(e.getAffectedKey());
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void rollbackAddStockItems(StockChangeEvent e) {
        if (e.getBasket() == m_dbBasket) {
            checkUpdate(e.getAffectedKey());
        }
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void canRemoveStockItems(StockChangeEvent e) throws VetoException {
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void noRemoveStockItems(StockChangeEvent e) {
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void removedStockItems(StockChangeEvent e) {
        checkUpdate(e.getAffectedKey());
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void commitRemoveStockItems(StockChangeEvent e) {
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void rollbackRemoveStockItems(StockChangeEvent e) {
        checkUpdate(e.getAffectedKey());
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void canEditStockItems(StockChangeEvent e) throws VetoException {
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void noEditStockItems(StockChangeEvent e) {
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void editingStockItems(StockChangeEvent e) {
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void commitEditStockItems(StockChangeEvent e) {
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void rollbackEditStockItems(StockChangeEvent e) {
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void addedCatalogItem(CatalogChangeEvent e) {
        if ((e.getBasket() == null) || (e.getBasket() == m_dbBasket)) {
            checkAdd(e.getAffectedItem().getName());
        }
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void commitedAddCatalogItem(CatalogChangeEvent e) {
        if (e.getBasket() != m_dbBasket) {
            checkAdd(e.getAffectedItem().getName());
        }
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void rolledbackAddCatalogItem(CatalogChangeEvent e) {
        if (e.getBasket() == m_dbBasket) {
            checkRemove(e.getAffectedItem().getName());
        }
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void canRemoveCatalogItem(CatalogChangeEvent e) throws VetoException {
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void noRemoveCatalogItem(CatalogChangeEvent e) {
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void removedCatalogItem(CatalogChangeEvent e) {
        checkRemove(e.getAffectedItem().getName());
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void commitedRemoveCatalogItem(CatalogChangeEvent e) {
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void rolledbackRemoveCatalogItem(CatalogChangeEvent e) {
        checkAdd(e.getAffectedItem().getName());
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void canEditCatalogItem(CatalogChangeEvent e) throws VetoException {
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void noEditCatalogItem(CatalogChangeEvent e) {
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void editingCatalogItem(CatalogChangeEvent e) {
        if (e.getBasket() != m_dbBasket) {
            checkRemove(e.getAffectedItem().getName());
        } else {
            e.getAffectedItem().addPropertyChangeListener(this);
        }
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void commitEditCatalogItem(CatalogChangeEvent e) {
        if (e.getBasket() != m_dbBasket) {
            checkAdd(e.getAffectedItem().getName());
        } else {
            e.getAffectedItem().removePropertyChangeListener(this);
            updateModel();
            fireTableDataChanged();
        }
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void rollbackEditCatalogItem(CatalogChangeEvent e) {
        if (e.getBasket() != m_dbBasket) {
            checkAdd(e.getAffectedItem().getName());
        } else {
            e.getAffectedItem().removePropertyChangeListener(this);
            updateModel();
            fireTableDataChanged();
        }
    }

    /**
    * Update the internal model and inform any listeners according to the received event.
    *
    * <p>This method is public as an implementation detail and must not be called directly.</p>
    *
    * @override Never
    */
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getSource() instanceof CatalogItem) {
            checkUpdate(((CatalogItem) e.getSource()).getName());
        }
    }

    /**
    * Internal helper method. Check where, if at all, the given CatalogItem has been added with respect to the
    * internal model.
    *
    * @param ci the added CatalogItem
    *
    * @override Never
    */
    protected synchronized void checkAdd(String sKey) {
        updateModel();
        int nIdx = m_lKeys.indexOf(sKey);
        if (nIdx > -1) {
            fireTableRowsInserted(nIdx, nIdx);
        }
    }

    /**
    * Internal helper method. Check from where, if at all, the given CatalogItem has been removed with respect
    * to the internal model.
    *
    * @param ci the removed CatalogItem
    *
    * @override Never
    */
    protected synchronized void checkRemove(String sKey) {
        int nIdx = m_lKeys.indexOf(sKey);
        updateModel();
        if (nIdx > -1) {
            fireTableRowsDeleted(nIdx, nIdx);
        }
    }

    /**
    * Internal helper method. Check for updates in the given CatalogItem.
    *
    * @param ci the updated CatalogItem
    *
    * @override Never
    */
    protected synchronized void checkUpdate(String sKey) {
        int nIdx1 = m_lKeys.indexOf(sKey);
        updateModel();
        int nIdx2 = m_lKeys.indexOf(sKey);
        if (nIdx1 == -1) {
            if (nIdx2 > -1) {
                fireTableRowsInserted(nIdx2, nIdx2);
            } else {
                return;
            }
        } else {
            if (nIdx2 > -1) {
                if (nIdx1 > nIdx2) {
                    int nTemp = nIdx2;
                    nIdx2 = nIdx1;
                    nIdx1 = nTemp;
                }
                fireTableRowsUpdated(nIdx1, nIdx2);
            } else {
                fireTableRowsDeleted(nIdx1, nIdx1);
            }
        }
    }
}
