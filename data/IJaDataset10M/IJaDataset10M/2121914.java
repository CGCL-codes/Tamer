package org.reward4j.model;

import static org.hamcrest.core.IsEqual.*;
import static org.hamcrest.core.IsNot.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * The {@code AccountTest} tests the {@link Account} model.
 *
 * @author Peter Kehren <mailto:kehren@eyeslide.de>
 */
public class AccountTest {

    @Test
    public void testGetBalance() {
        RateableAction action = new RateableAction("action");
        AccountPosition position1 = new AccountPosition(action, new Coin(4));
        AccountPosition position2 = new AccountPosition(action, new Coin(6));
        Account account = new Account("testaccount");
        account.addPosition(position1);
        account.addPosition(position2);
        assertThat(new Coin(10), equalTo(account.getBalance()));
        Account account2 = new Account("testaccount2");
        assertThat(new Coin(0), equalTo(account2.getBalance()));
    }

    @Test
    public void testEquals() {
        Account account1a = new Account("testaccount1");
        Account account1b = new Account("testaccount1");
        Account account2 = new Account("testaccount2");
        assertThat(account1a, equalTo(account1b));
        assertThat(account1a, not(equalTo(account2)));
    }
}
