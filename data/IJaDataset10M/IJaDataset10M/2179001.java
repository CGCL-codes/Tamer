package amint.amintd;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.sql.*;
import amint.util.*;

public class Admin {

    private Conn conn;

    private Logger logger;

    private Admin() {
    }

    public static Admin createAdmin(Conn conn) {
        if (!conn.isRoot()) return null;
        Admin admin = new Admin();
        admin.logger = conn.logger;
        admin.conn = conn;
        return admin;
    }

    public boolean deleteMint(int mintId) {
        try {
            String query = "delete from coins where mint_id = ?";
            PreparedStatement pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, mintId);
            pstmt.executeUpdate();
            query = "delete from protocoins where mint_id = ?";
            pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, mintId);
            pstmt.executeUpdate();
            query = "delete from spent_coins where mint_id = ?";
            pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, mintId);
            pstmt.executeUpdate();
            query = "delete from payments where mint_id = ?";
            pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, mintId);
            pstmt.executeUpdate();
            query = "delete from coin_keys where mint_id = ?";
            pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, mintId);
            pstmt.executeUpdate();
            query = "delete from mint_services where mint_id = ?";
            pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, mintId);
            pstmt.executeUpdate();
            query = "delete from mints where mint_id = ?";
            pstmt = conn.conn.prepareStatement(query);
            pstmt.setInt(1, mintId);
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            logger.severe(e.toString());
            return false;
        }
    }

    public int ownerCreate(String name, String password) {
        try {
            int j1 = 1;
            int owner_id = -1;
            PreparedStatement pstmt = conn.conn.prepareStatement("insert into owners (owner,password)" + "values (?,?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(j1++, name);
            pstmt.setString(j1++, password);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                owner_id = rs.getInt(1);
                logger.info("created new owner id=" + owner_id);
            } else {
                logger.severe("unable to create new owner");
            }
            return owner_id;
        } catch (Exception e) {
            logger.severe("Error in Admin ownerCreate");
            logger.severe(e.toString());
            return -1;
        }
    }

    public int ownerIdFromOwner(String ownerName) {
        try {
            int j1 = 1;
            int owner_id = -1;
            PreparedStatement pstmt = conn.conn.prepareStatement("select owner_id from owners where owner = ?");
            pstmt.setString(j1++, ownerName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                owner_id = rs.getInt(1);
            } else {
                logger.warning("owner not found");
            }
            return owner_id;
        } catch (Exception e) {
            logger.severe("Error in Admin ownerIdFromOwner");
            logger.severe(e.toString());
            return -1;
        }
    }

    public boolean ownerPasswordChange(int ownerId, String password) {
        try {
            int j1 = 1;
            int owner_id = -1;
            PreparedStatement pstmt = conn.conn.prepareStatement("update owners set password = ? where owner_id = ?");
            pstmt.setString(j1++, password);
            pstmt.setInt(j1++, ownerId);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                logger.warning("owner not found");
                return false;
            }
            logger.info("changed password");
            return true;
        } catch (Exception e) {
            logger.severe("Error in Admin ownerPasswordChange");
            logger.severe(e.toString());
            return false;
        }
    }

    public StringTable getOwnersList() {
        Vector v = new Vector<String[]>();
        try {
            String[] headers = { "owner", "owner_id" };
            StringTable st = new StringTable(headers);
            PreparedStatement pstmt = conn.conn.prepareStatement("select owner,owner_id from owners order by owner_id asc");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] s = new String[2];
                s[0] = rs.getString(1);
                s[1] = "" + rs.getInt(2);
                st.addRow(s);
            }
            return st;
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe(e.toString());
            return null;
        }
    }
}
