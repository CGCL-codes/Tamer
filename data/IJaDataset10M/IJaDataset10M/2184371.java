package jj2000.j2k;

/**
 * This exception is thrown whenever a next???? method is called and there is no
 * next element to return.
 * 
 */
public class NoNextElementException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructs a new <tt>NoNextElementException</tt> exception with no detail
	 * message.
	 * */
    public NoNextElementException() {
        super();
    }

    /**
	 * Constructs a new <tt>NoNextElementException</tt> exception with the
	 * specified detail message.
	 * 
	 * @param s
	 *            The detail message.
	 * */
    public NoNextElementException(String s) {
        super(s);
    }
}
