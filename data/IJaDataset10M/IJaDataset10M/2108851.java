package net.kano.joscar.rvproto.rvproxy;

import net.kano.joscar.BinaryTools;
import net.kano.joscar.ByteBlock;
import net.kano.joscar.DefensiveTools;
import java.io.IOException;
import java.io.OutputStream;

/**
 * An RV Proxy command used to indicate that some part of initializing a
 * connection failed.
 *
 * @rvproxy.src server
 */
public class RvProxyErrorCmd extends RvProxyCmd {

    /**
     * An error code sent upon connecting to an AOL Proxy Server and not sending
     * any initialization commands. It can be assumed that this error code
     * indicates that a connection has "timed out" waiting for data.
     */
    public static final int ERRORCODE_TIMEOUT = 0x001a;

    /** The error code sent in this command. */
    private final int errorCode;

    /**
     * Creates a new RV Proxy error command from the given incoming error
     * packet.
     *
     * @param header an incoming RV Proxy error packet
     */
    protected RvProxyErrorCmd(RvProxyPacket header) {
        super(header);
        ByteBlock data = header.getCommandData();
        errorCode = BinaryTools.getUShort(data, 0);
    }

    /**
     * Creates a new outgoing RV Proxy error command with the given error code.
     *
     * @param errorCode an error code, like {@link #ERRORCODE_TIMEOUT}
     */
    public RvProxyErrorCmd(int errorCode) {
        super(RvProxyPacket.CMDTYPE_ERROR);
        DefensiveTools.checkRange(errorCode, "errorCode", 0);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code sent in this command. As of this writing the only
     * error code on record is {@link #ERRORCODE_TIMEOUT}.
     *
     * @return this command's error code
     */
    public final int getErrorCode() {
        return errorCode;
    }

    public void writeCommandData(OutputStream out) throws IOException {
    }

    public String toString() {
        return "RvProxyErrorCmd: errorCode=" + errorCode;
    }
}
