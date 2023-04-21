package com.wwwc.admin.web.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.SQLException;

import com.wwwc.admin.web.database.*;
import com.wwwc.util.web.*;

public class adminProductManagerBean extends adminBean {

    public StringBuffer getDatabaseOptions(ResourceBundle config, String call_path, String database) {
	String databases   = config.getString("3wcenter.databases");
	StringBuffer sbf = new StringBuffer("");

	sbf.append("<TABLE border=0 style='font-size: 10pt' cellSpacing=10 cellPadding=0>");
	String temp = null;
	StringTokenizer tokens = new StringTokenizer(databases, ",");
	int cols = tokens.countTokens();
	sbf.append("<TR>");
	while (tokens.hasMoreTokens()) {
	    temp = tokens.nextToken();
	    if(database != null && database.equals(temp)) {
		sbf.append("<TD align=center><B>"+temp+"</B></TD>");
	    }
	    else {
		sbf.append("<TD align=center><A href="+call_path+"?database="+temp+"&aid="+">"+temp+"</A></TD>");
	    }
	}

	sbf.append("</TR>");
	sbf.append("</TABLE>");
	sbf.append("</FORM>");
	return sbf;
    }

    public StringBuffer managerBar (String call_path, String add_cb, String update_cb, String delete_cb, String action, String database) {
	StringBuffer sbf = new StringBuffer("");
	sbf.append("<TABLE border='0'  cellSpacing='10' cellPadding='10' align=center>");
	sbf.append("<TR>");
	if(action == null || !action.equals(add_cb)) {
	    sbf.append("<TD><A href="+call_path+"?aid="+add_cb+"&database="+database+">Add</A></TD>");
	}
	else {
	    sbf.append("<TD><B>Add</B></TD>");
	}

	if(action == null || !action.equals(update_cb)) {
	    sbf.append("<TD><A href="+call_path+"?aid="+update_cb+"&database="+database+">Update</A></TD>");
	}
	else {
	    sbf.append("<TD><B>Update</B></TD>");
	}
	if(action == null || !action.equals(delete_cb)) {
	    sbf.append("<TD><A href="+call_path+"?aid="+delete_cb+"&database="+database+">Delete</A></TD>");
	}
	else {
	    sbf.append("<TD><B>Delete</B></TD>");
	}
	sbf.append("</TR>");
	sbf.append("</TABLE>");
	return sbf;
    }

    public StringBuffer addProduct (ResourceBundle config, HttpServletRequest request, String call_path, 
				    String this_call_back, String database) {
	String databases   = config.getString("3wcenter.databases");
	Database db  = DatabaseAdapter.getDatabase(database, config);


	StringBuffer sbf = new StringBuffer("");
	String error = null;
	String submit = request.getParameter("add_product");

	String p_category_id = null;
	String p_name        = null;
	String p_description = null;
	String p_serial      = null;

	String p_in_stock    = null;
	String p_on_order    = null;
	
	int product_id = -1;

	if(submit != null && submit.equals("Add Product")) {
	    p_category_id = request.getParameter("p_category_id");
	    p_name        = request.getParameter("p_name");
	    p_description = request.getParameter("p_description");
	    p_serial      = request.getParameter("p_serial");
	    p_in_stock    = request.getParameter("p_in_stock");
	    p_on_order    = request.getParameter("p_on_order");

	    if(p_category_id == null || p_category_id.length()<1) {
		error = "Please select [Product Category].";
	    }

	    if(error == null) {
		if(p_name == null || p_name.length()<1) {
		    error = "Product name is required.";
		}
	    }

	    if(error == null) {
		// Table Product
		// ID(*) NAME DESCRIPTION CATEGORYID SERIALNUMBER UNITINSTOCK UNITONORDER REORDERLEVEL DISCONTINUE SUPPLIERID 
		// INT   VARN    VARN        INT         VARN         INT        INT          INT         BIT        VARN

		if(p_name.indexOf("'") != -1) {
		    p_name = p_name.replaceAll("'","'''");
		}

		if(p_description.indexOf("'") != -1) {
		    p_description = p_description.replaceAll("'","'''");
		}

		if(p_serial.indexOf("'") != -1) {
		    p_serial = p_serial.replaceAll("'","'''");
		}

		product_id = db.getNextProductId();

		String query = "INSERT INTO PRODUCT (Id, Name, Description, CategoryId, SerialNumber, UnitInStock, UnitOnOrder)"+
		    "VALUES ("+product_id+",'"+p_name+"','"+p_description+"', "+p_category_id+",'"+p_serial+"', "+
		    p_in_stock+","+p_on_order+")";

		String temp = null;
		StringTokenizer tokens = new StringTokenizer(databases, ",");
		int m =0;
		String selected_database = null;
		StringBuffer msbf = new StringBuffer();

		while (tokens.hasMoreTokens()) {
		    if(error != null) {
			break;
		    }

		    temp = tokens.nextToken();
		    selected_database = request.getParameter(temp);
		    if(selected_database == null) {
			continue;
		    }
		    m++;

		    db = DatabaseAdapter.getDatabase(selected_database, config);
		
		    if(db.insert(query)<=0) {
			error = "Save data error!";
			db = DatabaseAdapter.getDatabase(database, config);
		    }
		    else {
			msbf.append("Save data into "+selected_database+" ... OK.<BR>");
		    }
		}

		if(error == null && m==0) {
		    error = "No database have been selected.";
		}
		if(error == null) {
		    if(msbf.length()>0) {
			return msbf;
		    }
		}
	    }
	}
	
	sbf.append("<FORM method=post action="+call_path+">");
	sbf.append("<TABLE border='1'  cellSpacing='0' cellPadding='2' align=center>");
	if(error != null) {
	    sbf.append("<TR>");
	    sbf.append("<TD align=center colspan=2><img src=/images/star.gif>"+error+"</TD>");
	    sbf.append("</TR>");
	}

	sbf.append("<TR>");
	sbf.append("<TD><B>Product Category</B></TD>");
	sbf.append("<TD align=left>");

	
	String query = "SELECT * FROM PRODUCT_CATEGORY WHERE PID=-1;";
	db.select(query);
	Vector vv = db.getTableRecordVector();
	Enumeration enum = vv.elements();
	Vector v = null;
	String id   = null;
	String name = null;
	Vector svv = null;
	Vector cidv = null;
	Enumeration enum2 = null;
	int m=0;
	while(enum.hasMoreElements()) {
	    v = (Vector)enum.nextElement();
	    id   = (String)v.elementAt(0);
	    name = (String)v.elementAt(1);
	    sbf.append("<TABLE border='0'  cellSpacing='0' cellPadding='2'>");
	    sbf.append(getThisOption("p_category_id", p_category_id, id, db, -1));
	    
	    while(hasChild(id, db)) {
		cidv = getChildIdVector(id, db);
		enum2 = cidv.elements();
		while(enum2.hasMoreElements()) {
		    id = (String)enum2.nextElement();
		    sbf.append(getThisOption("p_category_id", p_category_id, id, db, m));
		    if(hasChild(id, db)) {
			m++;
			break;
		    }
		}
	    }
	    sbf.append("</TABLE>");
	}

	sbf.append("</TD></TR>");
	
	
	sbf.append("<TR><TD align=right><B>Name(*)</B></TD>");
	if(p_name == null) {
	    sbf.append("<TD><input type=text name=p_name value='' maxLength=200 size=50 style='font-size:8pt'></TD>");
	}
	else {
	    sbf.append("<TD><input type=text name=p_name value='"+p_name+"' maxLength=200 size=50 style='font-size:8pt'></TD>");
	}
	sbf.append("</TR>"); 
	sbf.append("<TR><TD align=right><B>Description</B></TD>");
	if(p_description == null) {
	    sbf.append("<TD><textarea name=p_description rows=3 cols=30></textarea></TD>");
	}
	else {
	    sbf.append("<TD><textarea name=p_description rows=3 cols=30>"+p_description+"</textarea></TD>");
	}
	sbf.append("</TR>"); 

	sbf.append("<TR><TD align=right><B>Serial Number</B></TD>");
	if(p_serial == null) {
	    sbf.append("<TD><input type=text name=p_serial value='' maxLength=20 size=50 style='font-size:8pt'></TD>");
	}
	else {
	    sbf.append("<TD><input type=text name=p_serial value='"+p_serial+"' maxLength=20 size=50 style='font-size:8pt'></TD>");
	}
	sbf.append("</TR>"); 

	sbf.append("<TR><TD align=right><B>Unit In Stock</B></TD>");
	sbf.append("<TD><SELECT name=p_in_stock style='font-size:8pt'>"+MyHtmlForm.getOptions(0,2000, p_in_stock)+"</SELECT></TD>");
	sbf.append("</TR>"); 

	sbf.append("<TR><TD align=right><B>Unit On Order</B></TD>");
	sbf.append("<TD><SELECT name=p_on_order style='font-size:8pt'>"+MyHtmlForm.getOptions(0,2000, p_on_order)+"</SELECT></TD>");
	sbf.append("</TR>"); 

	
	sbf.append("<TR><TD><B>Database</B></TD>");
	sbf.append("<TD>"+getDatabaseOptionCheckBox(databases, database));
	sbf.append("</TD></TR>");

	sbf.append("<TR>");
	sbf.append("<TD align=center colspan=2><input type=submit name=add_product value='Add Product' style='font-size: 8pt'></TD>");
	sbf.append("</TR>");

	sbf.append("</TABLE>");
	sbf.append("<input type=hidden name=aid value="+this_call_back+">");
	sbf.append("<input type=hidden name=database value="+database+">");
	sbf.append("</FORM>");
	return sbf;
    }

    private boolean hasChild(String id, Database db) {
	String query = "SELECT * FROM PRODUCT_CATEGORY WHERE PID="+id+";";
	db.select(query);
	Vector vv = db.getTableRecordVector();
	if(vv != null && !vv.isEmpty()) {
	    return true;
	}
	return false;
    }
    
    private Vector getChildIdVector(String pid, Database db) {
	String query = "SELECT ID FROM PRODUCT_CATEGORY WHERE PID="+pid+";";
	Vector vid = db.getSingleFieldValueVector(query, "ID");
	if(vid.isEmpty()) {
	    vid = null;
	}
	return vid;
    }

    private StringBuffer getThisOption(String option_name, String selected_id, String id, Database db, int position) {
	StringBuffer sbf = new StringBuffer();
	String query = "SELECT NAME FROM PRODUCT_CATEGORY WHERE ID="+id+";";
	String name = db.getSingleFieldValue(query, "NAME");
	String align="";
	for(int j=0; j<=position; j++) {
	    align += "&nbsp;&nbsp;&nbsp;";
	}

	if(selected_id != null && selected_id.equals(id)) {
	    sbf.append("<TR><TD>"+align+"<input type=radio name='"+option_name+"' value="+id+" checked>"+name+"</TD></TR>");
	}
	else {
	    sbf.append("<TR><TD>"+align+"<input type=radio name='"+option_name+"' value="+id+">"+name+"</TD></TR>");
	}

	return sbf;
    }
}

