package com.wwwc.index.web.ejb.database;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;

public class EJBDatabaseQueryBean implements SessionBean {
    private Connection con;

    public void ejbCreate() throws CreateException {}
    public void ejbRemove() {}
    public void ejbActivate() {}
    public void ejbPassivate() {}
    public void setSessionContext(SessionContext sc) {}
    
    private void makeConnection() {
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/DefaultDS");
            con =  ds.getConnection();
	}
	catch(EJBException e) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:makeConnection:EJBException:" + e.getMessage());
	}
	catch (Exception e) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:makeConnection:Exception:" + e.getMessage());
        }
    }

    private void releaseConnection() {
        try {
            con.close();
        } catch (SQLException e) {
	    System.out.println("EJBDatabaseQueryBean:releaseConnection:SQLException:" + e.getMessage());
	}
	catch (EJBException e) {
	    System.out.println("EJBDatabaseQueryBean:releaseConnection:EJBException:" + e.getMessage());
	} 
	catch (Exception e) {
	    System.out.println("EJBDatabaseQueryBean:releaseConnection:Exception:" + e.getMessage());
        }
    } 

    private void releaseConnection(PreparedStatement prepStmt) {
        try {
	    prepStmt.close();
            con.close();
        } catch (SQLException ex) {
	    System.out.println("EJBDatabaseQueryBean:releaseConnection:SQLException:"+ex.getMessage());
        }
	catch(EJBException ex) {
	    System.out.println("EJBDatabaseQueryBean:releaseConnection:EJBException:"+ex.getMessage());
	}
	catch (Exception ex) {
	    System.out.println("EJBDatabaseQueryBean:releaseConnection:Exception:"+ex.getMessage());
        }
    } 

    public ArrayList select(String query){
	ArrayList a = new ArrayList();
	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int colums = rsmd.getColumnCount();

	    while (rs.next()) {
		ArrayList aa = new ArrayList();
		for(int m = 1; m <= colums; m++) {
		    aa.add(rs.getString(m));
		}
		a.add(aa);
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("selectAllRecords="+se);
	}
        return a;
    }

    public ArrayList selectSingleRecord(String query){
	ArrayList a = new ArrayList();
	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int colums = rsmd.getColumnCount();

	    while (rs.next()) {
		for(int m = 1; m <= colums; m++) {
		    a.add(rs.getString(m)+"");
		}
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("selectSingleRecord="+se);
	}
        return a;
    }

    public ArrayList selectSingleForumRecord(String query){
	ArrayList a = new ArrayList();
	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int colums = rsmd.getColumnCount();
	    String temp = null;
	    while (rs.next()) {
		for(int m = 1; m <= colums; m++) {
		    temp = rs.getString(m);
		    if(temp != null) {
			a.add(temp);
		    }
		    else {
			a.add("");
		    }
		}
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("selectSingleRecord="+se);
	}
        return a;
    }

    public ArrayList selectUserScheduleDInfo(String user_name) {
	ArrayList alist = new ArrayList();
	ArrayList a = null;
	
	try {
	    String query = "SELECT ID, BROKER_ID, TOTAL_SOLD, YEAR, STATUS FROM TAX_SCHEDULE_D WHERE USER_NAME='"+
		user_name+"' ORDER BY YEAR, BROKER_ID";
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    
	    while(rs.next()) {
		a = new ArrayList();
		a.add(rs.getString(1)); //ID
		a.add(rs.getString(2)); //BROKER_ID
		a.add(rs.getString(3)); //TOTAL_SOLD
		a.add(rs.getString(4)); //YEAR
		a.add(rs.getString(5)); //STATUS
		alist.add(a);
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQUeryBean:selectUserScheduleDInfo(user)="+se);
	    return null;
	}
	catch(Exception se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQUeryBean:selectUserScheduleDInfo(user)="+se);
	    return null;
	}

	if(alist.isEmpty()) {
	    return null;
	}

        return alist;
    }
    
    public ArrayList selectUserScheduleDInfo (String user_name, String broker_id) {
	ArrayList alist = new ArrayList();
	ArrayList a = null;
	
	try {
	    String query = "SELECT ID, BROKER_ID, TOTAL_SOLD, YEAR, STATUS FROM TAX_SCHEDULE_D WHERE USER_NAME='"+
		user_name+"' AND BROKER_ID="+broker_id+" ORDER BY YEAR, ID";
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    
	    while(rs.next()) {
		a = new ArrayList();
		a.add(rs.getString(1)); //ID
		a.add(rs.getString(2)); //BROKER_ID
		a.add(rs.getString(3)); //TOTAL_SOLD
		a.add(rs.getString(4)); //YEAR
		a.add(rs.getString(5)); //STATUS
		alist.add(a);
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQUeryBean:selectUserScheduleDInfo(str, str, srtr)="+se);
	    return null;
	}
	catch(Exception se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQUeryBean:selectUserScheduleDInfo(str,str ,str)="+se);
	    return null;
	}

	if(alist.isEmpty()) {
	    return null;
	}

        return alist;
    }
    
    public ArrayList selectUserScheduleDInfo (String user_name, String broker_id, String year) {
	ArrayList alist = new ArrayList();
	ArrayList a = null;
	
	try {
	    String query = "SELECT ID, BROKER_ID, TOTAL_SOLD, YEAR, STATUS FROM TAX_SCHEDULE_D WHERE USER_NAME='"+
		user_name+"' AND BROKER_ID="+broker_id+" AND YEAR="+year+" ORDER BY YEAR, BROKER_ID, ID";
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    
	    while(rs.next()) {
		a = new ArrayList();
		a.add(rs.getString(1)); //ID
		a.add(rs.getString(2)); //BROKER_ID
		a.add(rs.getString(3)); //TOTAL_SOLD
		a.add(rs.getString(4)); //YEAR
		a.add(rs.getString(5)); //STATUS
		alist.add(a);
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQUeryBean:selectUserScheduleDInfo(str, str, srtr)="+se);
	}
	catch(Exception se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQUeryBean:selectUserScheduleDInfo(str,str ,str)="+se);
	}
	if(alist.isEmpty()) {
	    return null;
	}
        return alist;
    }

    public ArrayList getScheduleDInfo(String user_name,  String year, String status) {
	ArrayList alist = new ArrayList();
	ArrayList a = null;

	try {
	    String query = "SELECT ID, BROKER_ID, TOTAL_SOLD FROM TAX_SCHEDULE_D "+
		" WHERE USER_NAME='"+user_name+"' AND YEAR ="+year+" AND STATUS >= "+status+";";

	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    
	    while(rs.next()) {
		a = new ArrayList();
		a.add(rs.getString(1)); //ID
		a.add(rs.getString(2)); //BROKER_ID
		a.add(rs.getString(3)); //TOTAL_SOLD
		alist.add(a);
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	    if(alist.isEmpty()) {
		return null;
	    }
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQUeryBean:getScheduleDFormIds"+se);
	}
	catch(Exception se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQUeryBean:getScheduleDFormIds"+se);
	}

        return alist;
    }

    public ArrayList selectUserScheduleDInfo (String user_name, String broker_id, String year, int before) {
	ArrayList alist = new ArrayList();
	ArrayList a = null;
	
	try {
	    String query = null;
	    query = "SELECT ID, BROKER_ID, TOTAL_SOLD, YEAR, STATUS FROM TAX_SCHEDULE_D WHERE USER_NAME='";
	    if(before >0) {
		query = query + user_name+"' AND BROKER_ID="+broker_id+" AND YEAR>"+year+" ORDER BY YEAR, BROKER_ID, ID";
	    }
	    else {
		query = query + user_name+"' AND BROKER_ID="+broker_id+" AND YEAR<"+year+" ORDER BY YEAR, BROKER_ID, ID";
	    }

	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    
	    while(rs.next()) {
		a = new ArrayList();
		a.add(rs.getString(1)); //ID
		a.add(rs.getString(2)); //BROKER_ID
		a.add(rs.getString(3)); //TOTAL_SOLD
		a.add(rs.getString(4)); //YEAR
		a.add(rs.getString(5)); //STATUS
		alist.add(a);
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQUeryBean:selectUserScheduleDInfo(str, str, srtr)="+se);
	}
	catch(Exception se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQUeryBean:selectUserScheduleDInfo(str,str ,str)="+se);
	}
	if(alist.isEmpty()) {
	    return null;
	}
        return alist;
    }
    
    public ArrayList selectUserScheduleDRecords(String user_name) {
	ArrayList alist = new ArrayList();
	ArrayList a = null;
	
	try {
	    String query = "SELECT * FROM TAX_SCHEDULE_D WHERE USER_NAME='"+user_name+"'";
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    
	    while(rs.next()) {
		a = new ArrayList();
		a.add(rs.getString(1)); //ID
		a.add(rs.getString(2)); //USER_NAME
		a.add(rs.getString(3)); //BROKER_ID
		a.add(rs.getString(4)); //TOTAL_SOLD
		a.add(rs.getString(5)); //YEAR
		a.add(rs.getObject(6)); //PRE_HOLD
		a.add(rs.getObject(7)); //TRA_DATA 
		a.add(rs.getString(8)); //STATUS
		alist.add(a);
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("selectSingleRecord="+se);
	}
	if(alist.isEmpty()) {
	    return null;
	}
        return alist;
    }

    public ArrayList selectUserScheduleDRecords(String user_name, String broker_id, String year) {
	ArrayList alist = new ArrayList();
	ArrayList a = null;
	
	try {
	    String query = "SELECT * FROM TAX_SCHEDULE_D WHERE USER_NAME='"+
		user_name+"' AND BROKER_ID="+broker_id+" AND YEAR="+year+" ORDER BY YEAR;";
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    
	    while(rs.next()) {
		a = new ArrayList();
		a.add(rs.getString(1)); //ID
		a.add(rs.getString(2)); //USER_NAME
		a.add(rs.getString(3)); //BROKER_ID
		a.add(rs.getString(4)); //TOTAL_SOLD
		a.add(rs.getString(5)); //YEAR
		a.add(rs.getObject(6)); //PRE_HOLD
		a.add(rs.getObject(7)); //TRA_DATA 
		a.add(rs.getString(8)); //STATUS
		alist.add(a);
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("selectSingleRecord="+se);
	}
        return alist;
    }

    public Hashtable selectSingleRecordHashtable(String query){
	Hashtable rhs = new Hashtable();
	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int colums = rsmd.getColumnCount();

	    if(rs.next()) {
		for(int m = 1; m <= colums; m++) {
		    rhs.put(rsmd.getColumnName(m), rs.getString(m)+"");
		}
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("selectSingleRecordHashtable="+se);
	}
        return rhs;
    }

    public Vector selectScheduleDDetailRecords(String fid, String status, int column) {
	Vector v = new Vector();
	String order = "ORDER BY DATE, TICK, BUY_SELL";
	if(column==1) {
	    order = "ORDER BY BUY_SELL, DATE, TICK";
	}
	if(column==2) {
	    order = "ORDER BY TICK, DATE, BUY_SELL";
	}

	try {
	    String query = "SELECT BUY_SELL, COST, TICK, SHARES, PRICE, DATE FROM TAX_SCHEDULE_D_DETAIL WHERE FID="+
		fid+" AND STATUS="+status+" "+order;
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    //ID(*) FID USER_NAME YEAR BROKER_ID STATUS BUY_SELL COST TICK SHARES PRICE DATE 
	    while(rs.next()) {
		v.add(rs.getString(1));
		v.add(rs.getString(2));
		v.add(rs.getString(3));
		v.add(rs.getString(4));
		v.add(rs.getString(5));
		v.add(rs.getString(6));
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDATABSEQueryBean:selectScheduleDDetailRecorda="+se);
	}

	if(v.isEmpty()) {
	    return null;
	}

        return v;
    }

    public Vector selectScheduleDDetailRecords(String fid, String status, int column, String tick) {
	Vector v = new Vector();
	String order = "ORDER BY DATE, TICK, BUY_SELL";

	if(column==1) {
	    order = "ORDER BY BUY_SELL, DATE, TICK";
	}
	if(column==2) {
	    order = "ORDER BY TICK, DATE";
	}
	if(column==3) {
	    order = "ORDER BY DATE DESC";
	}

	try {
	    String query = "SELECT BUY_SELL, COST, TICK, SHARES, PRICE, DATE FROM TAX_SCHEDULE_D_DETAIL WHERE FID="+
		fid+" AND STATUS="+status+" AND TICK="+tick+" "+order;
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    //ID(*) FID USER_NAME YEAR BROKER_ID STATUS BUY_SELL COST TICK SHARES PRICE DATE 
	    while(rs.next()) {
		v.add(rs.getString(1));
		v.add(rs.getString(2));
		v.add(rs.getString(3));
		v.add(rs.getString(4));
		v.add(rs.getString(5));
		v.add(rs.getString(6));
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDATABSEQueryBean:selectScheduleDDetailRecorda="+se);
	}

	if(v.isEmpty()) {
	    return null;
	}

        return v;
    }

    public Vector getLastYearDetailHoldingRecords(String user_name, String year, String broker_id) {
	Vector v = new Vector();
	//ID(*) FID YEAR BROKER_ID USER_NAME BUY_SELL COST TICK SHARES PRICE DATE 
	try {
	    String query = "SELECT BUY_SELL, COST, TICK, SHARES, PRICE, DATE FROM TAX_SCHEDULE_D_HOLDING WHERE "+
		" USER_NAME='"+user_name+"' AND YEAR="+(Integer.parseInt(year)-1)+" AND BROKER_ID="+broker_id+" ORDER BY DATE;";
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    while(rs.next()) {
		v.add(rs.getString(1));
		v.add(rs.getString(2));
		v.add(rs.getString(3));
		v.add(rs.getString(4));
		v.add(rs.getString(5));
		v.add(rs.getString(6));
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDATABSEQueryBean:getLastYearDetailHoldingRecords="+se);
	}

	if(v.isEmpty()) {
	    return null;
	}

        return v;
    }

    public Vector getLastYearDetailHoldingRecordVector(String user_name, String year, String broker_id) {
	Vector vv = new Vector();
	//ID(*) FID YEAR BROKER_ID USER_NAME BUY_SELL COST TICK SHARES PRICE DATE 
	try {
	    String query = "SELECT BUY_SELL, COST, TICK, SHARES, PRICE, DATE FROM TAX_SCHEDULE_D_HOLDING WHERE "+
		" USER_NAME='"+user_name+"' AND YEAR="+(Integer.parseInt(year)-1)+" AND BROKER_ID="+broker_id+" ORDER BY DATE;";
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    Vector v = null;
	    while(rs.next()) {
		v = new Vector();
		v.add(rs.getString(1));
		v.add(rs.getString(2));
		v.add(rs.getString(3));
		v.add(rs.getString(4));
		v.add(rs.getString(5));
		v.add(rs.getString(6));
		vv.add(v);
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDATABSEQueryBean:getLastYearDetailHoldingRecords="+se);
	}

	if(vv.isEmpty()) {
	    return null;
	}

        return vv;
    }

    public int findBaseYear (String user_name, String broker_id, String year) {
	
	try {
	    String query = "SELECT YEAR FROM TAX_SCHEDULE_D WHERE USER_NAME='"+user_name+"' AND BROKER_ID="+
		broker_id+" AND YEAR <"+year+" AND STATUS >= 6 ORDER BY YEAR DESC;";

	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    int base_year = -1;

	    if(rs.next()) {
		base_year = rs.getInt(1);
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	    return base_year;
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQUeryBean:findBaseYaer:Error:"+se);
	    return -1;
	}
	catch(Exception se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQUeryBean:findBaseYaer:Error:"+se);
	    return -1;
	}
    }
    

    public Vector getBoughtOrSoldDetailRecords(String fid, String tick, int bs) {
	Vector v = new Vector();
	String query = null;
	try {
	    if(bs == 1) { //bought
		query = "SELECT BUY_SELL, COST, TICK, SHARES, PRICE, DATE FROM TAX_SCHEDULE_D_DETAIL WHERE FID="+
		    fid+"  AND TICK='"+tick+"' AND BUY_SELL='B' ORDER BY DATE;";
	    }
	    if(bs == -1) { //Sold
		query = "SELECT BUY_SELL, COST, TICK, SHARES, PRICE, DATE FROM TAX_SCHEDULE_D_DETAIL WHERE FID="+
		    fid+"  AND TICK='"+tick+"' AND BUY_SELL='S' ORDER BY DATE;";
	    }

	    if(bs == 0) { //Bought and sold
		query = "SELECT BUY_SELL, COST, TICK, SHARES, PRICE, DATE FROM TAX_SCHEDULE_D_DETAIL WHERE FID="+
		    fid+"  AND TICK='"+tick+"' ORDER BY DATE;";
	    }

	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    while(rs.next()) {
		v.add(rs.getString(1));
		v.add(rs.getString(2));
		v.add(rs.getString(3));
		v.add(rs.getString(4));
		v.add(rs.getString(5));
		v.add(rs.getString(6));
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDATABSEQueryBean:getBoughtAndSoldDetailRecorda="+se);
	}

	if(v.isEmpty()) {
	    return null;
	}

        return v;
    }

    public Vector selectScheduleDDetailRecordVector(String fid, int column) {
	Vector vv = new Vector();
	Vector v  = null;

	String order = "ORDER BY DATE";
	if(column==1) {
	    order = "ORDER BY BUY_SELL";
	}
	if(column==2) {
	    order = "ORDER BY TICK";
	}

	try {
	    String query = "SELECT BUY_SELL, COST, TICK, SHARES, PRICE, DATE FROM TAX_SCHEDULE_D_DETAIL WHERE FID="+fid+" "+order;
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    //ID(*) FID USER_NAME YEAR BROKER_ID STATUS BUY_SELL COST TICK SHARES PRICE DATE 
	    while(rs.next()) {
		v = new Vector();
		v.add(rs.getString(1));
		v.add(rs.getString(2));
		v.add(rs.getString(3));
		v.add(rs.getString(4));
		v.add(rs.getString(5));
		v.add(rs.getString(6));
		vv.add(v);
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDATABSEQueryBean:selectScheduleDDetailRecorda="+se);
	}

	if(vv.isEmpty()) {
	    return null;
	}
        return vv;
    }

    public Vector selectScheduleDDetailRecordVector(String fid, int column, String tick, String bs) {
	Vector vv = new Vector();
	Vector v  = null;

	String order = "ORDER BY DATE";
	if(column==1) {
	    order = "ORDER BY BUY_SELL";
	}
	if(column==2) {
	    order = "ORDER BY TICK";
	}
	if(column==3) {
	    order = "ORDER BY DATE DESC";
	}

	try {
	    String query = "SELECT BUY_SELL, COST, TICK, SHARES, PRICE, DATE FROM TAX_SCHEDULE_D_DETAIL WHERE FID="+fid+
		" AND TICK ='"+tick+"' AND BUY_SELL='"+bs+"' "+order;
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    //ID(*) FID USER_NAME YEAR BROKER_ID STATUS BUY_SELL COST TICK SHARES PRICE DATE 
	    while(rs.next()) {
		v = new Vector();
		v.add(rs.getString(1));
		v.add(rs.getString(2));
		v.add(rs.getString(3));
		v.add(rs.getString(4));
		v.add(rs.getString(5));
		v.add(rs.getString(6));
		vv.add(v);
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDATABSEQueryBean:selectScheduleDDetailRecorda="+se);
	}

	if(vv.isEmpty()) {
	    return null;
	}
        return vv;
    }

    public int scheduleDGetUmatchedShares(String fid, String user, String year, String broker_id, String tick){
	int bs = 0;
	int b = 0;
	int s = 0;
	try {	
	    makeConnection();
	    String query1 = "SELECT SUM(SHARES) FROM TAX_SCHEDULE_D_DETAIL WHERE FID="+fid+" AND TICK='"+tick+"'"+
		" AND BUY_SELL='B'";
	    String query2 = "SELECT SUM(SHARES) FROM TAX_SCHEDULE_D_DETAIL WHERE FID="+fid+" AND TICK='"+tick+"'"+
		" AND BUY_SELL='S'";

	    String query = "SELECT ("+query1+") AS bshares, ("+query2+") AS sshares FROM TAX_SCHEDULE_D_DETAIL WHERE FID="+
		fid+" AND TICK='"+tick+"';";

	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    prepStmt.setString(1, "B");
	    ResultSet rs = prepStmt.executeQuery();
	    if(rs.next()) {
		b = rs.getInt("bshares");
		s = rs.getInt("sshares");
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	    bs = (b-s);
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:scheduleDGetUmatchedShares:SQLException="+se);
	}
	catch(Exception se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:scheduleDGetUmatchedShares:SQLException="+se);
	}
	return bs;
    }


    public String  getScheduleDFinalGain(String fid, String user){
	String temp = null;
	try {	
	    makeConnection();
	    String query = "SELECT SUM(GAIN) FROM TAX_SCHEDULE_D_FINAL WHERE FID="+fid+" AND USER_NAME='"+user+"';";
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    if(rs.next()) {
		temp = rs.getString(1);
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:scheduleDSumTotalCost:SQLException="+se);
	}
	catch(Exception se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:scheduleDSumTotalCost:Exception="+se);
	}
	return temp;
    }

    public Object selectScheduleDTicks(String fid, int type){
	//type 1: ArrayList
	//type 2: Hashtable
	ArrayList a  = new ArrayList();
	Hashtable hs = new Hashtable();

	try {	
	    makeConnection();
	    String query = "SELECT DISTINCT TICK  FROM TAX_SCHEDULE_D_DETAIL WHERE FID="+fid+" ORDER BY TICK;";
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();

	    while(rs.next()) {
		a.add(rs.getString(1));
		hs.put(rs.getString(1), rs.getString(1));
	    }
	    
	    rs.close();
	    prepStmt.close();
	    releaseConnection();

	    if(a.isEmpty() || hs.isEmpty()) {
		a  = null;
		hs = null;
	    }
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:selectScheduleDTicks:SQLException="+se);
	}
	catch(Exception se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:selectScheduleDTicks:Exception:="+se);
	}
	if(type == 1) {
	    return a;
	}
	if(type == 2) {
	    return hs;
	}
	else {
	    return null;
	}
    }

    public boolean deleteThisYearHoldingRecords(String fid, String user, String year, String broker_id) {
	try { 
	    // TABLE : TAX_SCHEDULE_D_HOLDING	
	    // ID(*) FID YEAR BROKER_ID USER_NAME BUY_SELL COST  TICK SHARES PRICE  DATE 
	    // INT   INT INT     INT       VARN     VARN  DOUBLE VARN   INT  DOUBLE DATE 

	    String query = "DELETE FROM TAX_SCHEDULE_D_HOLDING WHERE FID="+fid+" AND USER_NAME='"+user
		+"' AND YEAR="+year+" AND BROKER_ID="+broker_id+";";
	    if(delete(query)<0) {
		return false;
	    }
	    return true;
	}
	catch (Exception e) {
	    return false;
	}
    }

    public boolean saveThisYearHoldingRecords(String fid, String user, String year, String broker_id, Vector vr) {
	int rs = -1;
	try {	
	    // TABLE : TAX_SCHEDULE_D_HOLDING	
	    // ID(*) FID YEAR BROKER_ID USER_NAME BUY_SELL COST  TICK SHARES PRICE  DATE 
	    // INT   INT INT     INT       VARN     VARN  DOUBLE VARN   INT  DOUBLE DATE 
	    if(fid == null || user == null || year == null || broker_id == null || vr == null) {
		return false;
	    }

	    makeConnection();
	    String query = "INSERT INTO TAX_SCHEDULE_D_HOLDING"+
		"(FID, YEAR, BROKER_ID, USER_NAME, BUY_SELL, COST, TICK, SHARES, PRICE, DATE) VALUES"+
		"(?,    ?,       ?,       ?,        ?,        ?,    ?,     ?,      ?,    ?);";
	    
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    com.wwwc.index.web.servlet.StockRecord r = null;
	    Enumeration enum = vr.elements();
	    while(enum.hasMoreElements()) {
		r = (com.wwwc.index.web.servlet.StockRecord)enum.nextElement();

		prepStmt.setInt(1, Integer.parseInt(fid));
		prepStmt.setInt(2, Integer.parseInt(year));
		prepStmt.setInt(3, Integer.parseInt(broker_id));
		prepStmt.setString(4, user);
		prepStmt.setString(5, r.getBuySell());
		prepStmt.setDouble(6, r.getCost());
		prepStmt.setString(7, r.getTick());
		prepStmt.setInt(8, r.getShare());
		prepStmt.setDouble(9, r.getPrice());
		prepStmt.setString(10, r.getDate());
		rs = prepStmt.executeUpdate();
	    }

	    prepStmt.close();
	    releaseConnection();
	    return true;
	}
	catch(SQLException se) {
	    rollback();
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:saveThisYearHoldingRecords:SQLException="+se);
	    return false;
	}
	catch(Exception se) {
	    rollback();
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:saveThisYearHoldingRecords:Exception:="+se);
	    return false;
	}
    }

    public boolean saveFinalRecords(String fid, String user, String year, String broker_id, Vector vv) {
	try {	
	    // TABLE : TAX_SCHEDULE_D_FINAL
	    // ID(*) FID USER_NAME BROKER_ID YEAR COMPANY TICK SHARE B_DATE B_COST S_DATE S_COST GAIN STATUS 
	    //  INT  INT   VARN      INT      INT   VARN  VARN  INT   DATE  DOUBLE  DATE  DOUBLE DOUBLE INT

	    if(fid == null || user == null || year == null || broker_id == null || vv == null || vv.isEmpty()) {
		return false;
	    }

	    String query = "DELETE FROM TAX_SCHEDULE_D_FINAL WHERE FID="+fid+" AND USER_NAME='"+user
		+"' AND YEAR="+year+" AND BROKER_ID="+broker_id+";";
	    if(delete(query)<0) {
		return false;
	    }

	    makeConnection();
	    query = "INSERT INTO TAX_SCHEDULE_D_FINAL"+
		"(FID, USER_NAME, BROKER_ID, YEAR, COMPANY, TICK, SHARE, B_DATE, B_COST, S_DATE, S_COST, GAIN) VALUES"+
		"(?,      ?,       ?,         ?,      ?,     ?,    ?,     ?,        ?,    ?,       ?,      ?);";
	    
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    Enumeration enum = vv.elements();
	    Vector v = null;
	    while(enum.hasMoreElements()) {
		v = (Vector)enum.nextElement();
		v.trimToSize();
		prepStmt.setInt    (1, Integer.parseInt(fid));
		prepStmt.setString (2, user);
		prepStmt.setInt    (3, Integer.parseInt(broker_id));
		prepStmt.setInt    (4, Integer.parseInt(year));
		prepStmt.setString (5, (String)v.elementAt(0));
		prepStmt.setString (6, (String)v.elementAt(1));
		prepStmt.setInt    (7, Integer.parseInt((String)v.elementAt(2)));
		prepStmt.setString (8, (String)v.elementAt(3));
		prepStmt.setDouble (9, Double.parseDouble((String)v.elementAt(4)));
		prepStmt.setString(10, (String)v.elementAt(5));
		prepStmt.setDouble(11, Double.parseDouble((String)v.elementAt(6)));
		prepStmt.setDouble(12, Double.parseDouble((String)v.elementAt(7)));
		prepStmt.executeUpdate();
	    }

	    prepStmt.close();
	    releaseConnection();
	    return true;
	}
	catch(SQLException se) {
	    rollback();
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:saveFinalRecords:SQLException="+se);
	    return false;
	}
	catch(Exception se) {
	    rollback();
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:saveFinalRecords:Exception:="+se);
	    return false;
	}
    }

    public Vector getFinalRecords(String fid, String user, String year, String broker_id) {
	try {	
	    // TABLE : TAX_SCHEDULE_D_FINAL
	    // ID(*) FID USER_NAME BROKER_ID YEAR COMPANY TICK SHARE B_DATE B_COST S_DATE S_COST GAIN STATUS 
	    //  INT  INT   VARN      INT      INT   VARN  VARN  INT   DATE  DOUBLE  DATE  DOUBLE DOUBLE INT

	    if(fid == null || user == null || year == null || broker_id == null) {
		return null;
	    }

	    Vector vv = new Vector();
	    Vector v  = null;

	    makeConnection();
	    String query = "SELECT SHARE, TICK, B_DATE, S_DATE, S_COST, B_COST, GAIN FROM  TAX_SCHEDULE_D_FINAL"+
		" WHERE FID="+fid+" AND USER_NAME='"+user+"' AND YEAR="+year+" AND BROKER_ID="+
		broker_id+" ORDER BY S_DATE;";

	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    while(rs.next()) {
		v = new Vector();
		v.add(rs.getString(1));
		v.add(rs.getString(2));
		v.add(rs.getString(3));
		v.add(rs.getString(4));
		v.add(rs.getString(5));
		v.add(rs.getString(6));
		v.add(rs.getString(7));
		vv.add(v);
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	    return vv;
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:selectFinalRecords:SQLException="+se);
	}
	catch(Exception se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:selectFinalRecords:Exception:="+se);
	}
	return null;
    }

    public Vector selectHoldingRecords(String fid, String user, String year, String broker_id) {
	try {	
	    // TABLE : TAX_SCHEDULE_D_HOLDING	
	    // ID(*) FID YEAR BROKER_ID USER_NAME BUY_SELL COST  TICK SHARES PRICE  DATE 
	    // INT   INT INT     INT       VARN     VARN  DOUBLE VARN   INT  DOUBLE DATE 
	    if(fid == null || user == null || year == null || broker_id == null) {
		return null;
	    }

	    makeConnection();
	    String query = "SELECT BUY_SELL, COST, TICK, SHARES, PRICE, DATE FROM  TAX_SCHEDULE_D_HOLDING"+
		" WHERE FID="+fid+" AND USER_NAME='"+user+"' AND YEAR="+year+" AND BROKER_ID="+broker_id+" ORDER BY DATE, BUY_SELL;";
	    
	    com.wwwc.index.web.servlet.StockRecord r = null;
	    Vector v  = null;
	    Vector vr = new Vector();

	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    while(rs.next()) {
		v = new Vector();
		v.add(rs.getString(1));
		v.add(rs.getString(2));
		v.add(rs.getString(3));
		v.add(rs.getString(4));
		v.add(rs.getString(5));
		v.add(rs.getString(6));
		r = new com.wwwc.index.web.servlet.StockRecord(v);
		vr.add(r);
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	    return vr;
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:saveThisYearHoldingRecords:SQLException="+se);
	    return null;
	}
	catch(Exception se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:saveThisYearHoldingRecords:Exception:="+se);
	    return null;
	}
    }


    public ArrayList getScheduleDUsers(){
	ArrayList a  = new ArrayList();

	try {	
	    makeConnection();
	    String query = "SELECT DISTINCT USER_NAME  FROM TAX_SCHEDULE_D ORDER BY USER_NAME;";
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();

	    while(rs.next()) {
		a.add(rs.getString(1));
	    }
	    
	    rs.close();
	    prepStmt.close();
	    releaseConnection();

	    if(a.isEmpty()) {
		a  = null;
	    }
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:getScheduleDUsers:SQLException="+se);
	}
	catch(Exception se) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQueryBean:getScheduleDUsers:Exception:="+se);
	}

	return a;
    }

    public Vector selectColumnNames(String table_name){
	String query = "SELECT LIMIT 0 1 * FROM "+table_name;
	Vector v = new Vector();

	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int colums = rsmd.getColumnCount();

	    for(int m = 1; m <= colums; m++) {
		v.add(rsmd.getColumnName(m));
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("selectColumnNames="+se);
	}
        return v;
    }

    public Vector selectColumnTypes(String table_name){
	String query = "SELECT LIMIT 0 1 * FROM "+table_name;
	Vector v = new Vector();

	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int colums = rsmd.getColumnCount();

	    for(int m = 1; m <= colums; m++) {
		v.add(rsmd.getColumnTypeName(m));
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("selectColumnTypes="+se);
	}
        return v;
    }

    public Vector selectColumnSize(String table_name){
	String query = "SELECT LIMIT 0 1 * FROM "+table_name;
	Vector v = new Vector();

	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int colums = rsmd.getColumnCount();

	    for(int m = 1; m <= colums; m++) {
		v.add(""+rsmd.getColumnDisplaySize(m));
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("selectColumnDisplaySize="+se);
	}
        return v;
    }

    public String selectSingleFieldValue(String query, String field){
	String result = null;
	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    if(rs.next()) {
		result = rs.getString(field);
	    }
	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("selectSingleValue="+se);
	}
        return result;
    }

    public Vector selectSingleFieldScheduleDData(int id, String user, String field){
	Vector v = null;
	PreparedStatement prepStmt = null;
	ResultSet rs = null;
	try {	
	    String query = "SELECT "+field+" FROM TAX_SCHEDULE_D  WHERE ID="+id+" AND USER_NAME='"+user+"';";
	    makeConnection();
	    prepStmt =  con.prepareStatement(query);
	    
	    rs = prepStmt.executeQuery();
	    if (rs.next()) {
		v = (Vector)rs.getObject(field);
	    }
	    
	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection(prepStmt);
	    releaseConnection();
	    System.out.println("selectSingleFieldScheduleDData="+se);
	}
	catch(Exception se) {
	    releaseConnection(prepStmt);
	    releaseConnection();
	    System.out.println("selectSingleFieldScheduleDData="+se);
	}
        return v;
    }

    public Object selectSingleFieldObject(String query, String field){
	Object o = null;
	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    if (rs.next()) {
		o = rs.getObject(field);
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("selectSingleFieldObject="+se);
	}
        return o;
    }

    public Vector selectSingleFieldValueVector(String query, String field){
	Vector v = new Vector();

	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    while (rs.next()) {
		v.add(rs.getString(field));
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("selectColumnDisplaySize="+se);
	}
        return v;
    }

    public int insertByteArray(String query, byte[] b) {
	int rs = -1;
	try {	
	    makeConnection();
	    query = "INSERT INTO TEST_IMAGE VALUES(?, ?);";
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    prepStmt.setObject(1, null);
	    prepStmt.setObject(2, b);
	    rs = prepStmt.executeUpdate();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    rollback();
	    releaseConnection();
	    System.out.println("InsertByteArray SQLException="+se);
	}
	catch(Exception e) {
	    rollback();
	    releaseConnection();
	    System.out.println("InserteByteArray Exception="+e);
	}
	return rs;
    }

    public int insertTaxScheduleD(String user, String broker_id, String t_sold, String year, Vector vv) {
	int rs = -1;
	try {	
	    makeConnection();
	    String query = "INSERT INTO TAX_SCHEDULE_D (USER_NAME, BROKER_ID, TOTAL_SOLD, YEAR, TRA_DATA)VALUES(?,?,?,?,?);";
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    prepStmt.setString (1, user);
	    prepStmt.setInt    (2, Integer.parseInt(broker_id));
	    prepStmt.setDouble (3, Double.parseDouble(t_sold));
	    prepStmt.setInt    (4, Integer.parseInt(year));
	    prepStmt.setObject (5, vv);

	    rs = prepStmt.executeUpdate();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    rollback();
	    releaseConnection();
	    System.out.println("InsertObject SQLException="+se);
	}
	catch(Exception e) {
	    rollback();
	    releaseConnection();
	    System.out.println("InserteObject Exception="+e);
	}
	return rs;
    }

    public int updateScheduleDTick(String fid, String otick, String ntick) {
	int rs = -1;
	try {	
	    String query = "UPDATE TAX_SCHEDULE_D_DETAIL SET TICK='"+ntick+"' WHERE FID="+fid+
		" AND TICK='"+otick+"';";
	    rs = update(query);
	}
	catch(Exception e) {
	    System.out.println("updateScheduleDTick:Exception:"+e);
	}
	return rs;
    }

    public int updateUserScheduleDStatus(String user, String broker_id, int status) {
	int rs = -1;
	try {	
	    String query = "UPDATE TAX_SCHEDULE_D SET STATUS="+status+" WHERE USER_NAME='"+user+
		"' AND BROKER_ID="+broker_id+";";
	    rs = update(query);
	}
	catch(Exception e) {
	    System.out.println("updateUserScheduleDStatus:Error:"+e);
	}
	return rs;
    }

    public int updateUserScheduleDStatus(String user, String year, String broker_id, int status) {
	int rs = -1;
	try {	
	    String query = "UPDATE TAX_SCHEDULE_D SET STATUS="+status+" WHERE USER_NAME='"+user+
		"' AND YEAR="+year+" AND BROKER_ID="+broker_id+";";
	    rs = update(query);
	}
	catch(Exception e) {
	    System.out.println("updateUserScheduleDStatus:Error:"+e);
	}
	return rs;
    }

    public int insertObject(String query, Object o) {
	int rs = -1;
	try {	
	    makeConnection();
	    //PreparedStatement prepStmt =  con.prepareStatement("INSERT INTO TEST_OBJECT (OBJECT) VALUES(?);");
	    //PreparedStatement prepStmt =  con.prepareStatement("UPDATE TEST_OBJECT SET OBJECT=? WHERE ID=0;");
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    prepStmt.setObject(1, o);
	    rs = prepStmt.executeUpdate();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    rollback();
	    releaseConnection();
	    System.out.println("InsertObject SQLException="+se);
	}
	catch(Exception e) {
	    rollback();
	    releaseConnection();
	    System.out.println("InserteObject Exception="+e);
	}
	return rs;
    }

    public int insert(String query, Vector v) {
	int rs = -1;
	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    Enumeration enum = v.elements();
	    int m=1;
	    while(enum.hasMoreElements()) {
		prepStmt.setObject(m, enum.nextElement());
		m++;
	    }
	    rs = prepStmt.executeUpdate();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    rollback();
	    releaseConnection();
	    System.out.println("Insert SQLException="+se);
	}
	catch(Exception e) {
	    rollback();
	    releaseConnection();
	    System.out.println("Inserte Exception="+e);
	}
	return rs;
    }

    public int insertScheduleDDetailRecords(int fid, String user_name, int year, int broker, int status, Vector vv) {
	int rs = -1;
	PreparedStatement prepStmt = null;
	try {	
	    //ID(*) FID USER_NAME YEAR BROKER_ID STATUS BUY_SELL COST   TICK SHARES PRICE  DATE 
	    //INT   INT VARN      INT     INT      INT    VARN   DOUBLE VARN INT    DOUBLE DATE 
	    String query = null; 
	    query = "INSERT INTO TAX_SCHEDULE_D_DETAIL(FID, USER_NAME, YEAR, BROKER_ID, STATUS, BUY_SELL,"+
		" COST, TICK, SHARES, PRICE, DATE) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	    
	    makeConnection();
	    prepStmt = con.prepareStatement(query);
	    Vector v = null;
	    Enumeration enum = vv.elements();
	 
	    while(enum.hasMoreElements()) {
		v = (Vector)enum.nextElement();
		prepStmt.setInt(1, fid);
		prepStmt.setString(2, user_name);
		prepStmt.setInt(3, year);
		prepStmt.setInt(4, broker);
		prepStmt.setInt(5, status);
		prepStmt.setString(6, (String)v.elementAt(0)); //Bought/Sold
		prepStmt.setDouble(7, Double.parseDouble((String)v.elementAt(1))); //cost
		prepStmt.setString(8, (String)v.elementAt(2)); //tick
		prepStmt.setInt(9, (new Double((String)v.elementAt(3))).intValue()); //share
		prepStmt.setDouble(10, Double.parseDouble((String)v.elementAt(4))); //price
		prepStmt.setString(11, (String)v.elementAt(5)); //date
		rs = prepStmt.executeUpdate();

		if(rs <0) {
		    rollback();
		    releaseConnection(prepStmt);
		    releaseConnection();
		    return -1;
		}
	    }

	    releaseConnection(prepStmt);
	    releaseConnection();
	    return rs;
	}
	catch(SQLException se) {
	    rollback();
	    releaseConnection(prepStmt);
	    releaseConnection();
	    System.out.println("[ERROR1]:InsertScheduleDDetailRecords SQLException="+se);
	    return -1;
	}
	catch(Exception e) {
	    rollback();
	    releaseConnection(prepStmt);
	    releaseConnection();
	    System.out.println("[ERROR2]:InsertScheduleDDetailRecords Exception="+e);
	    return -1;
	}
    }

    public boolean updateScheduleD1099PreHolding(String user, String fid, Vector vv) {
	int rs = -1;
	PreparedStatement prepStmt = null;

	// if (vv == null)
	//delete PreHolding in 1099

	try {	
	    makeConnection();
	    String query = "UPDATE TAX_SCHEDULE_D SET PRE_HOLD = ?, STATUS = ? WHERE ID = ? AND USER_NAME = ?;";

	    prepStmt =  con.prepareStatement(query);
	    prepStmt.setObject(1, vv);
	    prepStmt.setInt(2, 2); //set status to confirming pre-holding
	    prepStmt.setInt(3, Integer.parseInt(fid));
	    prepStmt.setString(4, user);
	    rs = prepStmt.executeUpdate();
	    releaseConnection(prepStmt);
	}
	catch(SQLException e) {
	    rollback();
	    releaseConnection(prepStmt);
	    System.out.println("EJBDatabseQueryBean:updateScheduleD1099PreHolding:SQLException="+e);
	    return false;
	}
	catch(Exception e) {
	    rollback();
	    releaseConnection(prepStmt);
	    System.out.println("EJBDatabseQueryBean:updateScheduleD1099PreHolding:Exception="+e);
	    return false;
	}
	if(rs <0) {
	    return false;
	}
	return true;
    }

    public byte[] selectByteArray(String query, String field){
	byte[] result = null;
	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    if(rs.next()) {
		result = rs.getBytes(field);
	    }
	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("selectByteArray="+se);
	}
        return result;
    }

    public byte[] selectByteArray(String query, int fid){
	byte[] result = null;
	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    if(rs.next()) {
		result = rs.getBytes(fid);
	    }
	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("selectByteArray="+se);
	}
        return result;
    }

    public Hashtable selectTwoFieldValueHashtable(String query, String f1, String f2){
	Hashtable rhs = new Hashtable();
	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    ResultSetMetaData rsmd = rs.getMetaData();

	    while (rs.next()) {
		rhs.put(rs.getString(f1)+"", rs.getString(f2)+"");
	    }
	    
	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("selectTwoFieldValueHashtable="+se);
	}
        return rhs;
    }

    public boolean userNameInUse (String user_name) {
	boolean in_use = false;
	try {	
	    String query = "SELECT USERNAME FROM USERS WHERE USERNAME='"+user_name+"';";
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();

	    if(rs.next()) {
		in_use = true;
	    }
	    
	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException e) {
	    releaseConnection();
	    System.out.println("EJBDatabaseQuery:userNameInUse:101:"+e);
	    in_use = true;
	}
	catch(Exception e) {
	    in_use = true;
	    releaseConnection();
	    System.out.println("EJBDatabaseQuery:userNameInUse:102:"+e);
	}
        return in_use;
    }

    public int getForumRowCounter(String table_name){
	int counter = -1;
	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement("SELECT COUNT(*) AS rowcount FROM "+table_name+" WHERE FIELD17 <> '-1';");
	    ResultSet rs = prepStmt.executeQuery();
	    if(rs.next()) {
		counter = rs.getInt("rowcount");
	    }
	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException e) {
	    System.out.println("getForumRowCounter="+e);
	    return -1;
	}
	catch(Exception e) {
	    System.out.println("getForumRowCounter="+e);
	    return -1;
	}

        return counter;
    }

    public int getRowCounter(String table_name){
	int counter = -1;
	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement("SELECT COUNT(*) AS rowcount FROM "+table_name+";");
	    ResultSet rs = prepStmt.executeQuery();
	    if(rs.next()) {
		counter = rs.getInt("rowcount");
	    }
	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException e) {
	    System.out.println("getRowCounter="+e);
	    return -1;
	}
	catch(Exception e) {
	    System.out.println("getRowCounter="+e);
	    return -1;
	}

        return counter;
    }

    public int delete(String query) {
	PreparedStatement prepStmt = null;
	int rs = -1; // if ok return 1, 2?
	try {	
	    makeConnection();
	    prepStmt =  con.prepareStatement(query);
	    rs = prepStmt.executeUpdate();
	    releaseConnection(prepStmt);
	    System.out.println("EJBDabaseQueryBean:delete rs="+rs);
	    return rs;
	}
	catch(SQLException se) {
	    rollback();
	    releaseConnection(prepStmt);
	    releaseConnection();
	    System.out.println("EJBDatabaseQuery:delete:SQLException="+se);
	    return -1;
	}
	catch(Exception se) {
	    rollback();
	    releaseConnection(prepStmt);
	    releaseConnection();
	    System.out.println("EJBDatabaseQuery:delete:Exception="+se);
	    return -1;
	}
    }

    public int insert(String query) {
	int rs = -1;
	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    rs = prepStmt.executeUpdate();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    rollback();
	    releaseConnection();
	    System.out.println("Insert SQLException="+se);
	}
	catch(Exception e) {
	    rollback();
	    releaseConnection();
	    System.out.println("Inserte Exception="+e);
	}
	return rs;
    }

    public int update(String query) {
	int rs = -1;
	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    rs = prepStmt.executeUpdate();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException e) {
	    rollback();
	    System.out.println("Update SQLException="+e);
	    releaseConnection();
	    return -1;
	}
	catch(Exception e) {
	    rollback();
	    System.out.println("Update Exception="+e);
	    releaseConnection();
	    return -1;
	}
	return rs;
    }

    private void rollback() {
	PreparedStatement prepStmt= null;
	try {	
	    makeConnection();
	    prepStmt =  con.prepareStatement("ROLLBACK");
	    prepStmt.executeUpdate();
	    prepStmt.close();
	    releaseConnection(prepStmt);
	    releaseConnection();
	}
	catch(SQLException e) {
	    releaseConnection(prepStmt);
	    releaseConnection();
	    System.out.println("Rollback SQLException="+e);
	}
	catch(Exception e) {
	    releaseConnection(prepStmt);
	    releaseConnection();
	    System.out.println("Rollback Exception="+e);
	}
    }


    public boolean addNewHotDeal(Vector v) {
	int rs = -1;
	try {	
	    makeConnection();
	    String query = "INSERT INTO SHOPPING_HOT_DEALS (TIME_STAMP, TITLE, PRICE, ORDER_URL,"+
		"DESCRIPTION, FEATURES, IMAGE, REBATE_URL, COUPON_CODE, COUPON_EXP_DATE,"+ 
		"COMPANY, COMPANY_URL) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";

	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    prepStmt.setString(1, "now");
	    prepStmt.setString(2, ""+v.elementAt(0));
	    prepStmt.setDouble(3, Double.parseDouble("0"+v.elementAt(1)));
	    prepStmt.setString(4, ""+v.elementAt(2));
	    prepStmt.setString(5, ""+v.elementAt(3));
	    prepStmt.setString(6, ""+v.elementAt(4));
	    prepStmt.setString(7, ""+v.elementAt(5));
	    prepStmt.setString(8, ""+v.elementAt(6));
	    prepStmt.setString(9, ""+v.elementAt(7));
	    prepStmt.setString(10, ""+v.elementAt(8));
	    prepStmt.setString(11, ""+v.elementAt(9));
	    prepStmt.setString(12, ""+v.elementAt(10));

	    rs = prepStmt.executeUpdate();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    rollback();
	    releaseConnection();
	    System.out.println("EJBDatabseQueryBean:addNewHotDeal:SQLException="+se);
	    return false;
	}
	catch(Exception e) {
	    rollback();
	    releaseConnection();
	    System.out.println("EJBDatabseQueryBean:addNewHotDeal:Exception="+e);
	    return false;
	}
	return true;
    }

    public ArrayList selectHotDeal(String fid){
	String query = "SELECT * FROM SHOPPING_HOT_DEALS WHERE ID="+fid+";";
	ArrayList a = new ArrayList();
	try {	
	    makeConnection();
	    PreparedStatement prepStmt =  con.prepareStatement(query);
	    ResultSet rs = prepStmt.executeQuery();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int colums = rsmd.getColumnCount();

	    while (rs.next()) {
		for(int m = 1; m <= colums; m++) {
		    a.add(rs.getString(m));
		}
	    }

	    rs.close();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    releaseConnection();
	    System.out.println("selectHotDeal:Error:"+se);
	}
        return a;
    }


    public boolean updateHotDeal(Vector v) {
	int rs = -1;
	try {	
	    makeConnection();
	    String query = "UPDATE SHOPPING_HOT_DEALS SET TITLE = ?, PRICE = ?, ORDER_URL = ?,"+
		"DESCRIPTION = ?, FEATURES = ?, IMAGE = ?, REBATE_URL = ?, COUPON_CODE = ?, "+
		"COUPON_EXP_DATE = ?, COMPANY = ?, COMPANY_URL = ? WHERE ID = ?;";

	    PreparedStatement prepStmt =  con.prepareStatement(query);

	    prepStmt.setString(1, ""+v.elementAt(1));
	    prepStmt.setDouble(2, Double.parseDouble("0"+v.elementAt(2)));
	    prepStmt.setString(3, ""+v.elementAt(3));
	    prepStmt.setString(4, ""+v.elementAt(4));
	    prepStmt.setString(5, ""+v.elementAt(5));
	    prepStmt.setString(6, ""+v.elementAt(6));
	    prepStmt.setString(7, ""+v.elementAt(7));
	    prepStmt.setString(8, ""+v.elementAt(8));
	    prepStmt.setString(9, ""+v.elementAt(9));
	    prepStmt.setString(10, ""+v.elementAt(10));
	    prepStmt.setString(11, ""+v.elementAt(11));
	    prepStmt.setInt(12, Integer.parseInt(""+v.elementAt(0)));

	    rs = prepStmt.executeUpdate();
	    prepStmt.close();
	    releaseConnection();
	}
	catch(SQLException se) {
	    rollback();
	    releaseConnection();
	    System.out.println("EJBDatabseQueryBean:updateHotDeal:SQLException="+se);
	    return false;
	}
	catch(Exception e) {
	    rollback();
	    releaseConnection();
	    System.out.println("EJBDatabseQueryBean:updateHotDeal:Exception="+e);
	    return false;
	}
	return true;
    }
}

