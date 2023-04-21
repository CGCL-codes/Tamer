package org.mov.portfolio;

import junit.framework.TestCase;
import org.mov.quote.Symbol;
import org.mov.quote.SymbolFormatException;
import org.mov.util.Money;
import org.mov.util.TradingDate;
import java.io.File;
import java.io.IOException;

/**
 * Tests the portfolio, accounts and transaction classes.
 *
 * @author Andrew Leppard
 */
public class PortfolioTest extends TestCase {

    private static final String PORTFOLIO_NAME = "TestPortfolioName";

    private static final String SHARE_ACCOUNT_NAME = "ShareAccountName";

    private static final String CASH_ACCOUNT_NAME = "CashAccountName";

    private static final String CASH_ACCOUNT_NAME2 = "CashAccountName2";

    /**
     * General portfolio test.
     */
    public void testPortfolio() {
        TradingDate today = new TradingDate();
        TradingDate yesterday = today.previous(1);
        Symbol CBA = null;
        try {
            CBA = Symbol.find("CBA");
        } catch (SymbolFormatException e) {
            fail(e.getMessage());
        }
        Portfolio portfolio = new Portfolio(PORTFOLIO_NAME);
        assertEquals(portfolio.getName(), PORTFOLIO_NAME);
        assertFalse(portfolio.isTransient());
        CashAccount cashAccount = new CashAccount(CASH_ACCOUNT_NAME);
        CashAccount cashAccount2 = new CashAccount(CASH_ACCOUNT_NAME2);
        assertEquals(cashAccount.getName(), CASH_ACCOUNT_NAME);
        ShareAccount shareAccount = new ShareAccount(SHARE_ACCOUNT_NAME);
        assertEquals(shareAccount.getName(), SHARE_ACCOUNT_NAME);
        portfolio.addAccount(cashAccount);
        portfolio.addAccount(cashAccount2);
        portfolio.addAccount(shareAccount);
        assertEquals(portfolio.getAccounts().size(), 3);
        assertEquals(portfolio.countAccounts(Account.CASH_ACCOUNT), 2);
        assertEquals(portfolio.countAccounts(Account.SHARE_ACCOUNT), 1);
        assertEquals(portfolio.findAccountByName(CASH_ACCOUNT_NAME), cashAccount);
        assertEquals(portfolio.findAccountByName(CASH_ACCOUNT_NAME2), cashAccount2);
        assertEquals(portfolio.findAccountByName(SHARE_ACCOUNT_NAME), shareAccount);
        Transaction transaction = Transaction.newDeposit(yesterday, new Money(10000), cashAccount);
        portfolio.addTransaction(transaction);
        transaction = Transaction.newWithdrawal(yesterday, new Money(500), cashAccount);
        portfolio.addTransaction(transaction);
        transaction = Transaction.newInterest(yesterday, new Money(50), cashAccount);
        portfolio.addTransaction(transaction);
        transaction = Transaction.newFee(yesterday, new Money(25), cashAccount);
        portfolio.addTransaction(transaction);
        transaction = Transaction.newAccumulate(yesterday, new Money(1000), CBA, 1000, new Money(25), cashAccount, shareAccount);
        portfolio.addTransaction(transaction);
        transaction = Transaction.newReduce(yesterday, new Money(1000), CBA, 500, new Money(25), cashAccount, shareAccount);
        portfolio.addTransaction(transaction);
        transaction = Transaction.newDividend(yesterday, new Money(100), CBA, cashAccount, shareAccount);
        portfolio.addTransaction(transaction);
        transaction = Transaction.newDividendDRP(today, new Money(100), CBA, 100, shareAccount);
        portfolio.addTransaction(transaction);
        transaction = Transaction.newTransfer(yesterday, new Money(100), cashAccount, cashAccount2);
        portfolio.addTransaction(transaction);
        assertEquals(portfolio.getStartDate(), yesterday);
        assertEquals(portfolio.getLastDate(), today);
        assertEquals(portfolio.getSymbolsTraded().size(), 1);
        assertEquals(portfolio.getStocksHeld().size(), 1);
        assertEquals(portfolio.countTransactions(), 9);
        assertEquals(portfolio.countTransactions(Transaction.WITHDRAWAL), 1);
        assertEquals(portfolio.getTransactions().size(), 9);
        assertEquals(new Money(9575), portfolio.getCashValue());
        Portfolio importedPortfolio = new Portfolio(PORTFOLIO_NAME);
        try {
            File tempFile = File.createTempFile("venice_test", null);
            portfolio.write(tempFile);
            importedPortfolio.read(tempFile);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        assertEquals(portfolio, importedPortfolio);
        Portfolio clonedPortfolio = (Portfolio) portfolio.clone();
        assertEquals(portfolio, clonedPortfolio);
        portfolio.removeAllTransactions();
        assertEquals(portfolio.getStartDate(), null);
        assertEquals(portfolio.getLastDate(), null);
        assertEquals(portfolio.getSymbolsTraded().size(), 0);
        assertEquals(portfolio.getStocksHeld().size(), 0);
        assertEquals(portfolio.countTransactions(), 0);
        assertEquals(portfolio.countTransactions(Transaction.WITHDRAWAL), 0);
        assertEquals(portfolio.getTransactions().size(), 0);
    }
}
