package org.oxyus.crawler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.oxyus.store.Store;

/**
 * Persists server data.
 *
 * @author Carlos Saltos (csaltos[@]users.sourceforge.net)
 */
public class Server {

    protected Logger log;

    /**
     * Oxyus repository connection
     */
    protected Store store;

    /**
     * Server primary key
     */
    protected int code;

    /**
     * Server protocol (i.e. http)
     */
    protected String protocol;

    /**
     * Name of the server host (i.e. www.gentoo.org)
     */
    protected String host;

    /**
     * Number of the port service (i.e 80 for default http)
     */
    protected int port;

    /**
     * Crea un nuevo server.
     */
    public Server() {
        log = Logger.getLogger(Server.class);
        reset();
    }

    /**
     * Reset internal properties
     */
    public void reset() {
        setCode(-1);
        setProtocol(null);
        setHost(null);
        setPort(-1);
    }

    /**
     * Reads a servers using its code
     */
    public void read() throws SQLException {
        boolean success = false;
        if (store == null) {
            throw new SQLException("Connection not opened");
        }
        PreparedStatement statement = store.prepareStatement("SELECT " + "code_server, protocol, host, port FROM ox_server WHERE " + "code_server = ?");
        statement.setInt(1, getCode());
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            setProtocol(result.getString("protocol"));
            setHost(result.getString("host"));
            setPort(result.getInt("port"));
            success = true;
        }
        result.close();
        statement.close();
        if (!success) {
            throw new SQLException("The server was not found");
        }
    }

    public void locateOrCreate() throws SQLException {
        PreparedStatement statement = store.prepareStatement("select code_server from ox_server where protocol = ? and " + "host = ? and port = ?");
        statement.setString(1, this.getProtocol());
        statement.setString(2, this.getHost());
        statement.setInt(3, this.getPort());
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            this.setCode(result.getInt("code_server"));
            result.close();
            statement.close();
            return;
        }
        this.setCode(store.nextCode("server"));
        statement = store.prepareStatement("insert into ox_server(code_server, protocol, host, port) " + "values (?,?,?,?)");
        statement.setInt(1, this.getCode());
        statement.setString(2, this.getProtocol());
        statement.setString(3, this.getHost());
        statement.setInt(4, this.getPort());
        statement.executeUpdate();
        statement.close();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String nombre) {
        if (nombre == null) {
            this.host = null;
        } else {
            this.host = nombre.toLowerCase().trim();
        }
    }

    public int getPort() {
        if (port == -1) {
            if ("http".equals(this.getProtocol())) {
                return 80;
            } else {
                return 443;
            }
        }
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setStore(Store connection) {
        this.store = connection;
    }

    public Store getStore() {
        return store;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
