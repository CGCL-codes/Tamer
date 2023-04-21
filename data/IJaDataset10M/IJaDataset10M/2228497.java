package net.jawe.scriptbot.impl;

import net.jawe.scriptbot.Server;

public class ServerImpl implements Server {

    private static final int DEFAULT_PORT = 6667;

    private String _hostname;

    private int _port = DEFAULT_PORT;

    private String _password;

    public ServerImpl() {
    }

    public ServerImpl(String hostname) {
        this(hostname, DEFAULT_PORT, null);
    }

    public ServerImpl(String hostname, int port) {
        this(hostname, port, null);
    }

    public ServerImpl(String hostname, int port, String password) {
        if (hostname == null || hostname.trim().length() == 0) {
            throw new IllegalArgumentException("hostname must not be blank");
        }
        if (port == 0) {
            throw new IllegalArgumentException("Invalid port: " + port);
        }
        _hostname = hostname;
        _port = port;
        _password = password;
    }

    public String getHostname() {
        return _hostname;
    }

    public String getPassword() {
        return _password;
    }

    public int getPort() {
        return _port;
    }

    /**
     * @param port The port to set.
     */
    public void setPort(int port) {
        _port = port;
    }

    @Override
    public boolean equals(Object obj) {
        boolean answer = obj != null;
        if (answer) {
            answer = obj == this;
            if (!answer) {
                answer = obj.getClass().equals(getClass());
                if (answer) {
                    Server other = (Server) obj;
                    answer = toString().equalsIgnoreCase(other.toString());
                }
            }
        }
        return answer;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return _hostname + ":" + _port;
    }

    /**
     * @param hostname
     *            The hostname to set.
     */
    public void setHostname(String hostname) {
        _hostname = hostname;
    }

    /**
     * @param password
     *            The password to set.
     */
    public void setPassword(String password) {
        _password = password;
    }
}
