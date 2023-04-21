package crm.server.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

/**
 * @author Maxim Tolstyh
 * @version 1.0 25/08/2010
 */
public class DBConnector {

    private Connection conn;

    private Statement state;

    public ResultSet res;

    private String url = "jdbc:mysql://localhost:3306/";

    private String usr;

    private String passwd;

    public DBConnector() {
    }

    public void executeSQL(String q) {
        try {
            state = (Statement) conn.createStatement();
            res = state.executeQuery(q);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
	 * 
	 * @return
	 */
    public boolean connect() {
        try {
            conn = (Connection) DriverManager.getConnection(url, usr, passwd);
            if (conn == null) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public void releaseQ() {
        try {
            state.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            res.close();
            state.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @return the url
	 */
    public final String getUrl() {
        return url;
    }

    /**
	 * @param url
	 *            the url to set
	 */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
	 * @return the usr
	 */
    public final String getUsr() {
        return usr;
    }

    /**
	 * @param usr
	 *            the usr to set
	 */
    public void setUsr(String usr) {
        this.usr = usr;
    }

    /**
	 * @return the passwd
	 */
    public final String getPasswd() {
        return passwd;
    }

    /**
	 * @param passwd
	 *            the passwd to set
	 */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    /**
	 * 
	 * @param conn
	 */
    public void setConn(Connection conn) {
        this.conn = conn;
    }

    /**
	 * 
	 * @return
	 */
    public final Connection getConn() {
        return conn;
    }

    /**
	 * @param state
	 *            the state to set
	 */
    public void setState(Statement state) {
        this.state = state;
    }

    /**
	 * @return the state
	 */
    public Statement getState() {
        return state;
    }
}
