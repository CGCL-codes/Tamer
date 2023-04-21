package backend.param.exceptions;

public class UnexpectedElementException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5560796364923458745L;

    public UnexpectedElementException(String error) {
        super(error);
    }
}
