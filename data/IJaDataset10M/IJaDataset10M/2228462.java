package gnujatella.gnutella;

import java.net.*;

public class Host {

    protected InetAddress ip;

    protected int port = -1;

    protected GUID guid;

    protected int hops = -1;

    protected int files = -1;

    protected int size = -1;

    public Host(String address) {
        port = 6346;
        if (address.lastIndexOf(":") != -1) {
            port = Integer.decode(address.substring(address.lastIndexOf(":") + 1)).intValue();
        }
        try {
            if (address.lastIndexOf(":") != -1) ip = InetAddress.getByName(address.substring(0, address.indexOf(":"))); else ip = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host!");
        }
    }

    public Host(InetAddress ip, int port) {
        this(ip, port, -1);
    }

    public Host(InetAddress ip, int port, int hops) {
        this(ip, port, hops, -1, -1);
    }

    public Host(InetAddress ip, int port, int hops, int files, int size) {
        this.ip = ip;
        this.port = port;
        this.hops = hops;
        this.files = files;
        this.size = size;
    }

    public void copyData(Host host) {
        setSize(host.getSize());
        setFiles(host.getFiles());
        setHops(host.getHops());
    }

    public GUID getGUID() {
        return guid;
    }

    public InetAddress getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getHops() {
        return hops;
    }

    public void setHops(int hops) {
        this.hops = hops;
    }

    public int getFiles() {
        return files;
    }

    public void setFiles(int files) {
        this.files = files;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Integer getHashCode() {
        return guid.getHashCode();
    }

    public boolean isFiltered() {
        if (ip.getAddress()[0] == 0) return true;
        if (ip.getAddress()[0] == 127) return true;
        if (ip.isMulticastAddress()) return true;
        return false;
    }

    public boolean equals(Object o) {
        if (o == null) return false;
        InetAddress cip = ((Host) o).getIP();
        if (cip == null) return false;
        return ip.equals(((Host) o).getIP());
    }

    public int hashCode() {
        return ip.hashCode() + port;
    }

    public String toString() {
        return ip.getHostAddress() + ":" + port;
    }
}
