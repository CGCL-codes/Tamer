package org.jgroups.protocols.strategy;

import java.util.Vector;
import org.jgroups.Address;
import org.jgroups.Event;
import org.jgroups.Message;
import org.jgroups.protocols.GM;
import org.jgroups.protocols.GMHeader;
import org.jgroups.protocols.GroupHeader;
import org.jgroups.protocols.UpperHeader;
import org.jgroups.stack.IpAddress;
import org.jgroups.structures.Group;
import org.jgroups.structures.Node;
import org.jgroups.structures.Service;
import org.jgroups.structures.ServiceGroup;

/**
 *
 * @author micky
 */
public class GMPeer extends GMStrategy {

    private boolean activo = false;

    private boolean pasServ_OK = false;

    private boolean sendwg = false;

    private Vector sendWG = new Vector();

    private boolean sendwgn = false;

    private Vector sendWGN = new Vector();

    private boolean sendwgk = false;

    private Vector sendWGK = new Vector();

    /**
     * Creates a new instance of GMPeer
     */
    public GMPeer(GM mwg) {
        super(mwg);
    }

    public boolean handleUpEvent(Event evt) {
        if (!activo) initStat();
        if (evt.getType() == Event.STOP_OK) {
            synchronized (this) {
                activo = false;
            }
        } else if (evt.getType() == Event.SET_LOCAL_ADDRESS) {
            setLocalAddress((Address) evt.getArg());
        } else if (evt.getType() == Event.MSG) {
            Message msg = (Message) evt.getArg();
            IpAddress ia = ((IpAddress) msg.getSrc());
            GMHeader head = (GMHeader) msg.getHeader(getGM().getName());
            if (head != null) {
                switch(head.tipo) {
                    case GMHeader.SP_OK_Serv:
                        if (isSuperPeerNow(ia)) {
                            pasServ_OK = true;
                            String group_serv = head.nom_GrupoServicio + " " + head.nom_Servicio;
                            boolean pas = false;
                            synchronized (this) {
                                if (sendwg && sendWG.contains(group_serv)) {
                                    sendWG.remove(group_serv);
                                    pas = true;
                                    if (sendWG.size() == 0) sendwg = false;
                                }
                                if (!pas && sendwgn && sendWGN.contains(group_serv)) {
                                    sendWGN.remove(group_serv);
                                    if (sendWGN.size() == 0) sendwgn = false;
                                }
                            }
                        }
                        break;
                    case GMHeader.SP_KO_Serv:
                        if (isSuperPeerNow(ia)) {
                            String group_serv = head.nom_GrupoServicio + " " + head.nom_Servicio;
                            boolean pasa = false;
                            synchronized (this) {
                                if (sendwg && sendWG.contains(group_serv)) {
                                    sendWG.remove(group_serv);
                                    pasa = true;
                                    if (sendWG.size() == 0) sendwg = false;
                                }
                                if (!pasa && sendwgn && sendWGN.contains(group_serv)) {
                                    sendWGN.remove(group_serv);
                                    pasa = true;
                                    if (sendWGN.size() == 0) sendwgn = false;
                                }
                                if (!pasa && sendwgk && sendWGK.contains(group_serv)) {
                                    sendWGK.remove(group_serv);
                                    if (sendWGN.size() == 0) sendwgk = false;
                                }
                            }
                        }
                        break;
                }
                return false;
            }
        }
        return true;
    }

    public boolean handleDownEvent(Event evt) {
        if (!activo) initStat();
        switch(evt.getType()) {
            case Event.MSG:
                Message msg = (Message) evt.getArg();
                GMHeader head = (GMHeader) msg.getHeader("GM");
                IpAddress ia = ((IpAddress) msg.getSrc());
                if (head != null) {
                    switch(head.tipo) {
                        case GMHeader.SP_Serv:
                            sendSP_Serv(head);
                            break;
                        case GMHeader.SP_New_Serv:
                            sendSP_New_Serv(head);
                            break;
                        case GMHeader.SP_KO_Serv_Nick:
                            sendSP_KO_Serv(head);
                            break;
                    }
                    return false;
                }
                GroupHeader headwg = (GroupHeader) msg.getHeader("Group");
                if (headwg != null) {
                    msg.setDest(getGM().getGroupMcast());
                    return true;
                }
            case Event.DISCONNECT:
                synchronized (this) {
                    activo = false;
                }
                return true;
        }
        return true;
    }

    /**
     * Se encarga de ejecutar la especificacion del
     */
    protected void sendSP_Serv(final GMHeader head) {
        final String nomgr = head.nom_GrupoServicio;
        final String nomserv = head.nom_Servicio;
        final String nick = head.nick;
        final Object obj = head.obj;
        new Thread() {

            public void run() {
                boolean ac, pasa = false;
                int cont = 0;
                String group_serv = nomgr + " " + nomserv;
                synchronized (this) {
                    ac = activo;
                    sendwg = true;
                    sendWG.add(group_serv);
                }
                while (ac && !pasa && cont < getGM().max_retry_serv) {
                    sendGMHeader(head);
                    cont++;
                    try {
                        Thread.currentThread().sleep(getGM().time_serv * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    synchronized (this) {
                        ac = activo;
                        if (!sendWG.contains(group_serv)) pasa = true;
                    }
                }
                synchronized (this) {
                    if (!sendWG.contains(group_serv)) {
                        sendWG.remove(group_serv);
                        sendwg = false;
                    }
                }
                if (pasa && pasServ_OK) {
                    ServiceGroup sg = new ServiceGroup(nomgr);
                    sg.addNode(new Node(((IpAddress) getLocalAddress()).getIpAddress().getHostAddress(), nick, ((IpAddress) getLocalAddress()).getPort(), obj));
                    Service s = new Service(nomserv);
                    s.addServiceGroup(sg);
                    getGroup().addService(s);
                    passUpGroup();
                }
            }
        }.start();
    }

    protected void sendSP_New_Serv(final GMHeader head) {
        final String nomgr = head.nom_GrupoServicio;
        final String nomserv = head.nom_Servicio;
        final String nick = head.nick;
        final Object obj = head.obj;
        new Thread() {

            public void run() {
                boolean ac, pasa = false;
                int cont = 0;
                String group_serv = nomgr + " " + nomserv;
                synchronized (this) {
                    ac = activo;
                    sendwgn = true;
                    sendWGN.add(group_serv);
                }
                while (ac && !pasa && cont < getGM().max_retry_serv) {
                    sendGMHeader(head);
                    cont++;
                    try {
                        Thread.currentThread().sleep(getGM().time_serv * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    synchronized (this) {
                        ac = activo;
                        if (!sendWGN.contains(group_serv)) pasa = true;
                    }
                }
                synchronized (this) {
                    if (sendWGN.contains(group_serv)) {
                        sendWGN.remove(group_serv);
                        sendwgn = false;
                    }
                }
                if (pasa && pasServ_OK) {
                    ServiceGroup sg = new ServiceGroup(nomgr);
                    sg.addNode(new Node(((IpAddress) getLocalAddress()).getIpAddress().getHostAddress(), nick, ((IpAddress) getLocalAddress()).getPort(), obj));
                    Service s = new Service(nomserv);
                    s.addServiceGroup(sg);
                    getGroup().addService(s);
                    passUpGroup();
                }
            }
        }.start();
    }

    /**
     * Se encarga de ejecutar la especificacion del
     */
    protected void sendSP_KO_Serv(final GMHeader head) {
        final String nomgr = head.nom_GrupoServicio;
        final String nomserv = head.nom_Servicio;
        final Object obj = head.obj;
        new Thread() {

            public void run() {
                boolean ac, pasa = false;
                int cont = 0;
                String group_serv = nomgr + " " + nomserv;
                synchronized (this) {
                    ac = activo;
                    sendwgk = true;
                    sendWGK.add(group_serv);
                }
                while (ac && !pasa && cont < getGM().max_retry_serv) {
                    sendGMHeader(head);
                    cont++;
                    try {
                        Thread.currentThread().sleep(getGM().time_serv * 1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    synchronized (this) {
                        ac = activo;
                        if (!sendWGK.contains(group_serv)) pasa = true;
                    }
                }
                synchronized (this) {
                    if (sendWGK.contains(group_serv)) {
                        sendWGK.remove(group_serv);
                        sendwgk = false;
                    }
                }
                if (pasa) {
                    getGroup().removeServiceGroupFromService(nomgr, nomserv);
                    if (getGroup().getNumServices() == 0) getGroup().addNode(((IpAddress) getLocalAddress()).getIpAddress().getHostAddress(), getGM().getNick(), ((IpAddress) getLocalAddress()).getPort(), obj);
                    passUpGroup();
                }
            }
        }.start();
    }

    private void sendGMHeader(GMHeader head) {
        Message msg = new Message(null);
        msg.setDest(getGM().getIpSuperPeer());
        msg.putHeader("GM", head);
        Event evt = new Event(Event.MSG, msg);
        getGM().passDown(evt);
    }

    private boolean isSuperPeerNow(IpAddress ia) {
        if (getGM().getIpSuperPeer().compare(ia) == 0) return true;
        return false;
    }

    private void initStat() {
        synchronized (this) {
            activo = true;
        }
    }
}
