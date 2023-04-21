package net.sf.chex4j.bankbalance.tests;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import net.sf.chex4j.bankbalance.ConcreteBankAccount;
import net.sf.chex4j.bankbalance.ConcreteBankAccountNoChex;
import org.junit.Before;
import org.junit.Test;

/**
 * All tests in this class will fail unless you run with VM argument -javaagent:target/chex4j-0.0.1-SNAPSHOT.jar
 * @author simon
 *
 */
public class TestInheritanceClass {

    private ConcreteBankAccount bankAccount;

    @Before
    public void setUp() throws Exception {
        bankAccount = new ConcreteBankAccount();
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
        bankAccount.setBalanceWithNoChex(new BigDecimal(-1));
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

    @Test(expected = java.lang.AssertionError.class)
    public void bankAccountDepositNegativeAmountNoChex() throws Exception {
        ConcreteBankAccountNoChex bankAccount = new ConcreteBankAccountNoChex();
        bankAccount.deposit(new BigDecimal(-10));
    }
}
