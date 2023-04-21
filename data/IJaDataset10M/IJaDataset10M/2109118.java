package jbe.ejip;

/**
*	Packet buffer handling for a minimalistic TCP/IP stack.
*
*/
public class Packet {

    public static final int MAX = 40;

    public static final int MAXW = 40 / 4;

    public static final int MAXLLH = 7;

    private static final int CNT = 2;

    /** interface source/destination */
    public LinkLayer interf;

    /** place for link layer data */
    public int[] llh;

    /** ip datagram */
    public int[] buf;

    /** length in bytes */
    public int len;

    /** usage of packet */
    private int status;

    public static final int FREE = 0;

    public static final int ALLOC = 1;

    public static final int SND = 2;

    public static final int RCV = 3;

    private static Object monitor;

    private Packet() {
        llh = new int[MAXLLH];
        buf = new int[MAXW];
        len = 0;
        status = FREE;
        interf = null;
    }

    private static boolean initOk;

    private static Packet[] packets;

    public static void init() {
        if (initOk) return;
        monitor = new Object();
        synchronized (monitor) {
            initOk = true;
            packets = new Packet[CNT];
            for (int i = 0; i < CNT; ++i) {
                packets[i] = new Packet();
            }
        }
    }

    private static void dbg() {
        synchronized (monitor) {
            Dbg.wr('|');
            for (int i = 0; i < CNT; ++i) {
                Dbg.wr('0' + packets[i].status);
            }
            Dbg.wr('|');
        }
    }

    /**
*	Request a packet of a given type from the pool and set new type.
*/
    public static Packet getPacket(int type, int newType) {
        int i;
        Packet p;
        if (!initOk) {
            init();
        }
        synchronized (monitor) {
            for (i = 0; i < CNT; ++i) {
                if (packets[i].status == type) {
                    break;
                }
            }
            if (i == CNT) {
                if (type == FREE) Dbg.wr('!');
                return null;
            }
            packets[i].status = newType;
            p = packets[i];
        }
        return p;
    }

    /**
*	Request a packet of a given type from the pool and set new type and source.
*/
    public static Packet getPacket(int type, int newType, LinkLayer s) {
        int i;
        Packet p;
        if (!initOk) {
            init();
        }
        synchronized (monitor) {
            for (i = 0; i < CNT; ++i) {
                if (packets[i].status == type) {
                    break;
                }
            }
            if (i == CNT) {
                if (type == FREE) Dbg.wr('!');
                return null;
            }
            packets[i].status = newType;
            packets[i].interf = s;
            p = packets[i];
        }
        return p;
    }

    /**
*	Request a packet of a given type and source from the pool and set new type.
*/
    public static Packet getPacket(LinkLayer s, int type, int newType) {
        int i;
        Packet p;
        if (!initOk) {
            init();
        }
        synchronized (monitor) {
            for (i = 0; i < CNT; ++i) {
                if (packets[i].status == type && packets[i].interf == s) {
                    break;
                }
            }
            if (i == CNT) {
                return null;
            }
            packets[i].status = newType;
            p = packets[i];
        }
        return p;
    }

    public void setStatus(int v) {
        synchronized (monitor) {
            status = v;
        }
    }

    public int getStatus() {
        return status;
    }

    public LinkLayer getLinkLayer() {
        return interf;
    }
}
