package net.pms.network;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import net.pms.PMS;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to handle the UPnP traffic that makes PMS discoverable by other clients.
 * See http://upnp.org/specs/arch/UPnP-arch-DeviceArchitecture-v1.0.pdf for the specifications.
 */
public class UPNPHelper {

    private static final Logger logger = LoggerFactory.getLogger(UPNPHelper.class);

    private static final String CRLF = "\r\n";

    private static final String ALIVE = "ssdp:alive";

    private static final String UPNP_HOST = "239.255.255.250";

    private static final int UPNP_PORT = 1900;

    private static final String BYEBYE = "ssdp:byebye";

    private static Thread listener;

    private static Thread aliveThread;

    private static SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);

    private static void sendDiscover(String host, int port, String st) throws IOException {
        String usn = PMS.get().usn();
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        if (st.equals(usn)) {
            usn = "";
        } else {
            usn += "::";
        }
        String discovery = "HTTP/1.1 200 OK" + CRLF + "CACHE-CONTROL: max-age=1200" + CRLF + "DATE: " + sdf.format(new Date(System.currentTimeMillis())) + " GMT" + CRLF + "LOCATION: http://" + PMS.get().getServer().getHost() + ":" + PMS.get().getServer().getPort() + "/description/fetch" + CRLF + "SERVER: " + PMS.get().getServerName() + CRLF + "ST: " + st + CRLF + "EXT: " + CRLF + "USN: " + usn + st + CRLF + "Content-Length: 0" + CRLF + CRLF;
        sendReply(host, port, discovery);
    }

    private static void sendReply(String host, int port, String msg) throws IOException {
        try {
            DatagramSocket ssdpUniSock = new DatagramSocket();
            logger.trace("Sending this reply [" + host + ":" + port + "]: " + StringUtils.replace(msg, CRLF, "<CRLF>"));
            InetAddress inetAddr = InetAddress.getByName(host);
            DatagramPacket dgmPacket = new DatagramPacket(msg.getBytes(), msg.length(), inetAddr, port);
            ssdpUniSock.send(dgmPacket);
            ssdpUniSock.close();
        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }
    }

    public static void sendAlive() throws IOException {
        logger.debug("Sending ALIVE...");
        MulticastSocket ssdpSocket = getNewMulticastSocket();
        sendMessage(ssdpSocket, "upnp:rootdevice", ALIVE);
        sendMessage(ssdpSocket, PMS.get().usn(), ALIVE);
        sendMessage(ssdpSocket, "urn:schemas-upnp-org:device:MediaServer:1", ALIVE);
        sendMessage(ssdpSocket, "urn:schemas-upnp-org:service:ContentDirectory:1", ALIVE);
        sendMessage(ssdpSocket, "urn:schemas-upnp-org:service:ConnectionManager:1", ALIVE);
        ssdpSocket.close();
        ssdpSocket = null;
    }

    private static MulticastSocket getNewMulticastSocket() throws IOException {
        MulticastSocket ssdpSocket = new MulticastSocket();
        ssdpSocket.setReuseAddress(true);
        if (PMS.getConfiguration().getServerHostname() != null && PMS.getConfiguration().getServerHostname().length() > 0) {
            logger.trace("Searching network interface for " + PMS.getConfiguration().getServerHostname());
            NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getByName(PMS.getConfiguration().getServerHostname()));
            if (ni != null) {
                ssdpSocket.setNetworkInterface(ni);
                Enumeration<InetAddress> enm = ni.getInetAddresses();
                while (enm.hasMoreElements()) {
                    InetAddress ia = enm.nextElement();
                    if (!(ia instanceof Inet6Address)) {
                        ssdpSocket.setInterface(ia);
                        break;
                    }
                }
            }
        } else if (PMS.get().getServer().getNi() != null) {
            logger.trace("Setting multicast network interface: " + PMS.get().getServer().getNi());
            ssdpSocket.setNetworkInterface(PMS.get().getServer().getNi());
        }
        logger.trace("Sending message from multicast socket on network interface: " + ssdpSocket.getNetworkInterface());
        logger.trace("Multicast socket is on interface: " + ssdpSocket.getInterface());
        ssdpSocket.setTimeToLive(32);
        ssdpSocket.joinGroup(getUPNPAddress());
        logger.trace("Socket Timeout: " + ssdpSocket.getSoTimeout());
        logger.trace("Socket TTL: " + ssdpSocket.getTimeToLive());
        return ssdpSocket;
    }

    public static void sendByeBye() throws IOException {
        logger.info("Sending BYEBYE...");
        MulticastSocket ssdpSocket = getNewMulticastSocket();
        sendMessage(ssdpSocket, "upnp:rootdevice", BYEBYE);
        sendMessage(ssdpSocket, "urn:schemas-upnp-org:device:MediaServer:1", BYEBYE);
        sendMessage(ssdpSocket, "urn:schemas-upnp-org:service:ContentDirectory:1", BYEBYE);
        sendMessage(ssdpSocket, "urn:schemas-upnp-org:service:ConnectionManager:1", BYEBYE);
        ssdpSocket.leaveGroup(getUPNPAddress());
        ssdpSocket.close();
        ssdpSocket = null;
    }

    private static void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
        }
    }

    private static void sendMessage(DatagramSocket socket, String nt, String message) throws IOException {
        String msg = buildMsg(nt, message);
        Random rand = new Random();
        DatagramPacket ssdpPacket = new DatagramPacket(msg.getBytes(), msg.length(), getUPNPAddress(), UPNP_PORT);
        socket.send(ssdpPacket);
        sleep(rand.nextInt(1800 / 2));
        socket.send(ssdpPacket);
        sleep(rand.nextInt(1800 / 2));
    }

    private static int delay = 10000;

    public static void listen() throws IOException {
        Runnable rAlive = new Runnable() {

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(delay);
                        sendAlive();
                        if (delay == 20000) {
                            delay = 180000;
                        }
                        if (delay == 10000) {
                            delay = 20000;
                        }
                    } catch (Exception e) {
                        logger.debug("Error while sending periodic alive message: " + e.getMessage());
                    }
                }
            }
        };
        aliveThread = new Thread(rAlive, "UPNP-AliveMessageSender");
        aliveThread.start();
        Runnable r = new Runnable() {

            public void run() {
                boolean bindErrorReported = false;
                while (true) {
                    try {
                        MulticastSocket socket = new MulticastSocket(PMS.getConfiguration().getUpnpPort());
                        if (bindErrorReported) {
                            logger.warn("Finally, acquiring port " + PMS.getConfiguration().getUpnpPort() + " was successful!");
                        }
                        if (PMS.getConfiguration().getServerHostname() != null && PMS.getConfiguration().getServerHostname().length() > 0) {
                            logger.trace("Searching network interface for " + PMS.getConfiguration().getServerHostname());
                            NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getByName(PMS.getConfiguration().getServerHostname()));
                            if (ni != null) {
                                socket.setNetworkInterface(ni);
                            }
                        } else if (PMS.get().getServer().getNi() != null) {
                            logger.trace("Setting multicast network interface: " + PMS.get().getServer().getNi());
                            socket.setNetworkInterface(PMS.get().getServer().getNi());
                        }
                        socket.setTimeToLive(4);
                        socket.setReuseAddress(true);
                        socket.joinGroup(getUPNPAddress());
                        while (true) {
                            byte[] buf = new byte[1024];
                            DatagramPacket packet_r = new DatagramPacket(buf, buf.length);
                            socket.receive(packet_r);
                            String s = new String(packet_r.getData());
                            InetAddress address = packet_r.getAddress();
                            if (s.startsWith("M-SEARCH")) {
                                String remoteAddr = address.getHostAddress();
                                int remotePort = packet_r.getPort();
                                if (PMS.getConfiguration().getIpFiltering().allowed(address)) {
                                    logger.trace("Receiving a M-SEARCH from [" + remoteAddr + ":" + remotePort + "]");
                                    if (StringUtils.indexOf(s, "urn:schemas-upnp-org:service:ContentDirectory:1") > 0) {
                                        sendDiscover(remoteAddr, remotePort, "urn:schemas-upnp-org:service:ContentDirectory:1");
                                    }
                                    if (StringUtils.indexOf(s, "upnp:rootdevice") > 0) {
                                        sendDiscover(remoteAddr, remotePort, "upnp:rootdevice");
                                    }
                                    if (StringUtils.indexOf(s, "urn:schemas-upnp-org:device:MediaServer:1") > 0) {
                                        sendDiscover(remoteAddr, remotePort, "urn:schemas-upnp-org:device:MediaServer:1");
                                    }
                                    if (StringUtils.indexOf(s, PMS.get().usn()) > 0) {
                                        sendDiscover(remoteAddr, remotePort, PMS.get().usn());
                                    }
                                }
                            } else if (s.startsWith("NOTIFY")) {
                                String remoteAddr = address.getHostAddress();
                                int remotePort = packet_r.getPort();
                                logger.trace("Receiving a NOTIFY from [" + remoteAddr + ":" + remotePort + "]");
                            }
                        }
                    } catch (BindException e) {
                        if (!bindErrorReported) {
                            logger.error("Unable to bind to " + PMS.getConfiguration().getUpnpPort() + ", which means that PMS will not automatically appear on your renderer! " + "This usually means that another program occupies the port. Please " + "stop the other program and free up the port. " + "PMS will keep trying to bind to it...[" + e.getMessage() + "]");
                        }
                        bindErrorReported = true;
                        sleep(5000);
                    } catch (IOException e) {
                        logger.error("UPNP network exception", e);
                        sleep(1000);
                    }
                }
            }
        };
        listener = new Thread(r, "UPNPHelper");
        listener.start();
    }

    public static void shutDownListener() {
        listener.interrupt();
        aliveThread.interrupt();
    }

    private static String buildMsg(String nt, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("NOTIFY * HTTP/1.1" + CRLF);
        sb.append("HOST: " + UPNP_HOST + ":").append(UPNP_PORT).append(CRLF);
        sb.append("NT: ").append(nt).append(CRLF);
        sb.append("NTS: ").append(message).append(CRLF);
        if (message.equals(ALIVE)) {
            sb.append("LOCATION: http://").append(PMS.get().getServer().getHost()).append(":").append(PMS.get().getServer().getPort()).append("/description/fetch" + CRLF);
        }
        sb.append("USN: ").append(PMS.get().usn());
        if (!nt.equals(PMS.get().usn())) {
            sb.append("::").append(nt);
        }
        sb.append(CRLF);
        if (message.equals(ALIVE)) {
            sb.append("CACHE-CONTROL: max-age=1800" + CRLF);
        }
        if (message.equals(ALIVE)) {
            sb.append("SERVER: ").append(PMS.get().getServerName()).append(CRLF);
        }
        sb.append(CRLF);
        return sb.toString();
    }

    private static InetAddress getUPNPAddress() throws IOException {
        return InetAddress.getByAddress(UPNP_HOST, new byte[] { (byte) 239, (byte) 255, (byte) 255, (byte) 250 });
    }
}
