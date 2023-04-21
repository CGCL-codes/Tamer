package amint.amintd;

import java.util.*;
import java.util.logging.Logger;
import java.sql.*;

public class Override {

    public static boolean coinDeleteByPaymentId(Conn conn, int paymentId) {
        try {
            int j1 = 1;
            String query = "delete from coins" + "where payment_id = ?";
            if (!conn.isRoot()) {
                query = query + " and owner_id = ?";
            }
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(j1++, paymentId);
            if (!conn.isRoot()) pstmt.setInt(j1++, conn.getOwnerId());
            int rows = pstmt.executeUpdate();
            conn.getLogger().info("" + rows + " rows deleted");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            conn.getLogger().severe(e.toString());
            return false;
        }
    }

    public static boolean coinDeleteByRowId(Conn conn, int rowId) {
        try {
            int j1 = 1;
            String query = "delete from coins" + "where row_id = ?";
            if (!conn.isRoot()) {
                query = query + " and owner_id = ?";
            }
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(j1++, rowId);
            if (!conn.isRoot()) pstmt.setInt(j1++, conn.getOwnerId());
            int rows = pstmt.executeUpdate();
            conn.getLogger().info("" + rows + " rows deleted");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            conn.getLogger().severe(e.toString());
            return false;
        }
    }

    public static boolean coinStatusByPaymentId(Conn conn, int newStatus, int paymentId) {
        try {
            int j1 = 1;
            String query = "update coins set coin_status = ? " + "where payment_id = ?";
            if (!conn.isRoot()) {
                query = query + " and owner_id = ?";
            }
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(j1++, newStatus);
            pstmt.setInt(j1++, paymentId);
            if (!conn.isRoot()) pstmt.setInt(j1++, conn.getOwnerId());
            int rows = pstmt.executeUpdate();
            conn.getLogger().info("" + rows + " rows updated");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            conn.getLogger().severe(e.toString());
            return false;
        }
    }

    public static boolean coinStatusByRowId(Conn conn, int newStatus, int rowId) {
        try {
            int j1 = 1;
            String query = "update coins set coin_status = ? " + "where row_id = ?";
            if (!conn.isRoot()) {
                query = query + " and owner_id = ?";
            }
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(j1++, newStatus);
            pstmt.setInt(j1++, rowId);
            if (!conn.isRoot()) pstmt.setInt(j1++, conn.getOwnerId());
            int rows = pstmt.executeUpdate();
            conn.getLogger().info("" + rows + " rows updated");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            conn.getLogger().severe(e.toString());
            return false;
        }
    }
}
