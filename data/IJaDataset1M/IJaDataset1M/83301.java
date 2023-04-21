package cz.muni.fi.rum.reflector.telnet;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author pmikulasek
 */
public class TelnetServer implements Serializable {

    private String address;

    private Integer port;

    private final User user;

    public class User implements Serializable {

        private String userName;

        private char[] pass;

        private User(String userName, char[] pass) {
            this.userName = userName;
            this.pass = pass;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof User)) {
                return false;
            }
            User other = (User) obj;
            return this.userName.equals(other.getUserName()) && Arrays.equals(this.pass, other.getPass());
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 47 * hash + (this.userName != null ? this.userName.hashCode() : 0);
            hash = 47 * hash + (this.pass != null ? this.pass.hashCode() : 0);
            return hash;
        }

        public char[] getPass() {
            return pass;
        }

        public String getUserName() {
            return userName;
        }
    }

    public TelnetServer(String address, Integer port, String userName, char[] pass) {
        this.address = address.trim();
        this.port = port;
        this.user = new User(userName, pass);
    }

    public Integer getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TelnetServer)) {
            return false;
        }
        TelnetServer other = (TelnetServer) obj;
        return this.address.equals(other.getAddress()) && this.port.equals(other.getPort()) && this.user.equals(other.getUser());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.address != null ? this.address.hashCode() : 0);
        hash = 41 * hash + (this.port != null ? this.port.hashCode() : 0);
        hash = 41 * hash + (this.user != null ? this.user.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Reflector server: ");
        builder.append(address);
        builder.append(":");
        builder.append(port);
        return builder.toString();
    }
}
