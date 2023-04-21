package org.tscribble.bitleech.core.download;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import org.apache.log4j.Logger;

/**
 * Created on Feb 26, 2007
 * 
 * @author triston
 */
public class Host {

    /**
	 * Logger for this class
	 */
    private static final Logger log = Logger.getLogger("Host");

    private long ping = -1;

    private String name;

    private String protocol;

    private String speed;

    private String _u;

    private boolean error;

    private String site;

    private InetSocketAddress addr;

    private SocketChannel ch;

    public Host(String url) {
        _u = url;
        int dslash = url.indexOf("//");
        int sslash = url.indexOf("/", dslash + 2);
        name = url.substring(dslash + 2, sslash);
        addr = new InetSocketAddress(name, 80);
        protocol = url.substring(0, url.indexOf(":"));
        site = url.substring(0, sslash);
    }

    public void ping() {
        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    long start = System.currentTimeMillis();
                    ch = SocketChannel.open(addr);
                    ping = System.currentTimeMillis() - start;
                } catch (Exception e) {
                    error = true;
                } finally {
                    try {
                        if (ch != null) ch.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
        if (ping == -1 && !error) {
            t.setName("Ping Thread");
            t.setDaemon(true);
            t.start();
        }
    }

    public String getUrl() {
        return _u.toString();
    }

    public long getPing() {
        return ping;
    }

    public String getName() {
        return name;
    }

    public String getSpeed() {
        return speed;
    }

    public InetSocketAddress getAddr() {
        return addr;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getSite() {
        return site;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_u == null) ? 0 : _u.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Host other = (Host) obj;
        if (_u == null) {
            if (other._u != null) return false;
        } else if (!_u.equals(other._u)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "[" + getName() + "]";
    }
}
