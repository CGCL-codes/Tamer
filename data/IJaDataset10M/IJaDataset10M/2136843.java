package com.rooster.action.vendor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.rowset.CachedRowSet;
import com.sun.rowset.CachedRowSetImpl;

public class GetMySubmissionsDB {

    String DB_URL;

    String DB_USERNAME;

    String DB_PASSWORD;

    Connection conSubmissions = null;

    Connection conSubmissions2 = null;

    Connection conSubmissions3 = null;

    String sError = "";

    /** Creates a new instance of DBConnect */
    public GetMySubmissionsDB(String DB_URL, String DB_USERNAME, String DB_PASSWORD) {
        this.setDB_URL(DB_URL);
        this.setDB_USERNAME(DB_USERNAME);
        this.setDB_PASSWORD(DB_PASSWORD);
    }

    private void createConnection101() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conSubmissions = DriverManager.getConnection(this.getDB_URL(), this.getDB_USERNAME(), this.getDB_PASSWORD());
        } catch (Exception e) {
        }
    }

    private void createConnection102() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conSubmissions2 = DriverManager.getConnection(this.getDB_URL(), this.getDB_USERNAME(), this.getDB_PASSWORD());
        } catch (Exception e) {
        }
    }

    private void createConnection103() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conSubmissions3 = DriverManager.getConnection(this.getDB_URL(), this.getDB_USERNAME(), this.getDB_PASSWORD());
        } catch (Exception e) {
        }
    }

    public CachedRowSet getRs(String sql) {
        CachedRowSet crsSubmissions = null;
        try {
            if (conSubmissions3 == null) {
                createConnection103();
            }
            Statement stmt = conSubmissions3.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            crsSubmissions = new CachedRowSetImpl();
            crsSubmissions.populate(rs);
            rs.close();
            stmt.close();
            rs = null;
            stmt = null;
        } catch (Exception e) {
            sError = e.toString();
        } finally {
            conSubmissions3 = null;
        }
        return crsSubmissions;
    }

    public ResultSet getRows(String sql) {
        try {
            if (conSubmissions == null) {
                createConnection101();
            }
            Statement stmt = conSubmissions.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return rs;
        } catch (Exception e) {
        } finally {
            conSubmissions = null;
        }
        return null;
    }

    public void deleteRows(String sql) {
        try {
            if (conSubmissions2 == null) {
                createConnection102();
            }
            Statement stmt2 = conSubmissions2.createStatement();
            stmt2.executeUpdate(sql);
        } catch (Exception e) {
        } finally {
            conSubmissions2 = null;
        }
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
