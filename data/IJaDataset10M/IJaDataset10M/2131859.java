package org.nakedobjects.nos.remote.encoded;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.log4j.Logger;
import org.nakedobjects.noa.NakedObjectRuntimeException;
import org.nakedobjects.noa.util.ByteDecoder;
import org.nakedobjects.noa.util.ByteEncoder;
import org.nakedobjects.nof.core.util.DebugByteDecoder;
import org.nakedobjects.nof.core.util.DebugByteEncoder;
import org.nakedobjects.nof.reflect.remote.data.Distribution;
import org.nakedobjects.nos.remote.command.Request;
import org.nakedobjects.nos.remote.command.socket.ServerConnection;

class EncodingServerConnection extends ServerConnection {

    private static final Logger LOG = Logger.getLogger(EncodingServerConnection.class);

    private final ByteEncoder encoder;

    private final ByteDecoder decoder;

    public EncodingServerConnection(final InputStream input, final OutputStream output, final Distribution server) {
        super(server);
        encoder = new DebugByteEncoder(output);
        decoder = new DebugByteDecoder(input);
    }

    protected Request awaitRequest() throws IOException {
        Request request = (Request) decoder.getObject();
        LOG.debug("request received: " + request);
        decoder.end();
        return request;
    }

    protected void sendError(NakedObjectRuntimeException exception) throws IOException {
        send(exception);
    }

    protected void sendResponse(Object response) throws IOException {
        send(response);
    }

    private void send(Object object) throws IOException {
        LOG.debug("send response: " + object);
        encoder.add(object);
        encoder.end();
    }
}
