package vivace.exception;

/**
 * Class modeling exceptions thrown by the undo/redo mechanism
 *
 */
public class HistoryException extends Exception {

    public HistoryException(String msg) {
        super(msg);
    }

    public HistoryException() {
        super();
    }
}
