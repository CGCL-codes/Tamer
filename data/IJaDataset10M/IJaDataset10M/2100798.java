package org.ourgrid.peer;

import org.ourgrid.common.interfaces.Constants;

public interface PeerConstants extends Constants {

    public static String LOCAL_ACCESS_OBJECT_NAME = "LOCAL_WORKERPROVIDER";

    public static String REMOTE_ACCESS_OBJECT_NAME = "REMOTE_WORKERPROVIDER";

    public static final String DS_MONITOR_OBJECT_NAME = "DS_MONITOR";

    public static final String DS_INTERESTED = "DS_INTERESTED";

    public static final String DS_CLIENT = "DS_CLIENT_RECEIVER";

    public static final String MODULE_NAME = "PEER";

    public static final String PEERMANAGER_OBJECT_NAME = "PEER";

    public static final String WORKER_MANAGEMENT_CLIENT_OBJECT_NAME = "WORKER_MANAGEMENT_CLIENT";

    public static final String REMOTE_WORKER_MANAGEMENT_CLIENT = "REMOTE_WORKER_MANAGEMENT_CLIENT";

    public static final String REMOTE_WORKER_PROVIDER_CLIENT_MONITOR = "REMOTE_WORKER_PROVIDER_CLIENT_MONITOR";

    public static final String WORKER_MONITOR_OBJECT_NAME = "WORKER_MONITOR";

    public static final String ACCOUNTING_OBJECT_NAME = "ACCOUNTING";

    public static final String CLIENT_MONITOR_OBJECT_NAME = "CLIENT_MONITOR";

    public static final String STATUS_PROVIDER_CLIENT_OBJECT_NAME = "PEER_STATUS_PROVIDER_CLIENT";

    public static final String SET_WORKERS_CMD_NAME = "setworkers";

    public static final String ADD_USER_CMD_NAME = "adduser";

    public static final String REMOVE_USER_CMD_NAME = "removeuser";

    public static final String ADD_WORKER_CMD_NAME = "addworker";

    public static final String ADD_ANNOTATIONS_CMD_NAME = "addannotations";

    public static final String REMOVE_WORKER_CMD_NAME = "removeworker";

    public static final String WORKER_SPEC_LISTENER_OBJECT_NAME = "PEER_WORKER_SPEC_LISTENER";

    public static final String REMOTE_WORKER_PROVIDER_CLIENT = "PEER_WORKER_PROVIDER_CLIENT";

    public static final String REMOTE_WORKER_MONITOR_OBJECT_NAME = "REMOTE_WORKER_MONITOR";

    public static final String DS_ACTION_NAME = "DS_ACTION_NAME";

    public static final String REQUEST_WORKERS_ACTION_NAME = "REQUEST_WORKERS_ACTION_NAME";

    public static final String SAVE_ACCOUNTING_ACTION_NAME = "SAVE_ACCOUNTING_ACTION_NAME";

    public static final String UPDATE_PEER_UPTIME_ACTION_NAME = "UPDATE_UPTIME_ACTION_NAME";

    public static final int STATUS_UPDATE_DELAY = 10;

    public static final int UPDATE_UPTIME_DELAY = 60;

    public static final long AGGREGATOR_DATA_INTERVAL = 1000 * 60 * 60 * 24;
}
