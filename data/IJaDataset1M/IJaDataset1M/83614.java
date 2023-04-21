package br.com.visualmidia.exception;

import br.com.visualmidia.business.Account;

public class AccountDoesNotExistsInPersistenceException extends QueryException {

    private static final long serialVersionUID = 3258413915343237938L;

    public AccountDoesNotExistsInPersistenceException(Account account) {
        super("A conta " + account.getName() + " n�o possue opera��es.");
    }
}
