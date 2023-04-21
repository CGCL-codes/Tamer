package org.yajul.jndi;

/**
 * Provides the names of standard JNDI objects for JBoss AS.
 * <br>
 * User: Josh
 * Date: Jan 14, 2010
 * Time: 6:19:07 AM
 */
public class JBossJndiNames implements JndiNames {

    public static final String TRANSACTION_MANAGER = "java:/TransactionManager";

    public static final String CONNECTION_FACTORY = "java:/ConnectionFactory";

    public static final String LOCAL_USER_TRANSACTION = "/UserTransaction";

    public String getTransactionManagerName() {
        return TRANSACTION_MANAGER;
    }

    public String getConnectionFactoryName() {
        return CONNECTION_FACTORY;
    }
}
