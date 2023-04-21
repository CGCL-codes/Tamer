package org.nakedobjects.example.simple.repository;

import java.util.List;
import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.example.simple.Account;

public class Accounts extends AbstractFactoryAndRepository {

    @Named("My accounts")
    public List<? extends Account> allAccounts() {
        return allInstances(Account.class);
    }

    public List<Account> allAccounts2() {
        return allInstances(Account.class);
    }

    public Account createAccount() {
        Account account = newPersistentInstance(Account.class);
        return account;
    }
}
