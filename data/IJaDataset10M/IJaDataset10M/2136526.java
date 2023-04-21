package org.slasoi.businessManager.common.dao;

import org.slasoi.businessManager.common.model.EmCurrencies;

public interface CurrencyDAO extends AbstractHibernateDAO<EmCurrencies, Long> {

    public abstract EmCurrencies getByName(String name);
}
