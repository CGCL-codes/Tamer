package KwiKoL;

/**
 * Occurs when an attempt is made to log in to the Kingdom of  Loathing web 
 * site in which either no account with the specified username exists or the 
 * specified password is incorrect for an account with the specified 
 * username
 */
public class KoLBadLoginException extends Exception {

    public KoLBadLoginException(String username, String password) {
        super("Invalid username (" + username + ") or password (" + password + ")");
    }
}
