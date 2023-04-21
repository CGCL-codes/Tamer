package com.pandmservices.support;

import com.pandmservices.*;
import com.pandmservices.core.*;
import com.pandmservices.web.*;
import java.sql.*;
import java.lang.*;
import java.lang.System;
import java.util.*;
import java.util.Vector;
import java.util.Date;
import java.security.*;

public class HeatLoadValues {

    private String fieldname;

    private String fieldselection;

    private String coolvalue;

    private String heatvalue;

    public HeatLoadValues(Connection c, String fieldname, String fieldselection) throws SQLException, TodoException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select * from heatloadvalues where fieldname='" + fieldname + "' and fieldselection='" + fieldselection + "';");
        if (!rs.next()) {
            throw new TodoException("Record not found, id = " + fieldname);
        }
        this.fieldname = rs.getString("fieldname");
        this.fieldselection = rs.getString("fieldselection");
        this.coolvalue = rs.getString("coolvalue");
        this.heatvalue = rs.getString("heatvalue");
    }

    public static Vector getIndItem(Connection c, String fieldname, String fieldvalue) throws SQLException, TodoException {
        Vector V = new Vector();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM heatloadvalues where fieldname='" + fieldname + "' and fieldselection='" + fieldvalue + "';");
        while (rs.next()) {
            HeatLoadValues t = new HeatLoadValues(c, rs.getString("fieldname"), rs.getString("fieldselection"));
            V.addElement(t);
        }
        return V;
    }

    public String fieldname() {
        return fieldname;
    }

    public String fieldselection() {
        return fieldselection;
    }

    public String coolvalue() {
        return coolvalue;
    }

    public String heatvalue() {
        return heatvalue;
    }
}
