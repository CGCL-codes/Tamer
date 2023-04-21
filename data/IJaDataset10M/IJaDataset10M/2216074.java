package com.soode.openospc.velocity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.servlet.VelocityServlet;
import com.soode.db.Db;
import com.soode.db.DbException;
import com.soode.openospc.security.Submenu;
import com.soode.openospc.util.DateUtil;
import com.soode.openospc.util.Id;

/**
 * This class will Only Save Data Form the Data Form and call the next sevlet for view chart.
 * @author Terakhir bin Wih
 * @email khirzrn@gmail.com
 * @version 1.0
 */
public class DataManager extends VelocityServlet {

    String theId;

    public Template handleRequest(HttpServletRequest request, HttpServletResponse response, Context context) throws ResourceNotFoundException, ParseErrorException, Exception {
        Template template = null;
        HttpSession session = request.getSession();
        if ((session.getAttribute("user") != null) && (session.getAttribute("roleId") != null) && (session.getAttribute("logId") != null)) {
            Hashtable hUser = (Hashtable) session.getAttribute("user");
            context.put("user", hUser);
            String roleId = (String) session.getAttribute("roleId");
            try {
                String smenu = "";
                Hashtable privileges;
                String addPermission = "0";
                String deletePermission = "0";
                String updatePermission = "0";
                String readPermission = "0";
                if (request.getParameter("smenu") != null) {
                    smenu = request.getParameter("smenu");
                    Submenu submenu = new Submenu();
                    privileges = submenu.getPrivileges(smenu, roleId);
                    if ("history".equals((String) privileges.get("servletName")) || "inspection".equals((String) privileges.get("servletName"))) {
                        addPermission = (String) privileges.get("addPermission");
                        deletePermission = (String) privileges.get("deletePermission");
                        updatePermission = (String) privileges.get("updatePermission");
                        readPermission = (String) privileges.get("readPermission");
                    }
                }
                context.put("readPermission", readPermission);
                context.put("updatePermission", updatePermission);
                context.put("addPermission", addPermission);
                context.put("deletePermission", deletePermission);
                if (addPermission.equals("1")) {
                    String dataFormId = request.getParameter("dataFormId");
                    String dataTable = "Data" + dataFormId;
                    String dataSheetTable = "DataSheet" + dataFormId;
                    String summaryTable = "Summary" + dataFormId;
                    String summarySheetTable = "SummarySheet" + dataFormId;
                    Id id = new Id();
                    Hashtable hId = id.getId("Data" + dataFormId);
                    theId = (String) hId.get("nextValue");
                    Vector vData = this.getField(dataTable, request, hUser);
                    Vector vSummary = this.getField(summaryTable, request, hUser);
                    this.addData(this.getSQLStatement(vData, dataTable), this.getSQLStatement(vSummary, summaryTable));
                    this.addData(this.getSQLStatement(vData, dataSheetTable), this.getSQLStatement(vSummary, summarySheetTable));
                    context.put("shift", request.getParameter("Shift"));
                    context.put("machineId", request.getParameter("MachineId"));
                    context.put("urlForward", "chart");
                    context.put("cValue", "");
                    String allInput = "<input name=\"dataFormId\" type=\"hidden\"  value=\"" + request.getParameter("dataFormId") + "\" >" + "<input name=\"machineId\" type=\"hidden\" value=\"" + request.getParameter("MachineId") + "\" />" + "<input name=\"shift\" type=\"hidden\" value=\"" + request.getParameter("Shift") + "\" />" + "<input name=\"date\" type=\"hidden\" value=\"" + request.getParameter("ProductionYear") + "-" + request.getParameter("ProductionMonth") + "-" + request.getParameter("ProductionDate") + "\" />";
                    context.put("allInput", allInput);
                    template = Velocity.getTemplate("datamanager/forward.vm");
                }
            } catch (Exception e) {
                context.put("msg", e);
                template = Velocity.getTemplate("dataManager.vm");
            }
        } else {
            response.sendRedirect("login");
        }
        return template;
    }

    /**
	 * To get a list of field and read the value from URL
	 */
    private Vector getField(String tableName, HttpServletRequest r, Hashtable hUser) throws SQLException, DbException {
        Vector v = new Vector();
        Db db = new Db("properties.openospc");
        Statement stmt = db.getStatement();
        String sql = "desc " + tableName;
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            if ((rs.getString("Field").equals("Id"))) {
                Hashtable h = new Hashtable();
                h.put("fieldName", rs.getString("Field"));
                h.put("fieldValue", theId);
                v.addElement(h);
            }
            if ((rs.getString("Field").equals("CreatedByUid"))) {
                Hashtable h = new Hashtable();
                h.put("fieldName", rs.getString("Field"));
                h.put("fieldValue", (String) hUser.get("uid"));
                v.addElement(h);
            }
            if ((rs.getString("Field").equals("ShiftGroup"))) {
                Hashtable h = new Hashtable();
                h.put("fieldName", rs.getString("Field"));
                h.put("fieldValue", r.getParameter("userShift"));
                v.addElement(h);
            }
            if ((rs.getString("Field").equals("CreatedShift"))) {
                Hashtable h = new Hashtable();
                h.put("fieldName", rs.getString("Field"));
                h.put("fieldValue", r.getParameter("Shift"));
                v.addElement(h);
            }
            if ((rs.getString("Field").equals("CreatedOnTime"))) {
                Hashtable h = new Hashtable();
                h.put("fieldName", rs.getString("Field"));
                DateUtil du = new DateUtil();
                h.put("fieldValue", du.getSystemDate() + " " + du.getSystemTime());
                v.addElement(h);
            }
            if ((rs.getString("Field").equals("UpdatedByUid"))) {
                Hashtable h = new Hashtable();
                h.put("fieldName", rs.getString("Field"));
                DateUtil du = new DateUtil();
                h.put("fieldValue", (String) hUser.get("uid"));
                v.addElement(h);
            }
            if ((rs.getString("Field").equals("UpdatedShift"))) {
                Hashtable h = new Hashtable();
                h.put("fieldName", rs.getString("Field"));
                h.put("fieldValue", (String) hUser.get("shiftGroup"));
                v.addElement(h);
            }
            if ((rs.getString("Field").equals("UpdatedOnTime"))) {
                Hashtable h = new Hashtable();
                h.put("fieldName", rs.getString("Field"));
                DateUtil du = new DateUtil();
                h.put("fieldValue", du.getSystemDate() + " " + du.getSystemTime());
                v.addElement(h);
            }
            if ((rs.getString("Field").equals("ProductionDate"))) {
                Hashtable h = new Hashtable();
                h.put("fieldName", rs.getString("Field"));
                h.put("fieldValue", "" + r.getParameter("ProductionYear") + "-" + r.getParameter("ProductionMonth") + "-" + r.getParameter("ProductionDate"));
                v.addElement(h);
            }
            if ((!rs.getString("Field").equals("Id")) && (!rs.getString("Field").equals("CreatedByUid")) && (!rs.getString("Field").equals("CreatedBy")) && (!rs.getString("Field").equals("CreatedShift")) && (!rs.getString("Field").equals("CreatedByLoginDate")) && (!rs.getString("Field").equals("CreatedByLoginTime")) && (!rs.getString("Field").equals("CreatedOnDate")) && (!rs.getString("Field").equals("CreatedOnTime")) && (!rs.getString("Field").equals("UpdatedBy")) && (!rs.getString("Field").equals("UpdatedByUid")) && (!rs.getString("Field").equals("UpdatedShift")) && (!rs.getString("Field").equals("UpdatedOnLoginDate")) && (!rs.getString("Field").equals("UpdatedOnLoginTime")) && (!rs.getString("Field").equals("UpdatedOnDate")) && (!rs.getString("Field").equals("ProductionDate")) && (!rs.getString("Field").equals("ShiftGroup")) && (!rs.getString("Field").equals("UpdatedOnTime"))) {
                Hashtable h = new Hashtable();
                try {
                    h.put("fieldName", rs.getString("Field"));
                    h.put("fieldValue", r.getParameter((String) h.get("fieldName")));
                } catch (Exception e) {
                }
                v.addElement(h);
            }
        }
        db.close();
        return v;
    }

    /**
	 * This method will create the add SQL statement 
	 * @param vField Contain field name and Data.
	 * @param tableName The table name
	 * @return The SQL Statement.
	 */
    private String getSQLStatement(Vector vField, String tableName) {
        String lsField = "";
        String lsValue = "";
        for (int i = 0; i < vField.size(); i++) {
            Hashtable h = new Hashtable();
            h = (Hashtable) vField.elementAt(i);
            if (i == 0) {
                lsField = (String) h.get("fieldName");
                lsValue = " '" + (String) h.get("fieldValue") + "' ";
            } else {
                lsField += ", " + (String) h.get("fieldName");
                lsValue += ", '" + (String) h.get("fieldValue") + "' ";
            }
        }
        return "INSERT INTO  " + tableName + " (" + lsField + ") values (  " + lsValue + " )";
    }

    /**
	 * This method will add the data into Database. 
	 * @param dataStmt The data SQL statement
	 * @param summaryStmt The summary SQL statement
	 * @throws DbException
	 * @throws SQLException
	 * @author khir
	 */
    private void addData(String dataStmt, String summaryStmt) throws DbException, SQLException {
        Db db = new Db("properties.openospc");
        Statement stmt = db.getStatement();
        stmt.setQueryTimeout(60);
        stmt.execute(dataStmt);
        stmt.execute(summaryStmt);
        db.close();
    }
}
