package com.fh.auge.core.quote;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Currency;

public class QuoteSource {

    private String id;

    private String name;

    private String searchUrlPattern;

    private String overviewUrlPattern;

    private QuoteProvider provider;

    public QuoteSource(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public QuoteSource(String id, String name, String searchUrlPattern, String overviewUrlPattern, QuoteProvider provider) {
        this(id, name);
        this.searchUrlPattern = searchUrlPattern;
        this.overviewUrlPattern = overviewUrlPattern;
        this.provider = provider;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getSearchUrl(String name) {
        int index = name.trim().indexOf(" ");
        String search = (index == -1) ? name : name.substring(0, index);
        return MessageFormat.format(searchUrlPattern, new Object[] { search });
    }

    public String getOverviewUrl(String symbol) {
        return MessageFormat.format(overviewUrlPattern, new Object[] { symbol });
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final QuoteSource other = (QuoteSource) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

    public Quote getQuote(String symbol, Currency currency) throws IOException, SymbolNotFoundException {
        return provider.getQuote(symbol, currency);
    }
}
