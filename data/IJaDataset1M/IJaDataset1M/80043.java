package com.aces2win.server;

import java.util.ArrayList;
import java.util.Date;
import com.aces2win.client.BackTestEngineService;
import com.aces2win.server.backtestengine.AmbiguousSituationException;
import com.aces2win.server.backtestengine.BackTestEngine;
import com.aces2win.server.backtestengine.Criterias;
import com.aces2win.server.entities.Quote;
import com.aces2win.server.entities.Wallet;
import com.aces2win.server.util.YahooFinanceQuoteUtil;
import com.aces2win.shared.beans.BackTestEngineResult;
import com.aces2win.shared.beans.QuoteBean;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.aces2win.server.entities.Trade;

public class BackTestEngineServiceImpl extends RemoteServiceServlet implements BackTestEngineService {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6718679139722102726L;

    public BackTestEngineResult performBackTestEngine(String symbol, Date from, Date to) {
        ArrayList<Quote> myQuotes = YahooFinanceQuoteUtil.fetchAllQuotes(symbol, from, to);
        ArrayList<QuoteBean> myQuotesBean = new ArrayList<QuoteBean>();
        for (Quote q : myQuotes) {
            myQuotesBean.add(q.toBean());
        }
        Criterias c = new Criterias() {

            public boolean shortConditions(ArrayList<Quote> quotes) {
                return false;
            }

            public boolean longConditions(ArrayList<Quote> quotes) {
                return true;
            }

            public long sizingConditions(ArrayList<Quote> quotes, Wallet wallet) {
                return Math.round(wallet.getBalance() / quotes.get(quotes.size() - 1).getOpen());
            }

            @Override
            public boolean closePosition(ArrayList<Quote> quotes, boolean isLong) {
                return true;
            }

            @Override
            public double stopLossConditions(ArrayList<Quote> quotes, Trade t) {
                return -1;
            }

            @Override
            public double stopProfitConditions(ArrayList<Quote> quotes, Trade t) {
                return -1;
            }
        };
        Wallet w = new Wallet();
        w.setBalance(5000);
        BackTestEngine backtestEngine = new BackTestEngine(w, myQuotes);
        try {
            backtestEngine.perform(c);
        } catch (AmbiguousSituationException e) {
            e.printStackTrace();
        }
        BackTestEngineResult result = new BackTestEngineResult();
        result.setWallet(w.toBean());
        result.setQuotes(myQuotesBean);
        return result;
    }
}
