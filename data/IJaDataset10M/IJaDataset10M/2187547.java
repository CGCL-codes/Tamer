package edu.ucsd.cse135.gas.logic.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import edu.ucsd.cse135.gas.logic.User;
import edu.ucsd.cse135.gas.logic.support.Database;

public class UserAdmin {

    public static ArrayList<User> getUsers() {
        try {
            Connection conn = Database.open();
            String sql = "SELECT * FROM user_role WHERE role != 'admin' ORDER BY mail;";
            PreparedStatement preStmt = conn.prepareStatement(sql);
            ResultSet rs = preStmt.executeQuery();
            ArrayList<User> uAl = new ArrayList<User>();
            while (rs.next()) {
                User u = new User();
                u.setMail(rs.getString("mail"));
                u.setRole(rs.getString("role"));
                uAl.add(u);
            }
            rs.close();
            preStmt.close();
            Database.close();
            return uAl;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void newUser(String mail, String role, String password) {
        try {
            Connection conn = Database.open();
            String sql = "INSERT INTO users (mail, role) VALUES (?, ?);";
            PreparedStatement preStmt = conn.prepareStatement(sql);
            preStmt.setString(1, mail);
            preStmt.setString(2, role);
            preStmt.executeUpdate();
            preStmt.close();
            Database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void editUser(String oldMail, String newMail, String password, String role) {
        try {
            Connection conn = Database.open();
            String sql = "UPDATE users SET mail = ?, password = md5(?) WHERE mail = ?;";
            PreparedStatement preStmt = conn.prepareStatement(sql);
            preStmt.setString(1, newMail);
            preStmt.setString(2, password);
            preStmt.setString(3, oldMail);
            preStmt.executeUpdate();
            preStmt.close();
            Database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(String mail) {
        try {
            Connection conn = Database.open();
            String sql = "DELETE users WHERE mail = ?;";
            PreparedStatement preStmt = conn.prepareStatement(sql);
            preStmt.setString(1, mail);
            preStmt.executeUpdate();
            preStmt.close();
            Database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
