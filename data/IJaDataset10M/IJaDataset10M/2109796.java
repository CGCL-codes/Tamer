package simplifiedPastry;

/**
 * PacketHeaders for simplified-pastry protocol.
 * 
 * @author Stanisław Ogórkis
 */
final class PacketHeaders {

    public static final short JOIN_REQUEST = 0x0001;

    public static final short RT_REQUEST = 0x0002;

    public static final short RT_UPDATE = 0x0003;

    public static final short MESSAGE = 0x0004;

    public static final short MESSAGE_ACCEPTED = 0x0005;

    public static final short KSEARCH = 0x0006;

    public static final short KSEARCH_RESPONSE = 0x0007;

    public static final short ERROR = 0x0008;

    public static final short ERROR_NODE_NOT_FOUND = 0x0009;
}
