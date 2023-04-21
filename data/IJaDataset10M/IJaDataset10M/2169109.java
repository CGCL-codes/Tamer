package org.jdiagnose.library;

import org.jdiagnose.DiagnosticUnit;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Tries to open a Tcp Port and then closes it. This Diagnostic
 * can be used to check connectivity to a particular host and
 * port.
 * 
 * @author jamie
 */
public class TcpDiagnostic extends DiagnosticUnit {

    private String host = "";

    private int port = 80;

    public TcpDiagnostic() {
    }

    /**
     * Create a new DiagnosticUnit with a Fully Qualified Name
     *
     * @param name the name of this diagnostic.
     */
    public TcpDiagnostic(String name, String host, int port) {
        super(name);
        this.host = host;
        this.port = port;
    }

    public TcpDiagnostic(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void diagnoseConnection() throws UnknownHostException, IOException {
        Socket socket = new Socket(host, port);
        socket.close();
    }

    /**
     * @return Returns the host.
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host The host to set.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return Returns the port.
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port The port to set.
     */
    public void setPort(int port) {
        this.port = port;
    }
}
