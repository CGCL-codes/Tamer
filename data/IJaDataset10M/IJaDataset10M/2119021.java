package net.sf.jradius.client.auth;

import net.sf.jradius.packet.attribute.AttributeList;

/**
 * Interface implemented by RadiusAuthenticators that provide a TLS tunnel.
 * @author David Bird
 */
public interface TunnelAuthenticator {

    public void setTunneledAttributes(AttributeList attributes);
}
