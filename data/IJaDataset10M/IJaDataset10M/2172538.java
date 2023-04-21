package com.fh.auge.core.internal.security;

import java.util.List;
import com.domainlanguage.time.CalendarDate;
import com.domainlanguage.time.CalendarInterval;
import com.fh.auge.core.IMarket;
import com.fh.auge.core.security.ISecurity;
import com.fh.auge.core.security.Quote;

public interface IQuoteDao {

    public Quote getQuote(ISecurity security, IMarket market, CalendarDate date);

    public void addHistoricalQuotes(ISecurity security, IMarket market, List<Quote> quotes);

    public void setLatestQuote(ISecurity security, IMarket market, Quote quote);

    public void dropQuotes(ISecurity security, IMarket market);

    public CalendarInterval getHoldInterval(ISecurity security, IMarket market);

    public void dropQuotes(ISecurity security);
}
