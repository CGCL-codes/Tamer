package net.sf.clearwork.service.axis2.server.transaction;

import net.sf.clearwork.service.axis2.server.exception.TransactionPoolException;
import net.sf.clearwork.service.axis2.server.transaction.domain.WSTransactionResource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author huqi
 * @version 1.0.0
 * @see
 * @since 1.0.0
 * @see TransactionSynchronizationManager
 */
public interface IWSTransactionSynchronizationPool {

    /**
	 *
	 * @param wsTransactionResource
	 * @return
	 * @throws TransactionPoolException
	 */
    public String pushWSTransactionResource(WSTransactionResource wsTransactionResource) throws TransactionPoolException;

    /**
	 *
	 * @param transactionUID
	 * @return
	 * @throws TransactionPoolException
	 */
    public WSTransactionResource releaseWSTransactionResource(String transactionUID) throws TransactionPoolException;

    /**
	 *
	 * @param transactionUID
	 * @return
	 * @throws TransactionPoolException
	 * @deprecated Please use #releaseWSTransactionResource
	 */
    public WSTransactionResource getWSTransactionResource(String transactionUID) throws TransactionPoolException;

    /**
	 *
	 * @param transactionUID
	 * @return
	 */
    public boolean isActive(String transactionUID);
}
