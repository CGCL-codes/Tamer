package prefwork.rating.datasource;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class SQLConnectionProvider {

    protected String db;

    protected String url;

    protected String userName = "netflix";

    protected String password = "aaa";

    protected Connection conn;

    public abstract void connect() throws SQLException;

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
