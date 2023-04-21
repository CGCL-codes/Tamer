package phex.connection;

import java.io.IOException;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import phex.common.address.AddressUtils;
import phex.common.address.DefaultDestAddress;
import phex.common.address.DestAddress;
import phex.common.address.IpAddress;
import phex.common.address.MalformedDestAddressException;
import phex.connection.handshake.HandshakeHandler;
import phex.connection.handshake.HandshakeStatus;
import phex.host.CaughtHostsContainer;
import phex.host.Host;
import phex.host.HostStatus;
import phex.http.GnutellaHeaderNames;
import phex.http.HTTPHeader;
import phex.http.HTTPHeaderGroup;
import phex.http.HTTPHeaderNames;
import phex.http.HTTPProcessor;
import phex.io.buffer.ByteBuffer;
import phex.msg.InvalidMessageException;
import phex.msg.Message;
import phex.msg.MessageProcessor;
import phex.msg.MsgHeader;
import phex.msg.vendor.CapabilitiesVMsg;
import phex.msg.vendor.MessagesSupportedVMsg;
import phex.msghandling.MessageService;
import phex.net.connection.Connection;
import phex.net.repres.PresentationManager;
import phex.prefs.core.MessagePrefs;
import phex.query.DynamicQueryConstants;
import phex.security.AccessType;
import phex.security.PhexSecurityManager;
import phex.servent.Servent;
import phex.utils.HexConverter;
import phex.utils.Localizer;

/**
 * <p>A worker that handles the communication between this and another gnutella
 * node.</p>
 *
 * <p>The remote node is represented as a Host object. Depending on whether the
 * host is in incoming or outgoing mode, this will perform the relevant
 * handshake negotiations. If this was an
 * outgoing connection, or during negotiations it becomes clear that the Host
 * wishes to partake in a Gnutella network, then enter a message handling loop
 * to forward all messages as necessary. This will usualy result in a message
 * being read, some bookkeeping to keep track of the message, discarding bad
 * messages and finally queuing a request with the Message Manager to pass on any
 * messages that must be generated in response.</p>
 */
public class ConnectionEngine implements ConnectionConstants {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionEngine.class);

    private final Servent servent;

    private final MessageService messageService;

    private final PhexSecurityManager securityService;

    /**
     * pre-allocated buffer for repeated uses.
     */
    private byte[] headerBuffer;

    private final Host connectedHost;

    private final Connection connection;

    private HTTPHeaderGroup headersRead;

    private HTTPHeaderGroup headersSend;

    public ConnectionEngine(Servent servent, Host connectedHost) {
        this.servent = servent;
        this.messageService = servent.getMessageService();
        this.securityService = servent.getSecurityService();
        this.connectedHost = connectedHost;
        this.connection = connectedHost.getConnection();
    }

    public void processIncomingData() throws IOException {
        headerBuffer = new byte[MsgHeader.DATA_LENGTH];
        try {
            while (true) {
                MsgHeader header = readHeader();
                byte[] body = MessageProcessor.readMessageBody(connection, header.getDataLength());
                connectedHost.incReceivedCount();
                int ttl = header.getTTL();
                int hops = header.getHopsTaken();
                if (ttl < 0 || hops < 0) {
                    messageService.dropMessage(header, body, "TTL or hops below 0", connectedHost);
                    continue;
                }
                if (hops > MessagePrefs.MaxNetworkTTL.get().intValue()) {
                    messageService.dropMessage(header, body, "Hops larger then maxNetworkTTL", connectedHost);
                    continue;
                }
                if (ttl >= MessagePrefs.MaxNetworkTTL.get().intValue()) {
                    header.setTTL((byte) (MessagePrefs.MaxNetworkTTL.get().intValue() - hops));
                }
                Message message;
                try {
                    message = MessageProcessor.createMessageFromBody(header, body, securityService);
                    if (message == null) {
                        messageService.dropMessage(header, body, "Unknown message type", connectedHost);
                        continue;
                    }
                } catch (InvalidMessageException exp) {
                    messageService.dropMessage(header, body, "Invalid message: " + exp.getMessage(), connectedHost);
                    logger.warn(exp.toString(), exp);
                    continue;
                }
                header.countHop();
                messageService.dispatchMessage(message, connectedHost);
            }
        } catch (IOException exp) {
            logger.debug(exp.toString(), exp);
            if (connectedHost.isConnected()) {
                connectedHost.setStatus(HostStatus.ERROR, exp.getMessage());
                connectedHost.disconnect();
            }
            throw exp;
        } catch (Exception exp) {
            logger.warn(exp.toString(), exp);
            if (connectedHost.isConnected()) {
                connectedHost.setStatus(HostStatus.ERROR, exp.getMessage());
                connectedHost.disconnect();
            }
            throw new IOException("Exception occured: " + exp.getMessage());
        }
    }

    private MsgHeader readHeader() throws IOException {
        MsgHeader header = MessageProcessor.parseMessageHeader(connection, headerBuffer);
        if (header == null) {
            throw new ConnectionClosedException("Connection closed by remote host");
        }
        int length = header.getDataLength();
        if (length < 0) {
            throw new IOException("Negative body size. Disconnecting the remote host.");
        } else if (length > MessagePrefs.MaxLength.get().intValue()) {
            if (logger.isWarnEnabled()) {
                byte[] body = MessageProcessor.readMessageBody(connection, 262144);
                String hexBody = HexConverter.toHexString(body);
                logger.warn(connectedHost + " - Body too big. Header: " + header + "\nBody(256KB): " + hexBody);
            }
            throw new IOException("Packet too big. Disconnecting the remote host.");
        }
        header.setArrivalTime(System.currentTimeMillis());
        return header;
    }

    public void initHostHandshake() throws IOException {
        try {
            if (connectedHost.isIncomming()) {
                initializeIncomingWith06();
            } else {
                initializeOutgoingWith06();
            }
            configureConnectionType(headersSend, headersRead);
            postHandshakeConfiguration(headersSend, headersRead);
        } finally {
            if (headersRead != null) {
                handleXTryHeaders(headersRead);
                headersRead = null;
                headersSend = null;
            }
        }
        connectedHost.setStatus(HostStatus.CONNECTED);
        servent.getHostService().addConnectedHost(connectedHost);
        servent.getMessageService().sendUdpPing(connectedHost.getHostAddress());
        servent.getMessageService().pingHost(connectedHost, MessagePrefs.TTL.get().byteValue());
        if (connectedHost.isVendorMessageSupported()) {
            MessagesSupportedVMsg vMsg = MessagesSupportedVMsg.getMyMsgSupported();
            connectedHost.queueMessageToSend(vMsg);
            CapabilitiesVMsg capVMsg = CapabilitiesVMsg.getMyCapabilitiesVMsg();
            connectedHost.queueMessageToSend(capVMsg);
        }
    }

    private void initializeIncomingWith06() throws IOException {
        headersRead = HTTPProcessor.parseHTTPHeaders(connection);
        if (logger.isDebugEnabled()) {
            logger.debug("{} - Connect headers: {}", connectedHost, headersRead.buildHTTPHeaderString());
        }
        configureRemoteHost(headersRead);
        HandshakeHandler handshakeHandler = HandshakeHandler.createHandshakeHandler(servent, connectedHost);
        HandshakeStatus myResponse = handshakeHandler.createHandshakeResponse(new HandshakeStatus(headersRead), false);
        headersSend = myResponse.getResponseHeaders();
        sendStringToHost(GNUTELLA_06 + " " + myResponse.getStatusCode() + " " + myResponse.getStatusMessage() + "\r\n");
        String httpHeaderString = myResponse.getResponseHeaders().buildHTTPHeaderString();
        sendStringToHost(httpHeaderString);
        sendStringToHost("\r\n");
        if (myResponse.getStatusCode() != STATUS_CODE_OK) {
            throw new IOException("Connection not accepted: " + myResponse.getStatusCode() + " " + myResponse.getStatusMessage());
        }
        HandshakeStatus inResponse = HandshakeStatus.parseHandshakeResponse(connection);
        if (logger.isDebugEnabled()) {
            logger.debug(connectedHost + " - Response Code: '" + inResponse.getStatusCode() + "'.");
            logger.debug(connectedHost + " - Response Message: '" + inResponse.getStatusMessage() + "'.");
            logger.debug(connectedHost + " - Response Headers: " + inResponse.getResponseHeaders().buildHTTPHeaderString());
        }
        if (inResponse.getStatusCode() != STATUS_CODE_OK) {
            throw new IOException("Host rejected connection: " + inResponse.getStatusCode() + " " + inResponse.getStatusMessage());
        }
        headersRead.replaceHeaders(inResponse.getResponseHeaders());
    }

    private void initializeOutgoingWith06() throws IOException {
        connectedHost.setStatus(HostStatus.CONNECTING, Localizer.getString("Negotiate0_6Handshake"));
        String greeting = servent.getGnutellaNetwork().getNetworkGreeting();
        String requestLine = greeting + '/' + PROTOCOL_06 + "\r\n";
        StringBuffer requestBuffer = new StringBuffer(100);
        requestBuffer.append(requestLine);
        HandshakeHandler handshakeHandler = HandshakeHandler.createHandshakeHandler(servent, connectedHost);
        HTTPHeaderGroup handshakeHeaders = handshakeHandler.createOutgoingHandshakeHeaders();
        requestBuffer.append(handshakeHeaders.buildHTTPHeaderString());
        requestBuffer.append("\r\n");
        headersSend = handshakeHeaders;
        String requestStr = requestBuffer.toString();
        sendStringToHost(requestStr);
        HandshakeStatus handshakeResponse = HandshakeStatus.parseHandshakeResponse(connection);
        headersRead = handshakeResponse.getResponseHeaders();
        if (logger.isDebugEnabled()) {
            logger.debug(connectedHost + " - Response Code: '" + handshakeResponse.getStatusCode() + "'.");
            logger.debug(connectedHost + " - Response Message: '" + handshakeResponse.getStatusMessage() + "'.");
            logger.debug(connectedHost + " - Response Headers: " + headersRead.buildHTTPHeaderString());
        }
        if (handshakeResponse.getStatusCode() != STATUS_CODE_OK) {
            if (handshakeResponse.getStatusCode() == STATUS_CODE_REJECTED) {
                throw new ConnectionRejectedException(handshakeResponse.getStatusCode() + " " + handshakeResponse.getStatusMessage());
            }
            throw new ConnectionRejectedException("Gnutella 0.6 connection rejected. Status: " + handshakeResponse.getStatusCode() + " - " + handshakeResponse.getStatusMessage());
        }
        configureRemoteHost(headersRead);
        HandshakeStatus myResponse = handshakeHandler.createHandshakeResponse(handshakeResponse, true);
        HTTPHeaderGroup myResponseHeaders = myResponse.getResponseHeaders();
        headersSend.replaceHeaders(myResponseHeaders);
        sendStringToHost(GNUTELLA_06 + " " + myResponse.getStatusCode() + " " + myResponse.getStatusMessage() + "\r\n");
        String httpHeaderString = myResponseHeaders.buildHTTPHeaderString();
        sendStringToHost(httpHeaderString);
        sendStringToHost("\r\n");
        if (myResponse.getStatusCode() != STATUS_CODE_OK) {
            throw new ConnectionRejectedException("Connection not accepted: " + myResponse.getStatusCode() + " " + myResponse.getStatusMessage());
        }
    }

    private void configureConnectionType(HTTPHeaderGroup myHeadersSend, HTTPHeaderGroup theirHeadersRead) {
        HTTPHeader myUPHeader = myHeadersSend.getHeader(GnutellaHeaderNames.X_ULTRAPEER);
        HTTPHeader theirUPHeader = theirHeadersRead.getHeader(GnutellaHeaderNames.X_ULTRAPEER);
        if (myUPHeader == null || theirUPHeader == null) {
            connectedHost.setConnectionType(Host.CONNECTION_NORMAL);
        } else if (myUPHeader.booleanValue()) {
            if (theirUPHeader.booleanValue()) {
                connectedHost.setConnectionType(Host.CONNECTION_UP_UP);
            } else {
                connectedHost.setConnectionType(Host.CONNECTION_UP_LEAF);
            }
        } else {
            if (theirUPHeader.booleanValue()) {
                connectedHost.setConnectionType(Host.CONNECTION_LEAF_UP);
            } else {
                connectedHost.setConnectionType(Host.CONNECTION_NORMAL);
            }
        }
    }

    private void handleXTryHeaders(HTTPHeaderGroup headers) {
        HTTPHeader[] hostAddresses = headers.getHeaders(GnutellaHeaderNames.X_TRY);
        if (hostAddresses != null) {
            handleXTryHosts(hostAddresses, true);
        }
        hostAddresses = headers.getHeaders(GnutellaHeaderNames.X_TRY_ULTRAPEERS);
        if (hostAddresses != null) {
            handleXTryHosts(hostAddresses, false);
        }
    }

    private void handleXTryHosts(HTTPHeader[] xtryHostAdresses, boolean isUltrapeerList) {
        short priority;
        if (isUltrapeerList) {
            priority = CaughtHostsContainer.HIGH_PRIORITY;
        } else {
            priority = CaughtHostsContainer.NORMAL_PRIORITY;
        }
        CaughtHostsContainer hostContainer = servent.getHostService().getCaughtHostsContainer();
        for (int i = 0; i < xtryHostAdresses.length; i++) {
            StringTokenizer tokenizer = new StringTokenizer(xtryHostAdresses[i].getValue(), ",");
            while (tokenizer.hasMoreTokens()) {
                String hostAddressStr = tokenizer.nextToken().trim();
                try {
                    DestAddress address = PresentationManager.getInstance().createHostAddress(hostAddressStr, DefaultDestAddress.DEFAULT_PORT);
                    AccessType access = securityService.controlHostAddressAccess(address);
                    switch(access) {
                        case ACCESS_DENIED:
                        case ACCESS_STRONGLY_DENIED:
                            continue;
                    }
                    IpAddress ipAddress = address.getIpAddress();
                    if (!isUltrapeerList && ipAddress != null && ipAddress.isSiteLocalIP()) {
                        priority = CaughtHostsContainer.LOW_PRIORITY;
                    }
                    hostContainer.addCaughtHost(address, priority);
                } catch (MalformedDestAddressException exp) {
                }
            }
        }
    }

    /**
     * This method uses the header fields to set attributes of the remote host
     * accordingly.
     */
    private void configureRemoteHost(HTTPHeaderGroup headers) {
        HTTPHeader header = headers.getHeader(HTTPHeaderNames.USER_AGENT);
        if (header != null) {
            connectedHost.setVendor(header.getValue());
        }
        if (connectedHost.isIncomming()) {
            header = headers.getHeader(GnutellaHeaderNames.LISTEN_IP);
            if (header == null) {
                header = headers.getHeader(GnutellaHeaderNames.X_MY_ADDRESS);
            }
            if (header != null) {
                DestAddress addi = connectedHost.getHostAddress();
                int port = AddressUtils.parsePort(header.getValue());
                if (port > 0) {
                    connectedHost.setHostAddress(new DefaultDestAddress(addi.getIpAddress(), port));
                }
            }
        }
        header = headers.getHeader(GnutellaHeaderNames.REMOTE_IP);
        if (header != null) {
            byte[] remoteIP = AddressUtils.parseIP(header.getValue());
            if (remoteIP != null) {
                IpAddress ip = new IpAddress(remoteIP);
                DestAddress address = PresentationManager.getInstance().createHostAddress(ip, -1);
                servent.updateLocalAddress(address);
            }
        }
        header = headers.getHeader(GnutellaHeaderNames.X_QUERY_ROUTING);
        if (header != null) {
            try {
                float version = Float.parseFloat(header.getValue());
                if (version >= 0.1f) {
                    connectedHost.setQueryRoutingSupported(true);
                }
            } catch (NumberFormatException e) {
            }
        }
        header = headers.getHeader(GnutellaHeaderNames.X_UP_QUERY_ROUTING);
        if (header != null) {
            try {
                float version = Float.parseFloat(header.getValue());
                if (version >= 0.1f) {
                    connectedHost.setUPQueryRoutingSupported(true);
                }
            } catch (NumberFormatException e) {
            }
        }
        header = headers.getHeader(GnutellaHeaderNames.X_DYNAMIC_QUERY);
        if (header != null) {
            try {
                float version = header.floatValue();
                if (version >= 0.1f) {
                    connectedHost.setDynamicQuerySupported(true);
                }
            } catch (NumberFormatException e) {
            }
        }
        byte maxTTL = headers.getByteHeaderValue(GnutellaHeaderNames.X_MAX_TTL, DynamicQueryConstants.DEFAULT_MAX_TTL);
        connectedHost.setMaxTTL(maxTTL);
        int degree = headers.getIntHeaderValue(GnutellaHeaderNames.X_DEGREE, DynamicQueryConstants.NON_DYNAMIC_QUERY_DEGREE);
        connectedHost.setUltrapeerDegree(degree);
    }

    private void postHandshakeConfiguration(HTTPHeaderGroup myHeadersSend, HTTPHeaderGroup theirHeadersRead) throws IOException {
        if (myHeadersSend.isHeaderValueContaining(HTTPHeaderNames.ACCEPT_ENCODING, "deflate") && theirHeadersRead.isHeaderValueContaining(HTTPHeaderNames.CONTENT_ENCODING, "deflate")) {
            connectedHost.activateInputInflation();
        }
        if (theirHeadersRead.isHeaderValueContaining(HTTPHeaderNames.ACCEPT_ENCODING, "deflate") && myHeadersSend.isHeaderValueContaining(HTTPHeaderNames.CONTENT_ENCODING, "deflate")) {
            connectedHost.activateOutputDeflation();
        }
        HTTPHeader header = theirHeadersRead.getHeader(GnutellaHeaderNames.VENDOR_MESSAGE);
        if (header != null && !header.getValue().equals("")) {
            connectedHost.setVendorMessageSupported(true);
        }
        header = theirHeadersRead.getHeader(GnutellaHeaderNames.GGEP);
        if (header != null && !header.getValue().equals("")) {
            connectedHost.setGgepSupported(true);
        } else {
            connectedHost.setGgepSupported(false);
        }
    }

    private void sendStringToHost(String str) throws IOException {
        logger.debug("{} - Send: {}", connectedHost, str);
        byte[] bytes = str.getBytes("ISO8859-1");
        connection.write(ByteBuffer.wrap(bytes));
        connection.flush();
    }
}
