package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;

public class Petze {

    public Petze(Exception error) {
        System.err.println(error);
        String url = "jdbc:mysql://web1.stadtzurwelt.de:3306/usr_web1_2";
        String password = "IqrrlCrB";
        String username = "web1";
        Connection con;
        Date d = new Date();
        String query = "INSERT  INTO `log` ( `uid` , `Fehler` , `dt` )" + "VALUES ('','" + error.toString() + error.getMessage() + "', '" + d.toLocaleString().toString() + "')";
        Statement stmt;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (java.lang.ClassNotFoundException e) {
            System.out.print("ClassNotFoundException: ");
            System.out.println(e.getMessage());
        }
        try {
            con = DriverManager.getConnection(url, username, password);
            stmt = con.createStatement();
            int rs = stmt.executeUpdate(query);
            System.out.println("rs:" + rs);
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.out.print("SQLException: ");
            System.out.println(ex.getMessage());
        }
    }

    public Petze(String s) {
        System.out.println(s);
        if (s.equals("Start")) s += " System:" + System.getProperty("os.name", "unbekannt") + " java.version:" + System.getProperty("java.version", "unbekannt") + " java.home:" + System.getProperty("java.home", "unbekannt") + " os.version:" + System.getProperty("os.version", "unbekannt") + " user.name:" + System.getProperty("user.name", "unbekannt") + " user.home:" + System.getProperty("user.home", "unbekannt") + " user.dir:" + System.getProperty("user.dir", "unbekannt");
        String url = "jdbc:mysql://web1.stadtzurwelt.de:3306/usr_web1_2";
        String password = "IqrrlCrB";
        String username = "web1";
        Connection con;
        Date d = new Date();
        String query = "INSERT  INTO `log` ( `uid` , `Fehler` , `dt` )" + "VALUES ('','" + s + "', '" + d.toLocaleString().toString() + "')";
        Statement stmt;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }
        try {
            con = DriverManager.getConnection(url, username, password);
            stmt = con.createStatement();
            int rs = stmt.executeUpdate(query);
            System.out.println("rs:" + rs);
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.err.print("SQLException: ");
            System.err.println(ex.getMessage());
        }
    }

    public Petze(Object o) {
        System.out.println(ToStringHelper.toString(o));
    }
}
