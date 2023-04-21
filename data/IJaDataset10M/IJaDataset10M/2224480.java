package org.jnetpcap;

import java.io.IOException;
import java.util.List;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.packet.JScanner;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

/**
 * A Pcap utility class which provides certain additional and convenience
 * methods.
 * 
 * @since 1.2
 * @author Mark Bednarczyk
 * @author Sly Technologies, Inc.
 */
public final class PcapUtils {

    /**
	 * Runs the dispatch function in a background thread. The function returns
	 * immediately and returns a PcapTask from which the user can interact with
	 * the background task.
	 * 
	 * @param pcap
	 *          an open pcap object
	 * @param cnt
	 *          number of packets to capture and exit, 0 for infinate
	 * @param handler
	 *          user supplied callback handler
	 * @param data
	 *          opaque, user supplied data object dispatched back to the handler
	 * @return a task object which allows interaction with the underlying capture
	 *         loop and thread
	 */
    public static <T> PcapTask<T> dispatchInBackground(Pcap pcap, int cnt, final ByteBufferHandler<T> handler, final T data) {
        return new PcapTask<T>(pcap, cnt, data) {

            public void run() {
                int remaining = count;
                while (remaining > 0) {
                    if (remaining != 0) {
                        Thread.yield();
                    }
                    this.result = this.pcap.dispatch(count, handler, data);
                    if (result < 0) {
                        break;
                    }
                    remaining -= result;
                }
            }
        };
    }

    /**
	 * Runs the dispatch function in a background thread. The function returns
	 * immediately and returns a PcapTask from which the user can interact with
	 * the background task.
	 * 
	 * @param pcap
	 *          an open pcap object
	 * @param cnt
	 *          number of packets to capture and exit, 0 for infinate
	 * @param handler
	 *          user supplied callback handler
	 * @param data
	 *          opaque, user supplied data object dispatched back to the handler
	 * @return a task object which allows interaction with the underlying capture
	 *         loop and thread
	 */
    public static <T> PcapTask<T> dispatchInBackground(Pcap pcap, int cnt, final JBufferHandler<T> handler, final T data) {
        return new PcapTask<T>(pcap, cnt, data) {

            public void run() {
                int remaining = count;
                while (remaining > 0) {
                    if (remaining != 0) {
                        Thread.yield();
                    }
                    this.result = this.pcap.dispatch(count, handler, data);
                    if (result < 0) {
                        break;
                    }
                    remaining -= result;
                }
            }
        };
    }

    /**
	 * Retrieves a network hardware address or MAC for a network interface
	 * 
	 * @see Pcap#findAllDevs(List, StringBuilder)
	 * @param netif
	 *          network device as retrieved from Pcap.findAllDevs().
	 * @return network interface hardware address or null if unable to retrieve it
	 * @throws IOException
	 *           any communication errors
	 */
    public static byte[] getHardwareAddress(PcapIf netif) throws IOException {
        return getHardwareAddress(netif.getName());
    }

    /**
	 * Retrieves a network hardware address or MAC for a network interface
	 * 
	 * @param device
	 *          network interface name
	 * @return network interface hardware address or null if unable to retrieve it
	 * @throws IOException
	 *           any communication errors
	 */
    public static native byte[] getHardwareAddress(String device) throws IOException;

    /**
	 * Runs the loop function in a background thread. The function returns
	 * immediately and returns a PcapTask from which the user can interact with
	 * the background task.
	 * 
	 * @param pcap
	 *          an open pcap object
	 * @param cnt
	 *          number of packets to capture and exit, 0 for infinate
	 * @param handler
	 *          user supplied callback handler
	 * @param data
	 *          opaque, user supplied data object dispatched back to the handler
	 * @return a task object which allows interaction with the underlying capture
	 *         loop and thread
	 */
    public static <T> PcapTask<T> loopInBackground(Pcap pcap, int cnt, final ByteBufferHandler<T> handler, final T data) {
        return new PcapTask<T>(pcap, cnt, data) {

            public void run() {
                this.result = pcap.loop(count, handler, data);
            }
        };
    }

    /**
	 * Runs the loop function in a background thread. The function returns
	 * immediately and returns a PcapTask from which the user can interact with
	 * the background task.
	 * 
	 * @param pcap
	 *          an open pcap object
	 * @param cnt
	 *          number of packets to capture and exit, 0 for infinate
	 * @param handler
	 *          user supplied callback handler
	 * @param data
	 *          opaque, user supplied data object dispatched back to the handler
	 * @return a task object which allows interaction with the underlying capture
	 *         loop and thread
	 */
    public static <T> PcapTask<T> loopInBackground(Pcap pcap, int cnt, final JBufferHandler<T> handler, final T data) {
        return new PcapTask<T>(pcap, cnt, data) {

            public void run() {
                this.result = pcap.loop(count, handler, data);
            }
        };
    }

    public static <T> int injectLoop(int cnt, int id, PcapPacketHandler<T> handler, T user, PcapPacket packet) {
        return injectLoop(cnt, id, handler, user, packet, packet.getState(), packet.getCaptureHeader(), JScanner.getThreadLocal());
    }

    /**
	 * A special method invokes the real native loop callback with fake pcap
	 * packets. The loop is used to test for memory leaks and performance that
	 * bypasses libpcap calls.
	 * 
	 * @param packet
	 *          packet to inject into the loop and callback
	 * @param flags
	 *          number of packets to inject or -1 for inifinite
	 * @param handler
	 *          handler to dispatch the injected packet to
	 * @param user
	 *          user data
	 * @param <T>
	 *          type of user data
	 * @param header
	 *          pcap header for this packet
	 * @return number of packets injected
	 */
    private static native <T> int injectLoop(int cnt, int id, PcapPacketHandler<T> handler, T user, PcapPacket packet, JPacket.State state, PcapHeader header, JScanner scanner);

    private PcapUtils() {
    }
}
