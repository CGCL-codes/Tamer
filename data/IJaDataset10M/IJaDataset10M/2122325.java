package jaxlib.net.socket;

import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: SocketPerformancePreferences.java 2791 2010-03-29 08:45:11Z joerg_wassmer $
 */
public final class SocketPerformancePreferences extends Object implements Serializable {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    /**
   * @serial
   * @since JaXLib 1.0
   */
    final int bandwidth;

    /**
   * @serial
   * @since JaXLib 1.0
   */
    final int connectionTime;

    /**
   * @serial
   * @since JaXLib 1.0
   */
    final int latency;

    public SocketPerformancePreferences(final int connectionTime, final int latency, final int bandwidth) {
        super();
        this.bandwidth = bandwidth;
        this.connectionTime = connectionTime;
        this.latency = latency;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true; else if (!(o instanceof SocketPerformancePreferences)) return false; else {
            SocketPerformancePreferences b = (SocketPerformancePreferences) o;
            return ((this.bandwidth == b.bandwidth) && (this.connectionTime == b.connectionTime) && (this.latency == b.latency));
        }
    }

    @Override
    public int hashCode() {
        int h = 31 + this.bandwidth;
        h = (31 * h) + this.connectionTime;
        return (31 * h) + this.latency;
    }

    public final void configureSocket(final ServerSocket socket) {
        socket.setPerformancePreferences(this.connectionTime, this.latency, this.bandwidth);
    }

    public final void configureSocket(final Socket socket) {
        socket.setPerformancePreferences(this.connectionTime, this.latency, this.bandwidth);
    }

    public final int getBandwidth() {
        return this.bandwidth;
    }

    public final int getConnectionTime() {
        return this.connectionTime;
    }

    public final int getLatency() {
        return this.latency;
    }
}
