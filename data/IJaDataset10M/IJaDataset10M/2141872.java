package com.zigabyte.stock.tradeobserver;

import com.zigabyte.metastock.data.*;
import com.zigabyte.stock.trade.*;
import java.util.*;
import java.text.*;
import java.io.*;

/** Observes a trading account and periodically writes a summary of
    the account value.

    @see TradeTraceObserver
**/
public class PeriodTraceObserver extends AbstractPeriodObserver {

    static final DateFormat DATE_FORMAT = new SimpleDateFormat("ddMMMyyyy");

    static final NumberFormat DOLLAR_FORMAT = new DecimalFormat("$#,##0.00");

    double lastCashBalance = Double.NaN, lastStockValue = Double.NaN;

    PrintWriter writer;

    /** Creates an unaligned periodic observer that outputs to {@link System#out}.
      @param unitCount number of calendar units in period
      @param calendarUnit unit counted, such as {@link Calendar#YEAR}
      or {@link Calendar#MONTH} or {@link Calendar#WEEK_OF_YEAR} or
      {@link Calendar#DATE}.
  **/
    public PeriodTraceObserver(int unitCount, int calendarUnit) {
        this(unitCount, calendarUnit, false, new PrintWriter(new OutputStreamWriter(System.out), true));
    }

    /** Creates a periodic observer that outputs to {@link System#out}.
      @param unitCount number of calendar units in period
      @param calendarUnit unit counted, such as {@link Calendar#YEAR}
      or {@link Calendar#MONTH} or {@link Calendar#WEEK_OF_YEAR} or
      {@link Calendar#DATE}.
      @param align whether to align to calendar unit, so for example
      monthly periods start on 1st day of month.
  **/
    public PeriodTraceObserver(int unitCount, int calendarUnit, boolean align) {
        this(unitCount, calendarUnit, align, new PrintWriter(new OutputStreamWriter(System.out), true));
    }

    /** Creates a trace observer that outputs to writer
      @param unitCount number of calendar units in period
      @param calendarUnit unit counted, such as {@link Calendar#YEAR}
      or {@link Calendar#MONTH} or {@link Calendar#WEEK_OF_YEAR} or
      {@link Calendar#DATE}.
      @param align whether to align to calendar unit, so for example
      monthly periods start on 1st day of month.
      @param writer where to write trace. **/
    public PeriodTraceObserver(int unitCount, int calendarUnit, boolean align, PrintWriter writer) {
        super(unitCount, calendarUnit, align);
        this.writer = writer;
    }

    /** Write initial cash balance and save value. **/
    public void initialized(Date date, TradingAccount account) {
        super.initialized(date, account);
        this.lastCashBalance = account.getCurrentCashBalance();
        this.lastStockValue = account.getCurrentStockValue();
        writer.println(DATE_FORMAT.format(date) + ": " + "cash=" + DOLLAR_FORMAT.format(this.lastCashBalance));
    }

    /** update values for current period. **/
    public void ordersCompleted(Date date, TradingAccount account) {
        super.ordersCompleted(date, account);
        this.lastCashBalance = account.getCurrentCashBalance();
        this.lastStockValue = account.getCurrentStockValue();
        this.lastDate = date;
    }

    /** Write [DATE]: cash=[CASHVALUE], stocks=[STOCKVALUE], total=[TOTALVALUE]
      based on values for last period. **/
    protected void periodStarted(Date lastPeriodStartDate, Date lastPeriodEndDate, Date tradingDate, TradingAccount account) {
        double total = this.lastCashBalance + this.lastStockValue;
        this.writer.println(DATE_FORMAT.format(lastDate) + ": " + "cash=" + DOLLAR_FORMAT.format(this.lastCashBalance) + ", " + "stocks=" + DOLLAR_FORMAT.format(this.lastStockValue) + ", " + "total=" + DOLLAR_FORMAT.format(total));
    }
}
