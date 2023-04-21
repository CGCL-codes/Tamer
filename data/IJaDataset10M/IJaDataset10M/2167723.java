package uk.ac.shef.oak.iracema;

/**
 * An exception class used for wrapping other exceptions thrown by Iracema.
 *
 * 
 * @author <a href="http://www.dcs.shef.ac.uk/~rodrigo">Rodrigo F. Carvalho</a>,
 * <a href="http://oak.dcs.shef.ac.uk/">OAK group</a>, 
 * <a href="http://www.shef.ac.uk/">The University of Sheffield</a><br />
 *
 * 16 Jan 2011
 *
 */
public class IracemaException extends Exception {

    /**
	 * Compiler defined serial version UID.
	 */
    private static final long serialVersionUID = 2918721079339336640L;

    public IracemaException(Exception e) {
        super(e);
    }

    public IracemaException(String message) {
        super(message);
    }
}
