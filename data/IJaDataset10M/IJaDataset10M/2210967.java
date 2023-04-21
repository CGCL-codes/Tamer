package com.pandmservices;

import com.pandmservices.core.*;
import com.pandmservices.web.*;
import java.sql.*;
import java.lang.*;
import java.lang.System;
import java.lang.Double;
import java.util.*;
import java.util.Vector;
import java.util.Date;
import java.security.*;

public class PropReport {

    private String wsdesc;

    private String wsdate;

    private String cname;

    private String homephone;

    private String city;

    private String qstatus;

    private int wsrec;

    private int custnum;

    public PropReport(Connection c, int transnum) throws SQLException, TodoException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT quotes.quotenum, quotes.status, quotes.crecnum, quotes.qdescription, quotes.qdate, customers.custnum, customers.cname, customers.city, customers.homephone FROM quotes, customers where quotes.crecnum=customers.custnum and quotes.quotenum=" + transnum + " GROUP BY quotes.status ORDER BY quotes.status, customers.cname, quotes.qdate;");
        while (rs.next()) {
            this.wsrec = rs.getInt("quotes.quotenum");
            this.wsdesc = rs.getString("quotes.qdescription");
            this.wsdate = rs.getString("quotes.qdate");
            this.cname = rs.getString("customers.cname");
            this.qstatus = rs.getString("quotes.status");
            this.city = rs.getString("customers.city");
            this.homephone = rs.getString("customers.homephone");
        }
    }

    public static Vector getAllItems(Connection c, String startdate, String enddate) throws SQLException, TodoException {
        Vector V = new Vector();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT quotenum from quotes where qdate>='" + startdate + "' and qdate<='" + enddate + "' ORDER BY status, crecnum, qdate;");
        while (rs.next()) {
            PropReport t = new PropReport(c, rs.getInt("quotenum"));
            V.addElement(t);
        }
        return V;
    }

    public int getQNum() {
        return wsrec;
    }

    public int getCustNum() {
        return custnum;
    }

    public String getQDesc() {
        return wsdesc;
    }

    public String getQStatus() {
        return qstatus;
    }

    public String getQDate() {
        return wsdate;
    }

    public String getCName() {
        return cname;
    }

    public String getCity() {
        return city;
    }

    public String getHomePhone() {
        return homephone;
    }
}
