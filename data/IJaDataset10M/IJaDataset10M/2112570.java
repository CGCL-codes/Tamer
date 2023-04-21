package net.sourceforge.jtds.ssl;

/**
 * Encapsulates a TLS handshake body.
 *
 * @author Rob Worsnop
 * @version $Id: TlsHandshakeBody.java,v 1.1 2005-01-04 17:13:04 alin_sinpalean Exp $
 */
class TlsHandshakeBody {

    public static final int HEADER_SIZE = 4;

    public static final int TYPE_CLIENTKEYEXCHANGE = 16;

    public static final int TYPE_CLIENTHELLO = 1;

    private byte handshakeType;

    private int length;

    /**
     * Constructs a TlsHandshakeBody.
     */
    public TlsHandshakeBody(byte handshakeType, int length) {
        this.handshakeType = handshakeType;
        this.length = length;
    }

    /**
     * Returns the handshake type.
     */
    public byte getHandshakeType() {
        return handshakeType;
    }

    /**
     * Returns the handshake length.
     */
    public int getLength() {
        return length;
    }
}
