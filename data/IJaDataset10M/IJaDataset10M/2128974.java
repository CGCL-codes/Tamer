package alterchat;

import java.util.*;
import java.security.*;

public class Core {

    private Hashtable rooms = null;

    public Core() {
        rooms = new Hashtable();
        rooms.put("alterchat", new Room("alterchat", 50));
    }

    public Enumeration getRoomsKeys() {
        return rooms.keys();
    }

    public void addRoom(String room, int maxusers) {
        rooms.put(room, new Room(room, maxusers));
    }

    public void delRoom(String room) {
        rooms.remove(room);
    }

    public String authUser(String nick, String room) {
        String sessId = null;
        Room r = (Room) rooms.get(room);
        if (r != null) synchronized (r) {
            if (!r.existNick(nick) && nick.length() < 21 && !r.isFull() && nick.charAt(0) != ' ') {
                long system = System.currentTimeMillis();
                byte[] time = new Long(system).toString().getBytes();
                try {
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    md5.update(time);
                    sessId = toHex(md5.digest());
                } catch (Exception e) {
                    System.err.println("Unable to calculate MD5 Digests.");
                    System.exit(0);
                }
                User u = new User(nick, room, sessId);
                r.addWaitingUser(u);
                return sessId;
            }
        }
        return sessId;
    }

    public boolean active(Worker w, String sessId, String room) {
        if (sessId != null) {
            Room r = (Room) rooms.get(room);
            if (r != null) synchronized (r) {
                return r.makeActive(sessId, w);
            }
        }
        return false;
    }

    public void sendMsgRoom(String sessId, String msg, String room) {
        Room r = (Room) rooms.get(room);
        if (r != null) synchronized (r) {
            r.sendMsgRoom(sessId, msg);
        }
    }

    public void sendMsgUser(String sessId, String msg, String room, String nick) {
        Room r = (Room) rooms.get(room);
        if (r != null) synchronized (r) {
            r.sendMsgUser(sessId, msg, nick);
        }
    }

    public void sendPrivMsgUser(String sessId, String msg, String room, String nick) {
        Room r = (Room) rooms.get(room);
        if (r != null) synchronized (r) {
            r.sendPrivMsgUser(sessId, msg, nick);
        }
    }

    public void disconnect(String room, int hashcode) {
        Room r = (Room) rooms.get(room);
        if (r != null) synchronized (r) {
            r.disconnect(hashcode);
        }
    }

    public String[] getNickList(String room) {
        Room r = (Room) rooms.get(room);
        if (r != null) synchronized (r) {
            return r.getNickList();
        }
        return null;
    }

    public boolean existNick(String room, String nick) {
        Room r = (Room) rooms.get(room);
        if (r != null) synchronized (r) {
            return r.existNick(nick);
        }
        return false;
    }

    public boolean existSessId(String room, String sessId) {
        Room r = (Room) rooms.get(room);
        if (r != null) synchronized (r) {
            return r.existSessId(sessId);
        }
        return false;
    }

    public String getStyleString(String room) {
        Room r = (Room) rooms.get(room);
        if (r != null) synchronized (r) {
            return r.getStyle().getCssString();
        }
        return null;
    }

    public Hashtable getStyle(String room) {
        Room r = (Room) rooms.get(room);
        if (r != null) synchronized (r) {
            return r.getStyle().getCss();
        }
        return null;
    }

    public void setStyle(String room, Hashtable h) {
        Room r = (Room) rooms.get(room);
        if (r != null) synchronized (r) {
            r.getStyle().setCss(h);
        }
    }

    public void kickUser(String room, String nick, String reason) {
        Room r = (Room) rooms.get(room);
        if (r != null) synchronized (r) {
            r.kickUser(nick, reason);
        }
    }

    private String toHex(byte[] digest) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            buff.append(Integer.toHexString((int) digest[i] & 0x00ff));
        }
        return buff.toString();
    }
}
