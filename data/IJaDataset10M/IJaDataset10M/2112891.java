package org.aquosine.aquofusion.accounts.dao;

import java.util.List;
import org.aquosine.aquofusion.accounts.Account;
import org.aquosine.aquofusion.accounts.AccountAddress;

public interface AccountDao {

    public void create(Account account);

    public void delete(Long id);

    public List<Account> findAll();

    public Account getById(Long id);

    public List<Account> getByName(String accountName);

    public void update(Account account);

    public List<Account> basicSearch(Account account);

    public List<Account> advancedSearch(Account account);
}
