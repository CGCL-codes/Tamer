package org.laboratory.investment.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import org.laboratory.investment.dataUniverse.Quote;
import org.laboratory.investment.service.QuotesPersistanceService;

/**
 * Implementacion de persistencia en memoria. Simplemente guarda la referencia a
 * quotes, no baja a disco.
 * 
 * 
 * @author Juan Miguel Albisu Frieyro
 * 
 */
public class MemoryQuotesPersistanceImpl implements QuotesPersistanceService {

    Collection<Quote> quotes;

    String ticker;

    public MemoryQuotesPersistanceImpl(String ticker) {
        this.ticker = ticker;
        quotes = new ArrayList<Quote>();
    }

    @Override
    public Collection<Quote> getQuotes() {
        return quotes;
    }

    @Override
    public void saveQuotes(Collection<Quote> quotes) {
        this.quotes = quotes;
    }
}
