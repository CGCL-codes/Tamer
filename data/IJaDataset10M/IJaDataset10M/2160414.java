package com.ucsd.cse135.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Populate {

    /** Method to clean existing entries in tables to prevent duplicates **/
    public static boolean clean() {
        try {
            Connection conn = Database.open();
            PreparedStatement preStmt;
            preStmt = conn.prepareStatement("DELETE FROM university;");
            preStmt.executeUpdate();
            preStmt = conn.prepareStatement("DELETE FROM location;");
            preStmt.executeUpdate();
            preStmt = conn.prepareStatement("DELETE FROM specialization;");
            preStmt.executeUpdate();
            preStmt = conn.prepareStatement("DELETE FROM discipline;");
            preStmt.executeUpdate();
            Database.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Method to write location information to database **/
    public static String location(ArrayList<String> c, ArrayList<String> s) {
        try {
            Connection conn = Database.open();
            PreparedStatement preStmt;
            preStmt = conn.prepareStatement("INSERT INTO location (name, state) VALUES ( ? , ? );");
            for (int i = 0; i < s.size(); i++) {
                preStmt.setString(1, s.get(i));
                preStmt.setInt(2, 1);
                preStmt.executeUpdate();
            }
            preStmt = conn.prepareStatement("INSERT INTO location (name) VALUES ( ? );");
            for (int i = 0; i < c.size(); i++) {
                preStmt.setString(1, c.get(i));
                preStmt.executeUpdate();
            }
            Database.close();
            return ("All countries and states were written to the database successfully.<br>");
        } catch (SQLException se) {
            String msg = "There was an error while writing countries to the database!\n\n";
            msg = msg + se.getMessage();
            return (msg);
        } catch (Exception e) {
            String msg = "There was an undefined error!<br>";
            msg = msg + e.getMessage() + "<br>";
            msg = msg + e.getStackTrace() + "<br>";
            msg = msg + e.getCause() + "<br>";
            msg = msg + e.getLocalizedMessage() + "<br>";
            return (msg);
        }
    }

    /** Method to write specializations to database **/
    public static String specialization(ArrayList<String> al) {
        try {
            Connection conn = Database.open();
            PreparedStatement preStmt;
            preStmt = conn.prepareStatement("INSERT INTO specialization (name) VALUES ( ? );");
            for (int i = 0; i < al.size(); i++) {
                preStmt.setString(1, al.get(i));
                preStmt.executeUpdate();
            }
            Database.close();
            return ("All specialization were written to the database successfully.<br>");
        } catch (SQLException se) {
            String msg = "There was an error while writing specializations to the database!<br>";
            msg = msg + se.getMessage();
            return (msg);
        } catch (Exception e) {
            String msg = "There was an undefined error!<br>";
            msg = msg + e.getMessage() + "<br>";
            msg = msg + e.getStackTrace() + "<br>";
            msg = msg + e.getCause() + "<br>";
            msg = msg + e.getLocalizedMessage() + "<br>";
            return (msg);
        }
    }

    /** Method to write major information to database **/
    public static String major(ArrayList<String> al) {
        try {
            Connection conn = Database.open();
            PreparedStatement preStmt;
            preStmt = conn.prepareStatement("INSERT INTO discipline (name, custom) VALUES ( ?, 0 );");
            for (int i = 0; i < al.size(); i++) {
                preStmt.setString(1, al.get(i));
                preStmt.executeUpdate();
                System.out.println(al.get(i));
            }
            Database.close();
            return ("All disciplines were written to the database successfully.<br>");
        } catch (SQLException se) {
            String msg = "There was an error while writing disciplines to the database!<br>";
            msg = msg + se.getMessage();
            return (msg);
        } catch (Exception e) {
            String msg = "There was an undefined error!<br>";
            msg = msg + e.getMessage() + "<br>";
            msg = msg + e.getStackTrace() + "<br>";
            msg = msg + e.getCause() + "<br>";
            msg = msg + e.getLocalizedMessage() + "<br>";
            return (msg);
        }
    }

    /** Method to write universities to database **/
    public static String university(ArrayList<ArrayList<String>> al) {
        try {
            Connection conn = Database.open();
            PreparedStatement preStmt;
            for (int i = 0; i < al.size(); i++) {
                ArrayList<String> locationAL = al.get(i);
                preStmt = conn.prepareStatement("SELECT id FROM location WHERE name = ? ;");
                String location = locationAL.get(0);
                preStmt.setString(1, location);
                ResultSet rs = preStmt.executeQuery();
                int cid = 0;
                while (rs.next()) {
                    cid = rs.getInt("id");
                }
                for (int j = 1; j < locationAL.size(); j++) {
                    preStmt = conn.prepareStatement("INSERT INTO university (name, location, custom) VALUES ( ? , ? , 0);");
                    preStmt.setString(1, locationAL.get(j));
                    preStmt.setInt(2, cid);
                    preStmt.executeUpdate();
                }
            }
            Database.close();
            return ("All universities were written to the database successfully.<br>");
        } catch (SQLException se) {
            String msg = "There was an error while writing universities to the database!<br><br>";
            msg = msg + se.getMessage();
            return (msg);
        } catch (Exception e) {
            String msg = "There was an undefined error!<br>";
            msg = msg + e.getMessage() + "<br>";
            msg = msg + e.getStackTrace() + "<br>";
            msg = msg + e.getCause() + "<br>";
            msg = msg + e.getLocalizedMessage() + "<br>";
            return (msg);
        }
    }
}
