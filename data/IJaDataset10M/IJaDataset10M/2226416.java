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

public class SupDoorType {

    private String doortype;

    public SupDoorType(Connection c, String doortype) throws SQLException, TodoException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select * from doortype where doortype='" + doortype + "';");
        if (!rs.next()) {
            throw new TodoException("Record not found, id = " + doortype);
        }
        this.doortype = rs.getString("doortype");
    }

    public static Vector getAllItems(Connection c) throws SQLException, TodoException {
        Vector V = new Vector();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM doortype order by doortype");
        while (rs.next()) {
            SupDoorType t = new SupDoorType(c, rs.getString("doortype"));
            V.addElement(t);
        }
        return V;
    }

    public String DoorType() {
        return doortype;
    }
}
