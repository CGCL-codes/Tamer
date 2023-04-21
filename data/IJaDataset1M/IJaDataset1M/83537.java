package net.medienablage.common;

public class FuhrparkException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public FuhrparkException(String string) {
        super(string);
    }

    public FuhrparkException(String string, Exception e) {
        super(string, e);
    }
}
