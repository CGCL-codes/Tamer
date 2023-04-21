package be.fedict.eid.applet.shared;

import be.fedict.eid.applet.shared.annotation.HttpHeader;
import be.fedict.eid.applet.shared.annotation.ProtocolVersion;

/**
 * Abstract protocol message class.
 * 
 * @author Frank Cornelis
 * 
 */
public abstract class AbstractProtocolMessage {

    public static final String HTTP_HEADER_PREFIX = "X-AppletProtocol-";

    public static final int PROTOCOL_VERSION = 1;

    @HttpHeader(HTTP_HEADER_PREFIX + "Version")
    @ProtocolVersion
    public static final int protocolVersion = PROTOCOL_VERSION;

    public static final String TYPE_HTTP_HEADER = HTTP_HEADER_PREFIX + "Type";
}
