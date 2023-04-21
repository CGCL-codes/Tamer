package document.lookup;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WritePlainFiles {

    /**
	 * Opens the database
	 * 
	 */
    public static Connection openDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/digg?useCursorFetch=true", "dbuser", "dbpass");
            return con;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String args[]) {
        Connection con = openDatabase();
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT docid from document_order order by orderid");
            PreparedStatement content = con.prepareStatement("SELECT body from newsitem where docid = ?");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int docID = rs.getInt(1);
                content.setInt(1, docID);
                ResultSet cRS = content.executeQuery();
                if (cRS.next()) {
                    PrintWriter rel = new PrintWriter(new BufferedWriter(new FileWriter("/home/pon3/dtfndry2/data/digg/files/" + docID)));
                    rel.print(cRS.getString(1).replaceAll("<.+/>", "").replaceAll("<.+>", "").replaceAll("\\{.+\\}", "").replaceAll("&.+;", ""));
                    rel.flush();
                    rel.close();
                }
            }
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
