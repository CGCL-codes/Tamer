package bexee.dao;

/**
 * This class is used to indicate that something went wrong trying to insert,
 * find, update etc. in a DAO class.
 * 
 * @version $Revision: 1.1 $, $Date: 2004/12/15 14:18:09 $
 * @author Patric Fornasier
 * @author Pawel Kowalski
 */
public class DAOException extends Exception {

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }
}
