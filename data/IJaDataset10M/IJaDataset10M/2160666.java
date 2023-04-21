package com.fh.auge.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.junit.Test;
import com.domainlanguage.money.Money;
import com.domainlanguage.time.CalendarDate;
import com.domainlanguage.time.TimePoint;
import com.fh.auge.performance.Gain;
import com.fh.auge.quote.OHLC;
import com.fh.auge.quote.Quote;
import com.fh.auge.quote.QuoteUtils;
import com.fh.auge.quote.TimeSeries;

public class PerformanceIndicatorTest {

    @Test
    public void testHist() {
        CalendarDate today = TimePoint.from(new Date()).calendarDate(TimeZone.getDefault());
        CalendarDate start = today.plusDays(-8);
        List<Quote> items = new ArrayList<Quote>();
        items.add(createHQ(start, 100));
        items.add(createHQ(start.plusDays(1), 10));
        items.add(createHQ(start.plusDays(2), 20));
        items.add(createHQ(start.plusDays(4), 40));
        items.add(createHQ(start.plusDays(5), 50));
        items.add(createHQ(start.plusDays(8), 80));
        TimeSeries<Quote> qs1 = QuoteUtils.createQuoteSeries(items);
        TimeSeries<Money> qs1Ts = QuoteUtils.createMoneySeriesFromQuotes(qs1, OHLC.Type.CLOSE);
        items = new ArrayList<Quote>();
        items.add(createHQ(start.plusDays(1), 1));
        items.add(createHQ(start.plusDays(2), 2));
        items.add(createHQ(start.plusDays(3), 3));
        items.add(createHQ(start.plusDays(6), 6));
        items.add(createHQ(start.plusDays(8), 8));
        TimeSeries<Quote> qs2 = QuoteUtils.createQuoteSeries(items);
        TimeSeries<Money> qs2Ts = QuoteUtils.createMoneySeriesFromQuotes(qs2, OHLC.Type.CLOSE);
        TimeSeries<Money> resTs = TimeSeries.moneySum(qs1Ts, qs2Ts);
    }

    private Quote createHQ(CalendarDate start, double m) {
        return new Quote(start, Money.euros(m), Money.euros(m), Money.euros(m), Money.euros(m), Long.valueOf(12345));
    }

    private Money createM(double m) {
        return Money.euros(m);
    }

    private Gain createG(double m1, double m2) {
        return Gain.create(createM(m1), createM(m2));
    }
}
