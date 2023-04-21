package org.unitmetrics.io;

/**
 * A data storage exception describes an exception related to data storage. 
 * 
 * The cause of such an exception can vary from data source problems to 
 * mapping and interpretational causes.
 * 
 * @author Martin Kersten
 */
@SuppressWarnings("serial")
public class DataStorageException extends RuntimeException {

    public DataStorageException(String message, Exception cause) {
        super(message, cause);
    }
}
