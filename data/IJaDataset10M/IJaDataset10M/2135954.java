package com.jalarbee.core.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import com.jalarbee.core.money.MonetaryAmount;
import com.jalarbee.core.util.exception.MoneyChangeException;
import com.jalarbee.core.util.exception.UnknownChangeRateException;

public class MoneyOperator implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String BASE_CURRENCY_CODE = "1235";

    private static Map<String, BigDecimal> change;

    public MonetaryAmount convert(MonetaryAmount monetaryAmount, Locale locale) throws UnknownChangeRateException, MoneyChangeException {
        BigDecimal rate = null;
        try {
            rate = change.get(monetaryAmount.getCurrency().getCurrencyCode());
        } catch (Exception e) {
            throw new MoneyChangeException("Enable to change money from " + monetaryAmount.getCurrency() + " to " + MonetaryAmount.DEFAUTCURRENCY, e);
        }
        if (rate == null || rate.equals(0)) {
            throw new UnknownChangeRateException("MoneyOperator1");
        }
        MonetaryAmount value = new MonetaryAmount(Currency.getInstance(locale), monetaryAmount.getAmount().multiply(rate));
        return value;
    }

    public void init() throws RuntimeException {
        change = null;
    }

    public static BigDecimal getAmount(Currency currency, BigDecimal amount) throws MoneyChangeException, UnknownChangeRateException {
        BigDecimal rate = null;
        try {
            rate = change.get(currency.getCurrencyCode());
        } catch (Exception e) {
            throw new MoneyChangeException("Enable to change money from " + currency.getCurrencyCode() + " to " + MonetaryAmount.DEFAUTCURRENCY, e);
        }
        if (rate == null || rate.equals(0)) {
            throw new UnknownChangeRateException("MoneyOperator1");
        }
        return rate.multiply(amount);
    }
}
