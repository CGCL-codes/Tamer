package com.schinzer.fin.dao.base.stub;

import java.util.List;
import org.springframework.dao.DataAccessException;
import com.schinzer.fin.dao.base.NoTransactionFlavourFoundException;
import com.schinzer.fin.dao.base.PaymentTransactionDAO;
import com.schinzer.fin.dao.base.PersistenceException;
import com.schinzer.fin.dao.base.TransactionFilter;
import com.schinzer.fin.model.base.Payment;
import com.schinzer.fin.model.base.PaymentTransaction;
import com.schinzer.fin.model.base.Transaction;

public class PaymentTransactionDaoStub implements PaymentTransactionDAO {

    public Transaction decorateTransaction(Transaction transaction) throws NoTransactionFlavourFoundException, PersistenceException {
        return null;
    }

    public List<Transaction> decorateTransactions(List<Transaction> transactions) throws DataAccessException {
        return null;
    }

    public void delete(PaymentTransaction txn) throws DataAccessException {
    }

    public Payment insert(PaymentTransaction pmtTxn) throws DataAccessException {
        return null;
    }

    public Payment select(long key) throws DataAccessException {
        return null;
    }

    public List<PaymentTransaction> select(TransactionFilter filter) throws DataAccessException {
        return null;
    }

    public List<Long> selectIds(TransactionFilter filter) throws DataAccessException {
        return null;
    }

    public boolean update(PaymentTransaction txn) throws DataAccessException {
        return false;
    }

    public List xDecorateTransactions(List<Transaction> transactions) throws DataAccessException {
        return null;
    }
}
