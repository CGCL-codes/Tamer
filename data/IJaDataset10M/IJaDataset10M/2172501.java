package dymnd.net;

import java.io.Serializable;

public class QueryPacket extends BasePacket implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 102;

    public static final int FUNCTION_ANNOUNCE_CLIENT = 0;

    public static final int FUNCTION_HANDSHAKE_CLIENT = 1;

    public static final int FUNCTION_SEND_USER_LIST = 4;

    public static final int FUNCTION_CLIENT_DISCONNECTED = 5;

    public static final int FUNCTION_HANDSHAKE_SERVER = 2;

    public static final int FUNCTION_GET_USER_LIST = 3;

    public int imageWidth;

    public int imageHeight;

    public int layerCount;

    public int permissions = 0;

    protected int queryID = -1;

    public int function;

    public String username;
}
