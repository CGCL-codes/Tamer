package php.java.bridge.http;

import java.io.IOException;

/** Thrown when the server is not available anymore */
public class FCGIConnectException extends IOException {

    private static final long serialVersionUID = 5242564093021250550L;

    protected FCGIConnectException(IOException ex) {
        super("Could not connect to server");
        initCause(ex);
    }

    protected FCGIConnectException(String reason, Exception ex) {
        super("Could not connect to server");
        IOException e = new IOException(reason);
        e.initCause(ex);
        initCause(e);
    }
}
