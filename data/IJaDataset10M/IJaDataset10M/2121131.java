package org.javasock.application.dhcpoptions;

public class ServerIdentifier extends DHCPOption {

    public static final int CODE = 54;

    /**
	  * Typically the id is an IP address.
	  */
    public ServerIdentifier(byte[] id) {
        super(CODE, id.length, id);
    }

    public ServerIdentifier(java.nio.ByteBuffer bb) {
        super(bb);
    }
}
