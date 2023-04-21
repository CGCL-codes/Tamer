package dao;

/**
 * Exception throwed when an error has occured while creating, deleting or updating an entry into the database with the DAO
 */
public class DAOException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructor of DAOException
	 * @param msg The message corresponding to the error occured while creating, deleting, updating an entry into the database
	 */
    public DAOException(String msg) {
        super(msg);
    }
}
