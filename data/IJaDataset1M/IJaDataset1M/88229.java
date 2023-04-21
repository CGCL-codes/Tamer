package de.schwarzrot.data.transaction.support;

import de.schwarzrot.data.transaction.ApplicationTransaction;
import de.schwarzrot.data.transaction.Transaction;

public class ApplicationTransactionFactory implements TransactionFactory {

    @Override
    public Transaction createTransaction() {
        return new ApplicationTransaction();
    }
}
