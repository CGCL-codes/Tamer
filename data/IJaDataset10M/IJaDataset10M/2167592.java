package exceptions;

public class NessunRisultatoException extends Throwable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 399238709261249350L;

    public NessunRisultatoException() {
        super();
    }

    public NessunRisultatoException(String message) {
        super(message);
    }
}
