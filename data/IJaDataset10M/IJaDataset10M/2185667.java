package banking.auth;

import banking.BankingException;

public class LoginException extends BankingException {

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Exception cause) {
        super(message, cause);
    }
}
