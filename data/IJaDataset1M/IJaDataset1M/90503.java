package net.sf.brightside.stockswatcher.server.service.exeptions;

public class MultipleUniqueUserException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public MultipleUniqueUserException() {
        super("Fatal error!\n Username already exists in multiple apperance");
    }

    public MultipleUniqueUserException(String message) {
        super();
    }
}
