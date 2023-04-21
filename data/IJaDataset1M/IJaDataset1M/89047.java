package mas.common;

/**
 * Wird bei falschen Logindaten geworfen
 * @author hypersquirrel
 */
public class AuthenticationException extends Exception {

    private static final long serialVersionUID = 7478208621678610009L;

    /**
	 * Wird an Exception delegiert
	 * @param message Meldung
	 */
    public AuthenticationException(String message) {
        super(message);
    }
}
