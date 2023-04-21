package rtjdds.rtps.transport;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import rtjdds.util.GlobalProperties;

public class ReceiverFactory {

    /**
	 * 
	 * @param domainId
	 * @param participantId
	 * @param priority
	 * @return
	 * @throws IOException 
	 */
    public static UDPMulticastReceiver createBuiltinMulticastReceiver(int domainId) throws IOException {
        int port = GlobalProperties.PB + GlobalProperties.DG * domainId + GlobalProperties.d0;
        String ip = GlobalProperties.DEFAULT_MULTICAST_ADDRESS;
        int bufferSize = GlobalProperties.BUFFER_SIZE;
        InetAddress ipaddr = InetAddress.getByName(ip);
        InetSocketAddress sock = new InetSocketAddress(ipaddr, port);
        UDPMulticastReceiver rcvr = new UDPMulticastReceiver(sock, bufferSize);
        return rcvr;
    }

    /**
	 * 
	 * @param domainId
	 * @param participantId
	 * @return
	 * @throws IOException
	 */
    public static UDPUnicastReceiver createBuiltinUnicastReceiver(String ip, int domainId, int participantId) throws IOException {
        int port = GlobalProperties.PB + GlobalProperties.DG * domainId + GlobalProperties.d1 + GlobalProperties.PG * participantId;
        int bufferSize = GlobalProperties.BUFFER_SIZE;
        InetAddress ipaddr = InetAddress.getByName(ip);
        InetSocketAddress sock = new InetSocketAddress(ipaddr, port);
        UDPUnicastReceiver rcvr = new UDPUnicastReceiver(sock, bufferSize);
        return rcvr;
    }

    /**
	 * 
	 * @param domainId
	 * @param participantId
	 * @param priority
	 * @return
	 * @throws IOException 
	 */
    public static UDPMulticastReceiver createUserMulticastReceiver(int domainId, int priority) throws IOException {
        int port = GlobalProperties.PB + GlobalProperties.DG * domainId + GlobalProperties.d2 + priority;
        String ip = GlobalProperties.DEFAULT_MULTICAST_ADDRESS;
        int bufferSize = GlobalProperties.BUFFER_SIZE;
        InetAddress ipaddr = InetAddress.getByName(ip);
        InetSocketAddress sock = new InetSocketAddress(ipaddr, port);
        UDPMulticastReceiver rcvr = new UDPMulticastReceiver(sock, bufferSize);
        return rcvr;
    }

    /**
	 * 
	 * @param domainId
	 * @param participantId
	 * @return
	 * @throws IOException
	 */
    public static UDPUnicastReceiver createUserUnicastReceiver(String ip, int domainId, int participantId, int priority) throws IOException {
        int port = GlobalProperties.PB + GlobalProperties.DG * domainId + GlobalProperties.d1 + GlobalProperties.PG * participantId + priority;
        int bufferSize = GlobalProperties.BUFFER_SIZE;
        InetAddress ipaddr = InetAddress.getByName(ip);
        InetSocketAddress sock = new InetSocketAddress(ipaddr, port);
        UDPUnicastReceiver rcvr = new UDPUnicastReceiver(sock, bufferSize);
        return rcvr;
    }
}
