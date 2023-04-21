package org.apache.harmony.javax.security.auth.login;

public class AccountNotFoundException extends AccountException {

    private static final long serialVersionUID = 1498349563916294614L;

    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}
