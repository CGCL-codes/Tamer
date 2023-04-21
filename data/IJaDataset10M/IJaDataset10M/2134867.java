package KwiKoL;

/**
 * Occurs when a connection cannot be established to the server currently 
 * being used to access the Kingdom of Loathing web site, such as if the 
 * server is down or is currently receiving too many other requests to be 
 * able to process the one sent by the program in a timely fashion
 */
public class KoLBadConnectionException extends Exception {

    public KoLBadConnectionException(String currentHost) {
        super("Could not connect to host (" + currentHost + ")");
    }
}
