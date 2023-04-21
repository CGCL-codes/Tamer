package com.zjhcsoft.jbosscache.util;

import org.jboss.cache.transaction.BatchModeTransactionManager;
import org.jboss.cache.transaction.DummyTransactionManager;
import org.jboss.cache.transaction.TransactionManagerLookup;
import javax.transaction.TransactionManager;

/**
 * FileName : TransactionManagerLookup.java
 * CreateTime : 11-11-11 上午10:18
 * Description : JbossCacheTransactionManagerLookup
 *
 * @author : liuys
 * @version : 1.0
 */
public class JbossCacheTransactionManagerLookup implements TransactionManagerLookup {

    @Override
    public TransactionManager getTransactionManager() throws Exception {
        return new DummyTransactionManager();
    }
}
