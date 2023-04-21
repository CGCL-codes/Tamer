package com.hx.persistence.ddg.impl;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import com.hx.persistence.tx.TransactionManager;

public class FindByPropertiesMethodInterceptor extends FindMethodInterceptor {

    public final List<String> queryProperties;

    public FindByPropertiesMethodInterceptor(TransactionManager transactionManager, Class<?> clazz, FindType findType, boolean withLazies, List<Boolean> ascOrderProperties, List<String> orderProperties, boolean limited, List<String> queryProperties) {
        super(transactionManager, clazz, findType, withLazies, ascOrderProperties, orderProperties, limited);
        this.queryProperties = queryProperties;
    }

    @Override
    protected void configure(Criteria criteria, Object[] args) {
        int index = 0;
        for (String propertyName : this.queryProperties) {
            Object value = args[index];
            if (value == null) {
                criteria.add(Restrictions.isNull(propertyName));
            } else {
                criteria.add(Restrictions.eq(propertyName, value));
            }
            index = index + 1;
        }
    }
}
