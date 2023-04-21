package org.brandao.jcptbr.transaction;

import java.util.Properties;

/**
 *
 * @author Brandao
 */
public abstract class TransactionFactory {

    public static void configure(String unit, Properties prop) {
    }

    public static TransactionFactory getFactory(String unit) {
        return null;
    }

    public abstract Transaction getNewTransaction();
}
