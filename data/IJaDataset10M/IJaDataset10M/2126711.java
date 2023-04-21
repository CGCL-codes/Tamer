package vegadataeditor.dataSources;

/**
 * This is a exception indicates a connection issue with the backend data store
 * being accessed.
 * @author lawinslow
 */
public class SourceConnectionException extends RuntimeException {

    SourceConnectionException(String message, Throwable ex) {
        super(message, ex);
    }
}
