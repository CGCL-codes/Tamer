package org.skycastle.protocol.negotiation;

import com.sun.sgs.app.ManagedObject;
import org.skycastle.core.old.ObjectReference;
import org.skycastle.protocol.protocols.Protocol;
import org.skycastle.protocol.registry.ProtocolRegistry;
import org.skycastle.util.ParameterChecker;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The server side protocol negotiation state machine.
 *
 * @author Hans Haggstrom
 */
public final class ServerSideProtocolNegotiator extends AbstractProtocolNegotiator {

    private final String myServerType;

    private final String myServerVersion;

    private String myClientType;

    private String myClientVersion;

    private int myStep = 0;

    private static final long serialVersionUID = 1L;

    /**
     * @param protocolRegistryReference a referenced to the {@link ProtocolRegistry} that contains available
     *                                  {@link Protocol}s.  We use a reference as the {@link ProtocolRegistry}
     *                                  is stored as a separate {@link ManagedObject} on the server side.
     * @param serverType                a string identifying the server to the clients.
     * @param serverVersion             a version string identifying the server version to the clients.
     */
    public ServerSideProtocolNegotiator(final ObjectReference<ProtocolRegistry> protocolRegistryReference, final String serverType, final String serverVersion) {
        super(protocolRegistryReference);
        ParameterChecker.checkNotNull(serverType, "serverType");
        ParameterChecker.checkNotNull(serverVersion, "serverVersion");
        myServerType = serverType;
        myServerVersion = serverVersion;
    }

    public boolean startsNegotiations() {
        return true;
    }

    /**
     * @return the type that the connected client is.
     */
    public String getClientType() {
        return myClientType;
    }

    /**
     * @return the version of the connected client.
     */
    public String getClientVersion() {
        return myClientVersion;
    }

    @Override
    protected String handleMessage(final String incomingMessage, final Map<String, String> parameters) {
        myStep++;
        switch(myStep) {
            case 1:
                return PROTOCOL_NEGOTIATION_START + PROTOCOL_NEWLINE_CHARACTER + SERVER_KEY + " " + myServerType + PROTOCOL_NEWLINE_CHARACTER + VERSION_KEY + " " + myServerVersion + PROTOCOL_NEWLINE_CHARACTER + KNOWN_PROTOCOLS_KEY + " " + getAvailableProtocolIdsAsString() + PROTOCOL_NEWLINE_CHARACTER;
            case 2:
                if (!incomingMessage.startsWith(PROTOCOL_NEGOTIATION_START)) {
                    setStatus(NegotiationStatus.INVALID_MESSAGE);
                    return null;
                }
                myClientType = parameters.get(CLIENT_KEY);
                myClientVersion = parameters.get(VERSION_KEY);
                final String selectedProtocol = findBestCommonSupportedProtocol(parameters.get(KNOWN_PROTOCOLS_KEY));
                setProtocol(selectedProtocol);
                if (selectedProtocol.length() > 0) {
                    setStatus(NegotiationStatus.SUCCESS);
                } else {
                    setStatus(NegotiationStatus.NO_COMMON_PROTOCOL_FOUND);
                }
                return SELECTED_PROTOCOL_KEY + " " + selectedProtocol + PROTOCOL_NEWLINE_CHARACTER;
            default:
                setStatus(NegotiationStatus.INVALID_MESSAGE);
                return null;
        }
    }

    private String findBestCommonSupportedProtocol(final String spaceSeparatedClientProtocols) {
        if (spaceSeparatedClientProtocols != null) {
            final Set<String> clientKnownProtocols = new HashSet<String>(Arrays.asList(spaceSeparatedClientProtocols.split(" ")));
            for (final String supportedProtocol : getAvailableProtocolsIds()) {
                if (clientKnownProtocols.contains(supportedProtocol)) {
                    return supportedProtocol;
                }
            }
        }
        return "";
    }
}
