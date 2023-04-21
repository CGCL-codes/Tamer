package net.sourceforge.ecldbtool.connect;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;
import org.eclipse.core.runtime.CoreException;

public class ConnectionProfile {

    private DriverDescriptor driverDescriptor;

    private String connectionURL;

    private String username;

    private Connection connection;

    ConnectionProfile(DriverDescriptor driverDescriptor, String connectionURL, String username) {
        this.driverDescriptor = driverDescriptor;
        this.connectionURL = connectionURL;
        this.username = username;
    }

    public void connect(String password) throws SQLException, CoreException {
        Driver d = driverDescriptor.getDriver();
        Properties p = new Properties();
        p.put("user", username);
        p.put("password", password);
        connection = d.connect(connectionURL, p);
    }

    public Connection getConnection() {
        return connection;
    }

    public DriverDescriptor getDriverDescriptor() {
        if (driverDescriptor == null) {
            driverDescriptor = new DriverDescriptor("", "", "", null);
        }
        return driverDescriptor;
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public String getUsername() {
        return username;
    }
}
