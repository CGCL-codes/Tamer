package com.pandmservices;

import com.pandmservices.core.*;
import com.pandmservices.web.*;
import java.sql.*;
import java.lang.*;
import java.lang.System;
import java.util.*;
import java.util.Vector;
import java.util.Date;
import java.security.*;

public class CustFormList {

    private int formnum;

    private String formname;

    private String formdesc;

    private String formdate;

    private String custsite;

    private String sitenum;

    private String username;

    public CustFormList(Connection c, int formnum) throws SQLException, TodoException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select * from custformlist where formnum=" + formnum + "");
        if (!rs.next()) {
            throw new TodoException("Record not found, id = " + formnum);
        }
        this.formname = rs.getString("formname");
        this.formdesc = rs.getString("formdescription");
        this.formnum = rs.getInt("formnum");
        this.formdate = rs.getString("formdate");
        this.custsite = rs.getString("custsite");
        this.sitenum = rs.getString("sitenum");
        this.username = rs.getString("username");
    }

    public static Vector getIndItem(Connection c, String formnum) throws SQLException, TodoException {
        Vector V = new Vector();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM custformlist where formnum=" + formnum + ";");
        while (rs.next()) {
            CustFormList t = new CustFormList(c, rs.getInt("formnum"));
            V.addElement(t);
        }
        return V;
    }

    public static Vector getMaxItem(Connection c) throws SQLException, TodoException {
        Vector V = new Vector();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("Select formnum,max(formnum) from custformlist group by formnum;");
        while (rs.next()) {
            CustFormList t = new CustFormList(c, rs.getInt("formnum"));
            V.addElement(t);
        }
        return V;
    }

    public static Vector getAllItems(Connection c, String custsite, String sitenum) throws SQLException, TodoException {
        Vector V = new Vector();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM custformlist where custsite='" + custsite + "' and sitenum='" + sitenum + "' order by formname;");
        while (rs.next()) {
            CustFormList t = new CustFormList(c, rs.getInt("formnum"));
            V.addElement(t);
        }
        return V;
    }

    public static void DeleteItem(Connection con, String formnum) throws SQLException {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("Delete from custformlist Where formnum=" + formnum + ";");
    }

    public static void UpdateItem(Connection con, String custsite, String sitenum, String formdate, String username, String formnum, String formname, String formdesc) throws SQLException {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("Update custformlist Set formname='" + formname + "',formdescription='" + formdesc + "', custsite='" + custsite + "',sitenum='" + sitenum + "',formdate='" + formdate + "', username='" + username + "' Where formnum=" + formnum + ";");
    }

    public static void AddItem(Connection con, String custsite, String sitenum, String formdate, String username, String formname, String formdesc) throws SQLException, NoSuchAlgorithmException, NoSuchProviderException {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO custformlist (custsite, sitenum, formdate, username, formname,formdescription) Values ('" + custsite + "','" + sitenum + "','" + formdate + "','" + username + "','" + formname + "','" + formdesc + "');");
    }

    public int getFormNum() {
        return formnum;
    }

    public String getFormName() {
        return formname;
    }

    public String getFormDesc() {
        return formdesc;
    }

    public String getSiteNum() {
        return sitenum;
    }

    public String getCustSite() {
        return custsite;
    }

    public String getFormDate() {
        return formdate;
    }

    public String getUserName() {
        return username;
    }
}
