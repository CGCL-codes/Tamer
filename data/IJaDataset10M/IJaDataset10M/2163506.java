package data.ooimpl;

import data.*;
import log.*;

/**
  * DataBasketEntry describing operations with CountingStock's items.
  *
  * @author Steffen Zschaler
  * @version 2.0 19/08/1999
  * @since v2.0
  */
public class CountingStockItemDBEntry extends StockItemDBEntry {

    /**
    * Create a new CountingStockItemDBEntry.
    *
    * @param sKey the affected key.
    * @param stiSource the source Stock.
    * @param stiDest the destination Stock.
    * @param nCount the number of affected items. This will be stored as the
    * {@link DataBasketEntry#getValue value attribute} of the DataBasketEntry.
    */
    public CountingStockItemDBEntry(String sKey, StockImpl stiSource, StockImpl stiDest, int nCount) {
        super(sKey, stiSource, stiDest, new Integer(nCount));
    }

    /**
    * Count the affected items.
    *
    * @return the number of affected items.
    *
    * @override Never
    */
    public int count() {
        return ((Integer) getValue()).intValue();
    }

    /**
    * Rollback the operation described by this {@link DataBasketEntry} for a given number of items.
    *
    * <p>The method will rollback the operation for the given number of items, updating the underlying
    * DataBasket correctly.</p>
    *
    * <p><strong>Attention</strong>: The method is public as an implementation detail and should not be called
    * directly.</p>
    *
    * @override Never
    *
    * @param nCount the number of items for which to rollback the operation.
    *
    * @exception IllegalArgumentException if <code>nCount >= {@link #count}</code>.
    */
    public void partialRollback(int nCount) {
        if (nCount <= 0) {
            return;
        }
        if (nCount >= ((Integer) getValue()).intValue()) {
            throw new IllegalArgumentException();
        }
        DataBasketEntry dbe = new CountingStockItemDBEntry(getSecondaryKey(), (StockImpl) getSource(), (StockImpl) getDestination(), nCount);
        dbe.setOwner(m_dbiOwner);
        m_oValue = new Integer(((Integer) getValue()).intValue() - nCount);
        dbe.rollback();
    }

    /**
    * LogEntry describing an operation on CountingStock StockItem's.
    *
    * @author Steffen Zschaler
    * @version 2.0 19/09/1999
    * @since v2.0
    */
    public static class CSDBELogEntry extends StockItemDBELogEntry {

        /**
      * The number of affected items.
      *
      * @serial
      */
        private int m_nCount;

        /**
      * Create a new CSDBELogEntry.
      *
      * @param sidbe the DataBasketEntry to be described.
      */
        public CSDBELogEntry(StockItemDBEntry sidbe) {
            super(sidbe);
            m_nCount = sidbe.count();
        }

        /**
      * Get the number of affected items.
      *
      * @override Never
      */
        public int getCount() {
            return m_nCount;
        }

        /**
      * Get a String representation of the operation.
      *
      * @override Sometimes
      */
        public String toString() {
            return "StockItem transfer: " + getCount() + " item(s) \"" + getKey() + "\" were transferred" + ((getSource() != null) ? (" from Stock \"" + getSource() + "\"") : ("")) + ((getDestination() != null) ? (" to Stock \"" + getDestination() + "\"") : ("")) + " on " + getLogDate() + ".";
        }
    }

    /**
    * Create and return a LogEntry describing this DataBasketEntry.
    *
    * @override Sometimes
    */
    public LogEntry getLogData() {
        return new CSDBELogEntry(this);
    }
}
