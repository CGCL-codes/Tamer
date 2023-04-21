package com.tredart.entities.impl;

import static junit.framework.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.tredart.testUtilities.TredartTestUtil;

public class CashManagerTest {

    CashManager cm;

    double startBalance;

    @Before
    public void setUp() {
        cm = new CashManager();
        startBalance = 10d;
        cm.setInitialBalance(startBalance);
        cm.setAccountName("TradeIndexDeltaOne");
    }

    @Test
    public void testGetCashBalance() {
        assertEquals("The Current Balance is not correct", startBalance, cm.getCashBalance());
    }

    @Test
    public void testTransactionClosed() {
        cm.transactionClosed(100d);
        assertEquals("The Current Balance is not correct", 110d, cm.getCashBalance());
    }

    @Test
    public void testResetBalance() {
        cm.resetBalance();
        assertEquals("The Current Balance is not correct", 10d, cm.getCashBalance());
    }

    @Test
    public void testInitialBalanceAfterClosingTheTransaction() {
        testTransactionClosed();
        testResetBalance();
    }

    @Test
    public void testMakingADepositAndWithDrawingCash() {
        cm.depositOrWithdrawCash(100d);
        assertEquals("The Current Balance is not correct", 110d, cm.getCashBalance());
        cm.depositOrWithdrawCash(-200d);
        assertEquals("The Current Balance is not correct", -90d, cm.getCashBalance());
    }

    /**
	 * Tests with all registered testers.
	 */
    @Test
    public void testWithUtilities() {
        TredartTestUtil.createTester().executeTest(cm);
    }
}
