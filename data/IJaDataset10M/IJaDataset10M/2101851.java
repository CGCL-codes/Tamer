package de.unikoblenz.isweb.xcosima.storage;

/**
 * Exception for wrapping repository-related exceptions, e.g. exceptions
 * from the underlying RDF repository.
 * @author Thomas Franz, http://isweb.uni-koblenz.de
 *
 */
public class RepositoryException extends Exception {

    /**
	 * 
	 */
    public RepositoryException() {
    }

    /**
	 * @param arg0
	 */
    public RepositoryException(String arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 */
    public RepositoryException(Throwable arg0) {
        super(arg0);
    }

    /**
	 * @param arg0
	 * @param arg1
	 */
    public RepositoryException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
