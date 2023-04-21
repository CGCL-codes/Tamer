package net.sf.chex4j.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import net.sf.chex4j.bankbalance.IBankAccount;
import net.sf.chex4j.bankbalance.PublicBankAccountImplWithoutChex;
import org.junit.Before;
import org.junit.Test;

/**
 * All tests in this class will fail unless you run with VM argument -javaagent:target/chex4j-0.0.1-SNAPSHOT.jar
 * @author simon
 *
 */
public class TestPublicInterfaceSuiteImplHasNoChex {

    private static final int TEN_THOUSAND = 10000;

    private IBankAccount bankAccount;

    @Before
    public void setUp() throws Exception {
        bankAccount = new PublicBankAccountImplWithoutChex();
    }

    @Test
    public void checkImplIsChex() throws Exception {
        ((PublicBankAccountImplWithoutChex) bankAccount).setBalanceDirectly(new BigDecimal(-1));
        return;
    }

    @Test
    public void bankAccountCanAcceptPostiveDeposit() throws Exception {
        BigDecimal b = bankAccount.deposit(new BigDecimal(1.1f));
        assertEquals(b, bankAccount.getBalance());
        return;
    }

    @Test(expected = java.lang.AssertionError.class)
    public void bankAccountCannotAcceptNegativeDeposit() throws Exception {
        BigDecimal b = bankAccount.deposit(new BigDecimal(-1));
        assertEquals(b, bankAccount.getBalance());
        return;
    }

    @Test
    public void bankAccountWillReturnPostiveBalance() throws Exception {
        bankAccount.deposit(new BigDecimal(1));
        BigDecimal b = bankAccount.getBalance();
        assertEquals(new BigDecimal(1), b);
        return;
    }

    @Test(expected = java.lang.AssertionError.class)
    public void bankAccountWillNotReturnNegativeBalance() throws Exception {
        ((PublicBankAccountImplWithoutChex) bankAccount).balance = new BigDecimal(-1);
        BigDecimal b = bankAccount.getBalance();
        assertEquals(new BigDecimal(-1), b);
        return;
    }

    @Test
    public void bankAccountCanWithdrawPostiveAmount() throws Exception {
        bankAccount.deposit(new BigDecimal(10));
        bankAccount.withdraw(new BigDecimal(1));
        BigDecimal b = bankAccount.getBalance();
        assertEquals(new BigDecimal(9), b);
        return;
    }

    @Test(expected = java.lang.AssertionError.class)
    public void bankAccountCannotWithdrawNegativeAmount() throws Exception {
        bankAccount.deposit(new BigDecimal(10));
        bankAccount.withdraw(new BigDecimal(-1));
        BigDecimal b = bankAccount.getBalance();
        assertEquals(new BigDecimal(11), b);
    }

    @Test(expected = java.lang.AssertionError.class)
    public void bankAccountCanotGoOverdrawn() throws Exception {
        bankAccount.deposit(new BigDecimal(10));
        bankAccount.withdraw(new BigDecimal(11));
        BigDecimal b = bankAccount.getBalance();
        assertEquals(new BigDecimal(-1), b);
    }

    @Test
    public void runsFast() throws Exception {
        BigDecimal randomAmount = new BigDecimal(Math.random() * 90);
        bankAccount.deposit(new BigDecimal(100));
        long start = (new java.util.Date()).getTime();
        for (int i = 0; i < TEN_THOUSAND; i++) {
            bankAccount.withdraw(randomAmount);
            bankAccount.deposit(randomAmount);
            bankAccount.getBalance();
        }
        long end = (new java.util.Date()).getTime();
        long duration = end - start;
        assertTrue((Double.valueOf("" + duration) / Double.valueOf("" + TEN_THOUSAND)) < 0.2d);
    }
}
