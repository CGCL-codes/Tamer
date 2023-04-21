package org.blogtrader.platform.core.data.dataserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.blogtrader.platform.core.data.DataChangeEvent;
import org.blogtrader.platform.core.data.timeseries.QuoteTimeSeries;
import org.blogtrader.platform.core.data.timeseries.QuoteTimeSeries.QuoteData;
import org.blogtrader.platform.core.data.dataserver.AbstractTickerDataServer.IntervalLastTicker;
import org.blogtrader.platform.core.data.timeseries.TimeUnit;
import org.blogtrader.platform.core.stock.Ticker;

/** This class will load the quote datas from data source to its data storage: quotes.
 * @TODO it will be implemented as a Data Server ?
 *
 * @author Caoyuan Deng
 */
public abstract class AbstractTickerDataServer extends AbstractDataServer {

    protected Map<String, List<Ticker>> dataPools = new HashMap();

    protected Map<String, IntervalLastTicker> tickerHelperMap = new HashMap();

    protected Map<String, Ticker> currentTickerMap = new HashMap();

    protected Calendar calendar = Calendar.getInstance();

    protected boolean ascending;

    protected boolean isAscending() {
        return ascending;
    }

    protected void preRegisterTimeSeries(String symbol) {
        List<Ticker> dataPool = dataPools.get(symbol);
        if (dataPool == null) {
            dataPools.put(symbol, new ArrayList());
        }
    }

    protected void postUnregisterTimeSeries(String symbol) {
        List<Ticker> dataPool = dataPools.get(symbol);
        if (dataPool != null) {
            dataPools.remove(symbol);
            dataPool.clear();
            dataPool = null;
        }
    }

    protected void preRefresh() {
        for (String symbol : registeredTimeSeriesMap.keySet()) {
            composeTimeSeries(symbol);
        }
    }

    protected void preLoad() {
        for (String symbol : registeredTimeSeriesMap.keySet()) {
            List<Ticker> dataPool = dataPools.get(symbol);
            dataPool.clear();
        }
    }

    private List<DataChangeEvent> dataLoadEvents = new ArrayList();

    protected void postLoad() {
        dataLoadEvents.clear();
        for (String symbol : registeredTimeSeriesMap.keySet()) {
            DataChangeEvent event = composeTimeSeries(symbol);
            if (event != null) {
                event.setType(DataChangeEvent.Type.FinishedLoading);
                event.getSource().fireDataChangeEvent(event);
                System.out.println(symbol + ": " + count + ", timeSeries loaded, load server finished");
            }
            dataPools.get(symbol).clear();
        }
    }

    protected void preUpdate() {
        for (String symbol : registeredTimeSeriesMap.keySet()) {
            List<Ticker> dataPool = dataPools.get(symbol);
            if (dataPool == null) {
                dataPools.put(symbol, new ArrayList());
            } else {
                dataPool.clear();
            }
        }
    }

    protected void postUpdate() {
        for (String symbol : registeredTimeSeriesMap.keySet()) {
            DataChangeEvent event = composeTimeSeries(symbol);
            if (event != null) {
                event.setType(DataChangeEvent.Type.Updated);
                event.getSource().fireDataChangeEvent(event);
                System.out.println(event.getSymbol() + ": update event:");
            }
            dataPools.get(symbol).clear();
        }
    }

    private DataChangeEvent composeTimeSeries(String symbol) {
        DataChangeEvent event = null;
        long startTime = Long.MAX_VALUE;
        long endTime = -Long.MAX_VALUE;
        QuoteTimeSeries tickeringTimeSeries = (QuoteTimeSeries) registeredTimeSeriesMap.get(symbol);
        QuoteTimeSeries quoteTimeSeriese = (QuoteTimeSeries) chainTimeSeriesMap.get(tickeringTimeSeries);
        TimeUnit dayUnit = quoteTimeSeriese.getUnit();
        List<Ticker> dataPool = dataPools.get(symbol);
        int size = dataPool.size();
        if (size > 0) {
            boolean needReverseDatas = isAscending() ? false : true;
            Ticker ticker = null;
            int i = needReverseDatas ? size - 1 : 0;
            while (i >= 0 && i <= size - 1) {
                ticker = dataPool.get(i);
                IntervalLastTicker tickerHelper = tickerHelperMap.get(symbol);
                Ticker prevTicker = currentTickerMap.get(symbol);
                QuoteData data;
                if (tickerHelper == null) {
                    tickerHelper = new IntervalLastTicker();
                    tickerHelperMap.put(symbol, tickerHelper);
                    tickerHelper.currIntervalLastTicker = ticker;
                    tickerHelper.prevIntervalLastTicker = new Ticker();
                    data = (QuoteData) tickeringTimeSeries.createEmptyDataOrEmptyIt(ticker.time);
                    data.setTime(ticker.time);
                    data.setOpen(ticker.dayOpen);
                    data.setHigh(ticker.dayHigh);
                    data.setLow(ticker.dayLow);
                    data.setClose(ticker.lastPrice);
                    data.setVolume(1);
                } else {
                    if (isInCurrentInterval(ticker.time, tickerHelper.currIntervalLastTicker.time)) {
                        tickerHelper.currIntervalLastTicker = ticker;
                        data = (QuoteData) tickeringTimeSeries.getData(ticker.time);
                    } else {
                        tickerHelper.prevIntervalLastTicker = tickerHelper.currIntervalLastTicker;
                        tickerHelper.currIntervalLastTicker = ticker;
                        data = (QuoteData) tickeringTimeSeries.createEmptyDataOrEmptyIt(ticker.time);
                        data.setTime(ticker.time);
                        data.setHigh(-Float.MAX_VALUE);
                        data.setLow(+Float.MAX_VALUE);
                        data.setOpen(ticker.lastPrice);
                    }
                    if (ticker.dayHigh > prevTicker.dayHigh) {
                        data.setHigh(ticker.dayHigh);
                    }
                    data.setHigh(Math.max(data.getHigh(), ticker.lastPrice));
                    if (ticker.dayLow < prevTicker.dayLow) {
                        data.setLow(ticker.dayLow);
                    }
                    data.setLow(Math.min(data.getLow(), ticker.lastPrice));
                    data.setClose(ticker.lastPrice);
                    data.setVolume(ticker.dayVolume - tickerHelper.prevIntervalLastTicker.dayVolume);
                }
                currentTickerMap.put(symbol, ticker);
                if (needReverseDatas) {
                    i--;
                } else {
                    i++;
                }
                long currentTime = data.getTime();
                startTime = Math.min(startTime, currentTime);
                endTime = Math.max(endTime, currentTime);
                updateDailyQuoteData(quoteTimeSeriese, ticker);
            }
            event = new DataChangeEvent(tickeringTimeSeries, null, symbol, startTime, endTime, ticker);
        } else {
            Ticker ticker = currentTickerMap.get(symbol);
            if (ticker != null) {
                long today = dayUnit.beginTimeOfUnitThatInclude(ticker.time);
                ;
                if (quoteTimeSeriese.getData(today) == null) {
                    updateDailyQuoteData(quoteTimeSeriese, ticker);
                }
            }
        }
        return event;
    }

    /**
     * Try to update (long)today's quote data according to ticker, if it does not
     * exist, create a new one, if stock's quoteDataServer exist, fire a data
     * updated event.
     */
    private void updateDailyQuoteData(QuoteTimeSeries quoteTimeSeries, Ticker ticker) {
        long today = TimeUnit.Day.beginTimeOfUnitThatInclude(ticker.time);
        QuoteData quoteDataToday = (QuoteData) quoteTimeSeries.getData(today);
        if (quoteDataToday == null) {
            quoteDataToday = (QuoteData) quoteTimeSeries.createEmptyDataOrEmptyIt(today);
            quoteDataToday.setTime(today);
        }
        if (ticker.dayHigh != 0 && ticker.dayLow != 0) {
            quoteDataToday.setOpen(ticker.dayOpen);
            quoteDataToday.setHigh(ticker.dayHigh);
            quoteDataToday.setLow(ticker.dayLow);
            quoteDataToday.setClose(ticker.lastPrice);
            quoteDataToday.setVolume(ticker.dayVolume);
            quoteDataToday.setClose_Ori(ticker.lastPrice);
            quoteDataToday.setClose_Adj(ticker.lastPrice);
            DataChangeEvent evt = new DataChangeEvent(quoteTimeSeries, DataChangeEvent.Type.Updated, "", today, today);
            quoteTimeSeries.fireDataChangeEvent(evt);
        }
    }

    protected boolean isInCurrentInterval(long time, long currentIntervalBeginTime) {
        calendar.setTimeInMillis(time);
        int year1 = calendar.get(Calendar.YEAR);
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);
        int hour1 = calendar.get(Calendar.HOUR_OF_DAY);
        int minute1 = calendar.get(Calendar.MINUTE);
        calendar.setTimeInMillis(currentIntervalBeginTime);
        int year2 = calendar.get(Calendar.YEAR);
        int day2 = calendar.get(Calendar.DAY_OF_YEAR);
        int hour2 = calendar.get(Calendar.HOUR_OF_DAY);
        int minute2 = calendar.get(Calendar.MINUTE);
        if (year1 == year2 && day1 == day2 && hour1 == hour2 && minute1 == minute2) {
            return true;
        } else {
            return false;
        }
    }

    private static Ticker blankTicker = new Ticker();

    public Ticker getCurrentTicker(String symbol) {
        IntervalLastTicker tickerHelper = tickerHelperMap.get(symbol);
        Ticker currentTicker = currentTickerMap.get(symbol);
        if (tickerHelper != null && tickerHelper.prevIntervalLastTicker != null && currentTicker != null) {
            return currentTicker;
        } else {
            blankTicker.fullName = symbol;
            return blankTicker;
        }
    }

    public Map<String, Ticker> getCurrentTickerMap() {
        return currentTickerMap;
    }

    protected void postStopUpdateServer() {
        tickerHelperMap.clear();
        currentTickerMap.clear();
    }

    public class IntervalLastTicker {

        public Ticker currIntervalLastTicker;

        public Ticker prevIntervalLastTicker;
    }
}
