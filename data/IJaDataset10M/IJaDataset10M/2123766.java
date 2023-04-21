package com.aelitis.azureus.core.networkmanager.impl;

import java.io.IOException;
import java.util.*;
import org.gudy.azureus2.core3.util.*;
import com.aelitis.azureus.core.networkmanager.*;
import com.aelitis.azureus.core.peermanager.messaging.*;

/**
 * A rate-controlled write entity backed by multiple peer connections, with an
 * emphasis on transmitting packets with full payloads, i.e. it writes to the
 * transport in mss-sized chunks if at all possible. It also employs fair,
 * round-robin write scheduling, where connections each take turns writing a
 * single full packet per round.
 */
public class MultiPeerUploader implements RateControlledEntity {

    private static final int FLUSH_CHECK_LOOP_TIME = 500;

    private static final int FLUSH_WAIT_TIME = 3 * 1000;

    private long last_flush_check_time = 0;

    private final RateHandler rate_handler;

    private boolean destroyed = false;

    private final HashMap waiting_connections = new HashMap();

    private final LinkedList ready_connections = new LinkedList();

    private final AEMonitor lists_lock = new AEMonitor("PacketFillingMultiPeerUploader:lists_lock");

    /**
   * Create a new packet-filling multi-peer upload entity,
   * rate-controlled by the given handler.
   * @param rate_handler listener to handle upload rate limits
   */
    public MultiPeerUploader(RateHandler rate_handler) {
        this.rate_handler = rate_handler;
    }

    public RateHandler getRateHandler() {
        return (rate_handler);
    }

    /**
   * Checks the connections in the waiting list to see if it's time to be force-flushed.
   */
    private void flushCheck() {
        long diff = SystemTime.getCurrentTime() - last_flush_check_time;
        if (!destroyed && (diff > FLUSH_CHECK_LOOP_TIME || diff < 0)) {
            try {
                lists_lock.enter();
                long current_time = SystemTime.getCurrentTime();
                for (Iterator i = waiting_connections.entrySet().iterator(); i.hasNext(); ) {
                    Map.Entry entry = (Map.Entry) i.next();
                    PeerData peer_data = (PeerData) entry.getValue();
                    long wait_time = current_time - peer_data.last_message_added_time;
                    if (wait_time > FLUSH_WAIT_TIME || wait_time < 0) {
                        NetworkConnectionBase conn = (NetworkConnectionBase) entry.getKey();
                        if (conn.getOutgoingMessageQueue().getTotalSize() > 0) {
                            conn.getOutgoingMessageQueue().cancelQueueListener(peer_data.queue_listener);
                            i.remove();
                            addToReadyList(conn);
                        } else {
                            peer_data.last_message_added_time = current_time;
                        }
                    }
                }
            } finally {
                lists_lock.exit();
            }
            last_flush_check_time = SystemTime.getCurrentTime();
        }
    }

    /**
   * Destroy this upload entity.
   * Note: Removes all peer connections in the process.
   */
    public void destroy() {
        destroyed = true;
        try {
            lists_lock.enter();
            for (Iterator i = waiting_connections.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry entry = (Map.Entry) i.next();
                NetworkConnectionBase conn = (NetworkConnectionBase) entry.getKey();
                PeerData data = (PeerData) entry.getValue();
                conn.getOutgoingMessageQueue().cancelQueueListener(data.queue_listener);
            }
            waiting_connections.clear();
            ready_connections.clear();
        } finally {
            lists_lock.exit();
        }
    }

    /**
   * Add the given connection to be managed by this upload entity.
   * @param peer_connection to be write managed
   */
    public void addPeerConnection(NetworkConnectionBase peer_connection) {
        int mss_size = peer_connection.getMssSize();
        boolean has_urgent_data = peer_connection.getOutgoingMessageQueue().hasUrgentMessage();
        int num_bytes_ready = peer_connection.getOutgoingMessageQueue().getTotalSize();
        if (num_bytes_ready >= mss_size || has_urgent_data) {
            addToReadyList(peer_connection);
        } else {
            addToWaitingList(peer_connection);
        }
    }

    /**
   * Remove the given connection from this upload entity.
   * @param peer_connection to be removed
   * @return true if the connection was found and removed, false if not removed
   */
    public boolean removePeerConnection(NetworkConnectionBase peer_connection) {
        try {
            lists_lock.enter();
            PeerData peer_data = (PeerData) waiting_connections.remove(peer_connection);
            if (peer_data != null) {
                peer_connection.getOutgoingMessageQueue().cancelQueueListener(peer_data.queue_listener);
                return true;
            }
            if (ready_connections.remove(peer_connection)) {
                return true;
            }
            return false;
        } finally {
            lists_lock.exit();
        }
    }

    private void addToWaitingList(final NetworkConnectionBase conn) {
        final PeerData peer_data = new PeerData();
        OutgoingMessageQueue.MessageQueueListener listener = new OutgoingMessageQueue.MessageQueueListener() {

            public boolean messageAdded(Message message) {
                return true;
            }

            public void messageQueued(Message message) {
                try {
                    lists_lock.enter();
                    if (waiting_connections.get(conn) == null) {
                        return;
                    }
                    int mss_size = conn.getMssSize();
                    boolean has_urgent_data = conn.getOutgoingMessageQueue().hasUrgentMessage();
                    int num_bytes_ready = conn.getOutgoingMessageQueue().getTotalSize();
                    if (num_bytes_ready >= mss_size || has_urgent_data) {
                        waiting_connections.remove(conn);
                        conn.getOutgoingMessageQueue().cancelQueueListener(this);
                        addToReadyList(conn);
                    } else {
                        if (!peer_data.bumped) {
                            peer_data.bumped = true;
                            peer_data.last_message_added_time = SystemTime.getCurrentTime();
                        }
                    }
                } finally {
                    lists_lock.exit();
                }
            }

            public void flush() {
                try {
                    lists_lock.enter();
                    if (waiting_connections.remove(conn) != null) {
                        conn.getOutgoingMessageQueue().cancelQueueListener(this);
                        addToReadyList(conn);
                    }
                } finally {
                    lists_lock.exit();
                }
            }

            public void messageRemoved(Message message) {
            }

            public void messageSent(Message message) {
            }

            public void protocolBytesSent(int byte_count) {
            }

            public void dataBytesSent(int byte_count) {
            }
        };
        peer_data.queue_listener = listener;
        peer_data.last_message_added_time = SystemTime.getCurrentTime();
        peer_data.bumped = false;
        try {
            lists_lock.enter();
            waiting_connections.put(conn, peer_data);
            conn.getOutgoingMessageQueue().registerQueueListener(listener);
        } finally {
            lists_lock.exit();
        }
    }

    private void addToReadyList(final NetworkConnectionBase conn) {
        try {
            lists_lock.enter();
            ready_connections.addLast(conn);
        } finally {
            lists_lock.exit();
        }
    }

    private int write(EventWaiter waiter, int num_bytes_to_write) {
        if (num_bytes_to_write < 1) {
            Debug.out("num_bytes_to_write < 1");
            return 0;
        }
        HashMap connections_to_notify_of_exception = new HashMap();
        ArrayList manual_notifications = new ArrayList();
        int num_bytes_remaining = num_bytes_to_write;
        try {
            lists_lock.enter();
            int num_unusable_connections = 0;
            while (num_bytes_remaining > 0 && num_unusable_connections < ready_connections.size()) {
                NetworkConnectionBase conn = (NetworkConnectionBase) ready_connections.removeFirst();
                if (!conn.getTransportBase().isReadyForWrite(waiter)) {
                    ready_connections.addLast(conn);
                    num_unusable_connections++;
                    continue;
                }
                int total_size = conn.getOutgoingMessageQueue().getTotalSize();
                if (total_size < 1) {
                    addToWaitingList(conn);
                    continue;
                }
                int mss_size = conn.getMssSize();
                int num_bytes_allowed = num_bytes_remaining > mss_size ? mss_size : num_bytes_remaining;
                int num_bytes_available = total_size > mss_size ? mss_size : total_size;
                if (num_bytes_allowed >= num_bytes_available) {
                    int written = 0;
                    try {
                        written = conn.getOutgoingMessageQueue().deliverToTransport(num_bytes_available, true);
                        if (written > 0) {
                            manual_notifications.add(conn);
                        }
                        boolean has_urgent_data = conn.getOutgoingMessageQueue().hasUrgentMessage();
                        int remaining = conn.getOutgoingMessageQueue().getTotalSize();
                        if (remaining >= mss_size || has_urgent_data) {
                            ready_connections.addLast(conn);
                            num_unusable_connections = 0;
                        } else {
                            addToWaitingList(conn);
                        }
                    } catch (Throwable e) {
                        if (AEDiagnostics.TRACE_CONNECTION_DROPS) {
                            if (e.getMessage() == null) {
                                Debug.out("null write exception message: ", e);
                            } else {
                                if (e.getMessage().indexOf("An existing connection was forcibly closed by the remote host") == -1 && e.getMessage().indexOf("Connection reset by peer") == -1 && e.getMessage().indexOf("Broken pipe") == -1 && e.getMessage().indexOf("An established connection was aborted by the software in your host machine") == -1) {
                                    System.out.println("MP: write exception [" + conn.getTransportBase().getDescription() + "]: " + e.getMessage());
                                }
                            }
                        }
                        if (!(e instanceof IOException)) {
                            Debug.printStackTrace(e);
                        }
                        connections_to_notify_of_exception.put(conn, e);
                        addToWaitingList(conn);
                    }
                    num_bytes_remaining -= written;
                } else {
                    ready_connections.addLast(conn);
                    num_unusable_connections++;
                    continue;
                }
            }
        } finally {
            lists_lock.exit();
        }
        for (int i = 0; i < manual_notifications.size(); i++) {
            NetworkConnectionBase conn = (NetworkConnectionBase) manual_notifications.get(i);
            conn.getOutgoingMessageQueue().doListenerNotifications();
        }
        for (Iterator i = connections_to_notify_of_exception.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            NetworkConnectionBase conn = (NetworkConnectionBase) entry.getKey();
            Throwable exception = (Throwable) entry.getValue();
            conn.notifyOfException(exception);
        }
        int num_bytes_written = num_bytes_to_write - num_bytes_remaining;
        if (num_bytes_written > 0) {
            rate_handler.bytesProcessed(num_bytes_written);
        }
        return num_bytes_written;
    }

    private static class PeerData {

        private OutgoingMessageQueue.MessageQueueListener queue_listener;

        private long last_message_added_time;

        private boolean bumped;
    }

    public long getBytesReadyToWrite() {
        long total = 0;
        try {
            lists_lock.enter();
            for (Iterator i = waiting_connections.keySet().iterator(); i.hasNext(); ) {
                NetworkConnectionBase conn = (NetworkConnectionBase) i.next();
                total += conn.getOutgoingMessageQueue().getTotalSize();
            }
            for (Iterator i = ready_connections.iterator(); i.hasNext(); ) {
                NetworkConnectionBase conn = (NetworkConnectionBase) i.next();
                total += conn.getOutgoingMessageQueue().getTotalSize();
            }
        } finally {
            lists_lock.exit();
        }
        return (total);
    }

    public int getConnectionCount() {
        return (waiting_connections.size() + ready_connections.size());
    }

    public int getReadyConnectionCount(EventWaiter waiter) {
        int total = 0;
        try {
            lists_lock.enter();
            for (Iterator i = waiting_connections.keySet().iterator(); i.hasNext(); ) {
                NetworkConnectionBase conn = (NetworkConnectionBase) i.next();
                if (conn.getTransportBase().isReadyForWrite(waiter)) {
                    total++;
                }
            }
            for (Iterator i = ready_connections.iterator(); i.hasNext(); ) {
                NetworkConnectionBase conn = (NetworkConnectionBase) i.next();
                if (conn.getTransportBase().isReadyForWrite(waiter)) {
                    total++;
                }
            }
        } finally {
            lists_lock.exit();
        }
        return (total);
    }

    public boolean canProcess(EventWaiter waiter) {
        flushCheck();
        if (ready_connections.isEmpty()) return false;
        if (rate_handler.getCurrentNumBytesAllowed() < 1) return false;
        return true;
    }

    public int doProcessing(EventWaiter waiter, int max_bytes) {
        int num_bytes_allowed = rate_handler.getCurrentNumBytesAllowed();
        if (num_bytes_allowed < 1) return 0;
        if (max_bytes > 0 && max_bytes < num_bytes_allowed) {
            num_bytes_allowed = max_bytes;
        }
        return write(waiter, num_bytes_allowed);
    }

    public int getPriority() {
        return RateControlledEntity.PRIORITY_HIGH;
    }

    public boolean getPriorityBoost() {
        return false;
    }

    public String getString() {
        StringBuffer str = new StringBuffer();
        str.append("MPU (" + waiting_connections.size() + "/" + ready_connections.size() + "): ");
        str.append("can_process=" + canProcess(null) + ",bytes_allowed=" + rate_handler.getCurrentNumBytesAllowed() + ", waiting=");
        try {
            lists_lock.enter();
            int num = 0;
            for (Iterator i = waiting_connections.keySet().iterator(); i.hasNext(); ) {
                NetworkConnectionBase conn = (NetworkConnectionBase) i.next();
                if (num++ > 0) {
                    str.append(",");
                }
                str.append(conn.getString());
            }
            str.append(": ready=");
            num = 0;
            for (Iterator i = ready_connections.iterator(); i.hasNext(); ) {
                NetworkConnectionBase conn = (NetworkConnectionBase) i.next();
                if (num++ > 0) {
                    str.append(",");
                }
                str.append(conn.getString());
            }
        } finally {
            lists_lock.exit();
        }
        return (str.toString());
    }
}
