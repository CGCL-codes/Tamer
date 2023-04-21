package tag4m.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnectorPOSTGRESQL {

    Connection conn = null;

    private static DBConnectorPOSTGRESQL instance;

    public static DBConnectorPOSTGRESQL getDBConnector() {
        if (instance == null) {
            instance = new DBConnectorPOSTGRESQL();
            try {
                instance.connect();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://" + DBConfigPOSTGRESQL.HOST + "/" + DBConfigPOSTGRESQL.DATABASE, DBConfigPOSTGRESQL.USER, DBConfigPOSTGRESQL.PWD);
        System.out.println("DB connection OK.");
    }

    public boolean insertAccGyroInstrumentData(long mac, int ax, int ay, int az, int gx, int gy) {
        try {
            double t = System.currentTimeMillis();
            String SQL = "select add_data2('" + mac + "','" + ax + "," + ay + "," + az + "," + gx + "," + gy + "'," + (double) (t / 1000) + ")";
            Statement s = conn.createStatement();
            ResultSet r = s.executeQuery(SQL);
            r.next();
            System.out.println("Data inserted: " + SQL + " " + r.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertTempPresureHumiditySollar(long mac, int a, int b, int c, int d) {
        try {
            double t = System.currentTimeMillis();
            String SQL = "select add_data2('" + mac + "','" + a + "," + b + "," + c + "," + d + "'," + (double) (t / 1000) + ")";
            System.out.println(SQL);
            Statement s = conn.createStatement();
            ResultSet r = s.executeQuery(SQL);
            r.next();
            System.out.println("Data inserted: " + SQL + " " + r.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DBConnectorPOSTGRESQL dbc = new DBConnectorPOSTGRESQL();
        dbc.connect();
        dbc.insertTempPresureHumiditySollar(3087036903L, 1, 2, 3, 4);
        System.out.println(System.currentTimeMillis());
    }
}
