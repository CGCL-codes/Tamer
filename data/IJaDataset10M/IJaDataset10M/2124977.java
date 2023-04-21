package org.nakedobjects.example.simple.repository;

import java.util.List;
import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.example.simple.Account;
import org.nakedobjects.example.simple.Client;

public class AbstractClients extends AbstractFactoryAndRepository {

    public List<Client> allClients() {
        return allInstances(Client.class);
    }

    public Client newClient() {
        Client c = newTransientInstance(Client.class);
        Account account = newTransientInstance(Account.class);
        c.setAccount(account);
        return c;
    }

    public void testContributed(Account acc) {
    }

    public void testContributed0(String name, Account acc) {
    }
}
