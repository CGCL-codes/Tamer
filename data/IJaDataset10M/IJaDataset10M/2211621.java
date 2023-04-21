package net.sourceforge.jaulp.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * Utility class for getting the ip from hosts.
 * 
 * @version 1.0
 * @author Asterios Raptis
 */
public class GetIP {

    /**
     * Gets the ip address from the given InetAddress object as a String.
     *
     * @param inetAddress the inet address
     * @return Returns the ip address from the given InetAddress as a String.
     */
    public static String getIP(final InetAddress inetAddress) {
        String ip = "";
        ip = inetAddress.getHostAddress();
        if (ip.equals("")) {
            final byte[] ipAddressInBytes = inetAddress.getAddress();
            for (int i = 0; i < ipAddressInBytes.length; i++) {
                if (i > 0) {
                    ip += ".";
                }
                ip += ipAddressInBytes[i] & 0xFF;
            }
        }
        return ip;
    }

    /**
     * Gets the ip address as a byte array. Wrappes the method getAddress() from
     * the InetAddress.
     *
     * @param inetAddress the inet address
     * @return Returns the ip address as a byte array.
     */
    public static byte[] getIPAsByte(final InetAddress inetAddress) {
        return inetAddress.getAddress();
    }

    /**
     * Gets the local ip address as a String.
     *
     * @return Returns the local ip address as a String or null.
     * @throws UnknownHostException the unknown host exception
     */
    public static String getLocalIP() throws UnknownHostException {
        return getIP(InetAddress.getLocalHost());
    }

    /**
     * Gets the local ip address as a byte array.
     * 
     * @return Returns the local ip address as a byte array.
     */
    public static byte[] getLocalIPAsByte() {
        byte[] ipAddressInBytes = null;
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
            ipAddressInBytes = inetAddress.getAddress();
        } catch (final UnknownHostException e) {
            e.printStackTrace();
        }
        return ipAddressInBytes;
    }

    /**
     * Gets the InetAddress object from the local host from a ServerSocket
     * object.
     * 
     * @param port
     *            the local TCP port
     * @param backlog
     *            the listen backlog
     * @return Returns the InetAddress object from the local host from a
     *         ServerSocket object.
     */
    public static InetAddress getLocalIPFromServerSocket(final int port, final int backlog) {
        InetAddress inetAddress = null;
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port, backlog, InetAddress.getLocalHost());
            inetAddress = socket.getInetAddress();
            socket.close();
        } catch (final UnknownHostException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            SocketUtils.closeServerSocket(socket);
        }
        return inetAddress;
    }

    /**
     * Gets the ip address from the local host as String. It use a ServerSocket
     * object to get it.
     * 
     * @return Returns the ip from the local host from a ServerSocket object as
     *         String.
     */
    public static String getLocalIPFromServerSocketAsString() {
        InetAddress inetAddress = null;
        inetAddress = GetIP.getLocalIPFromServerSocket(10000, 20000);
        return GetIP.getIP(inetAddress);
    }
}
