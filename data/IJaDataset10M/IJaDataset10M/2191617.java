package net.sf.javadc.net;

import java.util.HashMap;
import java.util.Map;
import net.sf.javadc.net.client.ConnectionState;
import net.sf.javadc.util.Enum;

/**
 * <code>DownloadRequestState</code> represents a single state of a <code>DownloadRequest</code> instance
 * 
 * @author Timo Westk�mper
 */
public class DownloadRequestState extends Enum {

    private static Map<ConnectionState, DownloadRequestState> activeChanges;

    private static Map<ConnectionState, DownloadRequestState> passiveChanges;

    public static final DownloadRequestState CONNECTING = new DownloadRequestState("Connecting");

    public static final DownloadRequestState DOWNLOADING = new DownloadRequestState("Downloading");

    public static final DownloadRequestState OFFLINE = new DownloadRequestState("Offline");

    public static final DownloadRequestState QUEUED = new DownloadRequestState("Queued");

    public static final DownloadRequestState REMOTELY_QUEUED = new DownloadRequestState("Remotely Queued");

    public static final DownloadRequestState WAITING = new DownloadRequestState("Waiting");

    static {
        activeChanges = new HashMap<ConnectionState, DownloadRequestState>();
        activeChanges.put(ConnectionState.DOWNLOADING, DownloadRequestState.DOWNLOADING);
        activeChanges.put(ConnectionState.CONNECTING, DownloadRequestState.CONNECTING);
        activeChanges.put(ConnectionState.WAITING, DownloadRequestState.WAITING);
        activeChanges.put(ConnectionState.REMOTELY_QUEUED, DownloadRequestState.REMOTELY_QUEUED);
        activeChanges.put(ConnectionState.NO_DOWNLOAD_SLOTS, DownloadRequestState.QUEUED);
        activeChanges.put(ConnectionState.NOT_CONNECTED, DownloadRequestState.OFFLINE);
        passiveChanges = new HashMap<ConnectionState, DownloadRequestState>();
        passiveChanges.put(ConnectionState.CONNECTING, DownloadRequestState.CONNECTING);
        passiveChanges.put(ConnectionState.DOWNLOADING, DownloadRequestState.QUEUED);
        passiveChanges.put(ConnectionState.REMOTELY_QUEUED, DownloadRequestState.REMOTELY_QUEUED);
        passiveChanges.put(ConnectionState.NO_DOWNLOAD_SLOTS, DownloadRequestState.QUEUED);
        passiveChanges.put(ConnectionState.NOT_CONNECTED, DownloadRequestState.OFFLINE);
    }

    /**
     * Derive the DownloadRequestState from the state of the related IConnection
     * 
     * @param connectionState ConnectionState from which the DownloadRequestState is to be derived
     * @param isDownloadRequestActive whether the download request state is active
     * @return the derived DownloadRequestState
     */
    public static DownloadRequestState deriveFromConnectionState(ConnectionState connectionState, boolean isDownloadRequestActive) {
        if (isDownloadRequestActive) {
            return activeChanges.get(connectionState);
        }
        return passiveChanges.get(connectionState);
    }

    /**
     * Create a DownloadRequestState with the given name
     * 
     * @param name Name to be used for this DownloadRequestState
     */
    private DownloadRequestState(String name) {
        super(name);
    }
}
