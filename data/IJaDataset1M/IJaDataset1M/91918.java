package org.red5.server.net.remoting;

/**
 * Remoting header to be sent to a server.
 * 
 * Informations about predefined headers can be found at
 * http://www.osflash.org/amf/envelopes/remoting/headers
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Joachim Bauch (jojo@struktur.de)
 *
 */
public class RemotingHeader {

    /** Name of header specifying string to add to gateway url. */
    public static String APPEND_TO_GATEWAY_URL = "AppendToGatewayUrl";

    /** Name of header specifying new gateway url to use. */
    public static String REPLACE_GATEWAY_URL = "ReplaceGatewayUrl";

    /** Name of header specifying new header to send. */
    public static String PERSISTENT_HEADER = "RequestPersistentHeader";

    /** Name of header containing authentication data. */
    public static String CREDENTIALS = "Credentials";

    /** Name of header to request debug informations from the server. */
    public static String DEBUG_SERVER = "amf_server_debug";

    /**
	 * The name of the header.
	 */
    protected String name;

    /**
	 * Is this header required?
	 */
    protected boolean required;

    /**
	 * The actual data of the header.
	 */
    protected Object data;

    /**
	 * Create a new header to be sent through remoting.
	 * 
	 * @param name
	 * @param required
	 * @param data
	 */
    public RemotingHeader(String name, boolean required, Object data) {
        this.name = name;
        this.required = required;
        this.data = data;
    }
}
