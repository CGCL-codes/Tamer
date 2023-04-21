package org.codegallery.jpagal.service;

import java.util.List;
import org.codegallery.jpagal.entity.Account;
import org.codegallery.jpagal.entity.Customer;

public interface AccountService {

    public Account save(Account account);

    public List<Account> findByCustomer(Customer customer);

    public void insertFoo(Account account);
}
