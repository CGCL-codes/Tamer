package com.wwwc.index.ejb.admin;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import java.rmi.RemoteException;
import com.wwwc.index.servlet.CategoryDetails;

public class AdminBean implements SessionBean {

    private CategoryDetails categoryDetails;

    private Connection con;

    public void ejbCreate() throws CreateException {
    }

    public void ejbRemove() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void setSessionContext(SessionContext sc) {
    }

    public ArrayList select(String query) {
        ArrayList a = new ArrayList();
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        try {
            makeConnection();
            prepStmt = con.prepareStatement(query);
            rs = prepStmt.executeQuery();
            rsmd = rs.getMetaData();
            int colums = rsmd.getColumnCount();
            ArrayList aa = null;
            while (rs.next()) {
                aa = new ArrayList();
                for (int m = 1; m <= colums; m++) {
                    aa.add(rs.getString(m));
                }
                a.add(aa);
            }
            rs.close();
            prepStmt.close();
            releaseConnection();
        } catch (Exception e) {
            releaseConnection();
            System.out.println("AdminBean:select:Error:" + e.getMessage());
        }
        return a;
    }

    public String update(String query) {
        String error = null;
        try {
            makeConnection();
            PreparedStatement prepStmt = con.prepareStatement(query);
            prepStmt.executeUpdate();
            prepStmt.close();
            releaseConnection();
        } catch (SQLException e) {
            releaseConnection();
            error = e.getMessage();
        } catch (Exception e) {
            releaseConnection();
            error = e.getMessage();
        }
        return error;
    }

    public String createLanguageTable() {
        StringBuffer sbf = new StringBuffer();
        sbf.append("CREATE TABLE WWWC_LANGUAGE (");
        sbf.append("NAME VARCHAR_IGNORECASE(100) PRIMARY KEY,");
        sbf.append("ABBERAVIATION VARCHAR_IGNORECASE(10),");
        sbf.append("ACTIVE INTEGER(1)");
        sbf.append(");");
        return update(sbf.toString());
    }

    public String createDirectoryTable(String table_name) {
        if (table_name == null || table_name.length() == 0) {
            return "Please enter a table name.";
        }
        StringBuffer sbf = new StringBuffer();
        sbf.append("CREATE TABLE " + table_name);
        sbf.append("(ID VARCHAR(200) PRIMARY KEY,");
        sbf.append("TYPE INTEGER NOT NULL,");
        sbf.append("NAME VARCHAR_IGNORECASE(100) NOT NULL,");
        sbf.append("LINK_CONTEXT VARCHAR(100),");
        sbf.append("LINK VARCHAR(500),");
        sbf.append("PARENT_ID VARCHAR(200) NOT NULL,");
        sbf.append("GROUP_ID VARCHAR(200),");
        sbf.append("GROUP_NAME VARCHAR(200),");
        sbf.append("DIRECTORY_LEVEL INTEGER,");
        sbf.append("DIRECTORY_AGE INTEGER,");
        sbf.append("PREVIEW_LEVEL INTEGER,");
        sbf.append("PREVIEW_AGE INTEGER,");
        sbf.append("READ_LEVEL INTEGER,");
        sbf.append("READ_AGE INTEGER,");
        sbf.append("WRITE_LEVEL INTEGER,");
        sbf.append("WRITE_AGE INTEGER,");
        sbf.append("MANAGERS VARCHAR_IGNORECASE(500),");
        sbf.append("ADMINS VARCHAR_IGNORECASE(100));");
        return update(sbf.toString());
    }

    private void makeConnection() {
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/DefaultDS");
            con = ds.getConnection();
        } catch (Exception e) {
            releaseConnection();
            System.out.println("EJB:CategoryBean: makeConnection error:" + e.getMessage());
        }
    }

    private void releaseConnection() {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("CategoryBean: Reaease connect error: " + e.getMessage());
        }
    }
}
