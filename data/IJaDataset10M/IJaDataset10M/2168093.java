package org.tripcom.distribution.transaction;

import java.net.URI;
import java.util.Hashtable;
import net.jini.space.JavaSpace;
import org.apache.log4j.Logger;
import org.tripcom.integration.entry.Component;
import org.tripcom.integration.entry.transaction.EndTransaction;
import org.tripcom.integration.javaspace.JavaSpacesUtil;

public class TransactionTaker extends Thread {

    public static Hashtable<URI, EndTransaction> transactionEntries = new Hashtable<URI, EndTransaction>();

    private static Logger log = Logger.getLogger(TransactionTaker.class);

    public void run() {
        EndTransaction templateEndTransaction = new EndTransaction(null, Component.DISTRIBUTIONMANAGER, null);
        try {
            JavaSpace systemBus = JavaSpacesUtil.lookupJavaSpace(Component.DISTRIBUTIONMANAGER);
            while (true) {
                EndTransaction endTransaction = (EndTransaction) systemBus.take(templateEndTransaction, null, Long.MAX_VALUE);
                if (log.isInfoEnabled()) log.info("Took EndTransaction from bus with operation ID" + endTransaction.operationID);
                putEntry(endTransaction);
            }
        } catch (Exception e) {
            throw new java.lang.RuntimeException("EndTransaction could not be taken" + e.toString(), e);
        }
    }

    /**
	 * 
	 * @param endTransaction
	 */
    public static void putEntry(EndTransaction endTransaction) {
        synchronized (transactionEntries) {
            if (!transactionEntries.contains(endTransaction.operationID)) {
                transactionEntries.put(endTransaction.transactionID, endTransaction);
            }
            transactionEntries.notifyAll();
        }
    }

    /**
	 * 
	 * @param id
	 * @return
	 */
    public static EndTransaction get(URI id) {
        synchronized (transactionEntries) {
            try {
                while (!transactionEntries.containsKey(id)) transactionEntries.wait();
                EndTransaction myEntry = transactionEntries.remove(id);
                return myEntry;
            } catch (InterruptedException e) {
                log.error(e);
                return null;
            }
        }
    }
}
