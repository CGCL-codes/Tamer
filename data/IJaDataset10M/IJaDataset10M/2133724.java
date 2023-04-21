package org.paccman.controller;

import org.paccman.paccman.Account;

/**
 *
 * @author joao
 */
public class AccountController extends Controller {

    /** Creates a new instance of DocumentController */
    public AccountController() {
        super(new Account());
    }

    /**
     * 
     * @return 
     */
    public Account getAccount() {
        return (Account) paccObj;
    }

    @Override
    public String toString() {
        return getAccount().getName();
    }
}
