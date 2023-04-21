package com.fh.auge.core.internal.ext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.springframework.beans.factory.annotation.Autowired;
import com.domainlanguage.money.Money;
import com.domainlanguage.time.CalendarDate;
import com.domainlanguage.time.CalendarInterval;
import com.fh.auge.core.Investment;
import com.fh.auge.core.Quote;
import com.fh.auge.core.Stock;
import com.fh.auge.core.TimeUtils;
import com.fh.auge.core.internal.IntervalMap;

public class TimeSeriesFactory {

    private QuoteService quoteService;

    public TimeSeries create(Stock stock, CalendarInterval interval) {
        TimeSeries t1 = new TimeSeries(stock.getSecurity().getName());
        for (Quote quote : quoteService.getQuotes(stock, interval)) {
            Day day = createDay(quote.getCalendarDate());
            t1.add(day, quote.getClose().breachEncapsulationOfAmount());
        }
        return t1;
    }

    private CalendarInterval getInterval(Investment intvestment, CalendarInterval interval) {
        CalendarInterval investmentCalendar = CalendarInterval.inclusive(intvestment.getPurchaseDate(), TimeUtils.getToday());
        CalendarInterval i = (CalendarInterval) investmentCalendar.intersect(interval);
        return i;
    }

    private Stock getStock(Investment intvestment) {
        return intvestment.getStock();
    }

    private Map<Investment, List<Quote>> getQuoteMap(InvestmentGroup group, CalendarInterval interval) {
        Map<Investment, List<Quote>> quotes = new HashMap<Investment, List<Quote>>();
        for (Investment intvestment : group.getInvestments()) {
            Stock stock = getStock(intvestment);
            CalendarInterval investmentInterval = getInterval(intvestment, interval);
            if (!investmentInterval.isEmpty()) {
                List<Quote> q = quoteService.getQuotesWithFixedStart(stock, investmentInterval);
                quotes.put(intvestment, q);
            }
        }
        return quotes;
    }

    @SuppressWarnings("unchecked")
    private List<CalendarDate> getDateList(Map<Investment, List<Quote>> map, CalendarInterval interval) {
        List<CalendarDate> dates = new ArrayList<CalendarDate>();
        for (List<Quote> quotes : map.values()) {
            for (Quote quote : quotes) {
                if (!dates.contains(quote.getCalendarDate())) dates.add(quote.getCalendarDate());
            }
        }
        for (Investment investment : map.keySet()) {
            System.err.println("||||" + investment.getPurchaseDate() + interval.includes(investment.getPurchaseDate()));
            if (interval.includes(investment.getPurchaseDate()) && !dates.contains(investment.getPurchaseDate())) {
                dates.add(investment.getPurchaseDate());
            }
        }
        Collections.sort(dates);
        return dates;
    }

    public TimeSeries create(InvestmentGroup group, CalendarInterval interval) {
        Map<Investment, List<Quote>> map = getQuoteMap(group, interval);
        List<CalendarDate> dates = getDateList(map, interval);
        Map<Investment, IntervalMap> intervalMap = getIntervalMap(map);
        TimeSeries ts = new TimeSeries("Group");
        for (CalendarDate calendarDate : dates) {
            Money m = null;
            for (Investment investment : map.keySet()) {
                IntervalMap im = intervalMap.get(investment);
                Quote q = im.get(calendarDate);
                if (q != null) {
                    Money close = q.getClose().times(investment.getSharesOwned());
                    if (m == null) m = close; else m = m.plus(close);
                }
            }
            if (m != null) {
                ts.add(createDay(calendarDate), m.breachEncapsulationOfAmount());
            }
        }
        return ts;
    }

    private Map<Investment, IntervalMap> getIntervalMap(Map<Investment, List<Quote>> map) {
        Map<Investment, IntervalMap> intervalMap = new HashMap<Investment, IntervalMap>();
        for (Investment investment : map.keySet()) {
            List<Quote> quotes = map.get(investment);
            intervalMap.put(investment, new IntervalMap(quotes));
        }
        return intervalMap;
    }

    private Day createDay(CalendarDate date) {
        Day day = new Day(date.breachEncapsulationOf_day(), date.breachEncapsulationOf_month(), date.breachEncapsulationOf_year());
        return day;
    }

    @Autowired
    public void setQuoteService(QuoteService quoteService) {
        this.quoteService = quoteService;
    }
}
