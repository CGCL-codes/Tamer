package itjava.db;

import itjava.data.LocalMachine;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Aniket
 * 
 */
public class WordInfoSQL {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DBConnection.GetConnection();
            java.sql.Statement stat = conn.createStatement();
            stat.executeUpdate("drop table if exists WordInfo;");
            stat.executeUpdate("create table WordInfo (" + " wordInfoId integer primary key autoincrement, " + " deliverableId integer references DeliverableInfo(deliverableId)," + " wordToBeBlanked varchar(30)," + " blankName varchar(20)," + " numOfHints integer default 0, " + " constraint uniqWordInfo unique(deliverableId, blankName) " + " );");
            conn.setAutoCommit(true);
            ResultSet rs = stat.executeQuery("select * from WordInfo;");
            while (rs.next()) {
                System.out.println("blankName = " + rs.getString("blankName"));
                System.out.println("numOfHints = " + rs.getInt("numOfHints"));
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
