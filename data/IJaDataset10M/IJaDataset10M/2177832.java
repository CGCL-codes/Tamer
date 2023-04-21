package com.fdm.banktest;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import com.fdm.model.accounts.*;
import com.fdm.model.exceptions.BankManagerException;

public class AccountBuilderTest {

    Map<String, Object> map;

    _Account account;

    AccountBuilder accountBuilder;

    AccountFactory accountFactory;

    String[] savingsDecorations;

    String[] currentDecorations;

    public void setup() {
        map = new HashMap<String, Object>();
        account = new ConcreteAccount();
        map.put("overDraftLimit", 100.0);
        map.put("accountNumber", 1000);
        map.put("balance", 100.0);
        map.put("accountType", "CURRENT");
        map.put("interestRate", 0.3);
        accountBuilder = new AccountBuilder();
        savingsDecorations = new String[] { "com.fdm.model.accounts.InterestRateDecorator" };
        currentDecorations = new String[] { "com.fdm.model.accounts.OverDraftLimitDecorator" };
    }

    @Test
    public void testOverDecoration() {
        setup();
        assertTrue(account.getOverDraftLimit() == 0);
        account = new OverDraftLimitDecorator(account);
        try {
            account.setAttributes(map);
        } catch (BankManagerException e) {
        }
        assertTrue(account.getOverDraftLimit() == 100);
    }

    @Test
    public void testIntDecoration() {
        setup();
        account = new InterestRateDecorator(account);
        try {
            account.setAttributes(map);
        } catch (BankManagerException e) {
            e.printStackTrace();
        }
        assertTrue(((InterestRateDecorator) account).getInterestRate() == .3);
    }

    @Test
    public void builderTest() {
        setup();
        try {
            account = accountBuilder.buildAccountComposite(savingsDecorations, map);
            assertTrue(account.getOverDraftLimit() == 0);
            assertTrue(((InterestRateDecorator) account).getInterestRate() == .3);
            account = accountBuilder.buildAccountComposite(currentDecorations, map);
        } catch (BankManagerException e) {
            assertTrue(false);
        }
        assertTrue(account.getOverDraftLimit() == 100);
    }
}
