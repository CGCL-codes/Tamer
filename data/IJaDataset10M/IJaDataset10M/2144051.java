package org.apache.catalina.ha.session;

import java.io.IOException;
import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Session;
import org.apache.catalina.ha.ClusterMessage;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.ha.*;

/**
 * Receive SessionID cluster change from other backup node after primary session
 * node is failed.
 * 
 * @author Peter Rossbach
 * @version $Revision: 467222 $ $Date: 2006-10-24 05:17:11 +0200 (Tue, 24 Oct 2006) $
 */
public class JvmRouteSessionIDBinderListener extends ClusterListener {

    /**
     * The descriptive information about this implementation.
     */
    protected static final String info = "org.apache.catalina.ha.session.JvmRouteSessionIDBinderListener/1.1";

    protected boolean started = false;

    /**
     * number of session that goes to this cluster node
     */
    private long numberOfSessions = 0;

    public JvmRouteSessionIDBinderListener() {
    }

    /**
     * Return descriptive information about this implementation.
     */
    public String getInfo() {
        return (info);
    }

    /**
     * @return Returns the numberOfSessions.
     */
    public long getNumberOfSessions() {
        return numberOfSessions;
    }

    /**
     * Add this Mover as Cluster Listener ( receiver)
     * 
     * @throws LifecycleException
     */
    public void start() throws LifecycleException {
        if (started) return;
        getCluster().addClusterListener(this);
        started = true;
        if (log.isInfoEnabled()) log.info(sm.getString("jvmRoute.clusterListener.started"));
    }

    /**
     * Remove this from Cluster Listener
     * 
     * @throws LifecycleException
     */
    public void stop() throws LifecycleException {
        started = false;
        getCluster().removeClusterListener(this);
        if (log.isInfoEnabled()) log.info(sm.getString("jvmRoute.clusterListener.stopped"));
    }

    /**
     * Callback from the cluster, when a message is received, The cluster will
     * broadcast it invoking the messageReceived on the receiver.
     * 
     * @param msg
     *            ClusterMessage - the message received from the cluster
     */
    public void messageReceived(ClusterMessage msg) {
        if (msg instanceof SessionIDMessage && msg != null) {
            SessionIDMessage sessionmsg = (SessionIDMessage) msg;
            if (log.isDebugEnabled()) log.debug(sm.getString("jvmRoute.receiveMessage.sessionIDChanged", sessionmsg.getOrignalSessionID(), sessionmsg.getBackupSessionID(), sessionmsg.getContextPath()));
            Container container = getCluster().getContainer();
            Container host = null;
            if (container instanceof Engine) {
                host = container.findChild(sessionmsg.getHost());
            } else {
                host = container;
            }
            if (host != null) {
                Context context = (Context) host.findChild(sessionmsg.getContextPath());
                if (context != null) {
                    try {
                        Session session = context.getManager().findSession(sessionmsg.getOrignalSessionID());
                        if (session != null) {
                            session.setId(sessionmsg.getBackupSessionID());
                        } else if (log.isInfoEnabled()) log.info(sm.getString("jvmRoute.lostSession", sessionmsg.getOrignalSessionID(), sessionmsg.getContextPath()));
                    } catch (IOException e) {
                        log.error(e);
                    }
                } else if (log.isErrorEnabled()) log.error(sm.getString("jvmRoute.contextNotFound", sessionmsg.getContextPath(), ((StandardEngine) host.getParent()).getJvmRoute()));
            } else if (log.isErrorEnabled()) log.error(sm.getString("jvmRoute.hostNotFound", sessionmsg.getContextPath()));
        }
        return;
    }

    /**
     * Accept only SessionIDMessages
     * 
     * @param msg
     *            ClusterMessage
     * @return boolean - returns true to indicate that messageReceived should be
     *         invoked. If false is returned, the messageReceived method will
     *         not be invoked.
     */
    public boolean accept(ClusterMessage msg) {
        return (msg instanceof SessionIDMessage);
    }
}
