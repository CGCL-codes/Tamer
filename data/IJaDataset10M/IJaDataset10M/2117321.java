package org.jgroups.protocols;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import org.jgroups.Event;
import org.jgroups.protocols.strategy.GMPeer;
import org.jgroups.protocols.strategy.GMSuperPeer;
import org.jgroups.protocols.strategy.GMStrategy;
import org.jgroups.report.SendIgmpReport;
import org.jgroups.stack.IpAddress;
import org.jgroups.stack.Protocol;
import org.jgroups.structures.Group;

/**
 *
 * @author micky
 *
 * Protocol used for the group management development
 * specified in the documentation
 *
 */
public class GM extends Protocol {

    private GMStrategy strategy = null;

    private Group workgroup = null;

    private boolean speer = false;

    private IpAddress mcast = null;

    private String name_WG = null;

    private IpAddress ip_speer = null;

    private String nick = null;

    public int time_serv = 1;

    public int max_retry_serv = 3;

    public static final String name = "GM";

    public boolean coordinator;

    /**
     * Creates a new instance of GM
     */
    public GM() {
    }

    public void up(Event evt) {
        synchronized (this) {
            if (strategy.handleUpEvent(evt)) {
                passUp(evt);
            }
        }
    }

    public void down(Event evt) {
        synchronized (this) {
            if (strategy.handleDownEvent(evt)) {
                passDown(evt);
            }
        }
    }

    public String getGroupName() {
        return name_WG;
    }

    public IpAddress getGroupMcast() {
        return mcast;
    }

    public IpAddress getIpSuperPeer() {
        return ip_speer;
    }

    public void setIpSuperPeer(IpAddress ip) {
        try {
            ip_speer = (IpAddress) ip.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
    }

    public boolean getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(boolean coord) {
        this.coordinator = coord;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public GMStrategy getStrategy() {
        return this.strategy;
    }

    public boolean setProperties(Properties props) {
        String str;
        super.setProperties(props);
        str = props.getProperty("speer");
        if (str != null) {
            speer = new Boolean(str).booleanValue();
            props.remove("speer");
        }
        str = props.getProperty("name_WG");
        if (str != null) {
            name_WG = str;
            props.remove("name_WG");
        }
        str = props.getProperty("mcast");
        if (str != null) {
            try {
                mcast = new IpAddress(str, 7600);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
                return false;
            }
            if (!mcast.isMulticastAddress()) {
                return false;
            }
            props.remove("mcast");
        }
        str = props.getProperty("ip_speer");
        if (str != null) {
            try {
                ip_speer = new IpAddress(str, 1111);
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
                return false;
            }
            props.remove("ip_speer");
        }
        str = props.getProperty("time_serv");
        if (str != null) {
            time_serv = Integer.parseInt(str);
            props.remove("time_serv");
        }
        str = props.getProperty("max_retry_serv");
        if (str != null) {
            max_retry_serv = Integer.parseInt(str);
            props.remove("max_retry_serv");
        }
        str = props.getProperty("nick");
        if (str != null) {
            nick = str;
            props.remove("nick");
        }
        if (props.size() > 0) {
            return false;
        }
        initStrategy();
        return true;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getName();
    }

    private void initStrategy() {
        if (speer) this.strategy = new GMSuperPeer(this); else this.strategy = new GMPeer(this);
        this.strategy.setGroup(new Group(name_WG, mcast.getIpAddress().getHostAddress()));
        sendIgmp(mcast.getIpAddress().getHostAddress());
    }

    public void sendIgmp(String m) {
        final String mult = m;
        new Thread() {

            public void run() {
                while (true) {
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
