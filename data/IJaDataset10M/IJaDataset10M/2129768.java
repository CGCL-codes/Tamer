package net.alinnistor.nk.domain;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:nad7ir@yahoo.com">Alin NISTOR</a>
 */
public class User {

    public static final String FONT_PROPERTY = "fontproperty";

    public static final String AVAILABLE = "av";

    public static final Object NIOSRVADDRESS = "nioserveraddress";

    public static final String STATUS = "status";

    public static final String SOCK_ADDR = "sockaddr";

    public static final String LAST_MSG = "lastmessage";

    public static final Object STATUS_TO_SEND = "statustosend";

    public static final String IMAGE_MAP = "imagemap";

    private String name;

    private SocketAddress sa;

    private Map<Object, Object> properties = new HashMap<Object, Object>();

    public User(String name) {
        this.name = name;
    }

    public SocketAddress getSa() {
        return sa;
    }

    public void setSa(SocketAddress sa) {
        this.sa = sa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((sa == null) ? 0 : sa.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        User other = (User) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (sa == null) {
            if (other.sa != null) return false;
        } else if (!sa.equals(other.sa)) return false;
        return true;
    }

    public void putCliProp(Object key, Object value) {
        properties.put(key, value);
    }

    public Object getCliProp(Object key) {
        return properties.get(key);
    }

    @Override
    public String toString() {
        return name;
    }
}
