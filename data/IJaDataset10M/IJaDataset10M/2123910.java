package iwallet.server.transport;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * The connection handler class.
 * @author 黄源河
 *
 */
public interface ConnectionHandler {

    /**
     * Handle connection.
     * @param requestStream the request inputstream
     * @param replyStream the reply outputstream
     */
    public void handleConnection(InputStream requestStream, OutputStream replyStream);
}
