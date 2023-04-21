package com.boc.botv.service;

import com.boc.botv.model.SiteAccount;
import com.boc.botv.model.Transaction;
import com.boc.botv.model.User;
import java.util.Date;
import java.util.List;

/**
 * Gestionnaire de transactions.
 * @author Fabien Renaud
 */
public interface TransactionManager {

    public List<Transaction> getTransactions();

    public List<Transaction> getTransactions(User user);

    public List<Transaction> getTransactions(SiteAccount site);

    public List<Transaction> getTransactionsBetween(Date firstDate, Date lastDate);

    public void createTransaction(Transaction transcation);
}
