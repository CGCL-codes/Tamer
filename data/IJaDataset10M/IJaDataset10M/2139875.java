package user;

import com.zigabyte.metastock.data.*;
import com.zigabyte.stock.trade.*;
import com.zigabyte.stock.indicator.*;
import com.zigabyte.stock.strategy.*;
import java.util.*;

public class TwoWeekMomentum implements TradingStrategy {

    private static final Indicator FastLine = new MovingAverage(3);

    private static final Indicator SlowLine = new MovingAverage(20);

    private static final Indicator MAXWEEK = new MovingMaximum(7, true);

    private static final Indicator DOLLARVOLUME = new MovingDollarVolume(20);

    private final Map<String, Double> previousMaxPriceCache = new HashMap<String, Double>();

    private Date lastDateCached = null;

    private final double minCashMaxBuyFraction;

    public TwoWeekMomentum(double sizingFactor) {
        this.minCashMaxBuyFraction = 1 / sizingFactor;
    }

    /** Calls {@link #placeSellOrders placeSellOrders} then
      {@link #placeBuyOrders placeBuyOrders} **/
    public void placeTradeOrders(final StockMarketHistory histories, final TradingAccount account, final Date date, int daysUntilMarketOpen) {
        placeBuyOrders(histories, account, date);
        placeSellOrders(histories, account, date);
    }

    /** Every trading day, look at the stocks that are in the account (if any),
      and sell next day at the Open if the stock's last Closing price
      13-day moving average is below the 50-day moving average. **/
    protected void placeSellOrders(final StockMarketHistory histories, final TradingAccount account, final Date date) {
        if (!histories.hasTradingData(date)) return;
        for (StockPosition position : account) {
            String symbol = position.getSymbol();
            StockHistory history = histories.get(symbol);
            int item = history.getIndexAtOrBefore(date);
            if (FastLine.compute(history, item) < SlowLine.compute(history, item)) {
                account.sellStock(symbol, position.getShares(), OrderTiming.NEXT_DAY_OPEN, Double.NaN);
            } else if (previousMaxPriceCache != null && previousMaxPriceCache.get(symbol) != null && history.get(item) != null && history.get(item).getAdjustedClose() < (previousMaxPriceCache.get(symbol) + position.getCostBasis()) / 2) {
                account.sellStock(symbol, position.getShares(), OrderTiming.NEXT_DAY_STOP, history.get(item).getAdjustedLow() - .01);
            } else {
                account.sellStock(symbol, position.getShares(), OrderTiming.NEXT_DAY_STOP, position.getCostBasis() / 1.08);
            }
        }
    }

    private double momentum(Date date, StockHistory history, int n) {
        Date base = new Date(date.getTime() - n * 86400000);
        if (history.getIndexAtOrBefore(base) < 0) return 0;
        return history.getAtOrBefore(date).getAdjustedClose() / history.getAtOrBefore(base).getAdjustedClose();
    }

    private int lowestFirst(double a, double b) {
        return a > b ? -1 : (a < b ? 1 : 0);
    }

    private int highestFirst(double a, double b) {
        return -1 * lowestFirst(a, b);
    }

    protected void placeBuyOrders(final StockMarketHistory histories, final TradingAccount account, final Date date) {
        if (dayOfWeek(date) != Calendar.SUNDAY) return;
        if (account.getProjectedCashBalance() < this.minCashMaxBuyFraction * (account.getProjectedCashBalance() + account.getProjectedStockValue())) return;
        updatePreviousMaxPriceCache(histories, date);
        List<StockHistory> candidates = new ArrayList<StockHistory>();
        for (StockHistory history : histories) {
            int item = history.getIndexAtOrBefore(date);
            if (item >= 0) {
                if (FastLine.compute(history, item) <= SlowLine.compute(history, item)) continue;
                candidates.add(history);
            }
        }
        Collections.sort(candidates, new Comparator<StockHistory>() {

            public int compare(StockHistory history1, StockHistory history2) {
                double dollarVolume1 = getDollarVolumeWeek(history1);
                double dollarVolume2 = getDollarVolumeWeek(history2);
                return (dollarVolume1 < dollarVolume2 ? 1 : dollarVolume1 > dollarVolume2 ? -1 : 0);
            }

            private double getDollarVolumeWeek(StockHistory history) {
                return DOLLARVOLUME.compute(history, history.getIndexAtOrBefore(date));
            }
        });
        candidates = candidates.subList(0, candidates.size() / 2);
        Collections.sort(candidates, new Comparator<StockHistory>() {

            public int compare(StockHistory history1, StockHistory history2) {
                double mo1 = momentum(date, history1, 20);
                double mo2 = momentum(date, history2, 20);
                return highestFirst(mo1, mo2);
            }
        });
        candidates = candidates.subList(0, candidates.size() / 3);
        Collections.sort(candidates, new Comparator<StockHistory>() {

            public int compare(StockHistory history1, StockHistory history2) {
                double mo1 = momentum(date, history1, 10);
                double mo2 = momentum(date, history2, 10);
                return highestFirst(mo1, mo2);
            }
        });
        candidates = candidates.subList(0, candidates.size() / 3);
        Collections.sort(candidates, new Comparator<StockHistory>() {

            public int compare(StockHistory history1, StockHistory history2) {
                double mo1 = momentum(date, history1, 3);
                double mo2 = momentum(date, history2, 3);
                return lowestFirst(Math.abs(mo1), Math.abs(mo2));
            }
        });
        double projectedAccountValue = account.getProjectedCashBalance() + account.getProjectedStockValue();
        double cashRemaining = account.getCurrentCashBalance();
        double positionSize = this.minCashMaxBuyFraction * projectedAccountValue;
        for (StockHistory history : candidates) {
            double projectedPrice = (history.getAtOrBefore(date).getAdjustedHigh() + history.getAtOrBefore(date).getAdjustedLow()) / 2;
            int nShares = (int) (positionSize / projectedPrice);
            double projectedCost = nShares * projectedPrice + account.getTradeFees(nShares);
            cashRemaining -= positionSize;
            if (cashRemaining <= 0) break;
            if (nShares > 0) {
                account.buyStock(history.getSymbol(), nShares, OrderTiming.NEXT_DAY_LIMIT, projectedPrice);
            }
        }
    }

    /** Return the day of the week as an int.
      To test if it is Sunday, use
      <pre>
        Calendar.SUNDAY == dayOfWeek(date)
      </pre> **/
    protected int dayOfWeek(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /** 
   **/
    private void updatePreviousMaxPriceCache(StockMarketHistory histories, Date untilDate) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(untilDate);
        calendar.add(Calendar.DATE, -7);
        untilDate = calendar.getTime();
        if (this.lastDateCached == null || this.lastDateCached.getTime() > untilDate.getTime()) {
            this.previousMaxPriceCache.clear();
            this.lastDateCached = null;
        }
        for (StockHistory history : histories) {
            int startIndex = (this.lastDateCached == null ? 0 : history.getIndexAtOrAfter(this.lastDateCached));
            int endIndex = history.getIndexAtOrBefore(untilDate);
            if (startIndex >= 0 && endIndex >= 0) {
                String symbol = history.getSymbol();
                Double cachedMaxPrice = previousMaxPriceCache.get(symbol);
                double maxPrice = (cachedMaxPrice == null ? 0.0 : cachedMaxPrice);
                for (int i = startIndex; i < endIndex; i++) maxPrice = Math.max(maxPrice, history.get(i).getAdjustedHigh());
                previousMaxPriceCache.put(symbol, maxPrice);
            }
        }
        this.lastDateCached = untilDate;
    }

    /** returns shortClassName+"("+minCashMaxBuyFraction+")" **/
    public String toString() {
        String className = this.getClass().getName();
        String shortName = className.substring(className.lastIndexOf('.') + 1);
        return shortName + "(" + this.minCashMaxBuyFraction + ")";
    }
}
