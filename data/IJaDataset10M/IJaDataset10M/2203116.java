package org.jledger.ui;

public interface JLedgerConstants {

    public static final String DEFAULT_CHART_OF_ACCOUNTS_NAME = "Basic Chart of Accounts";

    /**
     * Name used to lookup the current chart of accounts in the workbench 
     * session.
     */
    public static final String SESSION_ATTRIBUTE_CHART_OF_ACCOUNTS = JLedgerConstants.class.getName() + ".chart.of.accounts";

    /**
     * Name used to lookup the current transaction in the workbench session.
     */
    public static final String SESSION_ATTRIBUTE_TRANSACTION = JLedgerConstants.class.getName() + ".transaction";
}
