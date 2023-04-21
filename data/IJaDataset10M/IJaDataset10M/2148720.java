package connection;

import bussiness.subnet.IPAddress;
import java.io.Serializable;

/**
 *
 * @author necronet
 */
public class Ip implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;

    private Integer idipAdicionales;

    private String ip;

    private Red redIdredAdicional;

    public Ip() {
    }

    public Ip(Integer idipAdicionales) {
        this.idipAdicionales = idipAdicionales;
    }

    public Ip(Integer idipAdicionales, String ip) {
        this.idipAdicionales = idipAdicionales;
        this.ip = ip;
    }

    public Integer getIdipAdicionales() {
        return idipAdicionales;
    }

    public void setIdipAdicionales(Integer idipAdicionales) {
        this.idipAdicionales = idipAdicionales;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Red getRedIdredAdicional() {
        return redIdredAdicional;
    }

    public void setRedIdredAdicional(Red redIdredAdicional) {
        this.redIdredAdicional = redIdredAdicional;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idipAdicionales != null ? idipAdicionales.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Ip)) {
            return false;
        }
        Ip other = (Ip) object;
        if ((this.idipAdicionales == null && other.idipAdicionales != null) || (this.idipAdicionales != null && !this.idipAdicionales.equals(other.idipAdicionales))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "connection.IpAdicionales[idipAdicionales=" + idipAdicionales + "]";
    }

    public int compareTo(Object o) {
        Ip ipaddr = null;
        if (o instanceof Ip) ipaddr = (Ip) o; else throw new ClassCastException("A IPAddress Object was expected!");
        IPAddress ipr = new IPAddress(ipaddr.getIp());
        IPAddress ipro = new IPAddress(ip);
        return ipr.compareTo(ipro);
    }
}
