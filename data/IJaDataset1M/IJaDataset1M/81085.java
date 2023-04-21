package sippoint.framework.module;

import java.util.HashSet;
import java.util.Set;
import sippoint.data.message.model.IMessage;
import sippoint.framework.module.transaction.ITransactionService;
import sippoint.framework.module.transaction.Transaction;
import sippoint.framework.module.transport.TrafficChannel;

/**
 * @author Martin Hynar
 * 
 */
public class TransactionModule extends AbstractModule implements ITransactionService {

    Set<Transaction> transactions = new HashSet<Transaction>();

    /**
	 * {@inheritDoc}
	 */
    public void handleInTransaction(IMessage message) {
    }

    /**
	 * {@inheritDoc}
	 */
    public void reserveTransaction(TrafficChannel trafficChannel) {
        transactions.add(new Transaction(trafficChannel));
    }
}
