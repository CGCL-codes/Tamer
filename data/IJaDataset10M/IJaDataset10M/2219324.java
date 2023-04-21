package KwiKoL;

/**
 * Occurs when the account of the character current logged into the site is 
 * used to log in to the site using another resource, such as a browser. An 
 * example of this would be a user logging into the site in two different 
 * browser windows under the same account. The Kingdom of Loathing web site 
 * enforces the utilization of only one session at a time per character 
 * account and the purpose of this exception is to allow for handling 
 * attempts to supersede that restriction
 */
public class KoLInvalidSessionException extends Exception {

    public KoLInvalidSessionException() {
        super("Session has been terminated");
    }
}
