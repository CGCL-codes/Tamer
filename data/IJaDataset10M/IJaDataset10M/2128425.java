package influx.dao.transaction;

import java.util.Map;

/**
 * Transaction factory is used as a primary source of instantiating transactions throughout an applications life-cycle. The transaction classification is held within a map (key=interface,
 * value=implementation). <b>NOTE: Each of the implementations should have a no-argument constructor.</b>
 * 
 * @author whoover
 */
public interface ITransactionFactory {

    /**
	 * Sets the transaction mapping
	 * 
	 * @param theTypeMap
	 *            the transaction mapping
	 */
    public void setTransactions(final Map<Class<? extends ITransaction>, Class<? extends ITransaction>> theTypeMap);

    /**
	 * Gets the transaction.
	 * 
	 * @param type
	 *            the type
	 * @param <T>
	 *            the transaction
	 * 
	 * @return the transaction
	 */
    public <T extends ITransaction> T getTransaction(final Class<T> type);
}
