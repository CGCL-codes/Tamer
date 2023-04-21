package net.sf.homebank.dao;

import net.sf.homebank.data.Account;

public interface AccountDAO {

    public Account insertNew(Account account) throws Exception;
}
