package org.nomadpim.module.money.exchange_rate;

import java.math.BigDecimal;
import org.joda.time.DateTime;
import org.nomadpim.core.entity.IEntity;

public class NullExchangeRateProvider implements IExchangeRateProvider {

    public BigDecimal getExchangeRate(IEntity sourceCurrency, IEntity targetCurrency, DateTime date) throws NoSuchExchangeRateException {
        return new BigDecimal(1);
    }
}
