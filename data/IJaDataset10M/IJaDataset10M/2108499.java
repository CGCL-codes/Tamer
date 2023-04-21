package nz.org.venice.chart.source;

import nz.org.venice.chart.Graphable;
import nz.org.venice.util.Locale;
import nz.org.venice.util.Money;
import nz.org.venice.util.TradingDate;
import nz.org.venice.portfolio.Account;
import nz.org.venice.portfolio.Portfolio;
import nz.org.venice.quote.MissingQuoteException;
import nz.org.venice.quote.EODQuoteBundle;
import java.util.Iterator;
import java.util.List;

/**
 * Provides a Portfolio graph source. This class allows portfolios to be graphed.
 *
 * @author Andrew Leppard
 */
public class PortfolioGraphSource implements GraphSource {

    /** Graph the market value (day close) of the portfolio */
    public static final int MARKET_VALUE = 0;

    /** Graph the portfolio showing the return value */
    public static final int RETURN_VALUE = 1;

    /** Graph the portfolio cash value */
    public static final int CASH_VALUE = 2;

    /** Graph the portfolio share value */
    public static final int SHARE_VALUE = 3;

    /** Graph the number of stocks held */
    public static final int STOCKS_HELD = 4;

    private static final int ACCOUNT_VALUE = 5;

    private EODQuoteBundle quoteBundle;

    private int mode;

    private Graphable graphable;

    private Portfolio portfolio;

    private String accountName;

    /**
     * Create a graph source to graph the value of a portfolio. This constructor
     * allows you to graph the market value of the portfolio, the profit/loss made by
     * the portfolio, and the value of the portfolio's cash or share holdings.
     *
     * @param	portfolio	the portfolio to graph
     * @param	quoteBundle	quote bundle containing all the necessary
     *				quotes to calculate the portfolio value
     *				for every day
     * @param	mode		{@link #MARKET_VALUE} for the total market value of the portfolio;
     *                          {@link #RETURN_VALUE} for the return moade;
     *			        {@link #CASH_VALUE} for the cash value of the portfolio;
     *                          {@link #STOCKS_HELD} for the number of stocks held in the portfolio;
     *                          or {@link #SHARE_VALUE} for the share value of the portfolio.
     */
    public PortfolioGraphSource(Portfolio portfolio, EODQuoteBundle quoteBundle, int mode) {
        this.portfolio = portfolio;
        this.quoteBundle = quoteBundle;
        this.mode = mode;
        this.accountName = null;
        createGraphable();
    }

    /**
     * Create a graph source to graph the value of a single account in the portfolio.
     *
     * @param	portfolio	the portfolio to graph
     * @param	quoteBundle	quote bundle containing all the necessary
     *				quotes to calculate the portfolio value
     *				for every day
     * @param   accountName     name of account in portfolio to graph.
     */
    public PortfolioGraphSource(Portfolio portfolio, EODQuoteBundle quoteBundle, String accountName) {
        this.portfolio = portfolio;
        this.quoteBundle = quoteBundle;
        this.mode = ACCOUNT_VALUE;
        this.accountName = accountName;
        createGraphable();
    }

    private void createGraphable() {
        if (portfolio.getStartDate() != null) {
            graphable = new Graphable();
            List dateRange = TradingDate.dateRangeToList(portfolio.getStartDate(), quoteBundle.getLastDate());
            Iterator portfolioIterator = portfolio.iterator();
            for (Iterator dateIterator = dateRange.iterator(); dateIterator.hasNext(); ) {
                TradingDate date = (TradingDate) dateIterator.next();
                Portfolio portfolio = (Portfolio) portfolioIterator.next();
                try {
                    Money value;
                    if (mode == MARKET_VALUE) value = portfolio.getValue(quoteBundle, date); else if (mode == CASH_VALUE) value = portfolio.getCashValue(date); else if (mode == SHARE_VALUE) value = portfolio.getShareValue(quoteBundle, date); else if (mode == RETURN_VALUE) value = portfolio.getReturnValue(quoteBundle, date); else if (mode == STOCKS_HELD) {
                        int stocksHeld = portfolio.getStocksHeld().size();
                        value = new Money((double) stocksHeld);
                    } else {
                        assert mode == ACCOUNT_VALUE;
                        assert accountName != null;
                        Account account = portfolio.findAccountByName(accountName);
                        assert account != null;
                        value = account.getValue(quoteBundle, date);
                    }
                    graphable.putY((Comparable) date, new Double(value.doubleValue()));
                } catch (MissingQuoteException e) {
                }
            }
        } else graphable = null;
    }

    public Graphable getGraphable() {
        return graphable;
    }

    public String getName() {
        return portfolio.getName();
    }

    public int getType() {
        return GraphSource.PORTFOLIO;
    }

    public String getToolTipText(Comparable x) {
        TradingDate date = (TradingDate) x;
        Double value = graphable.getY(x);
        if (value != null) {
            String name = portfolio.getName();
            if (mode == RETURN_VALUE) {
                name = name.concat(" ");
                name = name.concat(Locale.getString("PROFIT"));
            }
            return new String("<html>" + name + ", " + date.toLongString() + "<p>" + getYLabel(value.doubleValue()));
        } else {
            return null;
        }
    }

    public String getYLabel(double value) {
        if (mode == STOCKS_HELD) return Integer.toString((int) value); else return Money.toString(value);
    }

    public double[] getAcceptableMajorDeltas() {
        if (mode == STOCKS_HELD) {
            double[] major = { 1D, 10D, 100D, 1000D, 10000D, 100000D, 1000000D, 10000000D, 100000000D, 1000000000D, 10000000000D };
            return major;
        } else {
            double[] major = { 0.001D, 0.01D, 0.1D, 1.0D, 10.0D, 100.0D, 1000.0D, 10000.0D, 100000.0D, 1000000.0D, 10000000.0D, 100000000.0D, 1000000000.0D, 1000000000.0D };
            return major;
        }
    }

    public double[] getAcceptableMinorDeltas() {
        if (mode == STOCKS_HELD) {
            double[] minor = { 1D, 2D, 3D, 4D, 5D, 6D, 8D };
            return minor;
        } else {
            double[] minor = { 1D, 1.1D, 1.25D, 1.3333D, 1.5D, 2D, 2.25D, 2.5D, 3D, 3.3333D, 4D, 5D, 6D, 6.5D, 7D, 7.5D, 8D, 9D };
            return minor;
        }
    }
}
