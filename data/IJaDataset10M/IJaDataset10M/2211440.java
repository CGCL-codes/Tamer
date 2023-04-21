package net.sf.jftp.gui.base;

public class FtpHost {

    public String name;

    public String hostname;

    public String username;

    public String password;

    public String port;

    public String toString() {
        return (name == null) ? ("<?> @ " + hostname) : (name + " @ " + hostname);
    }
}
