package data.ooimpl;

import java.util.*;
import data.events.*;
import data.*;

/**
  * Pure Java implementation of the {@link CountingStock} interface.
  *
  * @author Steffen Zschaler
  * @version 2.0 19/08/1999
  * @since v2.0
  */
public class CountingStockImpl extends StockImpl implements CountingStock {

    /**
    * Listens for the Catalog to ensure referential integrity.
    *
    * @serial
    */
    protected CatalogChangeListener m_cclReferentialIntegrityListener;

    /**
    * Create a new, initially empty CountingStockImpl.
    *
    * @param sName the name of the Stock.
    * @param ciRef the Catalog referenced by the Stock.
    */
    public CountingStockImpl(String sName, CatalogImpl ciRef) {
        super(sName, ciRef);
        m_sclEditCreatorListener = new StockChangeAdapter() {

            public void commitAddStockItems(StockChangeEvent e) {
                synchronized (getItemsLock()) {
                    String sKey = e.getAffectedKey();
                    Integer iAdded = (Integer) getTemporaryAddedItemsContainer().remove(sKey);
                    if (iAdded == null) {
                        return;
                    }
                    iAdded = new Integer(iAdded.intValue() - e.countAffectedItems());
                    if (iAdded.intValue() > 0) {
                        getTemporaryAddedItemsContainer().put(sKey, iAdded);
                    }
                    Integer iItems = (Integer) getItemsContainer().get(sKey);
                    if (iItems == null) {
                        iItems = new Integer(e.countAffectedItems());
                    } else {
                        iItems = new Integer(iItems.intValue() + e.countAffectedItems());
                    }
                    if (iAdded.intValue() < 0) {
                        iItems = new Integer(iItems.intValue() + iAdded.intValue());
                    }
                    if (iItems.intValue() > 0) {
                        getItemsContainer().put(sKey, iItems);
                    }
                    fireStockItemsAddCommit(new CountingStockChangeEvent(CountingStockImpl.this, e.getBasket(), sKey, ((iAdded.intValue() < 0) ? (e.countAffectedItems() + iAdded.intValue()) : (e.countAffectedItems()))));
                }
            }

            public void rollbackAddStockItems(StockChangeEvent e) {
                synchronized (getItemsLock()) {
                    String sKey = e.getAffectedKey();
                    Integer iAdded = (Integer) getTemporaryAddedItemsContainer().remove(sKey);
                    if (iAdded == null) {
                        return;
                    }
                    iAdded = new Integer(iAdded.intValue() - e.countAffectedItems());
                    if (iAdded.intValue() > 0) {
                        getTemporaryAddedItemsContainer().put(sKey, iAdded);
                    }
                    fireStockItemsAddRollback(new CountingStockChangeEvent(CountingStockImpl.this, e.getBasket(), sKey, ((iAdded.intValue() < 0) ? (e.countAffectedItems() + iAdded.intValue()) : (e.countAffectedItems()))));
                }
            }

            public void canRemoveStockItems(StockChangeEvent e) throws VetoException {
                throw new VetoException("Please use the editable version for this!");
            }

            public void commitRemoveStockItems(StockChangeEvent e) {
                synchronized (getItemsLock()) {
                    String sKey = e.getAffectedKey();
                    Integer iRemoved = (Integer) getTemporaryRemovedItemsContainer().remove(sKey);
                    if (iRemoved == null) {
                        return;
                    }
                    iRemoved = new Integer(iRemoved.intValue() - e.countAffectedItems());
                    if (iRemoved.intValue() > 0) {
                        getTemporaryRemovedItemsContainer().put(sKey, iRemoved);
                    }
                    fireStockItemsRemoveCommit(new CountingStockChangeEvent(CountingStockImpl.this, e.getBasket(), sKey, ((iRemoved.intValue() < 0) ? (e.countAffectedItems() + iRemoved.intValue()) : (e.countAffectedItems()))));
                }
            }

            public void rollbackRemoveStockItems(StockChangeEvent e) {
                synchronized (getItemsLock()) {
                    String sKey = e.getAffectedKey();
                    Integer iRemoved = (Integer) getTemporaryRemovedItemsContainer().remove(sKey);
                    if (iRemoved == null) {
                        return;
                    }
                    iRemoved = new Integer(iRemoved.intValue() - e.countAffectedItems());
                    if (iRemoved.intValue() > 0) {
                        getTemporaryRemovedItemsContainer().put(sKey, iRemoved);
                    }
                    Integer iItems = (Integer) getItemsContainer().get(sKey);
                    if (iItems == null) {
                        iItems = new Integer(e.countAffectedItems());
                    } else {
                        iItems = new Integer(iItems.intValue() + e.countAffectedItems());
                    }
                    if (iRemoved.intValue() < 0) {
                        iItems = new Integer(iItems.intValue() + iRemoved.intValue());
                    }
                    if (iItems.intValue() > 0) {
                        getItemsContainer().put(sKey, iItems);
                    }
                    fireStockItemsRemoveRollback(new CountingStockChangeEvent(CountingStockImpl.this, e.getBasket(), sKey, ((iRemoved.intValue() < 0) ? (e.countAffectedItems() + iRemoved.intValue()) : (e.countAffectedItems()))));
                }
            }

            public void canEditStockItems(StockChangeEvent e) throws VetoException {
                throw new VetoException("Please use the editable version for this!");
            }

            public void commitEditStockItems(StockChangeEvent e) {
                synchronized (getItemsLock()) {
                    String sKey = e.getAffectedKey();
                    Integer iEditing = (Integer) getEditingItemsContainer().remove(sKey);
                    if (iEditing == null) {
                        return;
                    }
                    iEditing = new Integer(iEditing.intValue() - e.countAffectedItems());
                    if (iEditing.intValue() > 0) {
                        getEditingItemsContainer().put(sKey, iEditing);
                    }
                    fireStockItemsEditCommit(new CountingStockChangeEvent(CountingStockImpl.this, e.getBasket(), sKey, ((iEditing.intValue() < 0) ? (e.countAffectedItems() + iEditing.intValue()) : (e.countAffectedItems()))));
                }
            }

            public void rollbackEditStockItems(StockChangeEvent e) {
                synchronized (getItemsLock()) {
                    String sKey = e.getAffectedKey();
                    Integer iEditing = (Integer) getEditingItemsContainer().remove(sKey);
                    if (iEditing == null) {
                        return;
                    }
                    iEditing = new Integer(iEditing.intValue() - e.countAffectedItems());
                    if (iEditing.intValue() > 0) {
                        getEditingItemsContainer().put(sKey, iEditing);
                    }
                    fireStockItemsEditRollback(new CountingStockChangeEvent(CountingStockImpl.this, e.getBasket(), sKey, ((iEditing.intValue() < 0) ? (e.countAffectedItems() + iEditing.intValue()) : (e.countAffectedItems()))));
                }
            }
        };
    }

    /**
    * Overridden to ensure referential integrity.
    *
    * @override Never
    */
    protected void internalSetCatalog(CatalogImpl ciRef) {
        if (m_ciCatalog != null) {
            m_ciCatalog.removeCatalogChangeListener(m_cclReferentialIntegrityListener);
        }
        super.internalSetCatalog(ciRef);
        if (m_ciCatalog != null) {
            if (m_cclReferentialIntegrityListener == null) {
                initReferentialIntegrityListener();
            }
            m_ciCatalog.addCatalogChangeListener(m_cclReferentialIntegrityListener);
        }
    }

    /**
    * Private helper function creating the listener that ensures referential integrity.
    *
    * @override Never
    */
    private void initReferentialIntegrityListener() {
        m_cclReferentialIntegrityListener = new CatalogChangeAdapter() {

            public void canRemoveCatalogItem(CatalogChangeEvent e) throws VetoException {
                synchronized (getItemsLock()) {
                    String sKey = e.getAffectedItem().getName();
                    if (getTemporaryAddedItemsContainer().containsKey(sKey)) {
                        throw new VetoException("Stock " + getName() + ": Having temporaryly added items for key \"" + sKey + "\"");
                    }
                    if (getTemporaryRemovedItemsContainer().containsKey(sKey)) {
                        DataBasketCondition dbc = new DataBasketConditionImpl(STOCK_ITEM_MAIN_KEY, sKey, CountingStockImpl.this, null, null);
                        BasketEntryValue bev = new BasketEntryValue() {

                            public Value getEntryValue(DataBasketEntry dbe) {
                                return new IntegerValue((Integer) dbe.getValue());
                            }
                        };
                        Integer iCount = (Integer) getTemporaryRemovedItemsContainer().get(sKey);
                        IntegerValue ivCount = new IntegerValue(new Integer(0));
                        int nCount = ((IntegerValue) e.getBasket().sumBasket(dbc, bev, ivCount)).getValue().intValue();
                        if (iCount.intValue() > nCount) {
                            throw new VetoException("Stock " + getName() + ": Having temporaryly removed items that are in another DataBasket. (Key: \"" + sKey + "\")");
                        }
                    }
                    if (getItemsContainer().containsKey(sKey)) {
                        int nCount = ((Integer) getItemsContainer().get(sKey)).intValue();
                        remove(sKey, nCount, e.getBasket());
                        getRefIntegrItemsContainer().put(sKey, new Integer(nCount));
                    }
                }
            }

            public void noRemoveCatalogItem(CatalogChangeEvent e) {
                synchronized (getItemsLock()) {
                    String sKey = e.getAffectedItem().getName();
                    if (getRefIntegrItemsContainer().containsKey(sKey)) {
                        int nCount = ((Integer) getRefIntegrItemsContainer().remove(sKey)).intValue();
                        add(sKey, nCount, e.getBasket());
                    }
                }
            }

            public void removedCatalogItem(CatalogChangeEvent e) {
                synchronized (getItemsLock()) {
                    getRefIntegrItemsContainer().remove(e.getAffectedItem().getName());
                }
            }

            public void commitRemoveCatalogItem(CatalogChangeEvent e) {
                synchronized (getItemsLock()) {
                    if (!((Catalog) e.getSource()).contains(e.getAffectedItem().getName(), e.getBasket())) {
                        ciGoneForEver(e);
                    }
                }
            }

            public void rollbackAddCatalogItem(CatalogChangeEvent e) {
                synchronized (getItemsLock()) {
                    DataBasketCondition dbc = new DataBasketConditionImpl(CatalogItemImpl.CATALOG_ITEM_MAIN_KEY, e.getAffectedItem().getName(), (CatalogImpl) e.getSource(), null, null);
                    if (!e.getBasket().contains(dbc)) {
                        ciGoneForEver(e);
                    }
                }
            }

            private void ciGoneForEver(CatalogChangeEvent e) {
                String sKey = e.getAffectedItem().getName();
                DataBasket db = e.getBasket();
                if (getTemporaryAddedItemsContainer().containsKey(sKey)) {
                    DataBasketCondition dbc = new DataBasketConditionImpl(STOCK_ITEM_MAIN_KEY, sKey, null, CountingStockImpl.this, null);
                    for (Iterator i = db.iterator(dbc); i.hasNext(); ) {
                        ((DataBasketEntry) i.next()).rollback();
                    }
                }
                getItemsContainer().remove(sKey);
                getRefIntegrItemsContainer().remove(sKey);
                if (getTemporaryRemovedItemsContainer().containsKey(sKey)) {
                    DataBasketCondition dbc = new DataBasketConditionImpl(STOCK_ITEM_MAIN_KEY, sKey, CountingStockImpl.this, null, null);
                    for (Iterator i = db.iterator(dbc); i.hasNext(); ) {
                        ((DataBasketEntry) i.next()).commit();
                    }
                }
            }
        };
    }

    /**
    * Add an item to the Stock.
    *
    * <p>The item will only be visible to users of the same DataBasket. Only after a {@link DataBasket#commit}
    * was performed on the DataBasket, the item will become visible to other users.</p>
    *
    * <p>A <code>addedStockItems</code> event will be fired.</p>
    *
    * @param si the item to be added.
    * @param db the DataBasket relative to which the item will be added. Must be either <code>null</code> or
    * a descendant of {@link DataBasketImpl}.
    *
    * @override Never
    *
    * @exception CatalogConflictException if the items key is not contained in the corresponding {@link Catalog}.
    * @exception DataBasketConflictException if the item has already been added/removed using another DataBasket.
    */
    public void add(StockItem si, DataBasket db) {
        add(si.getName(), 1, db);
    }

    /**
    * Overridden for efficiency reasons.
    *
    * @override Never
    */
    public void addStock(Stock st, DataBasket db, boolean fRemove) {
        if (st.getCatalog() != getCatalog()) {
            throw new CatalogConflictException();
        }
        Object oLock = ((db == null) ? (new Object()) : (((DataBasketImpl) db).getDBIMonitor()));
        synchronized (oLock) {
            synchronized (getItemsLock()) {
                Object oLock2 = ((st instanceof StockImpl) ? (((StockImpl) st).getItemsLock()) : (oLock));
                synchronized (oLock2) {
                    for (Iterator i = st.keySet(db).iterator(); i.hasNext(); ) {
                        String sKey = (String) i.next();
                        add(sKey, st.countItems(sKey, db), db);
                        if (fRemove) {
                            for (Iterator ii = st.get(sKey, db, false); ii.hasNext(); ) {
                                try {
                                    ii.next();
                                    ii.remove();
                                } catch (ConcurrentModificationException e) {
                                    break;
                                } catch (Exception e) {
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
    * Iterate all items with a given key.
    *
    * <p>This method, together with {@link #iterator} is the only way of accessing the individual
    * {@link StockItem StockItems} contained in a Stock. The iterator will deliver all items that have the
    * specified key and are visible using the given DataBasket. Depending on the <code>fForEdit</code>
    * parameter, the items will be retrieved in different ways. See {@link DataBasket} for an explanation of
    * the different possibilities.</p>
    *
    * <p><code>canEditStockItems</code> and <code>editingStockItems</code> events will be fired if
    * <code>fForEdit</code> == true. {@link VetoException VetoExceptions} will be converted into
    * <code>UnSupportedOperationException</code>s.</p>
    *
    * @override Never
    *
    * @param sKey the key for which to retrieve the StockItems.
    * @param db the DataBasket relative to which to retrieve the StockItems. Must be either <code>null</code>
    * or a decendant of {@link DataBasketImpl}.
    * @param fForEdit if true, the StockItems will be retrieved for editing.
    */
    public Iterator get(final String sKey, final DataBasket db, boolean fForEdit) {
        class I implements Iterator {

            private boolean m_fNextCalled = false;

            private int m_nCount;

            private CountingStockImpl m_cstiOwner;

            private StockItemImpl m_siiLast;

            public I(CountingStockImpl cstiOwner, int nCount) {
                super();
                m_cstiOwner = cstiOwner;
                m_nCount = nCount;
            }

            public boolean hasNext() {
                return (m_nCount > 0);
            }

            public Object next() {
                if ((m_nCount--) <= 0) {
                    m_fNextCalled = false;
                    throw new NoSuchElementException();
                }
                m_fNextCalled = true;
                m_siiLast = new StockItemImpl(sKey);
                m_siiLast.setStock(m_cstiOwner);
                return m_siiLast;
            }

            public void remove() {
                if (m_fNextCalled) {
                    m_fNextCalled = false;
                    try {
                        m_cstiOwner.remove(sKey, 1, db);
                    } catch (VetoException ex) {
                        throw new UnsupportedOperationException("VetoException: " + ex.getMessage());
                    }
                    m_siiLast.setStock(null);
                } else {
                    throw new IllegalStateException();
                }
            }
        }
        if ((getCatalog() != null) && (!getCatalog().contains(sKey, db))) {
            return new Iterator() {

                public boolean hasNext() {
                    return false;
                }

                public Object next() {
                    throw new NoSuchElementException();
                }

                public void remove() {
                }
            };
        }
        return new I(this, countItems(sKey, db));
    }

    /**
    * Count the StockItems with a given key that are visible using a given DataBasket.
    *
    * @override Never
    *
    * @param sKey the key for which to count the StockItems.
    * @param db the DataBasket that is used to determine visibility. Must be either <code>null</code> or a
    * descendant of {@link DataBasketImpl}.
    */
    public int countItems(String sKey, DataBasket db) {
        Object oLock = ((db == null) ? (new Object()) : (((DataBasketImpl) db).getDBIMonitor()));
        int nCount = 0;
        synchronized (oLock) {
            synchronized (getItemsLock()) {
                if ((getCatalog() != null) && (!getCatalog().contains(sKey, db))) {
                    return 0;
                }
                Integer iCount = (Integer) getItemsContainer().get(sKey);
                if (iCount != null) {
                    nCount = iCount.intValue();
                }
                if (db != null) {
                    DataBasketCondition dbc = new DataBasketConditionImpl(STOCK_ITEM_MAIN_KEY, sKey, null, this, null);
                    for (Iterator i = db.iterator(dbc); i.hasNext(); ) {
                        StockItemDBEntry sidbe = (StockItemDBEntry) i.next();
                        nCount += sidbe.count();
                    }
                }
            }
        }
        return nCount;
    }

    /**
    * Check whether the Stock contains the given StockItem.
    *
    * <p>Return true if the Stock contains a StockItem that is equal to the given one.</p>
    *
    * @param si the StockItem for which to check containment.
    * @param db the DataBasket used to check visibility. Must be either <code>null</code> or a descendant of
    * {@link DataBasketImpl}.
    *
    * @override Never
    */
    public boolean contains(StockItem si, DataBasket db) {
        return contains(si.getName(), db);
    }

    /**
    * Reimplemented for efficiency reasons.
    *
    * @override Never
    */
    public boolean containsStock(Stock st, DataBasket db) {
        Object oLock = ((db == null) ? (new Object()) : (((DataBasketImpl) db).getDBIMonitor()));
        synchronized (oLock) {
            synchronized (getItemsLock()) {
                Object oLock2 = ((st instanceof StockImpl) ? (((StockImpl) st).getItemsLock()) : (oLock));
                synchronized (oLock2) {
                    for (Iterator i = st.keySet(db).iterator(); i.hasNext(); ) {
                        String sKey = (String) i.next();
                        if (countItems(sKey, db) < st.countItems(sKey, db)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
    }

    /**
    * Remove one StockItem with the specified key from the Stock.
    *
    * <p>If there are any StockItems with the specified key, one will be removed. There is no guarantee as to
    * which StockItem will be removed. The removed item, if any, will be returned.</p>
    *
    * <p><code>canRemoveStockItems</code> and <code>removedStockItems</code> events will be fired.</p>
    *
    * @override Never
    *
    * @param sKey the key for which to remove an item.
    * @param db the DataBasket relative to which to remove the item. Must be either <code>null</code> or a
    * descendant of {@link DataBasketImpl}.
    *
    * @return the removed item
    *
    * @exception VetoException if a listener vetoed the removal.
    * @exception DataBasketConflictException if the item cannot be removed due to conflicts from DataBasket
    * usage.
    */
    public StockItem remove(String sKey, DataBasket db) throws VetoException {
        remove(sKey, 1, db);
        StockItemImpl sii = new StockItemImpl(sKey);
        sii.setStock(null);
        return sii;
    }

    /**
    * Remove the given StockItem from the Stock.
    *
    * <p>If the given StockItem is contained in the Stock, it will be removed. The removed item, if any, will
    * be returned.</p>
    *
    * <p><code>canRemoveStockItems</code> and <code>removedStockItems</code> events will be fired.</p>
    *
    * @override Never
    *
    * @param si the StockItem to be removed.
    * @param db the DataBasket relative to which to remove the item. Must be either <code>null</code> or a
    * descendant of {@link DataBasketImpl}.
    *
    * @return the removed item
    *
    * @exception VetoException if a listener vetoed the removal.
    * @exception DataBasketConflictException if the item cannot be removed due to conflicts from DataBasket
    * usage.
    */
    public StockItem remove(StockItem si, DataBasket db) throws VetoException {
        return remove(si.getName(), db);
    }

    /**
    * @override Always
    */
    protected StockImpl createPeer() {
        return new CountingStockImpl(getName(), (CatalogImpl) getCatalog());
    }

    /**
    * Add a number of items of a given key to the Stock.
    *
    * <p>As with any Stock the added items will not at once be visible to users of other DataBaskets.</p>
    *
    * <p>In general the method behaves as though it would call {@link Stock#add} <code>nCount</code> times.
    * Especially, the same exceptions might occur and the same constraints hold.</p>
    *
    * @override Never
    *
    * @param sKey the key for which to add a number of items.
    * @param nCount how many items are to be added?
    * @param db the DataBasket relative to which the adding is performed. Must be either <code>null</code> or a
    * descendant of {@link DataBasketImpl}.
    *
    * @exception IllegalArgumentException if <code>nCount <= 0</code>.
    * @exception CatalogConflictException if the key cannot be found in the Catalog.
    */
    public void add(String sKey, int nCount, DataBasket db) {
        if (nCount <= 0) {
            throw new IllegalArgumentException("nCount must be greater than 0.");
        }
        Object oLock = ((db == null) ? (new Object()) : (((DataBasketImpl) db).getDBIMonitor()));
        synchronized (oLock) {
            synchronized (getItemsLock()) {
                if ((getCatalog() != null) && (!getCatalog().contains(sKey, db))) {
                    throw new CatalogConflictException("Couldn't find key \"" + sKey + "\" in Catalog \"" + getCatalog().getName() + "\"");
                }
                if (db != null) {
                    final int[] anTempCount = { nCount };
                    DataBasketCondition dbc = new DataBasketConditionImpl(STOCK_ITEM_MAIN_KEY, sKey, this, null, null) {

                        public boolean match(DataBasketEntry dbe) {
                            if (anTempCount[0] == 0) {
                                return false;
                            }
                            StockItemDBEntry sidbe = (StockItemDBEntry) dbe;
                            if (anTempCount[0] >= sidbe.count()) {
                                anTempCount[0] -= sidbe.count();
                                return true;
                            } else {
                                CountingStockItemDBEntry csidbe = (CountingStockItemDBEntry) sidbe;
                                csidbe.partialRollback(anTempCount[0]);
                                anTempCount[0] = 0;
                                return false;
                            }
                        }
                    };
                    db.rollback(dbc);
                    nCount = anTempCount[0];
                    if (nCount > 0) {
                        Integer iCount = (Integer) getTemporaryAddedItemsContainer().remove(sKey);
                        if (iCount == null) {
                            iCount = new Integer(nCount);
                        } else {
                            iCount = new Integer(iCount.intValue() + nCount);
                        }
                        getTemporaryAddedItemsContainer().put(sKey, iCount);
                        db.put(new CountingStockItemDBEntry(sKey, null, this, nCount));
                        fireStockItemsAdded(new CountingStockChangeEvent(this, db, sKey, nCount));
                    } else {
                        if (db instanceof ListenableDataBasket) {
                            ((ListenableDataBasket) db).fireDataBasketChanged();
                        }
                    }
                } else {
                    Integer iCount = (Integer) getItemsContainer().get(sKey);
                    if (iCount == null) {
                        iCount = new Integer(nCount);
                    } else {
                        iCount = new Integer(iCount.intValue() + nCount);
                    }
                    getItemsContainer().put(sKey, iCount);
                    fireStockItemsAdded(new CountingStockChangeEvent(this, null, sKey, nCount));
                }
            }
        }
    }

    /**
    * Remove a number of items of a given key from the Stock.
    *
    * <p>In general the method behaves as though it would call
    * {@link Stock#remove(java.lang.String, data.DataBasket)} <code>nCount</code> times. Especially, the same
    * exceptions might occur and the same constraints hold.</p>
    *
    * @override Never
    *
    * @param sKey the key for which to remove a number of items.
    * @param nCount how many items are to be removed?
    * @param db the DataBasket relative to which the removal is performed. Must be either <code>null</code> or
    * a descendant of {@link DataBasketImpl}.
    *
    * @exception VetoException if a listener vetos the removal.
    * @exception NotEnoughElementsException if there are not enough elements to fulfill the request. If this
    * exception is thrown no items will have been removed.
    * @exception IllegalArgumentException if <code>nCount <= 0</code>
    */
    public void remove(String sKey, int nCount, DataBasket db) throws VetoException {
        if (nCount <= 0) {
            throw new IllegalArgumentException("nCount must be greater than 0.");
        }
        Object oLock = ((db == null) ? (new Object()) : (((DataBasketImpl) db).getDBIMonitor()));
        synchronized (oLock) {
            synchronized (getItemsLock()) {
                if ((getCatalog() != null) && (!getCatalog().contains(sKey, db))) {
                    throw new CatalogConflictException("Couldn't find key \"" + sKey + "\" in Catalog \"" + getCatalog().getName() + "\"");
                }
                if (countItems(sKey, db) < nCount) {
                    throw new NotEnoughElementsException();
                }
                fireCanRemoveStockItems(new CountingStockChangeEvent(this, db, sKey, nCount));
                if (db != null) {
                    final int[] anTempCount = { nCount };
                    DataBasketCondition dbc = new DataBasketConditionImpl(STOCK_ITEM_MAIN_KEY, sKey, null, this, null) {

                        public boolean match(DataBasketEntry dbe) {
                            if (anTempCount[0] == 0) {
                                return false;
                            }
                            StockItemDBEntry sidbe = (StockItemDBEntry) dbe;
                            if (anTempCount[0] >= sidbe.count()) {
                                anTempCount[0] -= sidbe.count();
                                return true;
                            } else {
                                CountingStockItemDBEntry csidbe = (CountingStockItemDBEntry) sidbe;
                                csidbe.partialRollback(anTempCount[0]);
                                anTempCount[0] = 0;
                                return false;
                            }
                        }
                    };
                    db.rollback(dbc);
                    nCount = anTempCount[0];
                    if (nCount > 0) {
                        Integer iCount = (Integer) getItemsContainer().remove(sKey);
                        if (iCount.intValue() > nCount) {
                            getItemsContainer().put(sKey, new Integer(iCount.intValue() - nCount));
                        }
                        iCount = (Integer) getTemporaryRemovedItemsContainer().remove(sKey);
                        if (iCount == null) {
                            iCount = new Integer(nCount);
                        } else {
                            iCount = new Integer(iCount.intValue() + nCount);
                        }
                        getTemporaryRemovedItemsContainer().put(sKey, iCount);
                        db.put(new CountingStockItemDBEntry(sKey, this, null, nCount));
                    } else {
                        if (db instanceof ListenableDataBasket) {
                            ((ListenableDataBasket) db).fireDataBasketChanged();
                        }
                    }
                } else {
                    Integer iCount = (Integer) getItemsContainer().get(sKey);
                    if (iCount.intValue() > nCount) {
                        iCount = new Integer(iCount.intValue() - nCount);
                        getItemsContainer().put(sKey, iCount);
                    } else {
                        getItemsContainer().remove(sKey);
                    }
                }
                fireStockItemsRemoved(new CountingStockChangeEvent(this, db, sKey, nCount));
            }
        }
    }

    /**
    * Get a String representation of the Stock.
    *
    * @override Sometimes
    */
    public String toString() {
        synchronized (getItemsLock()) {
            String sReturn = "Stock \"" + getName() + "\" [";
            boolean fFirst = true;
            for (Iterator i = keySet(null).iterator(); i.hasNext(); ) {
                String sKey = (String) i.next();
                sReturn += ((fFirst) ? ("") : (", ")) + sKey + ": " + countItems(sKey, null);
                fFirst = false;
            }
            return sReturn + "]";
        }
    }

    /**
    * Commit the removal of StockItems.
    *
    * <p>A <code>commitRemoveStockItems</code> will be fired.</p>
    *
    * @override Never
    */
    public void commitRemove(DataBasket db, DataBasketEntry dbe) {
        synchronized (getItemsLock()) {
            Integer iCount = (Integer) getTemporaryRemovedItemsContainer().remove(dbe.getSecondaryKey());
            int nRemains = iCount.intValue() - ((StockItemDBEntry) dbe).count();
            if (nRemains > 0) {
                getTemporaryRemovedItemsContainer().put(dbe.getSecondaryKey(), new Integer(nRemains));
            }
            fireStockItemsRemoveCommit(new CountingStockChangeEvent(this, db, dbe.getSecondaryKey(), ((StockItemDBEntry) dbe).count()));
        }
    }

    /**
    * Rollback the removal of StockItems.
    *
    * <p>A <code>rollbackRemoveStockItems</code> will be fired. Also, the Stock will try to make sure, that
    * a corresponding CatalogItem exists.</p>
    *
    * @override Never
    */
    public void rollbackRemove(DataBasket db, DataBasketEntry dbe) {
        synchronized (getItemsLock()) {
            prepareReferentialIntegrity(db, dbe);
            Integer iCount = (Integer) getTemporaryRemovedItemsContainer().remove(dbe.getSecondaryKey());
            int nCount = ((StockItemDBEntry) dbe).count();
            int nRemains = iCount.intValue() - nCount;
            if (nRemains > 0) {
                getTemporaryRemovedItemsContainer().put(dbe.getSecondaryKey(), new Integer(nRemains));
            }
            iCount = (Integer) getItemsContainer().remove(dbe.getSecondaryKey());
            if (iCount == null) {
                iCount = new Integer(nCount);
            } else {
                iCount = new Integer(iCount.intValue() + nCount);
            }
            getItemsContainer().put(dbe.getSecondaryKey(), iCount);
            fireStockItemsRemoveRollback(new CountingStockChangeEvent(this, db, dbe.getSecondaryKey(), nCount));
        }
    }

    /**
    * Commit the adding of StockItems.
    *
    * <p>A <code>commitAddStockItems</code> will be fired. A <code>commitEditStockItems</code> event may be
    * fired as a consequence of this method. Also, the Stock will try to make sure, that a corresponding
    * CatalogItem exists.</p>
    *
    * @override Never
    */
    public void commitAdd(DataBasket db, DataBasketEntry dbe) {
        synchronized (getItemsLock()) {
            prepareReferentialIntegrity(db, dbe);
            Integer iCount = (Integer) getTemporaryAddedItemsContainer().remove(dbe.getSecondaryKey());
            int nCount = ((StockItemDBEntry) dbe).count();
            int nRemains = iCount.intValue() - nCount;
            if (nRemains > 0) {
                getTemporaryAddedItemsContainer().put(dbe.getSecondaryKey(), new Integer(nRemains));
            }
            iCount = (Integer) getItemsContainer().remove(dbe.getSecondaryKey());
            if (iCount == null) {
                iCount = new Integer(nCount);
            } else {
                iCount = new Integer(iCount.intValue() + nCount);
            }
            getItemsContainer().put(dbe.getSecondaryKey(), iCount);
            fireStockItemsAddCommit(new CountingStockChangeEvent(this, db, dbe.getSecondaryKey(), nCount));
        }
    }

    /**
    * Rollback the adding of StockItems.
    *
    * <p>A <code>commitAddStockItems</code> will be fired. A <code>commitEditStockItems</code> event may be
    * fired as a consequence of this method.</p>
    *
    * @override Never
    */
    public void rollbackAdd(DataBasket db, DataBasketEntry dbe) {
        synchronized (getItemsLock()) {
            Integer iCount = (Integer) getTemporaryAddedItemsContainer().remove(dbe.getSecondaryKey());
            int nRemains = iCount.intValue() - ((StockItemDBEntry) dbe).count();
            if (nRemains > 0) {
                getTemporaryAddedItemsContainer().put(dbe.getSecondaryKey(), new Integer(nRemains));
            }
            fireStockItemsAddRollback(new CountingStockChangeEvent(this, db, dbe.getSecondaryKey(), ((StockItemDBEntry) dbe).count()));
        }
    }
}
