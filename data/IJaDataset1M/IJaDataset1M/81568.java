package org.jivesoftware.smack;

import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.Authentication;
import org.jivesoftware.smack.packet.IQ;

/**
 * Implementation of JEP-0078: Non-SASL Authentication. Follow the following
 * <a href=http://www.jabber.org/jeps/jep-0078.html>link</a> to obtain more
 * information about the JEP.
 *
 * @author Gaston Dombiak
 */
class NonSASLAuthentication implements UserAuthentication {

    private XMPPConnection connection;

    public NonSASLAuthentication(XMPPConnection connection) {
        super();
        this.connection = connection;
    }

    public String authenticate(String username, String password, String resource) throws XMPPException {
        Authentication discoveryAuth = new Authentication();
        discoveryAuth.setType(IQ.Type.GET);
        discoveryAuth.setUsername(username);
        PacketCollector collector = connection.createPacketCollector(new PacketIDFilter(discoveryAuth.getPacketID()));
        connection.sendPacket(discoveryAuth);
        IQ response = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
        if (response == null) {
            throw new XMPPException("No response from the server.");
        } else if (response.getType() == IQ.Type.ERROR) {
            throw new XMPPException(response.getError());
        }
        Authentication authTypes = (Authentication) response;
        collector.cancel();
        Authentication auth = new Authentication();
        auth.setUsername(username);
        if (authTypes.getDigest() != null) {
            auth.setDigest(connection.getConnectionID(), password);
        } else if (authTypes.getPassword() != null) {
            auth.setPassword(password);
        } else {
            throw new XMPPException("Server does not support compatible authentication mechanism.");
        }
        auth.setResource(resource);
        collector = connection.createPacketCollector(new PacketIDFilter(auth.getPacketID()));
        connection.sendPacket(auth);
        response = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
        if (response == null) {
            throw new XMPPException("Authentication failed.");
        } else if (response.getType() == IQ.Type.ERROR) {
            throw new XMPPException(response.getError());
        }
        collector.cancel();
        return response.getTo();
    }

    public String authenticateAnonymously() throws XMPPException {
        Authentication auth = new Authentication();
        PacketCollector collector = connection.createPacketCollector(new PacketIDFilter(auth.getPacketID()));
        connection.sendPacket(auth);
        IQ response = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
        if (response == null) {
            throw new XMPPException("Anonymous login failed.");
        } else if (response.getType() == IQ.Type.ERROR) {
            throw new XMPPException(response.getError());
        }
        collector.cancel();
        if (response.getTo() != null) {
            return response.getTo();
        } else {
            return connection.serviceName + "/" + ((Authentication) response).getResource();
        }
    }
}
