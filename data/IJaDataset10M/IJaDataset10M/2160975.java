package com.googlecode.simpleret.test;

import org.hibernate.cfg.Configuration;
import org.junit.BeforeClass;
import com.googlecode.simpleret.Utilities;
import com.googlecode.simpleret.database.HibernateUtility;

/**
 * Use this as a base class for any of your DBUnit tests.
 */
public abstract class DatabaseTestBase {

    @BeforeClass
    public static void beforeClass() throws Exception {
        Configuration cnf = Utilities.getConfiguration(All.CONFIGURATION);
        cnf.setProperty("hibernate.hbm2ddl.auto", "create");
        HibernateUtility.getSessionFactory(cnf).getCurrentSession();
    }
}
