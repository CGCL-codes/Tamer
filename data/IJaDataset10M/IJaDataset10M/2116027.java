package architecture.ext.sync.client;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class DataSyncException extends NestableRuntimeException {

    private static final long serialVersionUID = 6202766507431175181L;

    public DataSyncException() {
        super();
    }

    public DataSyncException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DataSyncException(String msg) {
        super(msg);
    }

    public DataSyncException(Throwable cause) {
        super(cause);
    }
}
