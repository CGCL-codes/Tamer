package exceptions;

public class PointAlreadySavedException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public PointAlreadySavedException() {
        super("Ponto já salvo");
    }
}
