package data.filters;

import data.*;
import data.events.*;
import java.util.*;

/**
  * Specialized StockChangeEvent for use with StockFilters.
  *
  * @author Steffen Zschaler
  * @version 2.0 19/08/1999
  * @since v2.0
  */
class StockFilterEvent extends StockChangeEvent {

    /**
    * The affected items.
    */
    private Set m_stItems;

    /**
    * The affected key.
    */
    private String m_sKey;

    /**
    * Create a new StockFilterEvent.
    *
    * @param astfSource the StockFilter that triggers the event.
    * @param sKey the affected key.
    * @param stItems the affected items.
    * @param db the DataBasket that was used to perform the operation.
    */
    public StockFilterEvent(AbstractStockFilter astfSource, String sKey, Set stItems, DataBasket db) {
        super(astfSource, db);
        m_sKey = sKey;
        m_stItems = stItems;
    }

    /**
    * Get the affected key.
    *
    * @override Never
    */
    public String getAffectedKey() {
        return m_sKey;
    }

    /**
    * Count the affected items.
    *
    * @override Never
    */
    public int countAffectedItems() {
        return m_stItems.size();
    }

    /**
    * Get all affected items.
    *
    * @override Never
    */
    public Iterator getAffectedItems() {
        return m_stItems.iterator();
    }
}
