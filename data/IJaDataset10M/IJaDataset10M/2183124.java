package fvm.sql;

import java.util.*;
import java.sql.*;

public interface IDriverManager {

    public void loadDriver(String driverClassName);

    public Connection getConnection(String url, Properties info) throws SQLException;

    public Connection getConnection(String url, String user, String password) throws SQLException;

    public Connection getConnection(String url) throws SQLException;

    public Driver getDriver(String url) throws SQLException;

    public void registerDriver(java.sql.Driver driver) throws SQLException;

    public void deregisterDriver(Driver driver) throws SQLException;

    public Enumeration getDrivers();

    public void setLoginTimeout(int seconds);

    public int getLoginTimeout();
}
