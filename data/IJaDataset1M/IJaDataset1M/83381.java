package com.sun.tutorial.javaee.dukesbank.request;

import com.sun.tutorial.javaee.dukesbank.exception.AccountNotFoundException;
import com.sun.tutorial.javaee.dukesbank.exception.CustomerNotFoundException;
import com.sun.tutorial.javaee.dukesbank.exception.IllegalAccountTypeException;
import com.sun.tutorial.javaee.dukesbank.exception.InvalidParameterException;
import com.sun.tutorial.javaee.dukesbank.util.AccountDetails;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;

@Remote
public interface AccountController {

    @RolesAllowed("bankAdmin")
    void addCustomerToAccount(Long customerId, Long accountId) throws InvalidParameterException, CustomerNotFoundException, AccountNotFoundException;

    @RolesAllowed("bankAdmin")
    Long createAccount(AccountDetails details, Long customerId) throws IllegalAccountTypeException, CustomerNotFoundException, InvalidParameterException;

    List<AccountDetails> getAccountsOfCustomer(Long customerId) throws InvalidParameterException, CustomerNotFoundException;

    List<Long> getCustomerIds(Long accountId) throws InvalidParameterException, AccountNotFoundException;

    AccountDetails getDetails(Long accountId) throws InvalidParameterException, AccountNotFoundException;

    @RolesAllowed("bankAdmin")
    void removeAccount(Long accountId) throws InvalidParameterException, AccountNotFoundException;

    @RolesAllowed("bankAdmin")
    void removeCustomerFromAccount(Long customerId, Long accountId) throws InvalidParameterException, CustomerNotFoundException, AccountNotFoundException;
}
