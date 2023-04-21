package org.tranche.remote;

/**
 * <p>Abstraction for the commands.</p>
 * @author Jayson Falkner - jfalkner@umich.edu
 * @author Bryan Smith - bryanesmith@gmail.com
 * @author James "Augie" Hill - augman85@gmail.com
 */
public class Token {

    /**
     * <p>The token for confirmation</p>
     */
    public static final String OK_STRING = "O";

    /**
     * <p>The bytes of the OK_STRING</p>
     */
    public static final byte[] OK = (OK_STRING + "\n").getBytes();

    /**
     * <p>The token to reset a timeout.</p>
     */
    public static final String KEEP_ALIVE_STRING = "K";

    /**
     * <p>The bytes of the KEEP_ALIVE_STRING</p>
     */
    public static final byte[] KEEP_ALIVE = (KEEP_ALIVE_STRING + "\n").getBytes();

    /**
     * <p>The token for negation</p>
     */
    public static final String NO_STRING = "N";

    /**
     * <p>The bytes of the NO_STRING</p>
     */
    public static final byte[] NO = (NO_STRING + "\n").getBytes();

    /**
     * <p>The token for sending errors</p>
     */
    public static final String ERROR_STRING = "E";

    /**
     * <p>The bytes of the ERROR_STRING</p>
     */
    public static final byte[] ERROR = (ERROR_STRING + "\n").getBytes();

    /**
     * <p>The token for sending data</p>
     */
    public static final String DATA_STRING = "D";

    /**
     * <p>The bytes form of DATA_STRING</p>
     */
    public static final byte[] DATA = (DATA_STRING + "\n").getBytes();

    /**
     * <p>Token for a remote shutdown request.</p>
     */
    public static final String SHUTDOWN_STRING = "Shutdown";

    /**
     * <p>The bytes form of SHUTDOWN_STRING</p>
     */
    public static final byte[] SHUTDOWN = (SHUTDOWN_STRING + "\n").getBytes();

    /**
     * <p>Token for a network status request.</p>
     */
    public static final String GET_NETWORK_STATUS_PORTION_STRING = "GetNetworkStatusPortion";

    /**
     * <p>The bytes form of GET_NETWORK_STATUS_STRING</p>
     */
    public static final byte[] GET_NETWORK_STATUS_PORTION = (GET_NETWORK_STATUS_PORTION_STRING + "\n").getBytes();

    /**
     * <p>The token for asking for data</p>
     */
    public static final String GET_DATA_STRING = "GetData";

    /**
     * <p>The bytes form of GET_DATA_STRING</p>
     */
    public static final byte[] GET_DATA = (GET_DATA_STRING + "\n").getBytes();

    /**
     * <p>The token for sending meta-data</p>
     */
    public static final String GET_META_STRING = "GetMetaData";

    /**
     * <p>The bytes form of GET_META_STRING</p>
     */
    public static final byte[] GET_META = (GET_META_STRING + "\n").getBytes();

    /** 
     * <p>The token for pinging data</p>
     */
    public static final String HAS_DATA_STRING = "HasData";

    /**
     * <p>The bytes form of HAS_DATA_STRING</p>
     */
    public static final byte[] HAS_DATA = (HAS_DATA_STRING + "\n").getBytes();

    /** 
     * <p>The token for sending meta-data</p>
     */
    public static final String HAS_META_STRING = "HasMeta";

    /**
     * <p>The bytes form of HAS_META_STRING</p>
     */
    public static final byte[] HAS_META = (HAS_META_STRING + "\n").getBytes();

    /**
     * <p>The token for sending nonce</p>
     */
    public static final String GET_NONCES_STRING = "GetNonces";

    /**
     * <p>The bytes form of GET_NONCE_STRING</p>
     */
    public static final byte[] GET_NONCES = (GET_NONCES_STRING + "\n").getBytes();

    /**
     * <p>The token for pinging the server</p>
     */
    public static final String PING_STRING = "Ping";

    /**
     * <p>The bytes form of PING_STRING</p>
     */
    public static final byte[] PING = (PING_STRING + "\n").getBytes();

    /**
     * <p>The token for getting data hashes</p>
     */
    public static final String GET_DATA_HASHES_STRING = "DataHashes";

    /**
     * <p>The bytes form of GET_DATA_HASHSES_STRING</p>
     */
    public static final byte[] GET_DATA_HASHES = (GET_DATA_HASHES_STRING + "\n").getBytes();

    /** 
     * <p>The token for getting project hashes</p>
     */
    public static final String GET_PROJECT_HASHES_STRING = "ProjectHashes";

    /**
     * <p>The bytes form of GET_PROJECT_HASHES_STRING</p>
     */
    public static final byte[] GET_PROJECT_HASHES = (GET_PROJECT_HASHES_STRING + "\n").getBytes();

    /** 
     * <p>The token for getting meta-data hashes</p>
     */
    public static final String GET_META_HASHES_STRING = "MetaHashes";

    /**
     * <p>The bytes form of GET_META_HASHES_STRING</p>
     */
    public static final byte[] GET_META_HASHES = (GET_META_HASHES_STRING + "\n").getBytes();

    /**
     * <p>The token for deleting data</p>
     */
    public static final String DELETE_DATA_STRING = "DeleteData";

    /**
     * <p>The bytes form of DELETE_DATA_STRING</p>
     */
    public static final byte[] DELETE_DATA = (DELETE_DATA_STRING + "\n").getBytes();

    /**
     * <p>The token for deleting data</p>
     */
    public static final String DELETE_META_STRING = "DeleteMeta";

    /**
     * <p>The bytes form of DELETE_META_STRING</p>
     */
    public static final byte[] DELETE_META = (DELETE_META_STRING + "\n").getBytes();

    /**
     * <p>The token for deleting data</p>
     */
    public static final String SET_DATA_STRING = "AddData";

    /**
     * <p>The bytes form of SET_DATA_STRING</p>
     */
    public static final byte[] SET_DATA = (SET_DATA_STRING + "\n").getBytes();

    /**
     * <p>The token for deleting data</p>
     */
    public static final String SET_META_STRING = "AddMetaData";

    /**
     * <p>The bytes form of SET_META_STRING</p>
     */
    public static final byte[] SET_META = (SET_META_STRING + "\n").getBytes();

    /**
     * <p>The token for registering a server</p>
     */
    public static final String REGISTER_SERVER_STRING = "AddServer";

    /**
     * <p>The bytes form of ADD_SERVER_STRING</p>
     */
    public static final byte[] REGISTER_SERVER = (REGISTER_SERVER_STRING + "\n").getBytes();

    /**
     * <p>The token for telling a server to close the connection</p>
     */
    public static final String CLOSE_STRING = "Exit";

    /**
     * <p>The bytes form of CLOSE_STRING</p>
     */
    public static final byte[] CLOSE = (CLOSE_STRING + "\n").getBytes();

    /**
     * <p>The token for getting configuration</p>
     */
    public static final String GET_CONFIGURATION_NO_SIG_STRING = "GetConfigNoSig";

    /**
     * <p>The bytes form of GET_CONFIGURATION_NO_SIG_STRING</p>
     */
    public static final byte[] GET_CONFIGURATION_NO_SIG = (GET_CONFIGURATION_NO_SIG_STRING + "\n").getBytes();

    /** 
     * <p>The token for getting configuration</p>
     */
    public static final String GET_CONFIGURATION_STRING = "GetConfig";

    /**
     * <p>The bytes form of GET_CONFIGURATION_STRING</p>
     */
    public static final byte[] GET_CONFIGURATION = (GET_CONFIGURATION_STRING + "\n").getBytes();

    /**
     * <p>The token for sending data</p>
     */
    public static final String SET_CONFIGURATION_STRING = "SetConfig";

    /**
     * <p>The bytes form of SET_CONFIGURATION_STRING</p>
     */
    public static final byte[] SET_CONFIGURATION = (SET_CONFIGURATION_STRING + "\n").getBytes();

    /**
     * <p>The token for getting activity log entries.</p>
     */
    public static final String GET_ACTIVITY_LOG_ENTRIES_STRING = "GetActivityLogEntries";

    /**
     * <p>The byte array form of GET_ACTIVITY_LOG_ENTRIES_STRING.</p>
     */
    public static final byte[] GET_ACTIVITY_LOG_ENTRIES = (GET_ACTIVITY_LOG_ENTRIES_STRING + "\n").getBytes();

    /**
     * <p>The token for getting activity log entries count.</p>
     */
    public static final String GET_ACTIVITY_LOG_ENTRIES_COUNT_STRING = "GetActivityLogEntriesCount";

    /**
     * <p>The byte array form of GET_ACTIVITY_LOG_ENTRIES_COUNT_STRING.</p>
     */
    public static final byte[] GET_ACTIVITY_LOG_ENTRIES_COUNT = (GET_ACTIVITY_LOG_ENTRIES_COUNT_STRING + "\n").getBytes();

    /**
     * <p>The new line</p>
     */
    public static final byte EOL = '\n';

    /**
     * <p>Rejected connection string</p>
     */
    public static final String REJECTED_CONNECTION_STRING = "ConnectionRejected";

    /**
     * <p>The bytes form of REJECTED_CONNECTION_STRING</p>
     */
    public static final byte[] REJECTED_CONNECTION = (REJECTED_CONNECTION_STRING + "\n").getBytes();

    /**
     * <p>Special error for security problems.</p>
     */
    public static final String SECURITY_ERROR = "Security error.";

    /**
     * <p>Special error for request problems.</p>
     */
    public static final String REQUEST_ERROR = "Request error.";

    /**
     * <p>Meta data can only be so big.</p>
     */
    public static final String ERROR_META_DATA_TOO_BIG = "Meta data too big error.";
}
