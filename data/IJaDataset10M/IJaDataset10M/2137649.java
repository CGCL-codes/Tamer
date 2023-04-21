package org.mortbay.cometd;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.mortbay.util.LazyList;
import org.mortbay.util.ajax.JSON;
import dojox.cometd.Client;
import dojox.cometd.Listener;

/**
 * 
 * @author gregw
 */
public class ClientImpl implements Client {

    private String _id;

    private String _type;

    private Queue<Map<String, Object>> _messageQ = new LinkedList<Map<String, Object>>();

    private AtomicInteger _responsesPending = new AtomicInteger(0);

    private ChannelImpl _connection = null;

    private ChannelImpl[] _subscriptions = new ChannelImpl[0];

    private boolean _JSONCommented;

    private Listener _listener;

    protected AbstractBayeux _bayeux;

    private String _browserId;

    protected ClientImpl(AbstractBayeux bayeux, String idPrefix, Listener listener) {
        _bayeux = bayeux;
        if (idPrefix == null) _id = Long.toString(bayeux.getRandom(System.identityHashCode(this) ^ System.currentTimeMillis()), 36); else _id = idPrefix + "_" + Long.toString(bayeux.getRandom(System.identityHashCode(this) ^ System.currentTimeMillis()), 36);
        _bayeux._clients.put(getId(), this);
        if (_bayeux.isLogInfo()) _bayeux.logInfo("newClient: " + this);
        _listener = listener;
    }

    /**
     * Connect the client.
     * @return the meta Channel for the connection
     */
    public ChannelImpl connect() {
        synchronized (this) {
            String connection_id = "/meta/connections/" + getId();
            _connection = (ChannelImpl) _bayeux.getChannel(connection_id, true);
            _connection.subscribe(this);
            return _connection;
        }
    }

    public void deliver(Client from, Map<String, Object> message) {
        synchronized (this) {
            if (_connection != null) {
                _messageQ.add(message);
                if (_bayeux.getAlwaysResumePoll() || _responsesPending.get() < 1) resume();
            }
            if (_listener != null) _listener.deliver(from, (String) message.get(AbstractBayeux.CHANNEL_FIELD), message.get(AbstractBayeux.DATA_FIELD), (String) message.get(AbstractBayeux.ID_FIELD));
        }
    }

    /**
     * @return the meta Channel for the connection or null if not connected.
     */
    public ChannelImpl getConnection() {
        return _connection;
    }

    public String getConnectionType() {
        return _type;
    }

    public String getId() {
        return _id;
    }

    public boolean hasMessages() {
        synchronized (this) {
            return _messageQ.size() > 0;
        }
    }

    /**
     * @return the commented
     */
    public boolean isJSONCommented() {
        synchronized (this) {
            return _JSONCommented;
        }
    }

    public boolean isLocal() {
        return true;
    }

    public void remove(boolean timeout) {
        _bayeux.removeClient(_id);
        if (_listener != null) {
            _listener.removed(_id, timeout);
        }
        onBrowser(null);
        resume();
    }

    public int responded() {
        return _responsesPending.getAndDecrement();
    }

    public int responsePending() {
        return _responsesPending.incrementAndGet();
    }

    /** Called by deliver to resume anything waiting on this client.
     */
    public void resume() {
    }

    /**
     * @param commented the commented to set
     */
    public void setJSONCommented(boolean commented) {
        synchronized (this) {
            _JSONCommented = commented;
        }
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public Listener getListener() {
        return _listener;
    }

    public Queue<Map<String, Object>> takeMessages() {
        synchronized (this) {
            if (_messageQ.size() == 0) return null;
            Queue<Map<String, Object>> messages = _messageQ;
            _messageQ = new LinkedList<Map<String, Object>>();
            return messages;
        }
    }

    public String toString() {
        return _id;
    }

    protected void addSubscription(ChannelImpl channel) {
        synchronized (this) {
            _subscriptions = (ChannelImpl[]) LazyList.addToArray(_subscriptions, channel, null);
        }
    }

    protected void removeSubscription(ChannelImpl channel) {
        synchronized (this) {
            _subscriptions = (ChannelImpl[]) LazyList.removeFromArray(_subscriptions, channel);
        }
    }

    protected void setConnectionType(String type) {
        synchronized (this) {
            _type = type;
        }
    }

    protected void setId(String _id) {
        synchronized (this) {
            this._id = _id;
        }
    }

    protected void unsubscribeAll() {
        ChannelImpl[] subscriptions;
        synchronized (this) {
            _messageQ.clear();
            subscriptions = _subscriptions;
            _subscriptions = new ChannelImpl[0];
        }
        for (ChannelImpl channel : subscriptions) channel.unsubscribe(this);
    }

    public void setBrowserId(String id) {
        _browserId = id;
    }

    public String getBrowserId() {
        return _browserId;
    }

    private String _connectedBrowserId;

    private int _connectedCount;

    public synchronized int onBrowser(String browserId) {
        Set<String> clients = null;
        if (_connectedBrowserId != null) {
            assert browserId == null || browserId.equals(_connectedBrowserId);
            if (browserId == null) {
                synchronized (_bayeux._browserPolls) {
                    clients = _bayeux._browserPolls.get(_connectedBrowserId);
                }
                synchronized (clients) {
                    clients.remove(getId());
                    _connectedCount = clients.size();
                }
                _connectedBrowserId = null;
            }
        } else if (browserId != null) {
            _connectedBrowserId = browserId;
            synchronized (_bayeux._browserPolls) {
                clients = _bayeux._browserPolls.get(browserId);
                if (clients == null) {
                    clients = new HashSet<String>();
                    _bayeux._browserPolls.put(browserId, clients);
                }
            }
            synchronized (clients) {
                clients.add(getId());
                _connectedCount = clients.size();
            }
        }
        return _connectedCount;
    }
}
