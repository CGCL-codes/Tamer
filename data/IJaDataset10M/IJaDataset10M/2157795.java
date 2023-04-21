package org.mov.quote;

import junit.framework.TestCase;
import org.mov.quote.EODQuote;
import org.mov.quote.MetaStockQuoteFilter;
import org.mov.quote.Symbol;
import org.mov.quote.SymbolFormatException;
import org.mov.util.TradingDate;

public class MetaStock2QuoteFilterTest extends TestCase {

    public void testConvert() {
        EODQuoteFilter filter = new MetaStock2QuoteFilter();
        EODQuote quote = null;
        try {
            quote = new EODQuote(Symbol.find("AAA"), new TradingDate(), 10000, 10.00D, 20.00D, 30.00D, 40.00D);
        } catch (SymbolFormatException e) {
            fail("Couldn't create symbol AAA");
        }
        EODQuote filteredQuote = null;
        String filteredString;
        filteredString = filter.toString(quote);
        try {
            filteredQuote = filter.toEODQuote(filteredString);
        } catch (QuoteFormatException e) {
            fail("Error parsing '" + filteredString + "'");
        }
        assertEquals(filteredQuote, quote);
    }
}
