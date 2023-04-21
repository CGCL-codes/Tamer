package com.openospc.velocity.erp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import com.openospc.db.DbErp;
import com.openospc.department.Department;
import com.openospc.department.Machine;
import com.openospc.erp.M_Condition;
import com.openospc.erp.M_Locator;
import com.openospc.erp.M_Movement;
import com.openospc.formcontroll.Token;
import com.openospc.product.DataField;
import com.openospc.product.Process;
import com.openospc.product.process.GlobalParameter;
import com.openospc.product.process.GlobalParameterCriteria;
import com.openospc.security.Log;
import com.openospc.util.DateUtil;
import com.openospc.velocity.HTMLVelocity;

public class ErpManagement extends HTMLVelocity {

    private Vector vfield;

    private Process process = new Process();

    private Vector vlocator = new Vector();

    private Vector vcond = new Vector();

    private DateUtil du = new DateUtil();

    public void doTemplate() {
        Log.print(5, "A user using menu module.");
        try {
            HttpSession s = request.getSession();
            Token erptoken = (Token) s.getAttribute("erptoken_" + process.getProcessId());
            Token newtoken = new Token();
            s.setAttribute("erptoken_" + process.getProcessId(), newtoken);
            context.put("erptoken", newtoken.getToken());
            shwProcess();
            vfield = this.getFieldList();
            context.put("user", user);
            user.getGivenName();
            user.getDN();
            user.getName();
            user.getRole();
            user.getShift();
            user.getUid();
            if ((request.getParameter("act") != null) && (request.getParameter("act")).equals("updateToERP") && erptoken.isValidToken((request.getParameter("session")))) {
                updateToERP();
            }
            shwLocator();
            shwConditionList();
            context.put("processid", request.getParameter("processid"));
            shwMovement();
            shwERPDataList();
            template = Velocity.getTemplate("/com/openospc/velocity/erp/spcerp.vm");
        } catch (ResourceNotFoundException e) {
            Log.print(1, " Error Occur At Menu Velocity Class <br><br> " + e + " <br><br> Please Troubleshoot the error to maintain system performance and reduce missing information", properties, email);
            e.printStackTrace();
        } catch (ParseErrorException e) {
            Log.print(1, " Error Occur At Menu Velocity Class <br><br> " + e + " <br><br> Please Troubleshoot the error to maintain system performance and reduce missing information", properties, email);
            e.printStackTrace();
        } catch (Exception e) {
            Log.print(1, " Error Occur At Menu Velocity Class <br><br> " + e + " <br><br> Please Troubleshoot the error to maintain system performance and reduce missing information", properties, email);
            e.printStackTrace();
        }
    }

    private void shwMovement() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        DbErp dberp = new DbErp();
        String sql = "SELECT * FROM M_MOVEMENT where DOCSTATUS = 'DR' order by DOCUMENTNO";
        Statement stmt = dberp.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        Vector vmovementline = new Vector();
        while (rs.next()) {
            M_Movement move = new M_Movement();
            move.setDocNo(rs.getString("DOCUMENTNO"));
            move.setDesc(rs.getString("DESCRIPTION"));
            move.setDocStatus(rs.getString("DOCSTATUS"));
            move.setMovementId(rs.getString("M_MOVEMENT_ID"));
            move.getDocNo();
            move.getDesc();
            move.getDocStatus();
            vmovementline.addElement(move);
        }
        context.put("movementlinelist", vmovementline);
        rs.close();
        dberp.close();
    }

    private void shwConditionList() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        DbErp dberp = new DbErp();
        String sql = "SELECT * FROM AD_REF_LIST where AD_REFERENCE_ID = '1000006' order by NAME";
        ResultSet rs;
        Statement stmt = dberp.getStatement();
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            M_Condition c = new M_Condition();
            c.setConditionId(rs.getString("AD_REF_LIST_ID"));
            c.setName(rs.getString("NAME"));
            vcond.addElement(c);
        }
        context.put("condlist", vcond);
    }

    private void shwLocator() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        DbErp dberp = new DbErp();
        String sql = "SELECT * FROM M_LOCATOR where M_WAREHOUSE_ID = '" + ((process.getDept()).getERPWarehouseId()) + "'  AND ISACTIVE = 'Y'  order by VALUE";
        ResultSet rs;
        Statement stmt = dberp.getStatement();
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            M_Locator l = new M_Locator();
            l.setValue(rs.getString("VALUE"));
            l.setLocatorId(rs.getString("M_LOCATOR_ID"));
            l.setDesc(rs.getString("DESCRIPTION"));
            vlocator.addElement(l);
        }
        context.put("locatorlist", vlocator);
        dberp.close();
    }

    private String genDocNo() throws SQLException {
        String docNo = "";
        String realDocNo = "";
        docNo = "" + (process.getDept()).getCode() + "" + du.getToday("yymmdd") + "" + user.getShift() + "" + du.getToday("hh");
        String sql = " SELECT * from DOCNO where Doc = 'ES' AND Pre = '" + docNo + "'";
        Statement stmt = db.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        int count = 0;
        while (rs.next()) {
            count += 1;
            int no = rs.getInt("No") + 1;
            realDocNo = "ES" + "" + rs.getString("Pre") + "-" + ((no < 10) ? "0" + no : "" + no);
        }
        if (count == 0) {
            sql = "INSERT INTO DOCNO (No, Pre, Doc) values ('1', '" + docNo + "', 'ES') ";
            realDocNo = "ES" + "" + docNo + "-" + "01";
        } else sql = "UPDATE DOCNO SET No = (No + 1)  where Doc = 'ES' AND Pre = '" + docNo + "'";
        try {
            Log.print(5, "SQL Command : " + sql);
            stmt = db.getStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            Log.print(1, "Error in SQL  Statement : [" + sql + "], when user = '" + user.getUid() + "' try to update Doc No <br><br>" + e, properties, email);
            e.printStackTrace();
        }
        return realDocNo;
    }

    private void updateToERP() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        DbErp dberp = new DbErp();
        Statement stmt;
        stmt = dberp.getStatement();
        Statement stmt2;
        stmt2 = dbdata.getStatement();
        stmt2.setQueryTimeout(10000);
        int M_MOVEMENT_ID = 0;
        try {
            if (request.getParameterValues("addtoerp") != null) {
                stmt.execute("commit");
                stmt2.execute("BEGIN");
                String[] dataid = request.getParameterValues("addtoerp");
                M_MOVEMENT_ID = getId("246");
                String sql = "INSERT INTO  M_MOVEMENT (M_MOVEMENT_ID, " + "AD_CLIENT_ID, " + "AD_ORG_ID, " + "ISACTIVE, " + "CREATED, " + "CREATEDBY, " + "UPDATEDBY, " + "UPDATED, " + "MOVEMENTDATE, " + "POSTED, " + "PROCESSED, " + "PROCESSING, " + "DOCACTION, " + "DOCSTATUS, " + "ISINTRANSIT, " + "C_DOCTYPE_ID, " + "ISAPPROVED," + "DOCUMENTNO," + "U_SHIFT" + ") values(" + M_MOVEMENT_ID + ", " + "'1000000'," + "'1000000'," + "'Y'," + "SYSDATE," + "'100'," + "'100'," + "SYSDATE," + "TO_DATE('" + du.getToday("yyyy-mm-dd") + " 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), " + "'N', " + "'N'," + "'N'," + "'CO', " + "'DR', " + "'N', " + "'1000020', " + "'N'," + "'" + genDocNo() + "', " + "'" + user.getShift() + "'" + ") ";
                Log.print(5, "SQL Command : " + sql);
                stmt = dberp.getStatement();
                stmt.setQueryTimeout(10000);
                stmt.execute(sql);
                for (int i = 0; i < dataid.length; i++) {
                    int lineid = getId("247");
                    try {
                        Log.print(5, "SQL Command : " + sql);
                        sql = " UPDATE Data" + request.getParameter("processid") + " SET ERP_MOVEMENT_LINEID = '" + lineid + "' where Id = '" + dataid[i] + "'";
                        stmt2.execute(sql);
                        sql = " UPDATE DataSheet" + request.getParameter("processid") + " SET ERP_MOVEMENT_LINEID = '" + lineid + "' where Id = '" + dataid[i] + "'";
                        stmt2.execute(sql);
                        sql = "INSERT INTO  M_MOVEMENTLINE  (M_MOVEMENTLINE_ID, " + "AD_CLIENT_ID , " + "AD_ORG_ID, " + "ISACTIVE, " + "CREATED, " + "CREATEDBY, " + "UPDATED , " + "UPDATEDBY, " + "M_MOVEMENT_ID, " + "M_LOCATOR_ID, " + "M_LOCATORTO_ID, " + "M_PRODUCT_ID, " + "MOVEMENTQTY, " + "DESCRIPTION, " + "M_ATTRIBUTESETINSTANCE_ID, " + "PROCESSED , " + "M_ATTRIBUTESETINSTANCETO_ID, " + "U_COND_FR, " + "U_COND_TO, " + "U_SPC_PROC_ID, " + "U_SPC_DATA_ID, " + "U_LOTNO_FR," + "U_LOTNO_TO," + "LINE) values (" + "" + lineid + ", " + "1000000, " + "1000000, " + "'Y', " + "SYSDATE, " + "100, " + "SYSDATE , " + "100, " + "" + M_MOVEMENT_ID + ", " + "" + request.getParameter("FROM_M_LOCATOR_ID") + ", " + "" + request.getParameter("TO_M_LOCATOR_ID") + ", " + "" + request.getParameter("erp_product_id") + ", " + "" + request.getParameter("qty_batch") + ", " + "'" + request.getParameter("desc_" + dataid[i]) + "'," + "1000000, " + "'N' , " + "1000000, " + "'" + request.getParameter("FROM_CONDITION") + "', " + "'" + request.getParameter("TO_CONDITION") + "', " + "" + request.getParameter("processid") + " , " + "" + dataid[i] + ", " + "'" + request.getParameter("LotNumber_" + dataid[i]) + "'," + "'" + request.getParameter("LotNumber_" + dataid[i]) + "'," + "" + ((i + 1) * 10) + ")";
                        Log.print(5, "SQL Command : " + sql);
                        stmt.execute(sql);
                        sql = " UPDATE Data" + request.getParameter("processid") + " SET ERPstatus = '1' where Id = '" + dataid[i] + "'";
                        stmt2.setQueryTimeout(10000);
                        stmt2.execute(sql);
                        sql = " UPDATE DataSheet" + request.getParameter("processid") + " SET ERPstatus = '1' where Id = '" + dataid[i] + "'";
                        stmt2.execute(sql);
                    } catch (Exception e) {
                        String recipients[] = { properties.getProperty("adminemail"), "mis2@soodekt.com.my" };
                        try {
                            email.postMail(recipients, "Failed on Update ERP from SPC System.", "<br>Movement Line Id = '" + lineid + "', SPC_ID = '" + dataid[i] + "' for Process " + process.getProcessName() + "<br>  ", properties.getProperty("systemsender"), properties);
                        } catch (MessagingException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            stmt.execute("rollback");
            stmt2.execute("rollback");
            String recipients[] = { properties.getProperty("adminemail"), "mis2@soodekt.com.my" };
            try {
                email.postMail(recipients, "Failed on Update ERP from SPC System.", "<br>Movement Id = '" + M_MOVEMENT_ID + "' for Process " + process.getProcessName() + "<br>  " + e, properties.getProperty("systemsender"), properties);
            } catch (MessagingException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            stmt.execute("commit");
            stmt2.execute("commit");
        }
        dberp.close();
    }

    private void shwERPDataList() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String and;
        DbErp dberp = new DbErp();
        String processid = request.getParameter("processid");
        String sql = "SELECT U_SPC_DATA_ID, DOCUMENTNO, M_LOCATOR_ID, M_LOCATORTO_ID, U_LOTNO_FR, U_LOTNO_TO, U_COND_FR, U_COND_TO, m.DESCRIPTION   describe, l.DESCRIPTION   remark  FROM  M_MOVEMENT  m, M_MOVEMENTLINE l where (l.U_SPC_PROC_ID = '" + processid + "') AND (l.M_MOVEMENT_ID = m.M_MOVEMENT_ID)  AND m.DOCSTATUS = 'DR' ";
        Statement stmt = dberp.getStatement();
        ResultSet rs;
        stmt.setQueryTimeout(10000);
        rs = stmt.executeQuery(sql);
        String html = "<table  width=\"100%\" border=\"0\" cellpading=\"2\" cellspacing=\"2\">" + "<tr><th>&nbsp;</th><th>&nbsp;</th>" + "<th></th>" + "<th colspan=\"2\">Locator</th>" + "<th colspan=\"2\">Lot</th>" + "<th colspan=\"2\">Condition</th>" + "<th>Remarks</th>" + "<th colspan=\"100\"></th>\n";
        html += " " + "<tr><th><input type=\"checkbox\" name=\"addcheckall2\"  onClick=\"selectAll()\" /></th>" + "<th>No</th>" + "<th>ID</th>" + "<th>Doc No.</th>" + "<th>From</th>" + "<th>To</th>" + "<th>From</th>" + "<th>To</th>" + "<th>From </th>" + "<th>To</th>" + "<th>Remarks</th>\n";
        for (int i = 0; i < vfield.size(); i++) {
            DataField df1 = (DataField) vfield.elementAt(i);
            html += "<th>" + df1.getDisplayName() + "</th>";
        }
        html += "</tr>\n";
        String style = "style_a";
        and = "";
        int count = 1;
        while (rs.next()) {
            html += "<tr class=\"" + style + "\"><td><input type=\"checkbox\" name=\"addtoerp1\" value=\"" + rs.getString("U_SPC_DATA_ID") + "\" /></td>" + "<td align=\"right\">" + count + ".</td>" + "<td>" + rs.getString("U_SPC_DATA_ID") + "</td>";
            html += "<td align=\"center\">" + rs.getString("DOCUMENTNO") + "</td>";
            html += "<td align=\"center\">" + this.getLocatorValue(rs.getString("M_LOCATOR_ID")) + "</td>";
            html += "<td align=\"center\">" + this.getLocatorValue(rs.getString("M_LOCATORTO_ID")) + "</td>" + "<td align=\"center\">" + rs.getString("U_LOTNO_FR") + "</td>" + "<td align=\"center\">" + rs.getString("U_LOTNO_TO") + "</td>" + "<td align=\"center\">" + rs.getString("U_COND_FR") + "</td>" + "<td align=\"center\">" + rs.getString("U_COND_TO") + "</td>" + "<td>" + rs.getString("remark") + "</td>";
            html += getERPSPCData(rs.getString("U_SPC_DATA_ID")) + "</tr>\n";
            if (style.equals("style_a")) style = "style_b"; else if (style.equals("style_b")) style = "style_a";
            count++;
        }
        html += "</table>";
        context.put("erpdatatable", html);
        rs.close();
        sql = "SELECT * FROM Data" + processid + " d, Summary" + processid + " s  where d.Id = s.Id AND ERPstatus != '1' " + and;
        Statement stmt1 = dbdata.getStatement();
        rs = stmt1.executeQuery(sql);
        html = "<table><tr><th><input type=\"checkbox\" name=\"addcheckall\"  onClick=\"selectAll(this)\" /></th><th>No.</th><th>ID</th>";
        for (int i = 0; i < vfield.size(); i++) {
            DataField df = (DataField) vfield.elementAt(i);
            html += "<th>" + df.getDisplayName() + "</th>";
        }
        html += "<th>Remark</th><th>Movement Line Id</th><th>Status</th></tr>\n";
        style = "style_a";
        count = 1;
        while (rs.next()) {
            String checked = "";
            if ((rs.getString("FinalResult")).equalsIgnoreCase("ok")) checked = "checked";
            html += "<tr class=\"" + style + "\"><td><input type=\"checkbox\" name=\"addtoerp\" value=\"" + rs.getString("d.Id") + "\"  " + checked + " /></td><td align=\"right\">" + count + ".</td><td>" + rs.getString("d.Id") + "</td>";
            for (int i = 0; i < vfield.size(); i++) {
                DataField df = (DataField) vfield.elementAt(i);
                html += "<td align=\"center\"> <input type=\"hidden\" name=\"" + df.getDataFieldName() + "_" + rs.getString("d.Id") + "\" value=\"" + rs.getString((df.getDataFieldName())) + "\" />      " + df.getHTMLDisplay(rs.getString((df.getDataFieldName()))) + "</td>";
            }
            html += "<td><input name=\"desc" + "_" + rs.getString("d.Id") + "\" value=\"-\" /></td>";
            html += "<td>" + rs.getString("ERP_MOVEMENT_LINEID") + "</td>" + "<td>" + ((rs.getInt("ERPstatus") == 0) ? "<a style=\"background:red\">New</a>" : (rs.getInt("ERPstatus") == 1 ? "Posted" : "Unknown")) + "</td></tr>\n";
            if (style.equals("style_a")) style = "style_b"; else if (style.equals("style_b")) style = "style_a";
            count++;
        }
        html += "</table>";
        context.put("datatable", html);
        rs.close();
        dberp.close();
    }

    private String getLocatorValue(String locatorid) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String value = "";
        DbErp dberp = new DbErp();
        String sql = "SELECT VALUE from M_LOCATOR where M_LOCATOR_ID = '" + locatorid + "'";
        Statement stmt = dberp.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            value = rs.getString("VALUE");
        }
        rs.close();
        dberp.close();
        return value;
    }

    private String getConditionName(String condid) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String value = "";
        DbErp dberp = new DbErp();
        String sql = "SELECT NAME FROM AD_REF_LIST where AD_REFERENCE_ID = '1000006' AND AD_REF_LIST_ID='" + condid + "'";
        ResultSet rs;
        Statement stmt = dberp.getStatement();
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            value = rs.getString("NAME");
        }
        dberp.close();
        return value;
    }

    private String getERPSPCData(String dataid) throws SQLException {
        Vector v = new Vector();
        String sql = "SELECT * FROM  Data" + request.getParameter("processid") + " d, Summary" + request.getParameter("processid") + " s  where d.Id = s.Id  AND (d.Id = '" + dataid + "')";
        Statement stmt = dbdata.getStatement();
        ResultSet rs;
        String html = "";
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            for (int i = 0; i < vfield.size(); i++) {
                DataField df = (DataField) vfield.elementAt(i);
                html += "<td>" + df.getHTMLDisplay(rs.getString((df.getDataFieldName()))) + "</td>";
            }
        }
        return html;
    }

    private Vector getFieldList() throws SQLException {
        Vector v = new Vector();
        String sql = "SELECT * from  SqlScripts s, SqlScriptDataFields sd, DataFields d where " + "s.ProcessId = '" + request.getParameter("processid") + "' AND " + "s.SqlScriptId = sd.SqlScriptId AND " + "sd.DataFieldId = d.DataFieldId " + " order by  sd.DataFieldSequence";
        Statement stmt = db.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        Vector mclist = getMachineList();
        while (rs.next()) {
            DataField df = new DataField();
            df.setFieldId(getString(rs.getString("d.DataFieldId")));
            df.setDisplayName(getString(rs.getString("d.DisplayName")));
            df.setFieldType(getString(rs.getString("d.FieldType")));
            df.setDataFieldName(getString(rs.getString("d.Name")));
            df.setAssignValue(getString(rs.getString("d.AssignValue")));
            df.setDataType(rs.getString("d.DataType"));
            df.setSize(rs.getInt("d.Size"));
            df.setRowspan(rs.getInt("d.Rowspan"));
            df.setColspan(rs.getInt("d.Colspan"));
            df.setHTMLId(getString(rs.getString("d.HTMLId")));
            df.setCategory(getString(rs.getString("d.Category")));
            df.setFontSize(rs.getInt("d.FontSize"));
            df.setBGColor(getString(rs.getString("d.BgColour")));
            df.setGlobalParameterId(rs.getString("d.GlobalParameterId"));
            if (((df.getGlobalParameterId()) != null) && !(df.getGlobalParameterId()).equals("") && !(df.getGlobalParameterId()).equals("0")) {
                df.setParameterStatus(2);
                df.setGlobalParameter(this.getGlobalParameter(rs.getString("d.GlobalParameterId")));
            } else {
            }
            df.setMachineList(mclist);
            v.addElement(df);
        }
        rs.close();
        return v;
    }

    private GlobalParameter getGlobalParameter(String gpid) throws SQLException {
        GlobalParameter gp = new GlobalParameter();
        String sql = " SELECT * from GlobalParameters where GlobalParameterId = '" + gpid + "'";
        Statement stmt = db.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            gp.setCircumstances(rs.getString("Circumstances"));
            gp.setDescription(rs.getString("Description"));
            gp.setParameterName(rs.getString("Name"));
            gp.setCriteriaList(getGlobalCriteriaList(gpid));
        }
        return gp;
    }

    private Vector getMachineList() throws SQLException {
        Vector v = new Vector();
        String sql = " SELECT * FROM Machines m,  Processes p, Departments d where (p.DepartmentId = d.DepartmentId) " + " AND (d.DepartmentId = m.DepartmentId) " + " AND p.ProcessId = '" + request.getParameter("processid") + "'";
        Statement stmt = db.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Machine m = new Machine();
            m.setMachineName(rs.getString("m.Name"));
            m.setMachineId(rs.getString("m.MachineId"));
            v.addElement(m);
        }
        return v;
    }

    private Vector getGlobalCriteriaList(String gpid) throws SQLException {
        Vector v = new Vector();
        String sql = " SELECT * from GlobalParameterCriterias where GlobalParameterId = '" + gpid + "'";
        Statement stmt = db.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            GlobalParameterCriteria gpc = new GlobalParameterCriteria();
            gpc.setGlobalParameterCriteriaId(rs.getString("GlobalParameterCriteriaId"));
            gpc.setSequence(rs.getInt("Sequence"));
            gpc.setValue(rs.getFloat("Value"));
            gpc.setBackgroundColor(rs.getString("BackgroundColor"));
            gpc.setAlertMessage(rs.getString("AlertMessage"));
            gpc.setAlert(rs.getInt("Alert"));
            gpc.setNotAllowed(rs.getInt("NotAllowed"));
            gpc.setCharacterValue(rs.getString("CharacterValue"));
            gpc.setValue1(rs.getFloat("Value1"));
            gpc.setValue2(rs.getFloat("Value2"));
            gpc.setBlinking(rs.getInt("Blinking"));
            gpc.setColor(rs.getString("Color"));
            v.addElement(gpc);
        }
        return v;
    }

    private String getString(String s) {
        if (s == null) return ""; else return s;
    }

    private int getId(String tblid) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        DbErp dberp = new DbErp();
        String sql = "SELECT * FROM AD_SEQUENCE where AD_SEQUENCE_ID = '" + tblid + "'";
        Statement stmt = dberp.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        int id = 0;
        while (rs.next()) {
            id = rs.getInt("CURRENTNEXT");
        }
        rs.close();
        sql = "UPDATE AD_SEQUENCE SET CURRENTNEXT = (CURRENTNEXT + 1), UPDATED = SYSDATE where AD_SEQUENCE_ID = '" + tblid + "'";
        try {
            Log.print(5, "SQL Command : " + sql);
            stmt = dberp.getStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            Log.print(1, "Error in SQL  Statement : [" + sql + "], when user = '" + user.getUid() + "' try to add new Product Group <br><br>" + e, properties, email);
            e.printStackTrace();
        }
        dberp.close();
        return id;
    }

    private void shwProcess() throws ResourceNotFoundException, ParseErrorException, Exception {
        String sql = "SELECT * FROM Processes as p, " + "ProductGroups as g, " + "ProductModels as m, Departments d where ProcessId = " + request.getParameter("processid") + " " + "AND (g.ProductGroupId = m.ProductGroupId) " + "AND (p.ProductModelId = m.ProductModelId) " + "AND p.DepartmentId = d.DepartmentId";
        Statement stmt = db.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            process.setProductGrpId(rs.getString("g.ProductGroupId"));
            process.getProductGrpId();
            process.setProductGrpName(rs.getString("g.Name"));
            process.getProductGrpName();
            process.setProductGrpDesc(rs.getString("g.Description"));
            process.getProductGrpDesc();
            process.setGrpStatus(rs.getInt("g.ProductGroupStatus"));
            process.getGrpStatus();
            process.setPrdModelName(rs.getString("m.Name"));
            process.getPrdModelName();
            process.setProcessId(rs.getString("p.ProcessId"));
            process.getProcessId();
            process.setProcessRev(rs.getString("p.Revision"));
            process.getProcessRev();
            process.setProcessName(rs.getString("p.Name"));
            process.getProcessName();
            process.setWho(rs.getString("p.Who"));
            process.getWho();
            process.setSequence(rs.getInt("p.ProcessSequence"));
            process.getSequence();
            process.setProcessStatus(rs.getInt("p.ProcessStatus"));
            process.getProcessStatus();
            process.setProcessDesc(rs.getString("p.ProcessDescription"));
            process.setLocatorId(rs.getString("ERP_M_LOCATOR_ID"));
            process.setPrdModelNumber(rs.getString("m.Number"));
            process.getPrdModelNumber();
            process.setERPProductId(rs.getString("ERP_M_PRODUCT_ID"));
            Department d = new Department();
            d.setId(rs.getString("d.DepartmentId"));
            d.setName(rs.getString("d.Name"));
            d.setCode(rs.getString("d.Code"));
            d.setERPWarehouseId(rs.getString("d.ERP_WAREHOUSE_ID"));
            process.setDept(d);
            process.setQuantity(rs.getInt("Quantity"));
        }
        context.put("process", process);
    }
}
