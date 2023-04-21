package com.tensegrity;

import org.palo.api.Connection;
import org.palo.api.ConnectionConfiguration;
import org.palo.api.ConnectionEvent;
import org.palo.api.ConnectionFactory;
import org.palo.api.ConnectionListener;
import org.palo.api.Cube;
import org.palo.api.Property;

public class ViewSave {

    public static void main(String[] args) throws Exception {
        Connection c = getConnection("localhost");
        Cube cube = c.getDatabaseByName("Demo").getCubeByName("Sales");
        cube.addCubeView("v1", new Property[] {});
        c.disconnect();
    }

    private static Connection getConnection(String host) {
        ConnectionConfiguration cc = new ConnectionConfiguration(host, "7777");
        cc.setUser("admin");
        cc.setPassword("admin");
        int type = Connection.TYPE_HTTP;
        cc.setType(type);
        Connection connection = ConnectionFactory.getInstance().newConnection(cc);
        connection.addConnectionListener(listener);
        return connection;
    }

    private static ConnectionListener listener = new ConnectionListener() {

        public void connectionChanged(ConnectionEvent arg0) {
        }
    };
}
