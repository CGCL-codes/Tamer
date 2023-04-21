package net.sf.l2j.loginserver.gameserverpackets;

import net.sf.l2j.loginserver.clientpackets.ClientBasePacket;

/**
 * @author Noctarius
 */
public class RequestLoginRestart extends ClientBasePacket {

    private String _servername;

    public RequestLoginRestart(byte[] decrypt) {
        super(decrypt);
        _servername = readS();
    }

    /**
	 * @return Returns the servername
	 */
    public String getServername() {
        return _servername;
    }
}
