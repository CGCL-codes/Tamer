package lb.edu.isae.entity;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author pfares
 */
public class Client {

    private InetAddress a;

    private int port;

    public Client(InetAddress a, int p) {
        this.a = a;
        port = p;
    }

    public Client(String h, int p) throws UnknownHostException {
        a = InetAddress.getByName(h);
        port = p;
    }

    /**
     * @return the a
     */
    public InetAddress getA() {
        return a;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return a.getHostName() + ":" + port;
    }
}
