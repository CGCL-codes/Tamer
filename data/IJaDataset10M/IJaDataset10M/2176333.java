package org.nomadpim.module.money.calculator;

import org.nomadpim.core.entity.IEntity;
import org.nomadpim.core.util.filter.IFilter;
import org.nomadpim.module.money.MoneyFacade;
import org.nomadpim.module.money.account.Account;
import org.nomadpim.module.money.exchange_rate.ExchangeRate;
import org.nomadpim.module.money.exchange_rate.NullExchangeRateProvider;
import org.nomadpim.module.money.transaction.Transaction;

public class CachingSumCalculatorPrefixTest extends AbstractSumCalculatorTest {

    public static class AmountGreater500Filter implements IFilter<IEntity> {

        public boolean evaluate(IEntity t) {
            return t.get(Transaction.AMOUNT).intValue() > 500;
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        calculator = new CachingSumCalculator(new AmountGreater500Filter(), createFromFilter(), createToFilter(), getContainer(Transaction.TYPE_NAME), getContainer(Account.TYPE_NAME), getContainer(ExchangeRate.TYPE_NAME), new NullExchangeRateProvider(), MoneyFacade.getDefaultCurrency());
    }

    /**
     * Tests that the result is correct when the target account of a transaction
     * changes in a way that it should be evaluated now because of the
     * prefilter.
     */
    public void testTransactionTargetAccountChangeAddViaPrefilter() {
        IEntity t = addTransaction(200, otherAccount, calculatorAccount);
        assertSumEquals(0);
        t.set(Transaction.AMOUNT, 600);
        assertSumEquals(t.get(Transaction.AMOUNT).intValue());
    }

    /**
     * Tests that the result is correct when the target account of a transaction
     * changes in a way that it should not be evaluated any more because of the
     * prefilter.
     */
    public void testTransactionTargetAccountChangeRemoveViaPrefilter() {
        IEntity t = addTransaction(700, otherAccount, calculatorAccount);
        assertSumEquals(t.get(Transaction.AMOUNT).intValue());
        t.set(Transaction.AMOUNT, 200);
        assertSumEquals(0);
    }
}
