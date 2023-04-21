package org.formaria.metrobank.dao;

import java.util.List;
import org.formaria.metrobank.domain.Account;
import org.formaria.metrobank.domain.AccountTx;
import org.formaria.metrobank.domain.Tx;

/**
 * A DAO for accessing account information
 * @author luano
 */
public interface AccountDao {

    public List<Account> getAccountsByCustomerId(int customerId);

    public List<Tx> getTxByAccountId(int accountId);

    public void saveAccount(Account acc);

    public void addTransaction(AccountTx txs);
}
