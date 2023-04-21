package org.jnerve;

import java.util.*;
import org.jnerve.util.*;

/** Banlist facility class. Centralizes tracking of banned ips and nicknames.
  */
public class Banlist {

    private Hashtable bannedIps = new Hashtable();

    private Hashtable bannedNicks = new Hashtable();

    public void addBannedIP(long ipAddress, String adminNick, String reason) {
        bannedIps.put(new Long(ipAddress), new BanRecord(ipAddress, adminNick, reason, getUTCTimeInSeconds()));
    }

    public void addBannedNick(String nick) {
        bannedNicks.put(nick, new Object());
    }

    public boolean isBannedIP(long ipAddress) {
        Long ip = new Long(ipAddress);
        if (bannedIps.get(ip) != null) {
            return true;
        }
        return false;
    }

    public boolean isBannedNick(String s) {
        if (bannedNicks.get(s) != null) {
            return true;
        }
        return false;
    }

    public void removeBannedIP(long ipAddress) {
        bannedNicks.remove(new Long(ipAddress));
    }

    public void removeBannedNick(String s) {
        bannedNicks.remove(s);
    }

    public BanRecord[] getBannedIPs() {
        int numIPBans = bannedIps.size();
        if (numIPBans == 0) return null;
        BanRecord[] records = new BanRecord[numIPBans];
        Enumeration ipEnum = bannedIps.keys();
        int x = 0;
        while (ipEnum.hasMoreElements()) {
            records[x] = (BanRecord) bannedIps.get((Long) ipEnum.nextElement());
            x++;
        }
        return records;
    }

    public String[] getBannedNicks() {
        int numNickBans = bannedNicks.size();
        if (numNickBans == 0) return null;
        String[] nicks = new String[numNickBans];
        Enumeration nickEnum = bannedNicks.keys();
        int x = 0;
        while (nickEnum.hasMoreElements()) {
            nicks[x] = (String) nickEnum.nextElement();
            x++;
        }
        return nicks;
    }

    public static final long getUTCTimeInSeconds() {
        return (long) System.currentTimeMillis() / (long) 1000;
    }
}
