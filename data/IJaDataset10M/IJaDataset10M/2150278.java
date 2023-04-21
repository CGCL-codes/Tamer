package com.pandmservices;

import java.sql.*;
import com.pandmservices.core.*;
import com.pandmservices.web.*;
import java.lang.*;
import java.util.*;
import java.util.Vector;

public class FuelPurch {

    private int recnum;

    private String callslip = null;

    private String descript = null;

    private double quant = 0;

    private double price = 0;

    private double total = 0;

    public FuelPurch(Connection c, int recnum) throws SQLException, TodoException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM svc_charges WHERE recnum=" + recnum + ";");
        if (!rs.next()) {
            throw new TodoException("Record not found, id = " + recnum);
        }
        this.recnum = rs.getInt("recnum");
        this.callslip = rs.getString("callslip");
        this.descript = rs.getString("descript");
        this.quant = rs.getDouble("quant");
        this.price = rs.getDouble("price");
        this.total = this.quant * this.price;
    }

    public static Vector getAllItems(Connection c, String callslip) throws SQLException, TodoException {
        Vector V = new Vector();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT recnum FROM svc_charges where callslip='" + callslip + "' ORDER BY descript;");
        while (rs.next()) {
            FuelPurch t = new FuelPurch(c, rs.getInt("recnum"));
            V.addElement(t);
        }
        return V;
    }

    public static Vector getIndItem(Connection c, int crec) throws SQLException, TodoException {
        Vector V = new Vector();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT recnum FROM svc_charges where recnum='" + crec + "';");
        while (rs.next()) {
            FuelPurch t = new FuelPurch(c, rs.getInt("recnum"));
            V.addElement(t);
        }
        return V;
    }

    public static void UpdateItem(Connection con, int recnum, String callslip, String descript, String quant, String price) throws SQLException {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("Update svc_charges Set callslip='" + callslip + "', descript='" + descript + "', quant='" + quant + "', price='" + price + "' Where recnum=" + recnum + ";");
    }

    public static void deleteItem(Connection con, int recnum) throws SQLException {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("Delete From svc_charges Where recnum=" + recnum + ";");
    }

    public static void AddItem(Connection con, String callslip, String descript, String quant, String price) throws SQLException {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("INSERT INTO svc_charges (callslip, descript, quant, price) Values ('" + callslip + "','" + descript + "','" + quant + "','" + price + "')");
    }

    public int getRecnum() {
        return recnum;
    }

    public String getCallslip() {
        return callslip;
    }

    public String getDescript() {
        return descript;
    }

    public double getQuant() {
        return quant;
    }

    public double getPrice() {
        return price;
    }

    public double getTotal() {
        return total;
    }
}
