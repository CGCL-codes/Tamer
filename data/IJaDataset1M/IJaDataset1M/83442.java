package gloodb.txmgr;

/**
 * Tagging interface for tx management strategies.
 */
public interface TxManagerStrategy {

    /**
	 * Transaction log factory getter.
	 * @return The factory.
	 */
    TxLogFactory getFactory();

    /**
	 * Transaction log configuration getter.
	 * @return The configuration.
	 */
    TxLogConfiguration getConfiguration();

    /**
	 * Returns any tx manager interceptor associated with the tx manager.
	 * @return The tx manager interceptor.
	 */
    TxManagerInterceptor getInterceptor();
}
