package jtcpfwd.listener;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.StringTokenizer;
import jtcpfwd.Lookup;
import jtcpfwd.destination.Destination;
import jtcpfwd.forwarder.Forwarder;
import socks.Proxy;
import socks.SocksServerSocket;

public class SOCKSListener extends Listener {

    public static final String SYNTAX = "SOCKS@#<proxyHost>[:<proxyPort>[:<user>[:<passwd>]]]#<destination>[#<addrFwd>]";

    public static final Class[] getRequiredClasses() {
        throw new UnsupportedOperationException("SOCKS based listeners and forwarders cannot be included into jTCPfwd lite version");
    }

    private final Proxy proxy;

    private final Destination destination;

    private final ObjectOutputStream addressStream;

    private SocksServerSocket ss;

    public SOCKSListener(String rule) throws Exception {
        StringTokenizer st = new StringTokenizer(rule.substring(1), "" + rule.charAt(0));
        proxy = Proxy.parseProxy(st.nextToken());
        destination = Destination.lookupDestination(st.nextToken());
        if (st.hasMoreTokens()) {
            Forwarder forwarder = Lookup.lookupForwarder(st.nextToken());
            addressStream = new ObjectOutputStream(forwarder.connect(null).getOutputStream());
        } else {
            addressStream = null;
        }
    }

    protected Socket tryAccept() throws IOException {
        final InetSocketAddress target = destination.getNextDestination();
        if (target == null) return null;
        ss = new SocksServerSocket(proxy, target.getAddress(), target.getPort());
        InetSocketAddress assigned = new InetSocketAddress(ss.getInetAddress(), ss.getLocalPort());
        if (addressStream != null) {
            synchronized (addressStream) {
                addressStream.writeObject(assigned);
                addressStream.flush();
                addressStream.reset();
            }
        } else {
            System.out.println("Proxy assigned address " + assigned);
        }
        Socket s = ss.accept();
        ss.close();
        ss = null;
        return s;
    }

    public void tryDispose() throws IOException {
        if (addressStream != null) addressStream.close();
        if (ss != null) ss.close();
    }
}
