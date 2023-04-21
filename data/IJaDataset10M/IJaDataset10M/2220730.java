package pl.otros.logview.batch;

public class BatchProcessingException extends Exception {

    public BatchProcessingException() {
        super();
    }

    public BatchProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public BatchProcessingException(String message) {
        super(message);
    }

    public BatchProcessingException(Throwable cause) {
        super(cause);
    }
}
