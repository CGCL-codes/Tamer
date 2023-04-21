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

public class SupInfiltration {

    private String infiltration;

    public SupInfiltration(Connection c, String infiltration) throws SQLException, TodoException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select * from infiltration where factor='" + infiltration + "';");
        if (!rs.next()) {
            throw new TodoException("Record not found, id = " + infiltration);
        }
        this.infiltration = rs.getString("factor");
    }

    public static Vector getAllItems(Connection c) throws SQLException, TodoException {
        Vector V = new Vector();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM infiltration order by factor");
        while (rs.next()) {
            SupInfiltration t = new SupInfiltration(c, rs.getString("factor"));
            V.addElement(t);
        }
        return V;
    }

    public String Infiltration() {
        return infiltration;
    }
}
