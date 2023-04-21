package net.grinder.tools.tcpproxy;

import java.io.IOException;
import java.net.Socket;
import net.grinder.common.Logger;
import net.grinder.common.UncheckedInterruptedException;

/**
 * Simple implementation of TCPProxyEngine that connects to a single
 * remote server.
 *
 * @author Phil Dawes
 * @author Philip Aston
 * @version $Revision: 3762 $
 */
public final class PortForwarderTCPProxyEngine extends AbstractTCPProxyEngine {

    private final ConnectionDetails m_connectionDetails;

    /**
   * Constructor.
   *
   * @param requestFilter Request filter.
   * @param responseFilter Response filter.
   * @param logger Logger.
   * @param connectionDetails Connection details.
   * @param useColour Whether to use colour.
   * @param timeout Timeout for server socket in milliseconds.
   *
   * @exception IOException If an I/O error occurs.
   */
    public PortForwarderTCPProxyEngine(TCPProxyFilter requestFilter, TCPProxyFilter responseFilter, Logger logger, ConnectionDetails connectionDetails, boolean useColour, int timeout) throws IOException {
        this(new TCPProxySocketFactoryImplementation(), requestFilter, responseFilter, logger, connectionDetails, useColour, timeout);
    }

    /**
   * Constructor that allows socket factory to be specified.
   *
   * @param socketFactory Socket factory.
   * @param requestFilter Request filter.
   * @param responseFilter Response filter.
   * @param logger Logger.
   * @param connectionDetails Connection details.
   * @param useColour Whether to use colour.
   * @param timeout Timeout for server socket in milliseconds.
   *
   * @exception IOException If an I/O error occurs.
   */
    public PortForwarderTCPProxyEngine(TCPProxySocketFactory socketFactory, TCPProxyFilter requestFilter, TCPProxyFilter responseFilter, Logger logger, ConnectionDetails connectionDetails, boolean useColour, int timeout) throws IOException {
        super(socketFactory, requestFilter, responseFilter, logger, connectionDetails.getLocalEndPoint(), useColour, timeout);
        m_connectionDetails = connectionDetails;
    }

    /**
   * Main event loop.
   */
    public void run() {
        while (true) {
            final Socket localSocket;
            try {
                localSocket = accept();
            } catch (IOException e) {
                UncheckedInterruptedException.ioException(e);
                logIOException(e);
                return;
            }
            try {
                launchThreadPair(localSocket, m_connectionDetails.getRemoteEndPoint(), EndPoint.clientEndPoint(localSocket), m_connectionDetails.isSecure());
            } catch (IOException e) {
                UncheckedInterruptedException.ioException(e);
                logIOException(e);
                try {
                    localSocket.close();
                } catch (IOException closeException) {
                    throw new AssertionError(closeException);
                }
            }
        }
    }
}
