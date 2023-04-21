package org.jgroups.protocols.strategy;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.jgroups.Address;
import org.jgroups.Event;
import org.jgroups.Header;
import org.jgroups.Message;
import org.jgroups.protocols.ClusterHeader;
import org.jgroups.protocols.GMHeader;
import org.jgroups.protocols.PDS;
import org.jgroups.protocols.PDSHeader;
import org.jgroups.protocols.GroupHeader;
import org.jgroups.protocols.SPeerHeader;
import org.jgroups.protocols.UpperHeader;
import org.jgroups.stack.IpAddress;
import org.jgroups.structures.IP;
import org.jgroups.structures.Node;
import org.jgroups.structures.ClusterStruct;
import org.jgroups.structures.Group;
import org.jgroups.structures.UpperStruct;
import org.jgroups.util.List;

/**
 *
 * @author micky
 */
public class PDSSuperPeerLocalCluster extends PDSStrategy {

    private ClusterStruct struct_updated = new ClusterStruct();

    private boolean connected = false;

    private ConcurrentHashMap time_node_cluster = new ConcurrentHashMap();

    private ConcurrentHashMap node_nick = new ConcurrentHashMap();

    private ConcurrentHashMap node_object = new ConcurrentHashMap();

    private ConcurrentHashMap workgroup_port = new ConcurrentHashMap();

    private int num_ident = 0;

    private boolean cluster_activo = false;

    private String nick;

    /**
     * 
     * Creates a new instance of PDSSuperPeerLocalCluster
     * @deprecated
     */
    public PDSSuperPeerLocalCluster(PDS mypds) {
        super(mypds);
    }

    public PDSSuperPeerLocalCluster(PDS mypds, String nick) {
        super(mypds);
        this.nick = nick;
    }

    public void sendInfo() {
        new Thread() {

            public void run() {
                while (connected) {
                    try {
                        Thread.currentThread().sleep(getPDS().time_info * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    if (getClusterStruct() != null) sendSP_Info_Cluster();
                    verifyNodes();
                }
            }
        }.start();
    }

    private void verifyNodes() {
        synchronized (this) {
            if (time_node_cluster.size() > 0) {
                List nod = this.getClusterStruct().getListNodesTotal();
                Enumeration en = nod.elements();
                while (en.hasMoreElements()) {
                    Node n = (Node) en.nextElement();
                    String stringTime = (String) time_node_cluster.get(n.getIP().toString());
                    if (stringTime != null) {
                        long lo = new Long(stringTime).longValue();
                        if ((lo + getPDS().time_info * 6000) < System.currentTimeMillis()) {
                            getClusterStruct().removeNodefromAll(n);
                            getUpperStruct().removeNodefromAll(n);
                            time_node_cluster.remove(n.toString());
                            synchronized (this) {
                                cluster_activo = true;
                                num_ident = getIdentNum();
                            }
                            setGlobalClusterStruct(struct_updated.joinClusterStruct(getClusterStruct()));
                            eventCLUSTER();
                        }
                    } else if (!n.getNick().equalsIgnoreCase(this.nick)) {
                        getClusterStruct().removeNodefromAll(n);
                        getUpperStruct().removeNodefromAll(n);
                        synchronized (this) {
                            cluster_activo = true;
                            num_ident = getIdentNum();
                        }
                        setGlobalClusterStruct(struct_updated.joinClusterStruct(getClusterStruct()));
                        eventCLUSTER();
                    }
                }
            }
        }
    }

    public void sendSP_Info_Cluster() {
        Message newmsg = new Message(null);
        try {
            newmsg.setDest(new IpAddress(getPDS().mcast.toString(), 0));
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        boolean aux = false;
        int n_ident = 0;
        ClusterStruct sc = null;
        synchronized (this) {
            aux = cluster_activo;
            if (aux) cluster_activo = false;
            n_ident = num_ident;
        }
        if (aux) {
            sc = getGlobalClusterStruct();
            sc = sc.reviewClusterStruct();
            newmsg.setObject(sc);
        }
        PDSHeader head = new PDSHeader(PDSHeader.SP_Info_Cluster, aux, n_ident);
        newmsg.putHeader(getPDS().getName(), head);
        Event newevent = new Event(Event.MSG, newmsg);
        getPDS().passDown(newevent);
    }

    public void sendSP_Port(IpAddress ip, String nomWG, String port, boolean coord) {
        Message newmsg = new Message(null);
        newmsg.setDest(ip);
        GMHeader h = new GMHeader(GMHeader.SP_Port_Serv, nomWG, new Integer(port).intValue(), coord);
        newmsg.putHeader("GM", h);
        getPDS().passDown(new Event(Event.MSG, newmsg));
    }

    public void sendSP_OK(IpAddress ip, String nick, Serializable obj) {
        Message newmsg = new Message(null);
        newmsg.setDest(ip);
        newmsg.putHeader(getPDS().getName(), new PDSHeader(PDSHeader.SP_OK, true, nick, obj));
        Event newevent = new Event(Event.MSG, newmsg);
        getPDS().passDown(newevent);
    }

    public void sendSP_KO(IpAddress ip) {
        Message newmsg = new Message(null);
        newmsg.setDest(ip);
        newmsg.putHeader(getPDS().getName(), new PDSHeader(PDSHeader.SP_KO));
        Event newevent = new Event(Event.MSG, newmsg);
        getPDS().passDown(newevent);
    }

    public void sendSP_KO_Group(String nomWorkGroup, IpAddress dest) {
        Message newmsg = new Message(null);
        newmsg.setDest(dest);
        newmsg.putHeader(getPDS().getName(), new PDSHeader(PDSHeader.SP_KO_WorkGroup, nomWorkGroup));
        Event newevent = new Event(Event.MSG, newmsg);
        getPDS().passDown(newevent);
    }

    public void sendSP_OK_Group(String nomWorkGroup, IpAddress dest) {
        Message newmsg = new Message(null);
        newmsg.setDest(dest);
        newmsg.putHeader(getPDS().getName(), new PDSHeader(PDSHeader.SP_OK_WorkGroup, nomWorkGroup));
        Event newevent = new Event(Event.MSG, newmsg);
        getPDS().passDown(newevent);
    }

    private int getIdentNum() {
        synchronized (this) {
            return num_ident = num_ident + 1;
        }
    }

    public String getNick() {
        return nick;
    }

    private void initClusterStruct() {
        if (getLocalAddress() != null) {
            String ip_local = getLocalAddress().toString();
            int port = ((IpAddress) getLocalAddress()).getPort();
            ip_local = ip_local.substring(0, ip_local.indexOf(":"));
            writeSuperPeerSocket(new IP(ip_local));
            ClusterStruct sc = new ClusterStruct(ip_local);
            sc.addNode((Node) new Node(ip_local, nick, port, getPDS().getProfileIdentifier()));
            UpperStruct us = new UpperStruct();
            us.addCluster(sc);
            setUpperStruct(us);
            setClusterStruct(sc);
            setGlobalClusterStruct(sc);
            eventCLUSTER();
        }
    }

    public void eventCLUSTER() {
        Message msg = new Message(null);
        msg.putHeader("Upper", new UpperHeader(getUpperStruct()));
        Event evt = new Event(Event.MSG, msg);
        getPDS().passUp(evt);
        Message msg1 = new Message(null);
        msg1.putHeader("Cluster", new ClusterHeader(getClusterStruct()));
        Event evt1 = new Event(Event.MSG, msg1);
        getPDS().passUp(evt1);
    }

    public boolean handleUpEvent(Event evt) {
        switch(evt.getType()) {
            case Event.CONNECT_OK:
                connected = true;
                sendInfo();
                initClusterStruct();
                break;
            case Event.DISCONNECT_OK:
                connected = false;
                break;
            case Event.SET_LOCAL_ADDRESS:
                setLocalAddress((Address) evt.getArg());
                break;
            case Event.MSG:
                Message msg = (Message) evt.getArg();
                PDSHeader head = (PDSHeader) msg.getHeader("PDS");
                if (head != null) {
                    handle(msg);
                    return false;
                }
        }
        return true;
    }

    public boolean handleDownEvent(Event evt) {
        switch(evt.getType()) {
            case Event.MSG:
                Message msg = (Message) evt.getArg();
                ClusterHeader head = (ClusterHeader) msg.getHeader("Cluster");
                if (head != null) {
                    synchronized (this) {
                        struct_updated = head.struct_cluster;
                        ClusterStruct sc = struct_updated.joinClusterStruct(getClusterStruct());
                        setGlobalClusterStruct(sc);
                        cluster_activo = true;
                        num_ident = getIdentNum();
                    }
                    return false;
                }
                UpperHeader uh = (UpperHeader) msg.getHeader("Upper");
                if (uh != null) {
                    setUpperStruct(uh.upper);
                    return false;
                }
                GMHeader headmwg = (GMHeader) msg.getHeader("GM");
                if (headmwg != null) {
                    if (headmwg.tipo == GMHeader.SP_Port_Serv) {
                        workgroup_port.put(headmwg.nom_WG, "" + headmwg.port);
                    }
                    return false;
                }
                GroupHeader headwg = (GroupHeader) msg.getHeader("Group");
                if (headwg != null) {
                    switch(headwg.tipo) {
                        case GroupHeader.SP_WorkGroup:
                            SPeerHeader sph = (SPeerHeader) msg.getHeader("SPeer");
                            if (sph != null) {
                                IpAddress ia = (IpAddress) msg.getSrc();
                                if (getGlobalClusterStruct().getNumGroups() > 0) {
                                    boolean exist = getGlobalClusterStruct().isGroup(headwg.wg.getName());
                                    Group workgroup = getClusterStruct().getGroup(headwg.wg.getName());
                                    boolean isNode = false;
                                    Node node = new Node(((IpAddress) getLocalAddress()).getIpAddress().getHostAddress(), nick, ((IpAddress) getLocalAddress()).getPort(), getPDS().getProfileIdentifier());
                                    if (workgroup == null || !workgroup.isNode(node)) isNode = false;
                                    if (exist && !isNode) {
                                        ClusterStruct stcl = getClusterStruct();
                                        Group wrkgr = headwg.wg.clone();
                                        wrkgr.addNode(node);
                                        stcl.addGroup(wrkgr);
                                        synchronized (this) {
                                            cluster_activo = true;
                                            num_ident = getIdentNum();
                                            setGlobalClusterStruct(struct_updated.joinClusterStruct(getClusterStruct()));
                                        }
                                        Enumeration en = getUpperStruct().getGroups().elements();
                                        while (en.hasMoreElements()) {
                                            Group gs = (Group) en.nextElement();
                                            if (wrkgr.getName().compareTo(gs.getName()) == 0) {
                                                gs.addNode(node);
                                                if (wrkgr.getNumServices() != 0) gs.addListServices(wrkgr.getServices());
                                            }
                                        }
                                        getPDS().passUp(new Event(Event.MSG, msg.copy()));
                                        eventCLUSTER();
                                    }
                                }
                            } else {
                                ClusterStruct stcl = getClusterStruct();
                                Group wrkgr = headwg.wg.clone();
                                stcl.addGroup(wrkgr);
                                synchronized (this) {
                                    cluster_activo = true;
                                    num_ident = getIdentNum();
                                    setGlobalClusterStruct(struct_updated.joinClusterStruct(getClusterStruct()));
                                }
                                Enumeration en = getUpperStruct().getGroups().elements();
                                while (en.hasMoreElements()) {
                                    Group gs = (Group) en.nextElement();
                                    if (wrkgr.getName().compareTo(gs.getName()) == 0) {
                                        if (wrkgr.getNumServices() != 0) {
                                            gs.addListServices(wrkgr.getServices());
                                        }
                                    }
                                }
                                eventCLUSTER();
                            }
                            return false;
                        case GroupHeader.SP_New_WorkGroup:
                            boolean exist = false;
                            if (getGlobalClusterStruct().getNumGroups() > 0) {
                                Enumeration enu = getGlobalClusterStruct().getGroups().elements();
                                while (enu.hasMoreElements()) {
                                    Group wg = (Group) enu.nextElement();
                                    if (wg.getName().compareTo(headwg.wg.getName()) == 0 || wg.getIpMulticast().compareTo(headwg.wg.getIPMulticast().toString()) == 0) {
                                        exist = true;
                                    }
                                }
                            }
                            if (!exist) {
                                Group workg = new Group(headwg.wg.getName(), headwg.wg.getIpMulticast());
                                Node node = new Node(((IpAddress) getLocalAddress()).getIpAddress().getHostAddress(), nick, ((IpAddress) getLocalAddress()).getPort(), getPDS().getProfileIdentifier());
                                workg.addNode(node);
                                Group wgnew = new Group(headwg.wg.getName(), headwg.wg.getIpMulticast());
                                wgnew.addNode(node);
                                getClusterStruct().addGroup(wgnew);
                                synchronized (this) {
                                    cluster_activo = true;
                                    num_ident = getIdentNum();
                                    setGlobalClusterStruct(struct_updated.joinClusterStruct(getClusterStruct()));
                                }
                                getUpperStruct().addGroup(wgnew);
                                getPDS().passUp(new Event(Event.MSG, msg.copy()));
                                eventCLUSTER();
                            }
                            return false;
                        case GroupHeader.SP_KO_WorkGroup:
                            IpAddress ia = (IpAddress) msg.getSrc();
                            boolean isInWG = false, pasa = false;
                            Group wg = null;
                            Node node = new Node(((IpAddress) getLocalAddress()).getIpAddress().getHostAddress(), nick, ((IpAddress) getLocalAddress()).getPort(), getPDS().getProfileIdentifier());
                            if (getGlobalClusterStruct().getNumGroups() > 0) {
                                Enumeration en = getClusterStruct().getGroups().elements();
                                while (en.hasMoreElements() && !pasa) {
                                    wg = (Group) en.nextElement();
                                    if (wg.getName().compareTo(headwg.wg.getName()) == 0) {
                                        pasa = true;
                                        if (wg.isNode(node)) isInWG = true;
                                    }
                                }
                            }
                            if (isInWG) {
                                getClusterStruct().removeNodefromGroup(node, wg);
                                if (getClusterStruct().getGroup(wg.getName()) == null) this.passUpSP_Group_KO(wg);
                                if (!getClusterStruct().isNodeinClusterStruct(node)) getClusterStruct().addNode(node);
                                synchronized (this) {
                                    cluster_activo = true;
                                    num_ident = getIdentNum();
                                    setGlobalClusterStruct(struct_updated.joinClusterStruct(getClusterStruct()));
                                }
                                Enumeration en = getUpperStruct().getGroups().elements();
                                while (en.hasMoreElements()) {
                                    Group g = (Group) en.nextElement();
                                    if (g.getName().compareTo(wg.getName()) == 0) {
                                        g.removeNode(node);
                                    }
                                }
                                eventCLUSTER();
                            }
                            return false;
                    }
                }
                return true;
        }
        return true;
    }

    /**
      * This method handles the messages received by the JChannel
      * @param msg Message
      **/
    private void handle(Message msg) {
        PDSHeader head = (PDSHeader) msg.getHeader("PDS");
        IpAddress ia = (IpAddress) msg.getSrc();
        IP ip = new IP(ia.getIpAddress());
        int port = ia.getPort();
        String ni = (String) node_nick.get(ia);
        Serializable obj = (Serializable) node_object.get(ia);
        Node nod = null;
        if (ni != null) nod = new Node(ip.toString(), ni, port, obj); else nod = new Node(ip.toString(), "strange", port, "strange");
        switch(head.tipo) {
            case PDSHeader.SP_OK:
                if (head.activo) {
                    nod = new Node(ip.toString(), head.nick, port, head.obj);
                    sendSP_OK(ia, head.nick, head.obj);
                    node_nick.put(ia, head.nick);
                    node_object.put(ia, head.obj);
                    synchronized (this) {
                        cluster_activo = true;
                    }
                }
                if (!getClusterStruct().isNodeinClusterStruct(nod)) {
                    if (!head.activo) {
                        sendSP_OK(ia, "strange", "strange");
                    } else {
                        Enumeration en = getUpperStruct().getClusters().elements();
                        while (en.hasMoreElements()) {
                            ClusterStruct cstr = (ClusterStruct) en.nextElement();
                            if (cstr.getIPSuperPeer().toString().compareTo(((IpAddress) getLocalAddress()).getIpAddress().getHostAddress().toString()) == 0) {
                                cstr.addNode(nod);
                            }
                        }
                        getClusterStruct().addNode(nod);
                        synchronized (this) {
                            num_ident = getIdentNum();
                            setGlobalClusterStruct(struct_updated.joinClusterStruct(getClusterStruct()));
                        }
                        eventCLUSTER();
                    }
                }
                synchronized (this) {
                    String time = new Long(System.currentTimeMillis()).toString();
                    time_node_cluster.put(ip.toString(), time);
                }
                break;
            case PDSHeader.SP_KO:
                if (getClusterStruct().isNodeinClusterStruct(nod)) {
                    sendSP_KO(ia);
                    Enumeration en = getUpperStruct().getClusters().elements();
                    while (en.hasMoreElements()) {
                        ClusterStruct cstr = (ClusterStruct) en.nextElement();
                        if (cstr.getIPSuperPeer().toString().compareTo(((IpAddress) getLocalAddress()).getIpAddress().getHostAddress().toString()) == 0) {
                            cstr.removeNodefromAll(nod);
                        }
                    }
                    Enumeration enu = getUpperStruct().getGroups().elements();
                    while (enu.hasMoreElements()) {
                        Group gr = (Group) enu.nextElement();
                        if (gr.isNode(nod)) gr.removeNode(nod);
                    }
                    getClusterStruct().removeNodefromAll(nod);
                    synchronized (this) {
                        cluster_activo = true;
                        num_ident = getIdentNum();
                        setGlobalClusterStruct(struct_updated.joinClusterStruct(getClusterStruct()));
                    }
                    eventCLUSTER();
                }
                synchronized (this) {
                    time_node_cluster.remove(ip.toString());
                }
                break;
            case PDSHeader.SP_Info_Retry:
                if (getClusterStruct().isNodeinClusterStruct(nod)) {
                    synchronized (this) {
                        cluster_activo = true;
                    }
                }
                break;
            case PDSHeader.SP_WorkGroup:
                if (getClusterStruct().isNodeinClusterStruct(nod)) {
                    if (getGlobalClusterStruct().getNumGroups() > 0) {
                        boolean exist = getGlobalClusterStruct().isGroup(head.workGroup.getName());
                        Group workgroup = getClusterStruct().getGroup(head.workGroup.getName());
                        boolean isNode = false;
                        if (workgroup == null || !workgroup.isNode(nod)) isNode = true;
                        if (exist && isNode) {
                            ClusterStruct stcl = getClusterStruct();
                            Group wrkgr = head.workGroup.clone();
                            if (wrkgr.getNumServices() == 0) wrkgr.addNode(nod);
                            stcl.addGroup(wrkgr);
                            synchronized (this) {
                                cluster_activo = true;
                                num_ident = getIdentNum();
                                setGlobalClusterStruct(struct_updated.joinClusterStruct(getClusterStruct()));
                            }
                            sendSP_OK_Port(head.workGroup, ia, false);
                            boolean pasa = false;
                            Enumeration en = getUpperStruct().getGroups().elements();
                            while (en.hasMoreElements()) {
                                Group gs = (Group) en.nextElement();
                                if (wrkgr.getName().compareTo(gs.getName()) == 0) {
                                    gs.addNode(nod);
                                    pasa = true;
                                }
                            }
                            if (!pasa) getUpperStruct().addGroup(wrkgr);
                            eventCLUSTER();
                        } else sendSP_KO_Group(head.workGroup.getName(), ia);
                    } else sendSP_KO_Group(head.workGroup.getName(), ia);
                }
                break;
            case PDSHeader.SP_KO_WorkGroup:
                if (getClusterStruct().isNodeinClusterStruct(nod)) {
                    boolean isInWG = false, pasa = false;
                    Group wg = null;
                    if (getGlobalClusterStruct().getNumGroups() > 0) {
                        Enumeration en = getGlobalClusterStruct().getGroups().elements();
                        while (en.hasMoreElements() && !pasa) {
                            wg = (Group) en.nextElement();
                            if (wg.getName().compareTo(head.nom_WG) == 0) {
                                pasa = true;
                                if (wg.isNode(nod)) {
                                    isInWG = true;
                                }
                            }
                        }
                    }
                    if (isInWG) {
                        getClusterStruct().removeNodefromGroup(nod, wg);
                        if (getClusterStruct().getGroup(wg.getName()) == null) {
                            this.passUpSP_Group_KO(wg);
                        }
                        sendSP_KO_Group(head.nom_WG, ia);
                        if (!getClusterStruct().isNodeinClusterStruct(nod)) getClusterStruct().addNode(nod);
                        synchronized (this) {
                            cluster_activo = true;
                            num_ident = getIdentNum();
                            setGlobalClusterStruct(struct_updated.joinClusterStruct(getClusterStruct()));
                        }
                        Enumeration en = getUpperStruct().getGroups().elements();
                        while (en.hasMoreElements()) {
                            Group g = (Group) en.nextElement();
                            if (g.getName().compareTo(wg.getName()) == 0) {
                                g.removeNode(nod);
                                if (g.getNumNodesTotal() == 0) {
                                    getUpperStruct().removeGroup(g);
                                }
                            }
                        }
                        eventCLUSTER();
                    }
                }
                break;
            case PDSHeader.SP_New_WorkGroup:
                if (getGlobalClusterStruct().isNodeinClusterStruct(nod)) {
                    boolean exist = false;
                    if (getGlobalClusterStruct().getNumGroups() > 0) {
                        Enumeration enu = getGlobalClusterStruct().getGroups().elements();
                        while (enu.hasMoreElements()) {
                            Group wg = (Group) enu.nextElement();
                            if (wg.getName().compareTo(head.nom_WG) == 0 || wg.getIpMulticast().compareTo(head.ip_mult_WG.toString()) == 0) {
                                exist = true;
                            }
                        }
                    }
                    if (exist) sendSP_KO_Group(head.nom_WG, ia); else {
                        Group workg = new Group(head.nom_WG, head.ip_mult_WG);
                        workg.addNode(nod);
                        sendSP_OK_Port(workg, ia, true);
                        Group wgnew = new Group(head.nom_WG, head.ip_mult_WG);
                        wgnew.addNode(nod);
                        getClusterStruct().addGroup(wgnew);
                        synchronized (this) {
                            cluster_activo = true;
                            num_ident = getIdentNum();
                            setGlobalClusterStruct(struct_updated.joinClusterStruct(getClusterStruct()));
                        }
                        getUpperStruct().addGroup(wgnew);
                        eventCLUSTER();
                    }
                }
                break;
        }
    }

    private void sendSP_OK_Port(Group wg, IpAddress ia, boolean coordinator) {
        final Group workgroup = wg.clone();
        final String ip = wg.getIpMulticast();
        final String nWG = wg.getName();
        final IpAddress dest = ia;
        final String p = (String) workgroup_port.get(nWG);
        final boolean coord = coordinator;
        if (p != null) {
            new Thread() {

                public void run() {
                    passUpSP_Group(workgroup);
                    sendSP_OK_Group(nWG, dest);
                    try {
                        Thread.currentThread().sleep(3500);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    sendSP_Port(dest, nWG, p, coord);
                }
            }.start();
        } else {
            new Thread() {

                public void run() {
                    passUpSP_Group(workgroup);
                    boolean aux;
                    String puerto = null;
                    while (puerto == null) {
                        try {
                            Thread.currentThread().sleep(10);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        puerto = (String) workgroup_port.get(nWG);
                    }
                    sendSP_OK_Group(nWG, dest);
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    sendSP_Port(dest, nWG, puerto, coord);
                }
            }.start();
        }
    }

    public void passUpSP_Group(Group wg) {
        Message m = new Message(null);
        GroupHeader hea = new GroupHeader(GroupHeader.SP_WorkGroup, wg);
        m.putHeader("Group", hea);
        getPDS().up(new Event(Event.MSG, m));
    }

    public void passUpSP_Group_KO(Group wg) {
        Message m = new Message(null);
        GroupHeader hea = new GroupHeader(GroupHeader.SP_KO_WorkGroup, wg);
        m.putHeader("Group", hea);
        getPDS().up(new Event(Event.MSG, m));
        workgroup_port.remove(wg.getName());
    }
}
