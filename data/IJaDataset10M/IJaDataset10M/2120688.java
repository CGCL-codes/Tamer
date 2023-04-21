package eu.popeye.middleware.groupmanagement.membership;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.PublicKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import eu.popeye.networkabstraction.communication.basic.util.SuperPeerConfig;

/**
 * This class implements the Member interface. 
 * @author Gerard Paris Aixala
 */
public class MemberImpl implements Member {

    private static final long serialVersionUID = 1L;

    private static final int PORT_NOT_TO_BE_USED = 0;

    private InetAddress addr;

    private int port;

    private String username;

    private Serializable key;

    private final transient Log log = LogFactory.getLog(getClass());

    /**
	 * Creates a member from an InetAddres
	 * @param addr
	 * @deprecated
	 */
    public MemberImpl(InetAddress addr) {
        this.addr = addr;
        this.port = PORT_NOT_TO_BE_USED;
        this.username = SuperPeerConfig.USERNAME;
        this.key = SuperPeerConfig.PUBLICKEY;
    }

    /**
	 * Creates a member from an InetAddres and a port
	 * @param addr
	 * @param port
	 * @deprecated
	 */
    public MemberImpl(InetAddress addr, int port) {
        this.addr = addr;
        this.port = port;
        this.username = SuperPeerConfig.USERNAME;
        this.key = SuperPeerConfig.PUBLICKEY;
    }

    /**
	 * Creates a member from an InetAddres and a port, public key
	 * and 
	 * @param addr
	 * @param port
	 */
    public MemberImpl(InetAddress addr, int port, String username, PublicKey key) {
        this.addr = addr;
        this.port = port;
        this.username = username;
        this.key = key;
    }

    /**
	 * Creates a member from a string containing the IP address and the port
	 * @param addr
	 * @deprecated
	 */
    public MemberImpl(String member) {
        int sep = member.indexOf(':');
        String ipStr = member.substring(0, sep);
        String portStr = member.substring(sep + 1);
        try {
            this.addr = InetAddress.getByName(ipStr);
            this.port = Integer.parseInt(portStr);
            this.username = SuperPeerConfig.USERNAME;
            this.key = SuperPeerConfig.PUBLICKEY;
        } catch (UnknownHostException e) {
            log.debug("Could not create Member for " + ipStr + ":" + portStr + " reason" + e.getStackTrace().toString());
        }
    }

    /**
	 * @param addr
	 * @param port
	 * @param username
	 * @param key
	 */
    public MemberImpl(InetAddress addr, int port, String username, Serializable key) {
        super();
        this.addr = addr;
        this.port = port;
        this.username = username;
        this.key = key;
    }

    /**
	 * Returns a String representation of this object
	 * @return a String containing the IP address and port of this Member
	 */
    public String toString() {
        return addr.getHostAddress() + ":" + port;
    }

    public String toFullString() {
        return addr.getHostAddress() + ":" + port + " | " + username + " has key = " + key;
    }

    /**
	 * Compares this Member with the specified object 
	 * @param obj the object to compare this <code>Member</code> against.
	 * @return <code>true</code> if the <code>Member</code> are equal; <code>false</code> otherwise.
	 */
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
	 * Returns the IP address bound to this member
	 * @return the IP address
	 */
    public InetAddress getInetAddress() {
        return addr;
    }

    /**
	 * Returns the port number bound to this member
	 * @return the port number
	 */
    public int getPort() {
        return port;
    }

    /**
	 * returns 0 if the InetAddress and port from the two objects are equal
	 * @param obj
	 * @return
	 */
    public int compareTo(Object obj) {
        MemberImpl member = (MemberImpl) obj;
        InetAddress addr1 = this.addr;
        InetAddress addr2 = member.getInetAddress();
        if (addr1.hashCode() == addr2.hashCode()) return 0;
        if (addr1.getAddress()[0] < addr2.getAddress()[0]) {
            return -1;
        } else if (addr1.getAddress()[0] > addr2.getAddress()[0]) {
            return 1;
        } else {
            if (addr1.getAddress()[1] < addr2.getAddress()[1]) {
                return -1;
            } else if (addr1.getAddress()[1] > addr2.getAddress()[1]) {
                return 1;
            } else {
                if (addr1.getAddress()[2] < addr2.getAddress()[2]) {
                    return -1;
                } else if (addr1.getAddress()[2] > addr2.getAddress()[2]) {
                    return 1;
                } else {
                    if (addr1.getAddress()[3] < addr2.getAddress()[3]) {
                        return -1;
                    } else if (addr1.getAddress()[3] > addr2.getAddress()[3]) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    }

    /**
	 * @return Returns the key.
	 */
    public Serializable getKey() {
        return key;
    }

    /**
	 * @param key The key to set.
	 */
    public void setKey(Serializable key) {
        this.key = key;
    }

    /**
	 * @return Returns the username.
	 */
    public String getUsername() {
        return username;
    }

    /**
	 * @param username The username to set.
	 */
    public void setUsername(String username) {
        this.username = username;
    }

    public static InetAddress getLocalInetAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
	 * Returns a copy of the current object
	 * @return
	 */
    public Object clone() {
        return new MemberImpl(this.addr, this.port, this.username, this.key);
    }
}
