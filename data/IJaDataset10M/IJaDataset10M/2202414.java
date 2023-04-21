package org.pbdb.manager;

/**
 *
 * @author  Richard A. Begg
 */
public class PBDBPlayerInfo {

    public static final String AUTHENTICATED = "Authenticated";

    public static final String ON_SERVER = "On Server";

    public static final String NO_CLIENT = "No Client";

    public String pbid;

    public String name;

    public String adr;

    public String wonid;

    public String hlid;

    public String time;

    public String frags;

    public String state;

    public String serverIp;

    public String toString() {
        return (name + " [" + wonid + "]");
    }
}
