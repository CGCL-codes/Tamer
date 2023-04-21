package org.jgroups.protocols;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Properties;
import org.jgroups.Event;
import org.jgroups.report.SendIgmpReport;
import org.jgroups.stack.IpAddress;
import org.jgroups.stack.Protocol;
import org.jgroups.protocols.strategy.PDSStrategy;
import org.jgroups.protocols.strategy.PDSPeer;
import org.jgroups.protocols.strategy.PDSSuperPeerLocalCluster;
import org.jgroups.protocols.strategy.PDSSuperPeerSPCluster;
import org.jgroups.structures.IP;
import org.jgroups.structures.ClusterStruct;

/**
 *
 * @author micky
 *
 * PDS es the main protocol uppon the
 * Peer-Discovery Service described in the documentation
 */
public class PDS extends Protocol {

    private PDSStrategy strategy = null;

    private boolean speer = false;

    private Serializable obj = null;

    public int max_time_OK_KO = 2;

    public int max_retry = 5;

    public int time_info = 1;

    public int time_workgroup = 2;

    public int max_retry_workgroup = 4;

    public int retry_sp_find = 5;

    public String nick = "micky";

    public boolean connect = false;

    public IP mcast;

    public static final String name = "PDS";

    /** Creates a new instance of PDS */
    public PDS() {
    }

    private void initStrategy() {
        connect = true;
        if (speer) {
            if (this.mcast.toString().equals("228.8.8.8")) {
                strategy = new PDSSuperPeerLocalCluster(this, nick);
                sendIgmp("228.8.8.8");
            } else {
                strategy = new PDSSuperPeerSPCluster(this);
                sendIgmp("224.0.0.8");
            }
        } else {
            strategy = new PDSPeer(this, nick);
            sendIgmp("228.8.8.8");
        }
    }

    public String getName() {
        return name;
    }

    /**
     * This method returns the profile identifier of the node
     * @return the public identifier of the node
     **/
    public Serializable getProfileIdentifier() {
        return obj;
    }

    /**
     * This method establishes the profile identifier of the node
     **/
    public void setProfileIdentifier(Serializable object) {
        obj = object;
    }

    public boolean setProperties(Properties props) {
        String str;
        super.setProperties(props);
        str = props.getProperty("speer");
        if (str != null) {
            speer = new Boolean(str).booleanValue();
            props.remove("speer");
        }
        str = props.getProperty("max_time_OK_KO");
        if (str != null) {
            max_time_OK_KO = Integer.parseInt(str);
            props.remove("max_time_OK_KO");
        }
        str = props.getProperty("time_info");
        if (str != null) {
            time_info = Integer.parseInt(str);
            props.remove("time_info");
        }
        str = props.getProperty("time_workgroup");
        if (str != null) {
            time_workgroup = Integer.parseInt(str);
            props.remove("time_workgroup");
        }
        str = props.getProperty("max_retry");
        if (str != null) {
            max_retry = Integer.parseInt(str);
            props.remove("max_retry");
        }
        str = props.getProperty("max_retry_workgroup");
        if (str != null) {
            max_retry_workgroup = Integer.parseInt(str);
            props.remove("max_retry_workgroup");
        }
        str = props.getProperty("retry_sp_find");
        if (str != null) {
            retry_sp_find = Integer.parseInt(str);
            props.remove("retry_sp_find");
        }
        str = props.getProperty("mcast");
        if (str != null) {
            mcast = new IP(str);
            if (!mcast.isMulticastAddress()) {
                return false;
            }
            props.remove("mcast");
        }
        str = props.getProperty("nick");
        if (str != null) {
            nick = str;
            props.remove("nick");
        }
        initStrategy();
        if (props.size() > 0) {
            return false;
        }
        return true;
    }

    public void up(Event evt) {
        synchronized (this) {
            if (strategy.handleUpEvent(evt)) passUp(evt);
        }
    }

    public void down(Event evt) {
        synchronized (this) {
            if (strategy.handleDownEvent(evt)) passDown(evt);
        }
    }

    public String toString() {
        return getName();
    }

    public ClusterStruct getGlobalClusterStruct() {
        return strategy.getGlobalClusterStruct();
    }

    public PDSStrategy getStrategy() {
        return strategy;
    }

    public void sendIgmp(String m) {
        final String mult = m;
        new Thread() {

            public void run() {
                while (connect) {
                    SendIgmpReport sir = new SendIgmpReport(mult);
                    sir.sendIgmpReportv4();
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
