package com.openospc.velocity.dataentry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import com.openospc.department.Department;
import com.openospc.department.Machine;
import com.openospc.product.DataField;
import com.openospc.product.DataGroup;
import com.openospc.product.Process;
import com.openospc.product.process.GlobalParameter;
import com.openospc.product.process.GlobalParameterCriteria;
import com.openospc.product.process.ProcessParameter;
import com.openospc.security.Log;
import com.openospc.util.DateUtil;
import com.openospc.velocity.HTMLVelocity;

public class ProcessForm extends HTMLVelocity {

    Vector next = new Vector();

    String formula = "";

    Vector globalParamList = new Vector();

    Vector mclist = new Vector();

    public void doTemplate() {
        Log.print(5, "A user using menu module.");
        try {
            Process process = this.getProcess();
            setDataFields();
            context.put("process", process);
            context.put("globalParamList", globalParamList);
            template = Velocity.getTemplate("/com/openospc/velocity/dataentry/dataentry.vm");
            try {
                StringWriter writer = new StringWriter();
                template.merge(context, writer);
                writer.close();
                StringBuffer sb = writer.getBuffer();
                FileOutputStream out1;
                PrintStream p;
                try {
                    try {
                        File f = new File("" + properties.getProperty("file.resource.loader.path") + "/process" + request.getParameter("processid"));
                        f.mkdir();
                    } catch (Exception e) {
                    }
                    String path = "" + properties.getProperty("file.resource.loader.path") + "/process" + request.getParameter("processid") + "/rev_" + process.getProcessRev() + ".vm";
                    out1 = new FileOutputStream(path);
                    p = new PrintStream(out1);
                    p.println(sb);
                    p.close();
                } catch (Exception e) {
                    System.err.println("Error writing to file" + e);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    throw ex;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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

    private Process getProcess() throws ResourceNotFoundException, ParseErrorException, Exception {
        String sql = "SELECT * FROM Processes as p, " + "ProductGroups as g, " + "ProductModels as m where ProcessId = " + request.getParameter("processid") + " " + "AND (g.ProductGroupId = m.ProductGroupId) AND (p.ProductModelId = m.ProductModelId)  ";
        Statement stmt = db.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        Process process = new Process();
        while (rs.next()) {
            process.setDept(this.getDepartment(rs.getString("DepartmentId")));
            mclist = this.getMachineList(rs.getString("DepartmentId"));
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
            process.getProcessDesc();
            process.setPrdModelNumber(rs.getString("m.Number"));
            process.getPrdModelNumber();
            process.setParameterList(getParameterList());
        }
        context.put("process", process);
        sql = "SELECT COUNT(DataGroupId) as c, GroupSequence " + "from DataGroups  " + "where ProcessId = '" + request.getParameter("processid") + "' " + "group by  GroupSequence";
        stmt = db.getStatement();
        rs = stmt.executeQuery(sql);
        Vector v = new Vector();
        int seq = 0;
        while (rs.next()) {
            seq = seq + 1;
            Vector vgroup = new Vector();
            sql = "SELECT * from DataGroups where ProcessId = " + "'" + request.getParameter("processid") + "'" + " AND GroupSequence = '" + seq + "'";
            System.out.println("THE STATEMENT = " + sql);
            Statement stmt1 = db.getStatement();
            ResultSet rs1 = stmt1.executeQuery(sql);
            Vector vx = new Vector();
            while (rs1.next()) {
                DataGroup grp = new DataGroup();
                grp.setDataGrpName(rs1.getString("Name"));
                grp.setId(rs1.getString("DataGroupId"));
                grp.setGrpSequence(rs1.getInt("GroupSequence"));
                grp.setBorderSize(rs1.getInt("BorderSize"));
                grp.setTotalColumn(rs1.getInt("TotalColumns"));
                grp.setTotalRows(rs1.getInt("TotalRows"));
                grp.setHTMLGroup(this.getGroup(grp.getId(), rs1.getInt("TotalRows"), rs1.getInt("TotalColumns")));
                vx.addElement(grp);
            }
            vgroup.addElement(vx);
            v.addElement(vgroup);
        }
        process.setHTMLGrpList(v);
        rs.close();
        return process;
    }

    private Department getDepartment(String deptid) throws SQLException {
        Department dept = new Department();
        String sql = "SELECT * FROM  Departments WHERE DepartmentId = '" + deptid + "' ";
        Statement stmt = db.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            dept.setCode(getString(rs.getString("Code")));
            dept.setId(getString(rs.getString("DepartmentId")));
            dept.setName(getString(rs.getString("Name")));
        }
        return dept;
    }

    private Vector getMachineList(String deptid) throws SQLException {
        Vector v = new Vector();
        String sql = "SELECT * FROM Machines where DepartmentId = '" + deptid + "' ";
        System.out.println(" SQL MC : " + sql);
        Statement stmt = db.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Machine m = new Machine();
            m.setMachineName(rs.getString("Name"));
            m.setMachineId(rs.getString("MachineId"));
            v.addElement(m);
        }
        return v;
    }

    private Vector getParameterList() throws SQLException {
        Vector v = new Vector();
        String sql = "SELECT * FROM Parameters where ProcessId = '" + request.getParameter("processid") + "' ";
        Statement stmt = db.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            ProcessParameter parameter = new ProcessParameter();
            parameter.setParameterId(getString(rs.getString("ParameterId")));
            parameter.setParameterName(getString(rs.getString("Name")));
            parameter.setUSL(rs.getFloat("USL"));
            parameter.setLSL(rs.getFloat("LSL"));
            parameter.setSL(rs.getFloat("SL"));
            v.addElement(parameter);
        }
        return v;
    }

    /**
	 * 
	 * @param grpid The DataGroup Id 
	 * @param totalrow The total row belongs to this group
	 * @param totalcolumn The total column belongs to this group
	 * @return HTML format for group (The group table)
	 * @throws SQLException If the SQL use produce bug to system.
	 */
    private String getGroup(String grpid, int totalrow, int totalcolumn) throws SQLException {
        String html = "";
        int column = totalcolumn;
        int row = totalrow;
        html += "<table border=\"0\" cellpadding=\2\" cellspacing=\"2\" class=\"group_style\">";
        Vector nospacelist = new Vector();
        String bg_style = "data_bg_a";
        for (int i = 0; i < row; i++) {
            html += "<tr>";
            for (int j = 0; j < column; j++) {
                DataField df = this.getField(j, i, grpid);
                if (haveSpace(nospacelist, "" + j + "," + i + "")) {
                    if (df.getFieldType() == null) df.setFieldType("label");
                    if ((df.getFieldType()).equals("label")) {
                        String fieldstyle = "";
                        String bgcolor = "";
                        if (df.getBGColor() != null && !(df.getBGColor()).equals("")) {
                            bgcolor = "bgColor=\"" + df.getBGColor() + "\"";
                        }
                        if (df.getFontSize() != 0) {
                            fieldstyle = "style=\"font-size: " + df.getFontSize() + "px\"";
                        }
                        if (!(fieldstyle.equals(""))) {
                            bg_style = "";
                        }
                        html += "<td    " + fieldstyle + "  " + bgcolor + "   class=\"" + bg_style + "\"  rowspan=\"" + df.getRowspan() + "\" colspan=\"" + df.getColspan() + "\" nowrap>" + df.getAssignValue() + "</td>";
                    } else if ((df.getFieldType()).equals("textfield")) {
                        String style = "";
                        if ((df.getCategory()).equals("data") && ((df.getDataType()).equalsIgnoreCase("float") || (df.getDataType()).equalsIgnoreCase("integer"))) {
                            style = "datafield_style_number";
                        } else if ((df.getCategory()).equals("summary") && ((df.getDataType()).equalsIgnoreCase("float") || (df.getDataType()).equalsIgnoreCase("integer"))) {
                            style = "summaryfield_style_number";
                        } else if ((df.getCategory()).equals("data")) {
                            style = "datafield_style";
                        } else if ((df.getCategory()).equals("summary")) {
                            style = "summaryfield_style";
                        }
                        String onClick = "";
                        if ((df.getDataType()).equalsIgnoreCase("date")) {
                            onClick = " onClick =\"setTheDate('" + df.getDataFieldName() + "')\" ";
                            df.setAssignValue((new DateUtil()).getToday("yyyy-mm-dd"));
                        }
                        String fieldstyle = "";
                        if (df.getFontSize() != 0) {
                            fieldstyle = "style=\"font-size: " + df.getFontSize() + "px; \" ";
                        }
                        String parameterController = "";
                        if (((df.getGlobalParameterId()) != null) && !(df.getGlobalParameterId()).equals("") && !(df.getGlobalParameterId()).equals("0")) {
                            parameterController = " goCheckControlLimit" + df.getHTMLId() + "()";
                        } else if (((df.getParameterId()) != null) && !(df.getParameterId()).equals("") && !(df.getParameterId()).equals("0")) {
                            parameterController = " parameterController(this, '" + df.getParameterId() + "') ";
                        }
                        if ((df.getDataType()).equalsIgnoreCase("machine")) {
                            html += "<td align=\"right\"  class=\"" + style + "\" >   <select  " + " name=\"" + df.getDataFieldName() + "\" " + " onFocus=\"goPrompt('" + df.getHTMLId() + "')\" " + " title=\"" + df.getDisplayName() + "\" " + " Id=\"" + df.getHTMLId() + "\" " + " title=\"" + df.getDisplayName() + "\" >";
                            for (int u = 0; u < mclist.size(); u++) {
                                Machine mc = (Machine) mclist.elementAt(u);
                                html += "<option value=\"" + mc.getMachineId() + "\">" + mc.getMachineName() + "</option>";
                            }
                            html += "" + "</select>\n" + "  </td>";
                        } else {
                            html += "<td align=\"right\"  class=\"" + style + "\" ><input class=\"" + style + "\"     " + " " + onClick + "  " + " onFocus=\"goPrompt('" + df.getHTMLId() + "')\" " + " onChange=\"" + parameterController + ";  calculate();  \" " + "type=\"" + df.getFieldType() + "\" name=\"" + df.getDataFieldName() + "\" title=\"" + df.getDisplayName() + "\" value=\"" + df.getAssignValue() + "\" size=\"" + df.getSize() + "\" Id=\"" + df.getHTMLId() + "\" " + fieldstyle + "     " + "/></td>";
                        }
                    } else if ((df.getFieldType()).equals("textarea")) {
                        String style = "";
                        if ((df.getCategory()).equals("data")) {
                            style = "datafield_style";
                        } else if ((df.getCategory()).equals("summary")) {
                            style = "summaryfield_style";
                        }
                        html += "<td><textarea  onClick=\"shwField('" + df.getFieldId() + "')\"  class=\"" + style + "\" " + " name=\"" + df.getDataFieldName() + "\" " + " id=\"" + df.getHTMLId() + "\">" + df.getAssignValue() + "</textarea></td>";
                    } else if ((df.getFieldType()).equals("select")) {
                        String style = "";
                        if ((df.getCategory()).equals("data")) {
                            style = "datafield_style";
                        } else if ((df.getCategory()).equals("summary")) {
                            style = "summaryfield_style";
                        }
                        String parameterController = "";
                        if (((df.getGlobalParameterId()) != null) && !(df.getGlobalParameterId()).equals("") && !(df.getGlobalParameterId()).equals("0")) {
                            parameterController = " goCheckControlLimit" + df.getHTMLId() + "()";
                        } else if (((df.getParameterId()) != null) && !(df.getParameterId()).equals("") && !(df.getParameterId()).equals("0")) {
                            parameterController = " parameterController(this, '" + df.getParameterId() + "') ";
                        }
                        html += "<td><select name=\"" + df.getDataFieldName() + "\"   " + "onFocus=\"goPrompt('" + df.getHTMLId() + "')\"   " + "Id=\"" + df.getHTMLId() + "\"  " + "  onChange=\"" + parameterController + ";  calculate();  \" " + ">" + (!(df.getCustomDataId()).equalsIgnoreCase("0") ? getCustomData(df.getCustomDataId()) : df.getSelectOption()) + "</select>    </td>";
                    }
                }
                if (df.getRowspan() > 1) {
                    for (int h = (i + 1); h < (df.getRowspan() + i); h++) {
                        nospacelist.addElement("" + j + "," + h + "");
                    }
                }
                if (df.getColspan() > 1) {
                    for (int h = (j); h < (df.getColspan() + 1); h++) {
                        nospacelist.addElement("" + h + "," + i + "");
                    }
                }
                System.out.println("Display Name = " + df.getDisplayName() + ", Field Id = " + df.getFieldId());
            }
            html += "</tr>\n";
            if (bg_style == "data_bg_b") {
                bg_style = "data_bg_a";
            } else if (bg_style == "data_bg_a") {
                bg_style = "data_bg_b";
            }
        }
        html += "</table>";
        return html;
    }

    private String getCustomData(String customdataid) throws SQLException {
        Vector v = new Vector();
        String sql = "SELECT * from CustomData d, CustomDataValue v  where d.CustomDataId = v.CustomDataId AND d.CustomDataId = '" + customdataid + "'";
        Statement stmt = db.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        String html = "";
        while (rs.next()) {
            String selected = "";
            if ((rs.getString("d.RefObj") != null) && (rs.getString("d.RefObj")).equalsIgnoreCase("User")) {
                if ((rs.getString("v.Value")).equalsIgnoreCase(user.getShift())) {
                    selected = "selected";
                }
            }
            html += "<option value=\"" + rs.getString("v.Value") + "\"  " + selected + " >" + rs.getString("v.DisplayValue") + "</option>";
        }
        return html;
    }

    private boolean haveSpace(Vector nospacelist, String s) {
        boolean b = true;
        for (int i = 0; i < nospacelist.size(); i++) {
            if (((String) nospacelist.get(i)).equals(s)) {
                b = false;
            }
        }
        return b;
    }

    private DataField getField(int x, int y, String dataGroupId) throws SQLException {
        DataField df = new DataField();
        String sql = "SELECT * FROM  DataFields where (XLocation = '" + x + "') AND (YLocation = '" + y + "') AND (DataGroupId = '" + dataGroupId + "') ";
        System.out.println("Field Select Sql  = " + sql);
        Statement stmt = db.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Hashtable h = new Hashtable();
            df.setFieldId(this.getString(rs.getString("DataFieldId")));
            df.setDisplayName(this.getString(rs.getString("DisplayName")));
            df.setFieldType(this.getString(rs.getString("FieldType")));
            df.setDataFieldName(this.getString(rs.getString("Name")));
            df.setAssignValue(this.getString(rs.getString("AssignValue")));
            df.setSize(rs.getInt("Size"));
            df.setRowspan(rs.getInt("Rowspan"));
            df.setColspan(rs.getInt("Colspan"));
            df.setCustomDataId(rs.getString("CustomDataId"));
            df.setDataType(getString(rs.getString("DataType")));
            df.setParameterId(getString(rs.getString("ParameterId")));
            df.setHTMLId(this.getString(rs.getString("HTMLId")));
            h.put("a", df.getHTMLId());
            df.setCategory(this.getString(rs.getString("Category")));
            df.setNextField(this.getString(rs.getString("NextField")));
            h.put("b", df.getNextField());
            df.setFontSize(rs.getInt("FontSize"));
            df.setGlobalParameterId(getString(rs.getString("GlobalParameterId")));
            if (((df.getGlobalParameterId()) != null) && !(df.getGlobalParameterId()).equals("") && !(df.getGlobalParameterId()).equals("0")) {
                Hashtable hGlobalParam = new Hashtable();
                hGlobalParam.put("globalparamfieldhtmlid", df.getHTMLId());
                hGlobalParam.put("globalparameter", this.getGlobalParameter(df.getGlobalParameterId()));
                globalParamList.addElement(hGlobalParam);
            }
            df.setSelectOption(rs.getString("selectOption"));
            df.setBGColor(rs.getString("BgColour"));
            if (df.getCategory().equals("summary")) {
                df.setFormula(getString(rs.getString("Formula")));
                formula += df.getFormula() + "\n";
            }
            context.put("formula", formula);
            if (df.getCategory().equals("data")) {
                next.addElement(h);
            }
        }
        context.put("fastkeylist", next);
        rs.close();
        return df;
    }

    private GlobalParameter getGlobalParameter(String globalparameterid) throws SQLException {
        GlobalParameter gp = new GlobalParameter();
        String sql = "SELECT * FROM  GlobalParameters where GlobalParameterId = '" + globalparameterid + "' ";
        Statement stmt = db.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            gp.setParameterName(getString(rs.getString("Name")));
            gp.setParameterId(getString(rs.getString("GlobalParameterId")));
            gp.setParameterStatus(rs.getInt("GlobalParameterStatus"));
            gp.setDescription(getString(rs.getString("Description")));
            gp.setTotalValue(rs.getInt("TotalValue"));
            gp.setCircumstances(getString(rs.getString("Circumstances")));
            gp.setCriteriaList(this.getGlobalParameterCriteria(gp.getParameterId()));
        }
        return gp;
    }

    private Vector getGlobalParameterCriteria(String globalparameterid) throws SQLException {
        String sql = "SELECT * FROM  GlobalParameterCriterias where GlobalParameterId = '" + globalparameterid + "'  order by Sequence";
        System.out.println(sql);
        Statement stmt = db.getStatement();
        ResultSet rs;
        rs = stmt.executeQuery(sql);
        Vector v = new Vector();
        while (rs.next()) {
            GlobalParameterCriteria gpc = new GlobalParameterCriteria();
            gpc.setGlobalParameterCriteriaId(getString(rs.getString("GlobalParameterCriteriaId")));
            gpc.setSequence(rs.getInt("Sequence"));
            gpc.setValue(rs.getFloat("Value"));
            gpc.setValue1(rs.getFloat("Value1"));
            gpc.setValue2(rs.getFloat("Value2"));
            gpc.setBackgroundColor(getString(rs.getString("BackgroundColor")));
            gpc.setAlertMessage(getString(rs.getString("AlertMessage")));
            gpc.setAlert(rs.getInt("Alert"));
            gpc.setNotAllowed(rs.getInt("NotAllowed"));
            gpc.setCharacterValue(getString(rs.getString("CharacterValue")));
            gpc.setBlinking(rs.getInt("Blinking"));
            gpc.setColor(getString(rs.getString("Color")));
            v.addElement(gpc);
        }
        return v;
    }

    private void setDataFields() throws SQLException {
        String sql = "SELECT * FROM Processes p, DataGroups g, DataFields f where " + "p.ProcessId = g.ProcessId AND g.DataGroupId = f.DataGroupId AND p.ProcessId = '" + request.getParameter("processid") + "' AND " + " (f.Category = 'data' OR f.Category = 'summary')   order by f.DisplayName";
        ResultSet rs;
        Statement stmt = db.getStatement();
        rs = stmt.executeQuery(sql);
        Vector vdata = new Vector();
        Vector vsummary = new Vector();
        while (rs.next()) {
            DataField df = new DataField();
            df.setFieldId(getString(rs.getString("f.DataFieldId")));
            df.setDisplayName(getString(rs.getString("f.DisplayName")));
            df.setFieldType(getString(rs.getString("f.FieldType")));
            df.setDataFieldName(getString(rs.getString("f.Name")));
            df.setAssignValue(getString(rs.getString("f.AssignValue")));
            df.setSize(rs.getInt("f.Size"));
            df.setRowspan(rs.getInt("f.Rowspan"));
            df.setColspan(rs.getInt("f.Colspan"));
            df.setHTMLId(getString(rs.getString("f.HTMLId")));
            df.setCategory(getString(rs.getString("f.Category")));
            df.setFontSize(rs.getInt("f.FontSize"));
            df.setBGColor(getString(rs.getString("f.BgColour")));
            if ((df.getCategory()).equalsIgnoreCase("data")) {
                vdata.addElement(df);
            } else if ((df.getCategory()).equalsIgnoreCase("summary")) {
                vsummary.addElement(df);
            }
        }
        HttpSession session = request.getSession();
        session.setAttribute("datalist_" + request.getParameter("processid"), vdata);
        session.setAttribute("summarylist_" + request.getParameter("processid"), vsummary);
    }

    private String getString(String s) {
        if (s == null) return ""; else return s;
    }
}
