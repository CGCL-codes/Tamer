package javajabberc.iqns;

import java.util.Vector;

public class RosterData {

    public static final int UNKNOWN = 0;

    public static final int TO = 1;

    public static final int FROM = 2;

    public static final int BOTH = 3;

    public static final int NONE = 4;

    public static final int REMOVE = 5;

    public static final int SUBSCRIBE = 1;

    public static final int UNSUBSCRIBE = 2;

    public String jid = null;

    public String name = null;

    public int subscription = 0;

    public int ask = 0;

    public Vector groups = new Vector();
}
