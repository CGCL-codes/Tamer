package com.rooster.action.hotlist.candidates;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.rowset.CachedRowSet;
import com.sun.rowset.CachedRowSetImpl;

public class GetHotlistCandidatesDB {

    String DB_URL;

    String DB_USERNAME;

    String DB_PASSWORD;

    Connection conHotlist = null;

    String sError = "";

    /** Creates a new instance of DBConnect */
    public GetHotlistCandidatesDB(String DB_URL, String DB_USERNAME, String DB_PASSWORD) {
        this.setDB_URL(DB_URL);
        this.setDB_USERNAME(DB_USERNAME);
        this.setDB_PASSWORD(DB_PASSWORD);
    }

    private void createConnectionHotlist() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conHotlist = DriverManager.getConnection(this.getDB_URL(), this.getDB_USERNAME(), this.getDB_PASSWORD());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CachedRowSet getRs(String sql) {
        CachedRowSet crs = null;
        try {
            if (conHotlist == null) {
                createConnectionHotlist();
            }
            Statement stmt = conHotlist.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            crs = new CachedRowSetImpl();
            crs.populate(rs);
            rs.close();
            stmt.close();
            rs = null;
            stmt = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conHotlist.close();
            } catch (Exception e) {
            }
            conHotlist = null;
        }
        return crs;
    }

    public String getDB_URL() {
        return DB_URL;
    }

    public void setDB_URL(String db_url) {
        DB_URL = db_url;
    }

    public String getDB_USERNAME() {
        return DB_USERNAME;
    }

    public void setDB_USERNAME(String db_username) {
        DB_USERNAME = db_username;
    }

    public String getDB_PASSWORD() {
        return DB_PASSWORD;
    }

    public void setDB_PASSWORD(String db_password) {
        DB_PASSWORD = db_password;
    }
}
