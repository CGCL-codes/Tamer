package com.pandmservices.dbserver;

import com.pandmservices.core.*;
import com.pandmservices.web.*;
import com.pandmservices.*;
import java.sql.*;
import java.lang.*;
import java.lang.System;
import java.util.*;
import java.util.Vector;
import java.util.Date;
import java.security.*;

public class UniFlatDate {

    private String dupdated;

    public UniFlatDate(Connection c, String dupdated) throws SQLException, TodoException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select * from flat_rate_date where updated='" + dupdated + "';");
        if (!rs.next()) {
            throw new TodoException("Record not found, id = " + dupdated);
        }
        this.dupdated = rs.getString("updated");
    }

    public static Vector getAllItems(Connection c) throws SQLException, TodoException {
        Vector V = new Vector();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM flat_rate_date");
        while (rs.next()) {
            UniFlatDate t = new UniFlatDate(c, rs.getString("updated"));
            V.addElement(t);
        }
        return V;
    }

    public static void UpdateItem(Connection con, String dupdated) throws SQLException {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("Update flat_rate_date Set servername='" + dupdated + "';");
    }

    public String getDateUpdated() {
        return dupdated;
    }
}
