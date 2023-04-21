package integrationtestclasses.bankaccount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 * It should not break the logic if i write shouldAddDepositToBalance()
 * 
 * @author Micael Vesterlund
 */
public class BankAccountBehavior {

    private BankAccount account;

    @Before
    public void setUp() {
        account = null;
    }

    @Test
    public void balanceOfANewlyCreatedAccountMustBeInitialBalance() {
        givenAnAccountWithInitialBalance(0);
        thenShouldBalanceEqualsTo(0);
    }

    @Test
    public void shouldAddDepositToBalance() {
        givenAnAccountWithInitialBalance(0);
        whenDepositAreCalledWithAmount(100);
        thenShouldBalanceEqualsTo(100);
        givenAnAccountWithInitialBalance(100);
        whenDepositAreCalledWithAmount(100);
        thenShouldBalanceEqualsTo(200);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotAcceptNegativeDepositeAmount() {
        givenAnAccountWithInitialBalance(0);
        whenDepositAreCalledWithAmount(-100);
        thenShouldAnExceptionBeThrown();
    }

    @Test
    public void shouldWithdrawAmountFromBalance() {
        givenAnAccountWithInitialBalance(100);
        whenWithdrawAreCalledWithAmount(20);
        thenShouldBalanceEqualsTo(80);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotAcceptNegativeWithdrawAmount() {
        givenAnAccountWithInitialBalance(0);
        whenWithdrawAreCalledWithAmount(-100);
        thenShouldAnExceptionBeThrown();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBeOverdrawn() {
        givenAnAccountWithInitialBalance(20);
        whenWithdrawAreCalledWithAmount(21);
        thenShouldAnExceptionBeThrown();
    }

    @Test
    public void shouldNotAffectBalanceIfAttemptToWithdrawOverBalance() {
        givenAnAccountWithInitialBalance(20);
        try {
            whenWithdrawAreCalledWithAmount(21);
            thenShouldAnExceptionBeThrown();
        } catch (IllegalStateException e) {
            thenShouldBalanceEqualsTo(20);
        }
    }

    @Test
    public void shouldNotAffectBalanceIfUncoveredWithdrawal() {
        givenAnAccountWithInitialBalance(0);
        try {
            whenDepositAreCalledWithAmount(-100);
            thenShouldAnExceptionBeThrown();
        } catch (IllegalStateException e) {
            thenShouldBalanceEqualsTo(0);
        }
    }

    @Test
    public void shouldBeEmptySpecification() {
    }

    private void thenShouldAnExceptionBeThrown() {
        fail("method hasn't caused an exception when it should");
    }

    private void whenWithdrawAreCalledWithAmount(int amount) {
        account.withdraw(amount);
    }

    private void thenShouldBalanceEqualsTo(int balance) {
        assertEquals(balance, account.balance());
    }

    private void whenDepositAreCalledWithAmount(int amount) {
        account.deposit(amount);
    }

    private void givenAnAccountWithInitialBalance(int initialBalance) {
        account = new BankAccount(initialBalance);
    }
}
