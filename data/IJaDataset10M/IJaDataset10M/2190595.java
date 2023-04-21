package org.tranche.remote;

import java.util.HashMap;
import java.util.Map;
import org.tranche.logs.ConnectionDiagnosticsLog;
import org.tranche.servers.ServerCallbackEvent;
import org.tranche.servers.ServerEvent;
import org.tranche.servers.ServerMessageDownEvent;
import org.tranche.servers.ServerMessageUpEvent;
import org.tranche.time.TimeUtil;

/**
 * <p>A listener than uses the ConnectionDiagnosticsLog to gather stats about a server.</p>
 * <p>Note that the ConnectionDiagnosticsLog can be shared between multiple listeners. This class is thread safe.</p>
 * @author Bryan Smith - bryanesmith@gmail.com
 */
public class RemoteTrancheServerPerformanceListener implements RemoteTrancheServerListener {

    private final String host;

    private final int id;

    private final ConnectionDiagnosticsLog connectionDiagnosticsLog;

    private final Map<Long, ServerMessageUpEvent> sentMessages;

    /**
     * <p>A listener than uses the ConnectionDiagnosticsLog to gather stats about a server.</p>
     * <p>Note that the ConnectionDiagnosticsLog can be shared between multiple listeners. This class is thread safe.</p>
     * @param connectionDiagnosticsLog
     * @param host
     * @param id
     */
    public RemoteTrancheServerPerformanceListener(ConnectionDiagnosticsLog connectionDiagnosticsLog, String host, int id) {
        this.connectionDiagnosticsLog = connectionDiagnosticsLog;
        this.host = host;
        this.id = id;
        this.sentMessages = new HashMap();
    }

    /**
     * <p>A notification that a server has been connected to.</p>
     * @param se
     */
    public void serverConnect(ServerEvent se) {
        this.connectionDiagnosticsLog.logConnection(host, se.getTimestamp());
    }

    /**
     * <p>A notification that a server has been banned.</p>
     * @param se
     */
    public void serverBanned(ServerEvent se) {
        this.connectionDiagnosticsLog.logBanned(host, se.getTimestamp());
    }

    /**
     * <p>A notification that a server has been unbanned.</p>
     * @param se
     */
    public void serverUnbanned(ServerEvent se) {
        this.connectionDiagnosticsLog.logUnbanned(host, se.getTimestamp());
    }

    /**
     * <p>A notification that an outbound message has been created.</p>
     * @param smue
     */
    public void upMessageCreated(ServerMessageUpEvent smue) {
        synchronized (this.sentMessages) {
            this.sentMessages.put(smue.getCallbackId(), smue);
        }
    }

    /**
     * <p>A notification that an outbound message has started to be sent.</p>
     * @param smue
     */
    public void upMessageStarted(ServerMessageUpEvent smue) {
    }

    /**
     * <p>A notification that an outbound message has been sent.</p>
     * @param smue
     */
    public void upMessageSent(ServerMessageUpEvent smue) {
    }

    /**
     * <p>A notification that an outbound message has failed.</p>
     * @param smue
     */
    public void upMessageFailed(ServerMessageUpEvent smue) {
        synchronized (this.sentMessages) {
            ServerMessageUpEvent e = this.sentMessages.remove(smue.getCallbackId());
            if (e != null) {
                long delta = TimeUtil.getTrancheTimestamp() - e.getTimestamp();
                String desc = smue.getTypeString() + " (up)";
                this.safeLogServerRequest(host, delta, desc);
            }
        }
    }

    /**
     * <p>A notification that an inbound message has been started.</p>
     * @param smde
     */
    public void downMessageStarted(ServerMessageDownEvent smde) {
    }

    /**
     * <p>A notification that an inbound message is being downloaded.</p>
     * @param smde
     */
    public void downMessageProgress(ServerMessageDownEvent smde) {
    }

    /**
     * <p>A notification that an inbound message has been completed.</p>
     * @param smde
     */
    public void downMessageCompleted(ServerMessageDownEvent smde) {
        synchronized (this.sentMessages) {
            ServerMessageUpEvent e = this.sentMessages.remove(smde.getCallbackId());
            if (e != null) {
                long delta = TimeUtil.getTrancheTimestamp() - e.getTimestamp();
                String desc = smde.getTypeString();
                this.safeLogServerRequest(host, delta, desc);
            }
        }
    }

    /**
     * <p>A notification that an inbound message has failed.</p>
     * @param smde
     */
    public void downMessageFailed(ServerMessageDownEvent smde) {
        synchronized (this.sentMessages) {
            ServerMessageUpEvent e = this.sentMessages.remove(smde.getCallbackId());
            if (e != null) {
                long delta = TimeUtil.getTrancheTimestamp() - e.getTimestamp();
                String desc = smde.getTypeString() + " (down)";
                this.safeLogServerRequest(host, delta, desc);
            }
        }
    }

    /**
     * <p>A notification that a server request has been created.</p>
     * @param sce
     */
    public void requestCreated(ServerCallbackEvent sce) {
    }

    /**
     * <p>A notification that a server request has been fulfilled by the server.</p>
     * @param sce
     */
    public void requestFulfilled(ServerCallbackEvent sce) {
    }

    /**
     * <p>A notification that a server request has failed.</p>
     * @param sce
     */
    public void requestFailed(ServerCallbackEvent sce) {
    }

    /**
     * <p>So can assert that RTS doesn't have already.</p>
     * @return
     */
    @Override()
    public int hashCode() {
        return this.host.hashCode() + this.id;
    }

    /**
     * 
     * @param host
     * @param delta
     * @param activityDescription
     */
    private void safeLogServerRequest(String host, long delta, String activityDescription) {
        synchronized (this.connectionDiagnosticsLog) {
            connectionDiagnosticsLog.logServerRequest(host, delta, activityDescription);
        }
    }

    /**
     * 
     * @param o
     * @return
     */
    @Override()
    public boolean equals(Object o) {
        if (o instanceof RemoteTrancheServerPerformanceListener) {
            RemoteTrancheServerPerformanceListener rl = (RemoteTrancheServerPerformanceListener) o;
            return (this.id == rl.id) && (rl.host.equals(this.host));
        }
        return false;
    }
}
