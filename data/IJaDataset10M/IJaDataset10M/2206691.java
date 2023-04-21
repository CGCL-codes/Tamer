package client.util;

import client.Main;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * This class provides all information about the user
 * @author danon
 */
public class UserProfile implements Comparable<UserProfile> {

    private HashMap<String, String> data;

    private String comp = "id";

    public UserProfile(int id) {
        comp = "id";
        data = new HashMap<String, String>();
        data.put("id", "" + id);
    }

    public UserProfile(int id, String comp) {
        this.comp = comp;
        data = new HashMap<String, String>();
        data.put("id", "" + id);
    }

    public synchronized int getId() {
        return Integer.parseInt(data.get("id"));
    }

    public synchronized String setProperty(String prop, String val) {
        return data.put(prop, val);
    }

    public synchronized String getProperty(String prop) {
        return data.get(prop);
    }

    public synchronized int compareTo(UserProfile o) {
        if (comp.equals("id")) {
            Integer myId = new Integer(getId());
            Integer oId = new Integer(o.getId());
            return myId.compareTo(oId);
        } else if (comp != null) {
            String mine = getProperty(comp);
            String others = getProperty(comp);
            if (mine == null) return -1;
            return mine.compareToIgnoreCase(others);
        } else return hashCode() - o.hashCode();
    }

    @Override
    public synchronized String toString() {
        return data.get("pub_nickname");
    }

    @Override
    public synchronized boolean equals(Object o) {
        if (o instanceof UserProfile) return this.compareTo((UserProfile) o) == 0; else return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.data != null ? this.data.hashCode() : 0);
        return hash;
    }

    /**
     * Updates all properties of user profile (data are retrieved from the server).
     * If server command failed, it does not make any changes.
     * @return <code>true</code> if profile was updated, otherwise - <code>false</code>.
     */
    public synchronized boolean update() {
        String r[] = Main.server.commandAndResponse(1000, "profile by id", getId() + "", Main.server.secret);
        if (r[0] == null || !r[0].equals("SUCCESS")) return false;
        for (int j = 1; j < r.length; j++) {
            if (r[j] != null && r[j].indexOf(':') > 0) {
                r[j] = r[j].trim();
                StringTokenizer stk = new StringTokenizer(r[j], ":");
                String k = stk.nextToken();
                String v = "";
                if (stk.hasMoreTokens()) v = stk.nextToken();
                data.put(k, v);
            }
        }
        return true;
    }
}
