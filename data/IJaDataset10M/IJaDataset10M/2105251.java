package timeregistrering_v01.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBM {

    private final String DATABASE_NAME = "andersthorborg2";

    private final String DATABASE_URL = "mysql.student.hum.au.dk";

    private final String USER_NAME = "andersthorborg2";

    private final String PASSWORD = "7t5hJ73X";

    private String JDBCString;

    public DBM() {
        createJDBCString();
    }

    public void update(String sqlStatement) {
        Connection con = null;
        try {
            con = getConnection();
            Statement st = con.createStatement();
            int affected = st.executeUpdate(sqlStatement);
            System.out.println("Succesfully updated - " + affected + " records affected..");
        } catch (Exception e) {
            System.err.println("Coudn't update - possible lack of connection");
            System.err.println(e.getMessage());
        } finally {
            try {
                if (con != null) con.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public ResultSet retrieveFromSql(String sqlStatement) {
        Connection con = null;
        try {
            con = getConnection();
            Statement st = con.createStatement();
            ResultSet data = st.executeQuery(sqlStatement);
            return data;
        } catch (Exception e) {
            System.err.println("Couldn't retrieve - possible lack of connection..");
            System.err.println(e.getMessage());
            return null;
        } finally {
            try {
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void createJDBCString() {
        JDBCString = "jdbc:mysql://" + DATABASE_URL + ":3306/" + DATABASE_NAME;
    }

    private Connection getConnection() throws SQLException {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(JDBCString, USER_NAME, PASSWORD);
        } catch (Exception e) {
            System.err.println("Could'nt get connection..");
            System.err.println(e.getMessage());
        }
        return con;
    }

    public String generateSQLSelect(String collumnNames, String tableNames) {
        String statement = "SELECT " + collumnNames + " from " + tableNames + ";";
        System.out.println("Statement:\n" + statement);
        return statement;
    }

    public String generateSQLSelectWhere(String collumnNames, String tableNames, String conditionCollumn, String conditionValue) {
        String statement = "SELECT " + collumnNames + " from " + tableNames + " WHERE " + conditionCollumn + " = " + conditionValue + ";";
        System.out.println("Statement:\n" + statement);
        return statement;
    }
}
