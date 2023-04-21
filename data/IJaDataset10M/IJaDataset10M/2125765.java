package lpr.minikazaa.util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lpr.minikazaa.minikazaaclient.Query;

/**
 *
 * @author Andrea Di Grazia, Massimiliano Giovine
 * @date 22-set-2008
 * @file NetUtil.java
 */
public class NetUtil {

    public static InetAddress getAddress() throws SocketException {
        InetAddress ipv6_addr = null;
        InetAddress ipv4_addr = null;
        try {
            System.out.println("Interfaccia controllata 0");
            NetworkInterface ni = NetworkInterface.getByName("eth0");
            Enumeration<InetAddress> ia_en = ni.getInetAddresses();
            ipv6_addr = ia_en.nextElement();
            System.out.println(ipv6_addr.toString());
            ipv4_addr = ia_en.nextElement();
            System.out.println(ipv4_addr.toString());
            return ipv4_addr;
        } catch (NullPointerException ex0) {
            try {
                System.out.println("Interfaccia controllata 1");
                NetworkInterface ni = NetworkInterface.getByName("eth1");
                Enumeration<InetAddress> ia_en = ni.getInetAddresses();
                ipv6_addr = ia_en.nextElement();
                System.out.println(ipv6_addr.toString());
                ipv4_addr = ia_en.nextElement();
                System.out.println(ipv4_addr.toString());
                return ipv4_addr;
            } catch (NullPointerException ex1) {
                try {
                    System.out.println("Interfaccia controllata 2");
                    NetworkInterface ni = NetworkInterface.getByName("eth2");
                    Enumeration<InetAddress> ia_en = ni.getInetAddresses();
                    ipv6_addr = ia_en.nextElement();
                    System.out.println(ipv6_addr.toString());
                    ipv4_addr = ia_en.nextElement();
                    System.out.println(ipv4_addr.toString());
                    return ipv4_addr;
                } catch (NullPointerException ex2) {
                    return null;
                } catch (NoSuchElementException such1) {
                    return ipv6_addr;
                }
            } catch (NoSuchElementException such2) {
                return ipv6_addr;
            }
        } catch (NoSuchElementException such3) {
            return ipv6_addr;
        }
    }

    public static void sendQuery(Query q) {
        try {
            Socket next_node_sock = new Socket(q.getReceiver().getIaNode(), q.getReceiver().getDoor());
            ObjectOutputStream output_query = new ObjectOutputStream(next_node_sock.getOutputStream());
            output_query.writeObject(q);
            next_node_sock.close();
        } catch (IOException ex) {
            Logger.getLogger(NetUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
