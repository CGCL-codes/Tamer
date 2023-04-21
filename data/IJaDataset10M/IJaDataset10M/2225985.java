package org.sac.browse.client.util;

public interface Constants {

    static final String PING_INTERVAL_SECS = "1";

    static final String SERVER_URL = "http://localhost:8080/BrowseServer/queueAction";

    static final String EMPTY_STRING = "";

    static final String NO_PATH = "NO_PATH";

    interface ACTION_VALUES {

        static final String CLIENT_READ = "CLIENT_READ";

        static final String CLIENT_WRITE = "CLIENT_WRITE";

        static final String SERVER_READ = "SERVER_READ";

        static final String SERVER_WRITE = "SERVER_WRITE";
    }

    interface REQUEST_KEYS {

        static final String ACTION = "ACTION";

        static final String MSG = "MSG";

        static final String PATH = "PATH";

        static final String REMOTE_SERVER_URL = "REMOTE_SERVER_URL";
    }

    interface RESPONSE_KEYS {

        static final String PATH = "PATH";

        static final String KEY = "KEY";

        static final String NONE = "NONE";

        static final String KEY_SEPERATOR = ";";

        static final String VALUE_SEPERATOR = "?";
    }

    interface PROPERTY_KEYS {

        static final String REMOTE_SERVER_URL = "REMOTE_SERVER_URL";

        static final String REMOTE_PING_INTERVAL_SECS = "REMOTE_PING_INTERVAL_SECS";

        static final String USE_NTLMAPS = "USE_NTLMAPS";

        static final String NTLMAPS_HOST = "NTLMAPS_HOST";

        static final String NTLMAPS_PORT = "NTLMAPS_PORT";

        static final String MY_CODE = "MY_CODE";

        static final String PERMITTED_LOCATIONS = "PERMITTED_LOCATIONS";
    }
}
