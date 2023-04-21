package data.ooimpl;

import data.*;
import data.events.*;
import java.util.*;

/**
  * StockChangeEvent for use with StoringStocks.
  *
  * @author Steffen Zschaler
  * @version 2.0 19/08/1999
  * @since v2.0
  */
class StoringStockChangeEvent extends StockChangeEvent {

    /**
    * The affected item.
    */
    private StockItemImpl m_siiItem;

    /**
    * Create a new StoringStockChangeEvent.
    *
    * @param lstSource the Stock that triggers the event.
    * @param siiItem the affected item.
    * @param db the DataBasket that was used to perform the operation.
    */
    public StoringStockChangeEvent(ListenableStock lstSource, StockItemImpl siiItem, DataBasket db) {
        super(lstSource, db);
        m_siiItem = siiItem;
    }

    /**
    * Get the affected key.
    *
    * @override Never
    */
    public String getAffectedKey() {
        return m_siiItem.getName();
    }

    /**
    * Count the affected items.
    *
    * @return 1
    *
    * @override Never
    */
    public int countAffectedItems() {
        return 1;
    }

    /**
    * Get the affected item.
    *
    * @override Never
    */
    public Iterator getAffectedItems() {
        return new Iterator() {

            private boolean m_fSpent = false;

            public boolean hasNext() {
                return !m_fSpent;
            }

            public Object next() {
                if (m_fSpent) {
                    throw new NoSuchElementException();
                }
                m_fSpent = true;
                return m_siiItem;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
