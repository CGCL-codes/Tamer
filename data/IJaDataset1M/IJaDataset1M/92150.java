package network;

import javax.swing.Timer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.Enumeration;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NettyBooFinder {

    private static final int CQ_PORT = 13246;

    private static final String MULTICAST_GROUP_ADDRESS = "225.0.0.27";

    private static final int RESPONSE_PORT = 61802;

    private MulticastSocket multicastSocket;

    private CopyOnWriteArrayList<FinderListener> finderListeners = new CopyOnWriteArrayList<FinderListener>();

    private HashMap<InetAddress, NettyBoo> nettyBoos = new HashMap<InetAddress, NettyBoo>();

    private java.util.Timer timer = new java.util.Timer("PeriodicPinger");

    public NettyBooFinder() {
        try {
            this.multicastSocket = new MulticastSocket(CQ_PORT);
            this.multicastSocket.joinGroup(InetAddress.getByName(MULTICAST_GROUP_ADDRESS));
            this.listenForPings();
            timer.scheduleAtFixedRate(new TimerTask() {

                public void run() {
                    broadcastPing();
                }
            }, 0, 3000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastPing() {
        try {
            byte[] pingBuffer = new byte[1024];
            final DatagramPacket pingingPacket = new DatagramPacket(pingBuffer, pingBuffer.length);
            pingingPacket.setPort(CQ_PORT);
            pingingPacket.setAddress(InetAddress.getByName("255.255.255.255"));
            pingingPacket.setData(InetAddress.getLocalHost().getHostName().getBytes());
            this.multicastSocket.send(pingingPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastPong() {
        try {
            byte[] pingBuffer = new byte[1024];
            final DatagramPacket pingingPacket = new DatagramPacket(pingBuffer, pingBuffer.length);
            pingingPacket.setPort(CQ_PORT);
            pingingPacket.setAddress(InetAddress.getByName("255.255.255.255"));
            pingingPacket.setData("".getBytes());
            this.multicastSocket.send(pingingPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void listenForPings() {
        new Thread(new Runnable() {

            public void run() {
                try {
                    while (true) {
                        byte[] listenBuffer = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(listenBuffer, listenBuffer.length);
                        multicastSocket.receive(packet);
                        if (!isLocalHost(packet.getAddress())) {
                            String hostName = new String(packet.getData(), packet.getOffset(), packet.getLength());
                            final InetAddress address = packet.getAddress();
                            if (!hostName.isEmpty()) {
                                Timer timeoutTimer = new Timer(10000, new ActionListener() {

                                    public void actionPerformed(ActionEvent e) {
                                        removeNettyBoo(address);
                                    }
                                });
                                NettyBoo nettyBoo = nettyBoos.get(address);
                                if (nettyBoo != null) {
                                    nettyBoo.setHostName(hostName);
                                    nettyBoo.getTimeoutTimer().restart();
                                    fireUpdated(nettyBoo);
                                } else {
                                    nettyBoo = new NettyBoo(packet.getAddress(), hostName, timeoutTimer);
                                    nettyBoos.put(address, nettyBoo);
                                    sendPing(address);
                                    fireFound(nettyBoo);
                                }
                            } else {
                                removeNettyBoo(address);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void removeNettyBoo(InetAddress address) {
        fireLost(nettyBoos.remove(address));
    }

    public static boolean isLocalHost(InetAddress inetAddress) {
        NetworkInterface iface = null;
        try {
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                iface = (NetworkInterface) ifaces.nextElement();
                InetAddress ia = null;
                for (Enumeration ips = iface.getInetAddresses(); ips.hasMoreElements(); ) {
                    ia = (InetAddress) ips.nextElement();
                    if (ia.equals(inetAddress)) return true;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void sendPing(InetAddress address) throws IOException {
        String hostName = InetAddress.getLocalHost().getHostName();
        byte[] responseBuffer = new byte[1024];
        DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
        responsePacket.setData(hostName.getBytes());
        DatagramSocket responseDatagramSocket;
        responseDatagramSocket = new DatagramSocket();
        responseDatagramSocket.connect(address, RESPONSE_PORT);
        responsePacket.setPort(RESPONSE_PORT);
        responseDatagramSocket.send(responsePacket);
    }

    public void addFinderListener(FinderListener listener) {
        finderListeners.add(listener);
    }

    public void removeFinderListener(FinderListener listner) {
        finderListeners.remove(listner);
    }

    private void fireFound(NettyBoo nettyBoo) {
        for (FinderListener finderListener : finderListeners) {
            finderListener.NettyBooFound(nettyBoo);
        }
    }

    private void fireLost(NettyBoo nettyBoo) {
        for (FinderListener finderListener : finderListeners) {
            finderListener.NettyBooLost(nettyBoo);
        }
    }

    private void fireUpdated(NettyBoo nettyBoo) {
        for (FinderListener finderListener : finderListeners) {
            finderListener.NettyBooUpdated(nettyBoo);
        }
    }
}
