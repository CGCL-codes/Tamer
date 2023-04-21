package thirdParty.jembench.ejip;

/**
 * Represents a single TCP connection.
 * 
 * 
 * 
 * @author Martin
 * 
 */
public class TcpConnection {

    /**
	 * State of the TCP connection.
	 */
    int state;

    int localIP;

    int remoteIP;

    int localPort;

    int remotePort;

    /**
	 * The next expected receive sequence number.
	 * Without the length of the incoming package.
	 */
    int rcvNxt;

    /**
	 * The last sent sequence number.
	 */
    int sndNxt;

    /**
	 * The outstandig packet. We only allow one packet on the fly.
	 */
    Packet outStanding;

    /**
	 * Timeout for retransmit of the outstanding packet. Will be
	 * decremented and retransmit on 0
	 */
    int timeout;

    /**
	 * Retransmission counter.
	 */
    int retryCnt;

    /**
	 * We shut down the connection after being idle for too long.
	 */
    int idleTime;

    /**
	 * Maximum number of active TCP connections
	 */
    static final int CNT = 10;

    static TcpConnection[] connections;

    private static Object mutex = new Object();

    static {
        connections = new TcpConnection[CNT];
        for (int i = 0; i < CNT; ++i) {
            connections[i] = new TcpConnection();
        }
    }

    private TcpConnection() {
        state = Tcp.FREE;
    }

    public static TcpConnection findConnection(Packet p) {
        int[] buf = p.buf;
        int dstPort = buf[Tcp.HEAD];
        int srcPort = dstPort >>> 16;
        dstPort &= 0xffff;
        int src = buf[Ip.SOURCE];
        int dest = buf[Ip.DESTINATION];
        return findConnection(src, srcPort, dest, dstPort);
    }

    public static TcpConnection findConnection(int src, int srcPort, int dest, int dstPort) {
        TcpConnection free = null;
        TcpConnection conn = null;
        synchronized (mutex) {
            for (int i = 0; i < CNT; ++i) {
                TcpConnection tc = connections[i];
                if (tc.state != Tcp.FREE) {
                    if (dstPort == tc.localPort && srcPort == tc.remotePort && src == tc.remoteIP && dest == tc.localIP) {
                        conn = tc;
                        break;
                    }
                } else {
                    if (free == null) {
                        free = tc;
                    }
                }
            }
            if (conn == null) {
                conn = free;
                if (free != null) {
                    free.state = Tcp.CLOSED;
                    free.localPort = dstPort;
                    free.remotePort = srcPort;
                    free.remoteIP = src;
                    free.localIP = dest;
                }
            }
            if (conn != null) {
                conn.idleTime = 0;
            }
        }
        int cnt = 0;
        for (int i = 0; i < CNT; ++i) {
            if (connections[i].state != Tcp.FREE) {
                ++cnt;
            }
        }
        if (Logging.LOG) {
            Logging.wr("getCon: con in use: ");
            Logging.intVal(cnt);
            Logging.lf();
        }
        return conn;
    }

    public static TcpConnection getFreeConnection() {
        return null;
    }

    /**
	 * Close the connection and return any outstanding packet to the pool.
	 *
	 */
    public void close(Ejip ejip) {
        synchronized (mutex) {
            if (outStanding != null) {
                Packet os = outStanding;
                outStanding = null;
                os.isTcpOnFly = false;
                ejip.returnPacket(os);
            }
            state = Tcp.FREE;
            outStanding = null;
        }
    }
}
