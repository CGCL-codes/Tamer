package exceptions;

public class FotoAlreadySavedException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public FotoAlreadySavedException() {
        super("Ja existe essa foto");
    }
}
