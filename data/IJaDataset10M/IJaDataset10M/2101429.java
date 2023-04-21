package sm.dao;

import java.lang.ClassNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LeilibPC
 */
public class DBManager {

    private String driver = "com.mysql.jdbc.Driver";

    private String sqlurl = "jdbc:mysql://127.0.0.1:3306/salary_management_system";

    private String userName = "root";

    private String password = "602382";

    private Connection connection = null;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(this.sqlurl, this.userName, this.password);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public void close() {
        if (this.connection != null) {
            try {
                if (!this.connection.isClosed()) {
                    try {
                        this.connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public ResultSet executeQuery(String sql) {
        ResultSet rs = null;
        try {
            Statement statement = this.getConnection().createStatement();
            rs = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet executeQuery(String sql, Object... objects) {
        ResultSet rs = null;
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
            for (int i = 0; i < objects.length; ++i) {
                preparedStatement.setObject(i + 1, objects[i]);
            }
            rs = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public int executeUpdate(String sql) {
        int rows = 0;
        try {
            Statement st = this.getConnection().createStatement();
            rows = st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public int executeUpdate(String sql, Object... objects) {
        int rows = 0;
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                preparedStatement.setObject(i + 1, objects[i]);
            }
            rows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }
}
