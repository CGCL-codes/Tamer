package com.zigabyte.stock.trade;

import com.zigabyte.metastock.data.StockHistory;
import com.zigabyte.metastock.data.StockMarketHistory;
import com.zigabyte.stock.strategy.TradingStrategy;
import java.util.*;
import java.text.SimpleDateFormat;

/** Simulate trading over a {@link StockMarketHistory}.
 A run starts with a {@link TradingAccount},
 and uses a {@link TradingStrategy} from start date to end date. **/
public class DefaultTradingSimulator {

    protected StockMarketHistory histories;

    public DefaultTradingSimulator(StockMarketHistory histories) {
        this.histories = histories;
    }

    /** Runs strategy for every day from startDate before endDate,
	 then sells off all remaining account positions on endDate. **/
    public void runStrategy(TradingStrategy strategy, TradingAccount account, Date startDate, Date endDate) {
        if (startDate.getTime() > endDate.getTime()) throw new IllegalArgumentException("startDate after endDate");
        Date nextTradingDate = nextTradingDate(startDate);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        final long msecPerDay = 24 * 60 * 60 * 1000;
        for (; calendar.getTimeInMillis() <= endDate.getTime(); calendar.add(Calendar.DAY_OF_MONTH, 1)) {
            Date date = calendar.getTime();
            if (nextTradingDate != null && nextTradingDate.getTime() == date.getTime()) {
                account.executeOrders(date);
            }
            nextTradingDate = nextTradingDate(date);
            if (nextTradingDate != null && nextTradingDate.getTime() <= endDate.getTime()) {
                int daysUntilMarketOpen = (int) ((nextTradingDate.getTime() - date.getTime()) / msecPerDay) - 1;
                strategy.placeTradeOrders(this.histories, account, date, daysUntilMarketOpen);
            }
        }
    }

    /** Return the next trading date after date in this
	 simulator's stock market history. **/
    public Date nextTradingDate(Date date) {
        return this.histories.nextTradingDate(date, true);
    }
}
