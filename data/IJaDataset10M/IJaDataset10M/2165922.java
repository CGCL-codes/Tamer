package com.fh.auge.security;

import java.util.Collections;
import java.util.Currency;
import org.junit.Before;
import org.junit.Test;
import com.fh.auge.quote.update.QuoteUpdateService;

public class QuoteUpdateServiceTest {

    private QuoteUpdateService service;

    private Security security;

    private Market market;

    @Before
    public void setUp() {
        DataFactory.clearTables();
        service = DataFactory.getQuoteUpdateService();
        market = new Market("fra", "Frankfurt", Currency.getInstance("EUR"));
        market.getProperties().setProperty("yahoo.de", "f");
        security = new Security("aapl", market);
        security.getProperties().setProperty("yahoo.de", "apc");
    }

    @Test
    public void testHist() {
        service.updateHistoricalQuotes(Collections.singleton(security));
    }

    @Test
    public void testLive() {
        service.updateIntradayQuote(Collections.singleton(security));
    }
}
