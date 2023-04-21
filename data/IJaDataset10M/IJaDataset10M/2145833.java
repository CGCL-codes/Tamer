package com.google.code.spotshout;

/**
 * SpotSHOUT Middleware Properties.
 */
public class RMIProperties {

    public static final boolean DEBUG = false;

    public static final void log(String msg) {
        if (DEBUG) System.out.println(msg);
    }

    /** Number of tries (to connect). */
    public static final byte NUMBER_OF_TRIES = 5;

    /** The client port of the discover RMI name server protocol. */
    public static final int UNRELIABLE_DISCOVER_CLIENT_PORT = 240;

    /** The server port of the discover RMI name server protocol. */
    public static final int UNRELIABLE_DISCOVER_HOST_PORT = 243;

    /**
     * The client port where a server will reply to create a reliable
     * connection with the client.
     */
    public static final int UNRELIABLE_INVOKE_CLIENT_PORT = 242;

    /**
     * The SunSPOT server port where it'll listen to create reliables
     * connection with clients.
     */
    public static final int RMI_SPOT_PORT = 244;

    /** The SunSPOT NameServer port. */
    public static final int RMI_SERVER_PORT = 245;

    /** Reliable connection timeout (radiostream) in ms. */
    public static final int RELIABLE_TIMEOUT = 10000;

    /** Little sleep time, to stablish basic variables of reliable connections. */
    public static final int LITTLE_SLEEP_TIME = 300;

    /** Timeout in milliseconds before throwing a TimeoutException in a invoke */
    public static final int TIMEOUT = 5000;

    /** Reliable protocol used. */
    public static final String RELIABLE_PROTOCOL = "radiostream";

    /** Unreliable protocol used. */
    public static final String UNRELIABLE_PROTOCOL = "radiogram";
}
