package org.nakedobjects.plugins.xstream.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.nakedobjects.plugins.xstream.shared.XStreamMarshaller;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.remoting.server.ServerConnection;
import org.nakedobjects.remoting.server.ServerConnectionDefault;
import org.nakedobjects.remoting.server.SocketsViewerAbstract;
import org.nakedobjects.remoting.transport.ConnectionException;
import org.nakedobjects.remoting.transport.simple.SimpleTransport;

public class XStreamOverSocketsViewer extends SocketsViewerAbstract {

    public XStreamOverSocketsViewer(ObjectEncoderDecoder objectEncoderDecoder) {
        super(objectEncoderDecoder);
    }

    @Override
    protected ServerConnection createServerConnection(final InputStream input, final OutputStream output, final ServerFacade distribution) {
        SimpleTransport transport = new SimpleTransport(getConfiguration(), input, output);
        XStreamMarshaller serverMarshaller = new XStreamMarshaller(getConfiguration(), transport);
        try {
            serverMarshaller.connect();
        } catch (IOException e) {
            throw new ConnectionException(e);
        }
        return new ServerConnectionDefault(distribution, serverMarshaller);
    }
}
