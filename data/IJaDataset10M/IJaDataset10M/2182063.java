package org.dicom4j.network.association.transport;

import java.net.InetSocketAddress;
import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.DefaultIoFilterChainBuilder;
import org.apache.mina.common.ExecutorThreadModel;
import org.apache.mina.common.IoConnector;
import org.apache.mina.common.IoConnectorConfig;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.ThreadModel;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;
import org.dolmen.network.transport.TransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To initiate TCP/IP connections 
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public class TransportConnector extends TransportService {

    public TransportConnector() {
        super();
    }

    /**
	 * Open a connection
	 * @param aHostname the hostname
	 * @param aPort the port
	 * @param aIoHandler the IoHandler
	 * @throws Exception if errors occurs
	 */
    public void openConnection(String aHostname, int aPort, IoHandler aIoHandler) throws Exception {
        fLogger.debug("trying to openConnection on " + aHostname + ", port" + aPort);
        IoConnectorConfig lConfig = new SocketConnectorConfig();
        lConfig.setThreadModel(ExecutorThreadModel.getInstance("TransportConnector"));
        IoConnector lConnector = new SocketConnector();
        ConnectFuture future = lConnector.connect(new InetSocketAddress(aHostname, aPort), aIoHandler, lConfig);
        future.join();
        if (!future.isConnected()) {
            fLogger.info("failed to open a connection");
            throw new TransportException("failed to open a connection");
        } else {
            fLogger.info("connection opened");
        }
    }

    private static Logger fLogger = LoggerFactory.getLogger(TransportConnector.class);
}
