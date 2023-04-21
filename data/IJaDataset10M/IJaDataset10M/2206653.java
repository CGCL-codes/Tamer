package core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.util.StringUtils;
import sun.security.util.Debug;

public class Jabber implements Protocols {

    private XMPPConnection connection;

    private ID id;

    Message message;

    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    MsgEvent ev;

    private PropertyChangeSupport chg = new PropertyChangeSupport(this);

    private Presence oldPresence = null;

    KontaktPresence kp, oldkp = new KontaktPresence("asdf", "asds", Stan.O);

    private Stan actStan;

    private final Typ typ = Typ.JABBER;

    Jabber() {
    }

    public Jabber(ID id) {
        this.id = id;
        connection = new XMPPConnection(id.getHost());
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connect() {
        try {
            connection.connect();
            connection.login(id.getLogin(), id.getPass());
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        presence();
    }

    @Override
    public void disconnect() {
        connection.disconnect();
        System.out.println("jabber disconnect");
    }

    @Override
    public ArrayList<Kontakt> getKontakty() {
        ArrayList<Kontakt> kontakty = new ArrayList<Kontakt>();
        String stat = "";
        Roster roster = connection.getRoster();
        roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            if (entry.getStatus() != null) {
                stat = entry.getStatus().toString();
            } else {
                stat = "";
            }
            String name = parse(entry.getUser());
            if (!(entry.getName() == null)) {
                name = entry.getName();
            }
            kontakty.add(new Kontakt(parse(entry.getUser()), stat, typ, name));
        }
        return kontakty;
    }

    @Override
    public void sendMessage(String to, String s) {
        message = new Message();
        message.setTo(to);
        message.setBody(s);
        message.setType(Message.Type.chat);
        connection.sendPacket(message);
    }

    public void setId(ID id) {
        this.id = id;
    }

    /**
	 * Odbiera przychodzace wiadomosci, i przesyla je dalej przez MsgEvent
	 */
    public void odbior() {
        PacketFilter filter = new PacketTypeFilter(Message.class);
        PacketListener myListener = new PacketListener() {

            public void processPacket(Packet packet) {
                if (packet instanceof Message) {
                    Message msg = (Message) packet;
                    String from = msg.getFrom();
                    String s = from.substring(0, from.indexOf("/"));
                    if (s.equals(-1)) s = from;
                    ev = new MsgEvent(this, msg.getBody(), s, msg.getTo());
                    fireMyListener(ev);
                }
            }
        };
        connection.addPacketListener(myListener, filter);
    }

    public void addMsgEventListener(MsgListener listener) {
        listenerList.add(MsgListener.class, listener);
    }

    public void removeMsgEventListener(MsgListener listener) {
        listenerList.remove(MsgListener.class, listener);
    }

    private void fireMyListener(MsgEvent ev) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == MsgListener.class) {
                ((MsgListener) listeners[i + 1]).msgEventOccurred(ev);
            }
        }
    }

    /**
	 * nasluchuje zmian presence
	 */
    private void presence() {
        Roster roster = connection.getRoster();
        roster.addRosterListener(new RosterListener() {

            public void entriesDeleted(Collection<String> addresses) {
            }

            public void entriesUpdated(Collection<String> addresses) {
                System.out.println("updated " + addresses.size());
            }

            public void presenceChanged(Presence presence) {
                Stan stan;
                if (presence.getType() == Presence.Type.available) {
                    if (presence.getMode() == Presence.Mode.xa) stan = Stan.NIEOBECNY; else {
                        if (presence.getMode() == Presence.Mode.away) stan = Stan.ZARAZ_WRACAM; else {
                            if (presence.getMode() == Presence.Mode.available || presence.getMode() == null) stan = Stan.DOSTEPNY; else stan = Stan.DOSTEPNY;
                        }
                    }
                } else stan = Stan.ROZLACZONY;
                String tmpStat = "";
                if (!(presence.getStatus() == null)) {
                    tmpStat = presence.getStatus();
                }
                kp = new KontaktPresence(parse(presence.getFrom()), tmpStat, stan);
                chg.firePropertyChange("kp", oldkp, kp);
            }

            @Override
            public void entriesAdded(Collection<String> addresses) {
                System.out.println(addresses.size());
            }
        });
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        chg.addPropertyChangeListener(l);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        chg.removePropertyChangeListener(l);
    }

    /**
	 * wycina / z adresu
	 * @param s
	 * @return string
	 */
    private String parse(String s) {
        int idx = s.indexOf("/");
        if (idx < 0) return s;
        String ss = s.substring(0, s.indexOf("/"));
        if (ss.length() == 0) ss = s;
        return ss;
    }

    /**
	 * Dodaje kontakt do rostera
	 * @param jid
	 * user@server.com
	 * @param name
	 * user@server.com
	 */
    public void addKontakt(String jid, String name) {
        RosterPacket.Item rosterItem = new RosterPacket.Item(jid, name);
        RosterPacket rosterPacket = new RosterPacket();
        rosterPacket.setType(IQ.Type.SET);
        rosterPacket.addRosterItem(rosterItem);
        rosterPacket.toXML();
        connection.sendPacket(rosterPacket);
        Presence presence = new Presence(Presence.Type.subscribe);
        presence.setTo(jid);
        presence.setFrom(connection.getUser());
        connection.sendPacket(presence);
    }

    public void removeKontakt(String jid, String name) {
        RosterEntry entry = connection.getRoster().getEntry(jid);
        try {
            connection.getRoster().removeEntry(entry);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    public void setPresence(Stan st, String opis) {
        Presence presence;
        switch(st) {
            case DOSTEPNY:
                presence = new Presence(Presence.Type.available, opis, 0, Presence.Mode.available);
                break;
            case ROZLACZONY:
                presence = new Presence(Presence.Type.unavailable);
                presence.setStatus(opis);
                break;
            case ZARAZ_WRACAM:
                presence = new Presence(Presence.Type.available, opis, 0, Presence.Mode.away);
                presence.setStatus(opis);
                break;
            case NIEOBECNY:
                presence = new Presence(Presence.Type.available, opis, 0, Presence.Mode.xa);
                presence.setStatus(opis);
                break;
            default:
                presence = new Presence(Presence.Type.available);
                break;
        }
        connection.sendPacket(presence);
        actStan = st;
    }

    public void setPresence(Stan st) {
        Presence presence;
        switch(st) {
            case DOSTEPNY:
                presence = new Presence(Presence.Type.available);
                presence.setMode(Presence.Mode.available);
                break;
            case ROZLACZONY:
                presence = new Presence(Presence.Type.unavailable);
                break;
            case ZARAZ_WRACAM:
                presence = new Presence(Presence.Type.available);
                presence.setMode(Presence.Mode.away);
                break;
            case NIEOBECNY:
                presence = new Presence(Presence.Type.available);
                presence.setMode(Presence.Mode.xa);
                break;
            default:
                presence = new Presence(Presence.Type.available);
                break;
        }
        connection.sendPacket(presence);
        actStan = st;
    }

    public Stan getActStan() {
        return actStan;
    }

    /**
	 * @param kontakt
	 * @return
	 * Pobiera poczatkowe presence
	 */
    public void chPresence(ArrayList<Kontakt> kontakty) {
        Presence presence;
        Stan stan = null;
        for (Kontakt k : kontakty) {
            presence = connection.getRoster().getPresence(k.getAdres());
            stan = Presence2Stan(presence.getType(), presence.getMode());
            String opis = presence.getStatus();
            if (opis == null) opis = "";
            kp = new KontaktPresence(parse(presence.getFrom()), opis, stan);
            chg.firePropertyChange("kp", oldkp, kp);
        }
    }

    /**
	 * @param t
	 * @param m
	 * @return
	 * Z Presence.type & mode zwraca Stan
	 */
    private Stan Presence2Stan(Presence.Type t, Presence.Mode m) {
        Stan st = null;
        switch(t) {
            case available:
                if (m != null) {
                    switch(m) {
                        case available:
                            st = Stan.DOSTEPNY;
                            break;
                        case away:
                            st = Stan.ZARAZ_WRACAM;
                            break;
                        case xa:
                            st = Stan.NIEOBECNY;
                            break;
                        case dnd:
                            st = Stan.NIEOBECNY;
                            break;
                        case chat:
                            st = Stan.DOSTEPNY;
                            break;
                        default:
                            break;
                    }
                } else {
                    st = Stan.DOSTEPNY;
                }
                break;
            case unavailable:
                st = Stan.ROZLACZONY;
                break;
            default:
                break;
        }
        return st;
    }

    public Typ getTyp() {
        return typ;
    }

    @Override
    public boolean isConnected() {
        return connection.isConnected();
    }
}
