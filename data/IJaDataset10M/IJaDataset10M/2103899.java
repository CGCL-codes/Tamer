package editor.common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SourceDetail implements Serializable {

    private String name;

    private String host;

    private String user;

    private String password;

    private String protocol;

    public SourceDetail(String name, String host, String user, String password, String protocol) {
        this.name = name;
        this.host = host;
        this.user = user;
        this.password = password;
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getUser() {
        return user;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("\n");
        str.append("\n name : " + (getName() != null ? getName() : ""));
        str.append("\n host : " + (getHost() != null ? getHost() : ""));
        str.append("\n user : " + (getUser() != null ? getUser() : ""));
        str.append("\n password : " + (getPassword() != null ? getPassword() : ""));
        str.append("\n protocol : " + (getProtocol() != null ? getProtocol() : ""));
        str.append("\n");
        return str.toString();
    }
}
