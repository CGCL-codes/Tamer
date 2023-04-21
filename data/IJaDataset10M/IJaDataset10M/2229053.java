package mindbright.ssh;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class SSHClientUserAdaptor implements SSHClientUser {

    protected String sshHost;

    protected int sshPort;

    public SSHClientUserAdaptor(String server, int port) {
        this.sshHost = server;
        this.sshPort = port;
    }

    public SSHClientUserAdaptor(String server) {
        this(server, SSH.DEFAULTPORT);
    }

    public String getSrvHost() {
        return sshHost;
    }

    public int getSrvPort() {
        return sshPort;
    }

    public Socket getProxyConnection() throws IOException {
        return null;
    }

    public String getDisplay() {
        return "";
    }

    public int getMaxPacketSz() {
        return 0;
    }

    public int getAliveInterval() {
        return 0;
    }

    public boolean wantX11Forward() {
        return false;
    }

    public boolean wantPrivileged() {
        return false;
    }

    public boolean wantPTY() {
        return false;
    }

    public SSHInteractor getInteractor() {
        return null;
    }
}
