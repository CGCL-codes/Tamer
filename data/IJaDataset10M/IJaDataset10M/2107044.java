package ade;

import ade.ADEGlobals;
import java.io.Serializable;
import java.util.HashSet;

/**
Utility class for passing minimal information (a subset of that
contained in the {@link ade.ADEServerInfo ADEServerInfo} class)
back and forth between servers. Mostly for use to pass information
from a server to an {@link ade.ADERegistryImpl ADERegistryImpl} for
use in the {@link ade.ADEServerInfo#update} method).
*/
public class ADEMiniServerInfo implements Serializable {

    private static String prg = "ADEMiniServerInfo";

    /** Server id (in <tt>type$name</tt> format). */
    public String id;

    public HashSet<String> userAccess;

    public HashSet<String> onlyonhosts;

    public int recoveryMultiplier = 1;

    public ADEGlobals.ServerState state;

    public ADEGlobals.RecoveryState recState;

    public HashSet<String> clients;

    public HashSet<String> servers;

    /** Constructor that allows manual filling in of data. */
    public ADEMiniServerInfo() {
        id = new String();
        userAccess = new HashSet<String>();
        onlyonhosts = new HashSet<String>();
        clients = new HashSet<String>();
        servers = new HashSet<String>();
    }

    /** Constructor with only the id, state, and recovery state. */
    public ADEMiniServerInfo(String i, ADEGlobals.ServerState s, ADEGlobals.RecoveryState rs) {
        id = i;
        state = s;
        recState = rs;
    }

    /** Constructor with all information parameterized. */
    public ADEMiniServerInfo(String i, HashSet<String> ua, HashSet<String> ooh, int rm, ADEGlobals.ServerState st, ADEGlobals.RecoveryState rst, HashSet<String> cls, HashSet<String> svs) {
        id = i;
        userAccess = new HashSet<String>();
        for (String s : ua) {
            userAccess.add(s);
        }
        onlyonhosts = new HashSet<String>();
        for (String s : ua) {
            onlyonhosts.add(s);
        }
        recoveryMultiplier = rm;
        state = st;
        recState = rst;
        clients = new HashSet<String>();
        for (String s : cls) {
            clients.add(s);
        }
        servers = new HashSet<String>();
        for (String s : svs) {
            servers.add(s);
        }
    }

    /** Print the data contained herein. */
    public void print() {
        System.out.println(toString());
    }

    /** Return the string representation of this object. */
    public String toString() {
        StringBuilder sb = new StringBuilder(prg);
        sb.append(": ");
        sb.append(id);
        if (state != null) {
            sb.append("\n\tState: ");
            sb.append(state);
        }
        if (recState != null) {
            sb.append("\n\tRecovery State: ");
            sb.append(recState);
        }
        sb.append("\n\tRecovery mult: ");
        sb.append(recoveryMultiplier);
        if (clients != null) {
            sb.append("\n\tHave refs: [");
            if (clients.size() > 0) {
                for (String s : clients) {
                    sb.append(s);
                    sb.append(", ");
                }
                sb.setLength(sb.length() - 2);
            } else {
                sb.append("none");
            }
        }
        if (servers != null) {
            sb.append("]\n\tGave refs: [");
            if (servers.size() > 0) {
                for (String s : servers) {
                    sb.append(s);
                    sb.append(", ");
                }
                sb.setLength(sb.length() - 2);
            } else {
                sb.append("none");
            }
        }
        sb.append("]\n");
        return sb.toString();
    }
}
