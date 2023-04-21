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

public class UniInvAll {

    private int catnum;

    private int itemnum;

    private String itemname;

    private String keycode;

    private String minquantity;

    private String tquant = null;

    public UniInvAll(Connection c, int itemnum) throws SQLException, TodoException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select * from inv_items where itemnum='" + itemnum + "';");
        if (!rs.next()) {
            throw new TodoException("Record not found, id = " + itemnum);
        }
        this.minquantity = rs.getString("minquant");
        this.itemname = rs.getString("itemname");
        this.keycode = rs.getString("description");
        this.catnum = rs.getInt("invcatnum");
        this.itemnum = rs.getInt("itemnum");
        rs.close();
        Statement stmt2 = c.createStatement();
        ResultSet rs2 = stmt2.executeQuery("SELECT sum(quantity) as quant from inv_detail where inv_detail.itemnum='" + this.itemnum + "' group by inv_detail.itemnum");
        while (rs2.next()) {
            this.tquant = rs2.getString("quant");
        }
    }

    public static Vector getIndividualItems(Connection c, int itemnum) throws SQLException, TodoException {
        Vector V = new Vector();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM inv_items where itemnum=" + itemnum + "");
        while (rs.next()) {
            UniInvAll t = new UniInvAll(c, rs.getInt("itemnum"));
            V.addElement(t);
        }
        rs.close();
        return V;
    }

    public static Vector getAllItems(Connection c, String keycode) throws SQLException, TodoException {
        Vector V = new Vector();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM inv_items where description='" + keycode + "' ");
        while (rs.next()) {
            UniInvAll t = new UniInvAll(c, rs.getInt("itemnum"));
            V.addElement(t);
        }
        return V;
    }

    public int getCatNum() {
        return catnum;
    }

    public int getItemNum() {
        return itemnum;
    }

    public String getItemName() {
        return itemname;
    }

    public String getKeyCode() {
        return keycode;
    }

    public String getMinQuantity() {
        return minquantity;
    }

    public String getActualQuant() {
        return tquant;
    }

    public void setItemNum(int itemnum) {
        this.itemnum = itemnum;
    }
}
