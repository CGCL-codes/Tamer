package com.ucsd.cse135.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import com.ucsd.cse135.db.Database;

public class Discipline {

    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** Get all majors without the custom ones **/
    public static ArrayList<Discipline> getDisciplines() {
        try {
            Connection conn = Database.open();
            String sql = "SELECT id, name FROM discipline WHERE custom = 0;";
            PreparedStatement preStmt = conn.prepareStatement(sql);
            ResultSet rs = preStmt.executeQuery();
            ArrayList<Discipline> mAl = new ArrayList<Discipline>();
            while (rs.next()) {
                Discipline m = new Discipline();
                m.setId(rs.getInt("id"));
                m.setName(rs.getString("name"));
                mAl.add(m);
            }
            rs.close();
            preStmt.close();
            Database.close();
            return mAl;
        } catch (Exception e) {
            Database.close();
            return null;
        }
    }

    /** Store custom major to database **/
    public static int storeCustomDiscipline(String discipline) {
        try {
            Connection conn = Database.open();
            boolean match = false;
            int id = -1;
            String sql = "SELECT id FROM discipline WHERE name = ?;";
            PreparedStatement preStmt = conn.prepareStatement(sql);
            preStmt.setString(1, discipline);
            ResultSet rs = preStmt.executeQuery();
            while (rs.next()) {
                match = true;
                id = rs.getInt("id");
            }
            if (match == false) {
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT nextval('discipline_id_seq') FROM discipline_id_seq;");
                rs.next();
                id = rs.getInt("nextval");
                sql = "INSERT INTO discipline (id, name) VALUES (?, ?);";
                preStmt = conn.prepareStatement(sql);
                preStmt.setInt(1, id);
                preStmt.setString(2, discipline);
                preStmt.executeUpdate();
                rs.close();
                preStmt.close();
                Database.close();
                return id;
            }
            rs.close();
            preStmt.close();
            Database.close();
            return id;
        } catch (Exception e) {
            Database.close();
            return -1;
        }
    }
}
