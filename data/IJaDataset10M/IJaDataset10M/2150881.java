package net.sf.erm.service.monitoring;

/**
 * @author lorban
 */
public class DataCollectorException extends Exception {

    public DataCollectorException(String message) {
        super(message);
    }

    public DataCollectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
