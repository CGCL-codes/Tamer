package nz.org.venice.portfolio;

import nz.org.venice.quote.MissingQuoteException;
import nz.org.venice.quote.EODQuoteBundle;
import nz.org.venice.util.Currency;
import nz.org.venice.util.Money;
import nz.org.venice.util.TradingDate;

/**
 * Generic interface for all financial account objects. This interface
 * defines some generic properties that all accounts need to have such
 * as name, type and value.
 *
 * @author Andrew Leppard
 */
public interface Account {

    /** Account is a cash account (bank account, cash management account etc)
     */
    public static final int CASH_ACCOUNT = 0;

    /** Account is a share trading account which contains a list of shares */
    public static final int SHARE_ACCOUNT = 1;

    /**
     * Return the name of this account.
     *
     * @return	name of the account
     */
    public String getName();

    /**
     * Return the type of this account.
     *
     * @return	type of the account, either {@link #CASH_ACCOUNT} or
     *		{@link #SHARE_ACCOUNT}
     */
    public int getType();

    /**
     * Return the value of this account on the given day.
     *
     * @param	quoteBundle	the quote bundle
     * @param	dateOffset fast date offset
     */
    public Money getValue(EODQuoteBundle quoteBundle, int dateOffset) throws MissingQuoteException;

    /**
     * Return the value of this account on the given day.
     *
     * @param quoteBundle the quote bundle
     * @param date        the date
     */
    public Money getValue(EODQuoteBundle quoteBundle, TradingDate date) throws MissingQuoteException;

    /**
     * Return the currency of the account.
     *
     * @return default currency.
     */
    public Currency getCurrency();

    /**
     * Perform a transaction on this account.
     *
     * @param	transaction	transaction to occur
     */
    public void transaction(Transaction transaction);

    /**
     * Remove all transactions from account.
     */
    public void removeAllTransactions();
}
