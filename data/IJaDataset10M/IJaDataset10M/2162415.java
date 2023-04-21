package com.integrance.budgetapp.dao.hibernate;

import com.integrance.budgetapp.dao.AbstractIncomeSourceDaoTests;

public class IncomeSourceDaoHibernateImplTests extends AbstractIncomeSourceDaoTests {

    protected String[] getConfigLocations() {
        return new String[] { "file:src/test/resources/applicationContext-hibernate-test.xml" };
    }
}
