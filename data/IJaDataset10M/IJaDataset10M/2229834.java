package org.yccheok.jstock.engine;

/**
 *
 * @author yccheok
 */
public interface StockHistoryServer {

    public Stock getStock(java.util.Calendar calendar);

    public java.util.Calendar getCalendar(int index);

    public int getNumOfCalendar();

    public long getSharesIssued();

    public long getMarketCapital();
}
