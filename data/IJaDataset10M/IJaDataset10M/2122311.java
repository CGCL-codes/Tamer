package org.mov.analyser;

import java.util.*;
import org.mov.portfolio.*;
import org.mov.quote.*;
import org.mov.util.*;

public class BasicPaperTradeResult implements PaperTradeResult {

    private Portfolio portfolio;

    private ScriptQuoteBundle quoteBundle;

    private float initialCapital;

    private float tradeCost;

    private String buyRule;

    private String sellRule;

    private TradingDate startDate;

    private TradingDate endDate;

    public BasicPaperTradeResult(Portfolio portfolio, ScriptQuoteBundle quoteBundle, float initialCapital, float tradeCost, String buyRule, String sellRule, TradingDate startDate, TradingDate endDate) {
        this.portfolio = portfolio;
        this.quoteBundle = quoteBundle;
        this.initialCapital = initialCapital;
        this.tradeCost = tradeCost;
        this.buyRule = buyRule;
        this.sellRule = sellRule;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TradingDate getStartDate() {
        return startDate;
    }

    public TradingDate getEndDate() {
        return endDate;
    }

    public String getSymbols() {
        Vector symbolsTraded = getPortfolio().getSymbolsTraded();
        String string = new String();
        Iterator iterator = symbolsTraded.iterator();
        while (iterator.hasNext()) {
            String symbol = (String) iterator.next();
            symbol = symbol.toUpperCase();
            if (string.length() > 0) string = string.concat(", " + symbol); else string = symbol;
        }
        return string;
    }

    public String getBuyRule() {
        return buyRule;
    }

    public String getSellRule() {
        return sellRule;
    }

    public float getTradeCost() {
        return tradeCost;
    }

    public int getNumberTrades() {
        int accumulateTrades = getPortfolio().countTransactions(Transaction.ACCUMULATE);
        int reduceTrades = getPortfolio().countTransactions(Transaction.REDUCE);
        return accumulateTrades + reduceTrades;
    }

    public float getInitialCapital() {
        return initialCapital;
    }

    public float getFinalCapital() {
        float finalCapital = 0.0F;
        try {
            finalCapital = portfolio.getValue(getQuoteBundle(), getEndDate());
        } catch (MissingQuoteException e) {
            assert false;
        }
        return finalCapital;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public ScriptQuoteBundle getQuoteBundle() {
        return quoteBundle;
    }
}
