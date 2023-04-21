package org.mortbay.j2ee.session;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import org.jboss.logging.Logger;
import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.JChannel;
import org.jgroups.MembershipListener;
import org.jgroups.MergeView;
import org.jgroups.Message;
import org.jgroups.MessageListener;
import org.jgroups.View;
import org.jgroups.blocks.GroupRequest;
import org.jgroups.blocks.MethodCall;
import org.jgroups.blocks.RpcDispatcher;

/**
 * publish changes to our state, receive and dispatch notification of
 * changes in other states, initialise our state from other members,
 * allow other members to initialise their state from us - all via
 * JGroups...
 *
 * @author <a href="mailto:jules@mortbay.com">Jules Gosnell</a>
 * @version 1.0
 */
public class JGStore extends AbstractReplicatedStore implements MessageListener, MembershipListener {

    protected Logger _log = Logger.getLogger(JGStore.class);

    protected String _protocolStack = "" + "UDP(mcast_addr=228.8.8.8;mcast_port=45566;ip_ttl=32;ucast_recv_buf_size=16000;ucast_send_buf_size=16000;mcast_send_buf_size=32000;mcast_recv_buf_size=64000;loopback=true):" + "PING(timeout=2000;num_initial_members=3):" + "MERGE2(min_interval=5000;max_interval=10000):" + "FD_SOCK:VERIFY_SUSPECT(timeout=1500):" + "pbcast.STABLE(desired_avg_gossip=20000):" + "pbcast.NAKACK(gc_lag=50;retransmit_timeout=300,600,1200,2400,4800;max_xmit_size=8192):" + "UNICAST(timeout=2000):" + "FRAG(frag_size=8192;down_thread=false;up_thread=false):" + "pbcast.GMS(join_timeout=5000;join_retry_timeout=2000;shun=false;print_local_addr=true):" + "pbcast.STATE_TRANSFER";

    public String getProtocolStack() {
        return _protocolStack;
    }

    public void setProtocolStack(String protocolStack) {
        _protocolStack = protocolStack;
    }

    protected String _subClusterName = "DefaultSubCluster";

    public String getSubClusterName() {
        return _subClusterName;
    }

    public void setSubClusterName(String subClusterName) {
        _subClusterName = subClusterName;
    }

    protected int _retrievalTimeOut = 20000;

    public int getRetrievalTimeOut() {
        return _retrievalTimeOut;
    }

    public void setRetrievalTimeOut(int retrievalTimeOut) {
        _retrievalTimeOut = retrievalTimeOut;
    }

    protected int _distributionModeInternal = GroupRequest.GET_ALL;

    protected int getDistributionModeInternal() {
        return _distributionModeInternal;
    }

    protected void setDistributionModeInternal(String distributionMode) {
        try {
            _distributionModeInternal = GroupRequest.class.getDeclaredField(distributionMode).getInt(GroupRequest.class);
        } catch (Exception e) {
            _log.error("could not convert " + distributionMode + " to GroupRequest field", e);
        }
    }

    protected String _distributionMode = "GET_ALL";

    public String getDistributionMode() {
        return _distributionMode;
    }

    public void setDistributionMode(String distributionMode) {
        _distributionMode = distributionMode;
        setDistributionModeInternal(_distributionMode);
    }

    protected int _distributionTimeOut = 5000;

    public int getDistributionTimeOut() {
        return _distributionTimeOut;
    }

    public void setDistributionTimeOut(int distributionTimeOut) {
        _distributionTimeOut = distributionTimeOut;
    }

    public Object clone() {
        _log.trace("cloning...");
        JGStore jgs = (JGStore) super.clone();
        jgs.setProtocolStack(getProtocolStack());
        jgs.setSubClusterName(getSubClusterName());
        jgs.setRetrievalTimeOut(getRetrievalTimeOut());
        jgs.setDistributionMode(getDistributionMode());
        jgs.setDistributionTimeOut(getDistributionTimeOut());
        _log.trace("...cloned");
        return jgs;
    }

    protected Channel _channel;

    protected RpcDispatcher _dispatcher;

    protected Vector _members;

    public String[] getMembers() {
        Address[] addresses;
        synchronized (_members) {
            addresses = (Address[]) _members.toArray(new Address[_members.size()]);
        }
        String[] members = new String[1 + addresses.length];
        members[0] = _channel.getLocalAddress().toString();
        for (int i = 0; i < addresses.length; ++i) members[1 + i] = addresses[i].toString();
        return members;
    }

    protected void init() {
        _log = Logger.getLogger(JGStore.class.getName() + "#" + getContextPath());
        _log.trace("initialising...");
        try {
            _channel = new JChannel(getProtocolStack());
            MessageListener messageListener = this;
            MembershipListener membershipListener = this;
            Object serverObject = this;
            _dispatcher = new RpcDispatcher(_channel, messageListener, membershipListener, serverObject);
            _dispatcher.setMarshaller(new RpcDispatcher.Marshaller() {

                public Object objectFromByteBuffer(byte[] buf) {
                    ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
                    try {
                        Thread.currentThread().setContextClassLoader(getLoader());
                        return MarshallingInterceptor.demarshal(buf);
                    } catch (Exception e) {
                        _log.error("could not demarshal incoming update", e);
                    } finally {
                        Thread.currentThread().setContextClassLoader(oldLoader);
                    }
                    return null;
                }

                public byte[] objectToByteBuffer(Object obj) {
                    try {
                        return MarshallingInterceptor.marshal(obj);
                    } catch (Exception e) {
                        _log.error("could not marshal outgoing update", e);
                    }
                    return null;
                }
            });
            _log.debug("JGroups RpcDispatcher initialised");
            _channel.setOpt(Channel.GET_STATE_EVENTS, Boolean.TRUE);
            _log.debug("JGroups Channel initialised");
            View view = _channel.getView();
            if (view != null) _members = (Vector) view.getMembers().clone();
            _members = (_members == null) ? new Vector() : (Vector) _members.clone();
            if (_log.isDebugEnabled()) _log.debug("JGroups View: " + _members);
            _members.remove(_channel.getLocalAddress());
        } catch (Exception e) {
            _log.error("could not initialise JGroups Channel and Dispatcher", e);
        }
        _log.trace("...initialised");
    }

    public String getChannelName() {
        return "JETTY_HTTPSESSION_DISTRIBUTION:" + getContextPath() + "-" + getSubClusterName();
    }

    public void start() throws Exception {
        _log.trace("starting...");
        _log.info("starting JGroups " + org.jgroups.Version.version);
        super.start();
        init();
        String channelName = getChannelName();
        if (_log.isDebugEnabled()) _log.debug("starting JGroups...: (" + channelName + ")");
        _channel.connect(channelName);
        _log.trace("JGroups Channel connected");
        _dispatcher.start();
        _log.trace("JGroups Dispatcher started");
        if (!_channel.getState(null, getRetrievalTimeOut())) _log.info("cluster state is null - this must be the first node");
        _log.debug("...JGroups started");
        _log.trace("...started");
    }

    public void stop() {
        _log.trace("stopping...");
        _timer.cancel();
        _log.trace("Touch Timer stopped");
        if (_log.isDebugEnabled()) _log.debug("stopping JGroups...: (" + getChannelName() + ")");
        _dispatcher.stop();
        _log.trace("JGroups RpcDispatcher stopped");
        _channel.disconnect();
        _log.trace("JGroups Channel disconnected");
        _log.debug("...JGroups stopped");
        super.stop();
        _log.trace("...stopped");
    }

    public void destroy() {
        _log.trace("destroying...");
        _timer = null;
        _dispatcher = null;
        _channel = null;
        super.destroy();
        _log.trace("...destroyed");
    }

    protected Object _idsLock = new Object();

    protected Set _ids = new HashSet();

    protected Timer _timer = new Timer();

    protected long _period = 0;

    protected class TouchTimerTask extends TimerTask {

        protected Set _oldIds = null;

        protected Set _newIds = new HashSet();

        public void run() {
            synchronized (_idsLock) {
                _oldIds = _ids;
                _ids = _newIds;
                _newIds = null;
            }
            publish(null, TOUCH_SESSIONS, new Object[] { _oldIds.toArray(new String[_oldIds.size()]), new Long(System.currentTimeMillis() + _period) });
            _oldIds.clear();
            _newIds = _oldIds;
            _oldIds = null;
        }
    }

    public long getBatchPeriod() {
        return _period;
    }

    public void setBatchPeriod(long period) {
        _period = period;
    }

    protected void publish(String id, Method method, Object[] argInstances) {
        if (_log.isTraceEnabled()) {
            String args = "";
            for (int i = 0; i < argInstances.length; i++) args += (i > 0 ? "," : "") + argInstances[i];
            if (_log.isTraceEnabled()) _log.trace("publishing method...: " + id + "." + method.getName() + "(" + args + ")");
        }
        if (_period > 0) {
            if (method.equals(SET_LAST_ACCESSED_TIME)) {
                synchronized (_idsLock) {
                    if (_ids.size() == 0) {
                        _timer.schedule(new TouchTimerTask(), _period);
                        _log.debug("Touch Timer scheduled: _period");
                    }
                    _ids.add(id);
                }
                ;
                return;
            } else if (method.equals(DESTROY_SESSION)) {
                String tmp = (String) argInstances[0];
                synchronized (_idsLock) {
                    _ids.remove(tmp);
                }
            }
        }
        try {
            Class[] tmp = { String.class, Integer.class, Object[].class };
            MethodCall mc = new MethodCall(getClass().getMethod("dispatch", tmp));
            Object[] args = new Object[3];
            args[0] = id;
            args[1] = _methodToInteger.get(method.getName());
            args[2] = argInstances;
            mc.setArgs(args);
            _dispatcher.callRemoteMethods(_members, mc, getDistributionModeInternal(), getDistributionTimeOut());
            _log.trace("...method published");
        } catch (Exception e) {
            _log.error("problem publishing change in state over JGroups", e);
        }
    }

    public void dispatch(String id, Integer method, Object[] argInstances) {
        Method m = _integerToMethod[method.intValue()];
        if (_log.isTraceEnabled()) {
            String args = "";
            for (int i = 0; i < argInstances.length; i++) args += (i > 0 ? "," : "") + argInstances[i];
            if (_log.isTraceEnabled()) _log.trace("dispatching method... : " + id + "." + _integerToMethod[method.intValue()].getName() + "(" + args + ")");
        }
        if (m.equals(DESTROY_SESSION)) {
            String tmp = (String) argInstances[0];
            synchronized (_idsLock) {
                _ids.remove(tmp);
            }
        }
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(getLoader());
            super.dispatch(id, method, argInstances);
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
        _log.trace("...method dispatched");
    }

    /**
   * receive notification of someone else's change in state
   *
   * @param msg a <code>Message</code> value
   */
    public void receive(Message msg) {
        byte[] buf = msg.getBuffer();
    }

    public synchronized byte[] getState() {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(getLoader());
            _log.info("initialising another store from our current state");
            LocalState[] state;
            synchronized (_sessions) {
                _log.info("sending " + _sessions.size() + " sessions");
                state = new LocalState[_sessions.size()];
                int j = 0;
                for (Iterator i = _sessions.values().iterator(); i.hasNext(); ) state[j++] = (LocalState) i.next();
            }
            Object[] data = { new Long(System.currentTimeMillis()), state };
            try {
                return MarshallingInterceptor.marshal(data);
            } catch (Exception e) {
                _log.error("Unable to getState from JGroups: ", e);
                return null;
            }
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }

    /**
   * initialise ourself from the current state of another store...
   *
   * @param new_state an <code>Object</code> value
   */
    public synchronized void setState(byte[] tmp) {
        if (tmp != null) {
            _log.info("initialising our state from another Store");
            Object[] data = null;
            try {
                data = (Object[]) MarshallingInterceptor.demarshal(tmp);
            } catch (Exception e) {
                _log.error("Unable to setState from JGroups: ", e);
                return;
            }
            try {
                AbstractReplicatedStore.setReplicating(true);
                long remoteTime = ((Long) data[0]).longValue();
                long localTime = System.currentTimeMillis();
                long disparity = (localTime - remoteTime) / 1000;
                _log.info("time disparity: " + disparity + " secs");
                LocalState[] state = (LocalState[]) data[1];
                _log.info("receiving " + state.length + " sessions...");
                for (int i = 0; i < state.length; i++) {
                    LocalState ls = state[i];
                    _sessions.put(ls.getId(), ls);
                    getManager().getHttpSession(ls.getId());
                }
            } finally {
                AbstractReplicatedStore.setReplicating(false);
            }
        }
    }

    public void block() {
        _log.trace("handling JGroups block()...");
        _log.trace("...JGroups block() handled");
    }

    public synchronized void suspect(Address suspected_mbr) {
        if (_log.isTraceEnabled()) _log.trace("handling JGroups suspect(" + suspected_mbr + ")...");
        _log.warn("cluster suspects member may have been lost: " + suspected_mbr);
        _log.trace("...JGroups suspect() handled");
    }

    public synchronized void viewAccepted(View newView) {
        if (_log.isTraceEnabled()) _log.trace("handling JGroups viewAccepted(" + newView + ")...");
        if (newView instanceof MergeView) _log.warn("NYI - merging: view is " + newView);
        Vector newMembers = newView.getMembers();
        if (newMembers != null) {
            _members.clear();
            _members.addAll(newMembers);
            _log.info("JGroups View: " + _members);
            _members.remove(_channel.getLocalAddress());
        }
        _log.trace("...JGroups viewAccepted() handled");
    }
}
