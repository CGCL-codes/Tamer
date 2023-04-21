package com.pandmservices;

import java.util.Date;
import java.math.*;
import java.text.*;
import java.lang.String;
import java.sql.*;
import com.pandmservices.core.*;
import com.pandmservices.web.*;
import java.lang.*;
import java.util.*;
import java.util.Vector;

public class PrintPagreement {

    private int contnum = 0;

    private int servsync = 0;

    private int eenum = 0;

    private String mbody = "";

    private int ecustnum = 0;

    private String brand = null;

    private String modelnum = null;

    private String serialnum = null;

    private String filter = null;

    private String enotes = null;

    private String type = null;

    private int enum1 = 0;

    private int enum2 = 0;

    private int enum3 = 0;

    private int enum4 = 0;

    private int enum5 = 0;

    private int enum6 = 0;

    private int enum7 = 0;

    private int enum8 = 0;

    private int enum9 = 0;

    private int enum10 = 0;

    private String aservice = null;

    private String startdate = null;

    private String enddate = null;

    private int term = 0;

    private String cost = null;

    private String notes = null;

    private String agrdate = null;

    private int vperyear = 0;

    private String visit1 = null;

    private String visit2 = null;

    private String visit3 = null;

    private String visit4 = null;

    private String visit5 = null;

    private String visit6 = null;

    private String custname = null;

    private String sitenum = null;

    private String custsite = null;

    private String cname = null;

    private String address1 = null;

    private String address2 = null;

    private String city = null;

    private String state = null;

    private int totalvisits = 0;

    private String zip = null;

    private String homephone = null;

    private String altphone = null;

    private String cust_notes = null;

    private String psform = null;

    private String etable = "";

    private String vtable = "";

    private String custtype = null;

    private int id;

    private int custnum = 0;

    public PrintPagreement(Connection c, int id, String username) throws SQLException, TodoException, Exception {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM pagreement WHERE contnum=" + id + ";");
        if (!rs.next()) {
            throw new TodoException("Record not found, id = " + id);
        }
        this.enum1 = rs.getInt("enum1");
        this.enum2 = rs.getInt("enum2");
        this.enum3 = rs.getInt("enum3");
        this.enum4 = rs.getInt("enum4");
        this.enum5 = rs.getInt("enum5");
        this.enum6 = rs.getInt("enum6");
        this.enum7 = rs.getInt("enum7");
        this.enum8 = rs.getInt("enum8");
        this.enum9 = rs.getInt("enum9");
        this.enum10 = rs.getInt("enum10");
        this.aservice = rs.getString("aservice");
        this.visit1 = rs.getString("visit1");
        this.visit2 = rs.getString("visit2");
        this.visit3 = rs.getString("visit3");
        this.visit4 = rs.getString("visit4");
        this.visit5 = rs.getString("visit5");
        this.visit6 = rs.getString("visit6");
        this.startdate = rs.getString("startdate");
        this.enddate = rs.getString("enddate");
        this.term = rs.getInt("term");
        this.cost = rs.getString("cost");
        this.notes = rs.getString("notes");
        this.agrdate = doFormatDate(getDate(rs.getString("agrdate")));
        this.vperyear = rs.getInt("vperyear");
        this.servsync = rs.getInt("servsync");
        this.custnum = rs.getInt("custnum");
        totalvisits = term * vperyear;
        Vector vc;
        vc = UniCustomer.getIndItem(c, custnum);
        for (int i = 0; i < vc.size(); i++) {
            UniCustomer t = (UniCustomer) vc.elementAt(i);
            this.custname = t.getCustomerName();
            this.address1 = t.getAddress1();
            this.address2 = t.getAddress2();
            this.city = t.getCity();
            this.state = t.getState();
            this.zip = t.getZip();
            this.custsite = t.getCustSite();
            this.sitenum = t.getSiteNum();
            this.homephone = t.getHomePhone();
            this.altphone = t.getAltPhone();
            this.custtype = t.getCustType();
        }
        Vector v;
        v = ResPsForm.getAllItems(c);
        int counter = 0;
        for (int i = 0; i < v.size(); i++) {
            ResPsForm tp = (ResPsForm) v.elementAt(i);
            this.psform = tp.getResPsForm();
        }
        stmt = c.createStatement();
        ResultSet rs1 = stmt.executeQuery("SELECT * FROM  equipment where enum='" + enum1 + "' or enum='" + enum2 + "' or enum='" + enum3 + "' or enum='" + enum4 + "' or enum='" + enum5 + "' or enum='" + enum6 + "' or enum='" + enum7 + "' or enum='" + enum8 + "' or enum='" + enum9 + "' or enum='" + enum10 + "';");
        etable = UniCash.combinestring(etable, "<table border=1 width=95% font=\"-2\">");
        etable = UniCash.combinestring(etable, "<th>Brand</th><th>Model</th><th>Serial</th><th>Filter</th><th>Type</th><th>Notes</th>");
        while (rs1.next()) {
            brand = rs1.getString("brand");
            modelnum = rs1.getString("modelnum");
            serialnum = rs1.getString("serialnum");
            filter = rs1.getString("filter");
            notes = rs1.getString("notes");
            type = rs1.getString("etype");
            etable = UniCash.combinestring(etable, "<tr><td>" + brand + "</td><td>" + modelnum + "</td><td>" + serialnum + "</td><td>" + filter + "</td><td>" + type + "</td><td>" + notes + "</tr>");
        }
        etable = UniCash.combinestring(etable, "</table> ");
        if ((visit1 != null) && !visit1.equalsIgnoreCase("-")) {
            vtable = UniCash.combinestring(vtable, "<br><br> ");
            vtable = UniCash.combinestring(vtable, "<center><h3>Schedule of Visits</h3></center>");
            vtable = UniCash.combinestring(vtable, "<table width=\"50%\" align=left border=\"1\">");
            if ((visit1 != null) && !visit1.equalsIgnoreCase("-")) {
                vtable = UniCash.combinestring(vtable, "<tr><td>Visit 1:</td><td>" + visit1 + "</td></tr> ");
            }
            if ((visit2 != null) && !visit2.equalsIgnoreCase("-")) {
                vtable = UniCash.combinestring(vtable, "<tr><td>Visit 2:</td><td>" + visit2 + "</td></tr> ");
            }
            if ((visit3 != null) && !visit3.equalsIgnoreCase("-")) {
                vtable = UniCash.combinestring(vtable, "<tr><td>Visit 3:</td><td>" + visit3 + "</td></tr> ");
            }
            if ((visit4 != null) && !visit4.equalsIgnoreCase("-")) {
                vtable = UniCash.combinestring(vtable, "<tr><td>Visit 4:</td><td>" + visit4 + "</td></tr> ");
            }
            if ((visit5 != null) && !visit5.equalsIgnoreCase("-")) {
                vtable = UniCash.combinestring(vtable, "<tr><td>Visit 5:</td><td>" + visit5 + "</td></tr> ");
            }
            if ((visit6 != null) && !visit6.equalsIgnoreCase("-")) {
                vtable = UniCash.combinestring(vtable, "<tr><td>Visit 6:</td><td>" + visit6 + "</td></tr> ");
            }
            vtable = UniCash.combinestring(vtable, "</table><br><br>");
        }
        mbody = UniCash.combinestring(mbody, psform);
        String tmbody1 = mbody.replaceAll("%address1%", address1);
        String tmbody2 = tmbody1.replaceAll("%address2%", address2);
        String tmbody3 = tmbody2.replaceAll("%city%", city);
        String tmbody4 = tmbody3.replaceAll("%zip%", zip);
        String tmbody5 = tmbody4.replaceAll("%customername%", custname);
        String tmbody6 = tmbody5.replaceAll("%equipmenttable%", etable);
        String tmbody7 = tmbody6.replaceAll("%price%", cost);
        String tmbody8 = tmbody7.replaceAll("%term%", "" + term + "");
        String tmbody9 = tmbody8.replaceAll("%totalvisits%", "" + totalvisits + "");
        String tmbody10 = tmbody9.replaceAll("%agreementdate%", agrdate);
        String tmbody11 = tmbody10.replaceAll("%state%", state);
        String tmbody12 = "";
        if (aservice.length() > 1) {
            tmbody12 = tmbody11.replaceAll("%additionalservice%", "<br><h4>Additional Notes:</h4><br>" + aservice + "<br></td></tr>");
        } else {
            tmbody12 = tmbody11.replaceAll("%additionalservice%", "");
        }
        String tmbody13 = tmbody12.replaceAll("%techname%", doGetTechInfo_name(c, username));
        String tmbody14 = tmbody13.replaceAll("%visittable%", vtable);
        mbody = tmbody14;
    }

    public static Vector getIndItem(Connection c, int custnum, int contnum, String username) throws SQLException, TodoException, Exception {
        Vector V = new Vector();
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT contnum FROM pagreement where custnum='" + custnum + "' and contnum = '" + contnum + "' ORDER BY contnum;");
        while (rs.next()) {
            PrintPagreement t = new PrintPagreement(c, rs.getInt("contnum"), username);
            V.addElement(t);
        }
        return V;
    }

    public static String combinestring(String oldstring, String newstring) {
        String rstring = oldstring.concat(newstring);
        return rstring;
    }

    public String doFormatDate(Date visited) throws Exception {
        Date tdate;
        Format formatter;
        formatter = new SimpleDateFormat("MM-dd-yyyy");
        String newdate = formatter.format(visited);
        return newdate;
    }

    public int doFormatDateComp(Date visited) throws Exception {
        Date tdate;
        Format formatter;
        formatter = new SimpleDateFormat("yyyyMMdd");
        String newdate = formatter.format(visited);
        int inewdate = Integer.parseInt(newdate);
        return inewdate;
    }

    public String doGetTechInfo_name(Connection c, String iusername) throws Exception {
        Vector v;
        v = UniTechInfo.getAllItems(c, iusername);
        int counter = 0;
        String tech_name = null;
        for (int i = 0; i < v.size(); i++) {
            UniTechInfo t = (UniTechInfo) v.elementAt(i);
            tech_name = t.getTechName();
        }
        return tech_name;
    }

    public Date getDate(String token) {
        Date visited;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos;
        try {
            pos = new ParsePosition(0);
            visited = sdf.parse(token, pos);
        } catch (NullPointerException pe) {
            System.out.println("Cannot parse visited date! " + token);
            pos = new ParsePosition(0);
            visited = sdf.parse("16/06/97", pos);
        }
        return visited;
    }

    public String getAgreement() {
        return mbody;
    }
}
