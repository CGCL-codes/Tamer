package com.integrance.budgetapp.dao.hibernate;

import com.integrance.budgetapp.dao.AbstractBudgetDaoTests;

public class BudgetDaoHibernateImplTests extends AbstractBudgetDaoTests {

    protected String[] getConfigLocations() {
        return new String[] { "file:src/test/resources/applicationContext-hibernate-test.xml" };
    }
}
