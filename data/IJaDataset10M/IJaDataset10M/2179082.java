package org.nakedobjects.plugins.xstream.shared;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.SocketException;
import org.apache.log4j.Logger;
import org.nakedobjects.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.remoting.exchange.Request;
import org.nakedobjects.remoting.protocol.IllegalRequestException;
import org.nakedobjects.remoting.protocol.MarshallerAbstract;
import org.nakedobjects.remoting.transport.ConnectionException;
import org.nakedobjects.remoting.transport.Transport;
import com.thoughtworks.xstream.XStream;

public class XStreamMarshaller extends MarshallerAbstract {

    private static final Logger LOG = Logger.getLogger(XStreamMarshaller.class);

    private final XStream xstream = new XStream();

    private ObjectInputStream input;

    private ObjectOutputStream output;

    public XStreamMarshaller(final NakedObjectConfiguration configuration, final Transport transport) {
        super(configuration, transport);
    }

    public void connect() throws IOException {
        super.connect();
        this.output = new ObjectOutputStream(getTransport().getOutputStream());
        this.input = new ObjectInputStream(getTransport().getInputStream());
    }

    public Object request(final Request request) throws IOException {
        final String requestData = xstream.toXML(request);
        if (LOG.isInfoEnabled()) {
            LOG.info("sending " + requestData.length() + " bytes of data");
        }
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("sending request \n" + requestData);
            }
            try {
                output.writeObject(requestData);
            } catch (final SocketException e) {
                reconnect();
                output.writeObject(requestData);
            }
            output.flush();
            final String responseData = (String) input.readObject();
            if (LOG.isDebugEnabled()) {
                LOG.debug("response received: \n" + responseData);
            }
            return xstream.fromXML(responseData);
        } catch (final StreamCorruptedException e) {
            try {
                final int available = input.available();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("error in reading; skipping bytes: " + available);
                }
                input.skip(available);
            } catch (final IOException e1) {
                e1.printStackTrace();
            }
            throw new ConnectionException(e.getMessage(), e);
        } catch (final ClassNotFoundException e) {
            throw new ConnectionException("Failed request", e);
        }
    }

    public Request readRequest() throws IOException {
        try {
            final String requestData = (String) input.readObject();
            if (LOG.isDebugEnabled()) {
                LOG.debug("request received \n" + requestData);
            }
            final Request request = (Request) xstream.fromXML(requestData);
            return request;
        } catch (final ClassNotFoundException e) {
            throw new IllegalRequestException("unknown class received; closing connection: " + e.getMessage(), e);
        }
    }

    public void sendError(final NakedObjectException exception) throws IOException {
        final String responseData = xstream.toXML(exception);
        sendData(responseData);
    }

    public void sendResponse(final Object response) throws IOException {
        final String responseData = xstream.toXML(response);
        sendData(responseData);
    }

    private void sendData(final String responseData) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("send response \n" + responseData);
        }
        output.writeObject(responseData);
        output.flush();
    }
}
