package net.megakiwi.jirclib.net.connection;

import net.megakiwi.jirclib.exception.ConnectionException;
import java.net.*;
import java.io.*;

/**
 * Represents a IRC connection.
 * 
 * @author  Lars 'Levia' Wesselius
*/
public class IRCConnection implements iConnection {

    protected String m_Host;

    protected int m_Port = 6667;

    protected Socket m_Connection = null;

    protected BufferedReader m_In = null;

    protected PrintWriter m_Out = null;

    /**
     * Connects to something.
     * 
     * @param   host    The host to connect to.
     * @param   port    The port to connect through.
     * @return  True if such was succesful, false if not.
    */
    public boolean connect(final String host, final int port) throws ConnectionException {
        try {
            m_Host = host;
            m_Port = port;
            m_Connection = new Socket(host, port);
            m_Connection.setKeepAlive(true);
            if (!m_Connection.isConnected()) {
                return false;
            }
            m_Out = new PrintWriter(m_Connection.getOutputStream(), true);
            m_In = new BufferedReader(new InputStreamReader(m_Connection.getInputStream()));
        } catch (Exception ex) {
            throw new ConnectionException(this, "Couldn't connect: " + ex.getMessage());
        }
        return true;
    }

    /**
     * Disconnects this instance.
     * 
     * @return  True if such was succesful, false if not.
    */
    public boolean disconnect() throws ConnectionException {
        try {
            m_Connection.close();
        } catch (IOException ex) {
            throw new ConnectionException(this, "Couldn't disconnect: " + ex.getMessage());
        }
        return true;
    }

    /**
     * Send a raw command to the server.
     * 
     * @param   cmd The command.
    */
    public void send(String cmd) {
        m_Out.write(cmd);
        m_Out.flush();
    }

    /**
     * Returns whether this instance is connected or not.
     * 
     * @return  True if so, false if not.
    */
    public boolean isConnected() {
        return m_Connection.isConnected();
    }

    /**
     * Returns the connected host.
     * 
     * @return  The host it is connected to.
    */
    public String getHost() {
        return m_Host;
    }

    /**
     * Returns the port it connected through.
     * 
     * @return  The port it connected to.
    */
    public int getPort() {
        return m_Port;
    }
}
