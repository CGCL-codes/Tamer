package com.fh.auge.security;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import com.domainlanguage.money.Money;
import com.fh.auge.currency.CurrencyExchangeRate;
import com.fh.auge.currency.CurrencyExchangeRateDao;

public class CurrencyRateDaoTest {

    private CurrencyExchangeRateDao dao;

    @Before
    public void setUp() {
        DataFactory.clearTables();
        dao = DataFactory.getCurrencyExchangeRateDao();
    }

    @Test
    public void testRates() {
        CurrencyExchangeRate rate = new CurrencyExchangeRate(Currency.getInstance("EUR"), Currency.getInstance("USD"), BigDecimal.valueOf(1.3415), new Date());
        Assert.assertEquals(Money.dollars(1.3415), rate.convert(Money.euros(BigDecimal.ONE)));
        dao.add(rate);
        rate = dao.getCurrencyExchangeRate(Currency.getInstance("EUR"), Currency.getInstance("USD"));
        Assert.assertEquals(Money.dollars(1.3415), rate.convert(Money.euros(BigDecimal.ONE)));
    }
}
