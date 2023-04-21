package com.zigabyte.stock.tradeobserver;

import com.zigabyte.stock.trade.*;
import java.util.Date;

/** A TradeObserver is informed by a TradingAccount after each trade executes,
    and after orders are completed each trading day. **/
public interface TradeObserver {

    /** The TradingAccount was initialized.
      @param date initial date for simulation.
      @param account account that was initialized. **/
    public void initialized(Date date, TradingAccount account);

    /** The TradingAccount executed the BUY order.
      @param order The order executed.  Its executed price has been set.
      @param tradingDate The trading date.
      @param position The resulting position in the stock after the trade.
      @param account The account making the trade. **/
    public void orderBought(TradeOrder order, Date tradingDate, StockPosition position, TradingAccount account);

    /** The TradingAccount executed the SELL order.
      @param order The order executed.  Its executed price has been set.
      @param tradingDate The trading date.
      @param position The resulting position in the stock after the trade.
      @param account The account making the trade. **/
    public void orderSold(TradeOrder order, Date tradingDate, StockPosition position, TradingAccount account);

    /** The TradingAccount cancelled the order (no price available).
      @param order The order cancelled.  Its executed price is 0.
      @param tradingDate The trading date.
      @param position The current position in the stock.
      @param account The account making the trade. **/
    public void orderCancelled(TradeOrder order, Date tradingDate, StockPosition position, TradingAccount account);

    /** The TradingAccount has finished executing its orders for tradingDate.
      This is called only on trading days.  It is called whether or not
      the account executed any trades that day.
      @param tradingDate The trading date.
      @param account The observed account. **/
    public void ordersCompleted(Date tradingDate, TradingAccount account);
}
