package jp.ac.nii.ubisim.common;

/**
 * Common constants in the bridge.
 *
 * @author Eric Platon <platon@nii.ac.jp>
 */
public final class BridgeCommons {

    /**
     * This application server listening port.
     */
    public static final int DEFAULT_SERVER_PORT = 6666;

    /**
     * The peer server listening port, to which the client access.
     */
    public static final int DEFAULT_CLIENT_PORT = 7777;

    /**
     * The acknowledgment message.
     */
    public static final String ACK = "ACK";

    /**
     * The broadcast channel through the bridge.
     */
    public static final int BRIDGE_BROADCAST_PORT = 99;

    /**
     * The communication port to the base station.
     */
    public static final int BRIDGE_BS_PORT = 98;

    /**
     * The communication port for node discovery.
     */
    public static final int DISCOVERY_BROADCAST_PORT = 97;

    /**
     * Hidden default constructor (Utility class).
     */
    private BridgeCommons() {
    }
}
