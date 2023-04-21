package org.sac.crosspather.common.util;

public interface Constants {

    static final String VERSION = "0.6.280";

    static final String PING_INTERVAL_SECS = "1";

    static final String SERVER_URL = "http://localhost:8080/BrowseServer/queueAction";

    static final String EMPTY_STRING = "";

    static final String NO_PATH = "NO_PATH";

    interface ACTION_VALUES {

        static final String CLIENT_READ = "CLIENT_READ";

        static final String CLIENT_WRITE = "CLIENT_WRITE";

        static final String SERVER_READ = "SERVER_READ";

        static final String SERVER_WRITE = "SERVER_WRITE";

        static final String GET_FILE = "GET_FILE";

        static final String GET_DIR_SIZE = "GET_DIR_SIZE";

        static final String FILE_RECEIVED = "FILE_RECEIVED";

        static final String START_SLEEP_MODE = "START_SLEEP_MODE";
    }

    interface REQUEST_RESPONSE_KEYS {

        static final String REMOTE_SERVER_URL = "REMOTE_SERVER_URL";

        static final String ACTION = "ACTION";

        static final String MSG = "MSG";

        static final String PATH = "PATH";

        static final String KEY = "KEY";

        static final String CODE = "CODE";

        static final String CLIENT_ID = "CLIENT_ID";

        static final String FILE_NAME = "FILE_NAME";

        static final String FILE_PARTS = "FILE_PARTS";

        static final String ZIP_FLAG = "ZIP_FLAG";

        static final String NONE = "NONE";

        static final String UNKNOWN = "UNKNOWN";

        static final String KEY_SEPERATOR = ";";

        static final String VALUE_SEPERATOR = "?";

        static final String FILE_CLIENT_UPLOAD_STATUS = "FILE_CLIENT_UPLOAD_STATUS";

        static final String FILE_SERVER_UPLOAD_STATUS = "FILE_SERVER_UPLOAD_STATUS";

        static final String SUB_ACTION = "SUB_ACTION";

        static final String BATCH_NO = "BATCH_NO";

        static final String REQUESTER_ID = "REQUESTER_ID";

        static final String PARAMETER = "PARAMETER";

        static final String PARAM_LENGTH = "PARAM_LENGTH";

        static final String PARAM_SEPERATOR = "*";
    }

    interface RESPONSE_KEYS {
    }

    interface PROPERTY_KEYS {

        static final String REMOTE_SERVER_URL = "REMOTE_SERVER_URL";

        static final String ACTIVE_PING_INTERVAL_SECS = "ACTIVE_PING_INTERVAL_SECS";

        static final String PASSIVE_PING_INTERVAL_SECS = "PASSIVE_PING_INTERVAL_SECS";

        static final String USE_NTLMAPS = "USE_NTLMAPS";

        static final String PROXY_HOST = "PROXY_HOST";

        static final String PROXY_PORT = "PROXY_PORT";

        static final String MY_CODE = "MY_CODE";

        static final String TARGET_CODE = "TARGET_CODE";

        static final String PERMITTED_LOCATIONS = "PERMITTED_LOCATIONS";

        static final String DEFAULT_OUT_DIRECTORY = "DEFAULT_OUT_DIRECTORY";

        static final String DOMAIN = "DOMAIN";

        static final String USER = "USER";

        static final String PASSWORD = "PASSWORD";

        static final String CONSOLE_LOG_LEVEL = "CONSOLE_LOG_LEVEL";

        static final String FILE_LOG_LEVEL = "FILE_LOG_LEVEL";

        static final String OTHER_LOG_LEVEL = "OTHER_LOG_LEVEL";

        static final String FILE_LOGGING = "FILE_LOGGING";

        static final String RESPONSE_RETRY_INTERVAL = "RESPONSE_RETRY_INTERVAL";

        static final String RESPONSE_REPEAT_ATTEMPTS = "RESPONSE_REPEAT_ATTEMPTS";

        static final String EXTENDED_RESPONSE_RETRY_INTERVAL = "EXTENDED_RESPONSE_RETRY_INTERVAL";

        static final String EXTENDED_RESPONSE_REPEAT_ATTEMPTS = "EXTENDED_RESPONSE_REPEAT_ATTEMPTS";

        static final String ALWAYS_USE_EXTENDED_TIMINGS = "ALWAYS_USE_EXTENDED_TIMINGS";

        static final String SEND_FILE_PROGRESS = "SEND_FILE_PROGRESS";

        static final String USER_AGENT = "USER_AGENT";

        static final String SERVER_SLEEP_TIME = "SERVER_SLEEP_TIME";
    }

    interface ERROR_CODES {

        static final String INVALID_JSON_CONTENT = "Invalid JSON Text content";

        static final String MESSAGE_SENDING_ERROR = "Error while trying to send message";

        static final String MESSAGE_RECEIVING_ERROR = "Error while trying to receive message";

        static final String UKNOWN_MESSAGE = "Unknown message - ?";

        static final String INVALID_DESTINATION_FILE = "Invalid destination file specified - ?";

        static final String RESPONSE_TIMEOUT = "Timedout after failing to receive response.";

        static final String CORRUPTED_RESPONSE = "Corrupted response received - unable to proceed";

        static final String CONNECTION_ERROR = "Unexpected connection error ?";

        static final String FILESERVER_SCHEDULE_ERROR = "Error shcheduling File Server startup";

        static final String FILE_PART_RECEIVE_TIMEOUT = "No file part received for last ? seconds";

        static final String UNAUTHORIZED_REQUEST = "Received unauthorized request ?";

        static final String PROXY_AUTHENTICATION_REQUIRED = "Proxy authentication required";
    }

    interface CLIENT_SERVER_STATUS {

        static final short STOPPED = 0;

        static final short STARTING = 1;

        static final short ONLINE = 2;

        static final short FAILED = 3;

        static final short SLEEP = 4;

        static final short ACTIVE = 5;

        static final short PASSIVE = 6;
    }
}
