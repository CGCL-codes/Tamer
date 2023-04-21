package com.fh.auge.yahoo;

import java.io.IOException;
import java.util.Currency;
import com.fh.auge.core.quote.Quote;
import com.fh.auge.core.quote.QuoteProvider;
import com.fh.auge.core.quote.SymbolNotFoundException;

public class RandomQuoteProvider implements QuoteProvider {

    public Quote getQuote(String symbol, Currency currency) throws IOException, SymbolNotFoundException {
        return null;
    }
}
