package com.go.teaservlet.util.cluster;

import java.io.*;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.RMISocketFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.AlreadyBoundException;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Vector;
import com.go.trove.util.PropertyMap;
import com.go.trove.log.Syslog;

/******************************************************************************
 * 
 * @author Jonathan Colwell
 * @version
 * <!--$$Revision: 3 $-->, <!--$$JustDate:-->  2/19/02 <!-- $-->
 */
public class ClusterManager {

    /**
     * Provides an easy way to create a cluster manager when provided with a 
     * PropertyMap containing some of the following properties, and an 
     * implementation of the Clustered interface.  In order to use the 
     * multicast discovery feature, launchAuto() should be called on the
     * returned instance.  killAuto() should also be called when finished 
     * with the manager.
     * <pre>
     * cluster {
     *     servers = cd-test0;cd-test1
     *     name = FooBar
     *     localNet = 10.192.1.0/24
     *     rmi.port = 1099
     *     multicast {
     *         port = 1099
     *         group = 224.0.1.20
     *     }
     * }</pre>
     *
     */
    private static int cActiveRegistryPort = -1;

    public static ClusterManager createClusterManager(PropertyMap properties, Clustered clusterObj) throws Exception {
        ClusterManager manager = null;
        if (properties != null) {
            properties = properties.subMap("cluster");
            if (properties.keySet().size() > 0) {
                String servers = properties.getString("servers");
                String clusterName = properties.getString("name");
                InetAddress multicastGroup = null;
                String netInterface = properties.getString("localNet");
                int rmiPort = properties.getInt("rmi.port", 1099);
                int multicastPort = properties.getInt("multicast.port", 1099);
                String group = properties.getString("multicast.group");
                if (group != null) {
                    try {
                        multicastGroup = InetAddress.getByName(group);
                    } catch (UnknownHostException uhe) {
                    }
                }
                if (multicastGroup != null) {
                    manager = new ClusterManager(clusterObj, multicastGroup, multicastPort, rmiPort, netInterface, servers);
                } else if (servers != null) {
                    manager = new ClusterManager(clusterObj, rmiPort, netInterface, servers);
                }
            }
        }
        return manager;
    }

    private boolean DEBUG = false;

    private Vector mUnresolvedServerNames;

    private final Vector mExplicitlySpecifiedServerNames;

    private MulticastSocket mSock;

    private InetAddress mMultiGroup;

    private String mHostName;

    private RMISocketFactory mRMISocketFactory;

    private Clustered mCluster;

    private int mMultiPort, mRmiPort;

    private Registry mLocalRegistry;

    private AutomaticClusterManagementThread mAuto;

    /** 
     * Convenience method for users of the Restartable interface within the 
     * TeaServletClusterHook.
     */
    public ClusterManager(Restartable restartableObj, String clusterName, String serverName, InetAddress multicastGroup, int multicastPort, int rmiPort, String netInterface, String serverNames) throws IOException, RemoteException {
        this(new TeaServletClusterHook(restartableObj, clusterName, serverName), multicastGroup, multicastPort, rmiPort, netInterface, serverNames);
    }

    /**
     * Creates a cluster manager that will use multicast discovery to find 
     * peers with similar configurations.
     */
    public ClusterManager(Clustered cluster, InetAddress multicastGroup, int multicastPort, int rmiPort) throws IOException {
        this(cluster, multicastGroup, multicastPort, rmiPort, null, null);
    }

    /**
     * Creates a cluster manager that will use multicast discovery as well as
     * a list of server names to find peers in this cluster.
     */
    public ClusterManager(Clustered cluster, InetAddress multicastGroup, int multicastPort, int rmiPort, String netInterface, String serverNames) throws IOException {
        this(cluster, rmiPort, netInterface, serverNames);
        mMultiGroup = multicastGroup;
        mMultiPort = multicastPort;
        setUpMulticast(multicastGroup, multicastPort, cluster.getClusterName(), cluster.getServerName(), netInterface);
    }

    /** 
     * Convenience method for users of the Restartable interface within the 
     * TeaServletClusterHook.
     */
    public ClusterManager(Restartable restartableObj, String clusterName, String serverName, int rmiPort, String netInterface, String serverNames) throws IOException {
        this(new TeaServletClusterHook(restartableObj, clusterName, serverName), rmiPort, netInterface, serverNames);
    }

    /** 
     * Allows the cluster to be defined explicitly by providing names rather 
     * than by using multicast discovery.
     */
    public ClusterManager(Clustered cluster, int rmiPort, String netInterface, String serverNames) throws IOException {
        if (rmiPort > 0) {
            mRmiPort = rmiPort;
        } else {
            mRmiPort = 1099;
        }
        mCluster = cluster;
        mLocalRegistry = prepareRegistry(cluster, rmiPort, netInterface);
        mExplicitlySpecifiedServerNames = new Vector();
        if (serverNames != null) {
            StringTokenizer st = new StringTokenizer(serverNames, ",; ");
            while (st.hasMoreTokens()) {
                mExplicitlySpecifiedServerNames.add(st.nextToken().toLowerCase());
            }
        }
        mUnresolvedServerNames = (Vector) mExplicitlySpecifiedServerNames.clone();
    }

    public String[] resolveServerNames() {
        synchronized (mUnresolvedServerNames) {
            Iterator resolveIt = mUnresolvedServerNames.iterator();
            while (resolveIt.hasNext()) {
                String nextHost = (String) resolveIt.next();
                try {
                    String namingURL = ("//" + nextHost + ':' + mRmiPort + '/' + mCluster.getClusterName());
                    if (DEBUG) {
                        Syslog.debug("Looking Up " + namingURL);
                    }
                    Clustered bcl = (Clustered) Naming.lookup(namingURL);
                    if (bcl != null) {
                        if (!mCluster.containsPeer(bcl)) {
                            resolveIt.remove();
                            mCluster.addPeer(bcl);
                            Syslog.info("Successfullly resolved " + bcl.getServerName() + " as a member of the " + mCluster.getClusterName() + " cluster.");
                        }
                    }
                    continue;
                } catch (Exception e) {
                }
                if (DEBUG) {
                    Syslog.debug("Failed to resolve " + nextHost + " as part of this cluster");
                }
            }
        }
        try {
            Clustered[] peers = getCluster().getKnownPeers();
            ArrayList peerNames = new ArrayList();
            boolean lostOne = false;
            for (int j = 0; j < peers.length; j++) {
                try {
                    peerNames.add(peers[j].getServerName());
                } catch (RemoteException re) {
                    getCluster().removePeer(peers[j]);
                    lostOne = true;
                }
            }
            if (lostOne) {
                mUnresolvedServerNames = (Vector) mExplicitlySpecifiedServerNames.clone();
                mUnresolvedServerNames.removeAll(peerNames);
            }
            return (String[]) peerNames.toArray(new String[peerNames.size()]);
        } catch (RemoteException re2) {
            Syslog.warn(re2);
        }
        return null;
    }

    public Clustered getCluster() {
        return mCluster;
    }

    public int getRMIPort() {
        return mRmiPort;
    }

    public int getMulticastPort() {
        return mMultiPort;
    }

    public InetAddress getMulticastGroup() {
        return mMultiGroup;
    }

    public void send(byte[] msg) throws IOException {
        try {
            mSock.send(new DatagramPacket(msg, msg.length, mMultiGroup, mMultiPort));
        } catch (SecurityException se) {
            se.printStackTrace();
        }
    }

    public DatagramPacket getNextPacket() throws IOException {
        DatagramPacket pack = new DatagramPacket(new byte[1024], 1024);
        try {
            mSock.receive(pack);
        } catch (SecurityException se) {
            se.printStackTrace();
        }
        return pack;
    }

    /**
     * Launches an AutomaticClusterManagementThread in active mode.
     */
    public void launchAuto() {
        launchAuto(true);
    }

    /**
     * Allows the management thread to passively take part in the cluster
     * operations.  
     * Other cluster members will not be made aware of this instance.
     */
    public void launchAuto(boolean active) {
        killAuto();
        if (mSock != null) {
            try {
                mAuto = new AutomaticClusterManagementThread(this, mCluster.getClusterName(), active);
            } catch (Exception e) {
                mAuto = new AutomaticClusterManagementThread(this, active);
            }
            if (mAuto != null) {
                mAuto.start();
            }
        }
    }

    /** 
     * permits the AutomaticClusterManagementThread to be subclassed
     * and used into the ClusterManager.
     */
    public void launchAuto(AutomaticClusterManagementThread auto) {
        killAuto();
        if (mSock != null) {
            mAuto = auto;
            if (mAuto != null) {
                mAuto.start();
            }
        }
    }

    public void killAuto() {
        if (mAuto != null) {
            mAuto.kill();
        }
        mAuto = null;
    }

    public void joinCluster() throws IOException {
        send(("join~" + getCluster().getClusterName() + '~' + getHostName()).getBytes());
    }

    public void pingCluster() throws IOException {
        send(("ping~" + getCluster().getClusterName() + '~' + getHostName()).getBytes());
    }

    public String getHostName() throws IOException {
        if (mHostName == null) {
            mHostName = InetAddress.getLocalHost().getHostName();
        }
        return mHostName;
    }

    public void destroy() throws IOException {
        killAuto();
        if (mSock != null) {
            send(("leave~" + mCluster.getClusterName() + '~' + getHostName()).getBytes());
            mSock.leaveGroup(mMultiGroup);
            mSock.close();
            mSock = null;
        }
    }

    protected Registry prepareRegistry(Clustered cluster, int port, String netInterface) throws IOException {
        if (mRMISocketFactory == null) {
            try {
                RMISocketFactory factory = RMISocketFactory.getSocketFactory();
                if (factory == null) {
                    factory = new BackchannelSocketFactory(getHostName(), netInterface);
                    if (factory != null) {
                        RMISocketFactory.setSocketFactory(factory);
                        Syslog.info("RMI will now operate on " + ((BackchannelSocketFactory) factory).getInetAddress().getHostAddress());
                    }
                }
                mRMISocketFactory = factory;
            } catch (IOException ioe) {
                Syslog.warn(ioe);
            }
        }
        Registry reg = null;
        if (DEBUG) {
            Syslog.debug("preparing registry on port " + port);
            Syslog.debug("old port " + cActiveRegistryPort);
        }
        if (cActiveRegistryPort < 0) {
            try {
                if (mRMISocketFactory != null) {
                    reg = LocateRegistry.createRegistry(port, mRMISocketFactory, mRMISocketFactory);
                    Syslog.info("RMI registry created on " + ((BackchannelSocketFactory) mRMISocketFactory).getInetAddress().getHostAddress() + ':' + port);
                } else {
                    reg = LocateRegistry.createRegistry(port);
                }
                cActiveRegistryPort = port;
            } catch (RemoteException re) {
                Syslog.warn(re);
                reg = null;
            }
        }
        if (reg == null) {
            try {
                if (mRMISocketFactory != null) {
                    reg = LocateRegistry.getRegistry(getHostName(), port, mRMISocketFactory);
                    Syslog.info("RMI registry reference created for " + ((BackchannelSocketFactory) mRMISocketFactory).getInetAddress().getHostAddress() + ':' + port);
                } else {
                    reg = LocateRegistry.getRegistry(getHostName(), port);
                }
                reg.list();
            } catch (RemoteException re) {
                Syslog.warn(re);
                reg = null;
            }
        }
        if (reg != null) {
            try {
                reg.bind(cluster.getClusterName(), cluster);
            } catch (AlreadyBoundException abe) {
                reg.rebind(cluster.getClusterName(), cluster);
            } catch (NoSuchObjectException nsoe) {
                Syslog.warn(nsoe);
                if (nsoe.detail != null) {
                    Syslog.warn(nsoe.detail);
                } else {
                    Syslog.warn("No detail available");
                }
            }
            Syslog.info(cluster.getClusterName() + " bound on " + cluster.getServerName() + ':' + port);
        } else {
            throw new IOException("Failed to connect to a valid RMI registry");
        }
        return reg;
    }

    protected void setUpMulticast(InetAddress group, int port, String clusterName) throws IOException {
        setUpMulticast(group, port, clusterName, null, null);
    }

    protected void setUpMulticast(InetAddress group, int port, String clusterName, String host) throws IOException {
        setUpMulticast(group, port, clusterName, host, null);
    }

    protected void setUpMulticast(InetAddress group, int port, String clusterName, String host, String netInterface) throws IOException {
        Syslog.info("Setting up Multicast");
        if (mSock == null) {
            mSock = new MulticastSocket(port);
            if (host == null) {
                host = getHostName();
            } else {
                mHostName = host;
            }
            InetAddress interf = mSock.getInterface();
            Syslog.debug("current interface IP: " + interf.getHostAddress());
            if (netInterface != null) {
                try {
                    InetAddress address = LocalNetResolver.resolveLocalNet(host, netInterface);
                    if (address != null) {
                        mSock.setInterface(address);
                        host = address.getHostAddress();
                        Syslog.info("new interface IP: " + host);
                        mHostName = host;
                    }
                } catch (IOException ioe) {
                    Syslog.warn(ioe);
                }
            }
            mSock.setSendBufferSize(1024);
            mSock.setReceiveBufferSize(1024);
            mSock.setTimeToLive(1);
            mSock.joinGroup(group);
        }
    }

    /**
     * converts an array of four bytes to a long. 
     * @return the converted bytes as a lon or -1 on error.
     */
    public long convertIPBytes(byte[] ipBytes) {
        if (ipBytes.length == 4) {
            long ipLong = (((long) ipBytes[0]) << 24);
            ipLong |= (((long) ipBytes[1]) << 16);
            ipLong |= (((long) ipBytes[2]) << 8);
            ipLong |= (long) ipBytes[3];
            return ipLong;
        }
        return -1;
    }

    /**
     * turns a dot delimited IP address string into a long.
     */
    public long convertIPString(String ip) throws NumberFormatException, IllegalArgumentException {
        StringTokenizer st = new StringTokenizer(ip, ".");
        if (st.countTokens() == 4) {
            long ipLong = (Long.parseLong(st.nextToken()) << 24);
            ipLong += (Long.parseLong(st.nextToken()) << 16);
            ipLong += (Long.parseLong(st.nextToken()) << 8);
            ipLong += Long.parseLong(st.nextToken());
            return ipLong;
        } else {
            throw new IllegalArgumentException("Invalid IP string");
        }
    }

    /**
     * converts a long to a dot delimited IP address string.
     */
    public String convertIPBackToString(long ip) {
        StringBuffer sb = new StringBuffer(16);
        sb.append(Long.toString((ip >> 24) & 0xFF));
        sb.append('.');
        sb.append(Long.toString((ip >> 16) & 0xFF));
        sb.append('.');
        sb.append(Long.toString((ip >> 8) & 0xFF));
        sb.append('.');
        sb.append(Long.toString(ip & 0xFF));
        return sb.toString();
    }
}
