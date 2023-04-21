package jvc.web.action.report;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jvc.util.AppUtils;
import jvc.util.RecordSetUtils;
import jvc.util.XMLUtils;
import jvc.util.cache.LRUCache;
import jvc.util.db.MyDB;
import jvc.util.log.Logger;
import jvc.web.action.ActionContent;
import jvc.web.action.BaseAction;
import jvc.web.action.QueryAction;
import jvc.web.component.Options;
import jvc.web.component.Table;
import jvc.web.module.JVCResult;
import java.io.*;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.*;

public class JvcXMLReportAction implements BaseAction {

    private static Logger logger = Logger.getLogger(JvcXMLReportAction.class.getName());

    public static LRUCache pool = new LRUCache(100);

    MyDB reportDB = null;

    private List lDataSource = new ArrayList();

    private String sqlReportDatasource = "";

    private void AddDataSource(String sDataSource) {
        if (sDataSource.length() == 0) return;
        if (lDataSource.contains(sDataSource)) return;
        lDataSource.add(sDataSource);
        ;
    }

    public String getReportDatasource(MyDB mydb, String reportId) {
        try {
            genXmlReport(mydb, reportId, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlReportDatasource;
    }

    private void genXmlReport(MyDB mydb, String reportId, boolean isGen) throws Exception {
        ResultSet rs = mydb.executeQuery("select * from jvc_report where reportId='" + reportId + "'");
        if (rs.next()) {
            AddDataSource(RecordSetUtils.getString(rs, "resultDataSource"));
            AddDataSource(RecordSetUtils.getString(rs, "mainDataSource"));
        }
        rs = mydb.executeQuery("select * from jvc_report where reportId='" + reportId + "'");
        Element e = RecordSetUtils.toXMLElement("report", rs);
        if (e.getAttributes().size() == 0) return;
        rs = mydb.executeQuery("select * from jvc_report_condition where reportId='" + reportId + "' order by conditionId");
        while (rs.next()) AddDataSource(RecordSetUtils.getString(rs, "componentDS"));
        rs = mydb.executeQuery("select * from jvc_report_condition where reportId='" + reportId + "' order by conditionId");
        Element eCondition = RecordSetUtils.toXMLList("condition", rs);
        rs = mydb.executeQuery("select * from jvc_report_result where reportId='" + reportId + "' and isMain=1 order by resultId");
        while (rs.next()) AddDataSource(RecordSetUtils.getString(rs, "fieldDS"));
        rs = mydb.executeQuery("select * from jvc_report_result where reportId='" + reportId + "' and isMain=1 order by resultId");
        Element eMainResult = RecordSetUtils.toXMLList("result_main", rs);
        rs = mydb.executeQuery("select * from jvc_report_result where reportId='" + reportId + "' and isMain=0 order by resultId");
        while (rs.next()) AddDataSource(RecordSetUtils.getString(rs, "fieldDS"));
        rs = mydb.executeQuery("select * from jvc_report_result where reportId='" + reportId + "' and isMain=0 order by resultId");
        Element eSubResult = RecordSetUtils.toXMLList("result_sub", rs);
        rs = mydb.executeQuery("select * from jvc_report_sum where reportId='" + reportId + "'  order by SumID");
        Element eResultSum = RecordSetUtils.toXMLList("result_sum", rs);
        if (lDataSource.size() > 0) {
            StringBuffer sDataSource = new StringBuffer(" dsId in (");
            for (Iterator it = lDataSource.iterator(); it.hasNext(); ) {
                sDataSource.append("'" + String.valueOf(it.next()) + "'");
                if (it.hasNext()) sDataSource.append(",");
            }
            sDataSource.append(")");
            sqlReportDatasource = sDataSource.toString();
            rs = mydb.executeQuery("select * from jvc_report_datasource where " + sDataSource + " order by dsId");
            Element eDataSource = RecordSetUtils.toXMLList("datasource", rs);
            e.addContent(eDataSource);
        }
        if (!isGen) return;
        e.addContent(eCondition);
        e.addContent(eMainResult);
        e.addContent(eSubResult);
        e.addContent(eResultSum);
        XMLOutputter out = new XMLOutputter("  ", true, "GBK");
        out.setEncoding("GBK");
        java.io.FileWriter writer = new java.io.FileWriter(AppUtils.AppPath + "/reports/" + reportId + ".xml");
        Document document = new Document(e);
        out.output(document, writer);
        writer.flush();
        writer.close();
    }

    public String Excute(ActionContent input, ActionContent output, MyDB mydb) {
        String faultview = input.getParam("fault");
        String successview = input.getParam("success");
        if (faultview.equals("")) faultview = successview;
        String reportId = input.getParam("reportId");
        LRUCache.CacheNode cn = pool.getCacheNode(reportId);
        Element root = null;
        if (reportId.length() == 0) return returnMsg("�������", faultview, output);
        mydb.check();
        try {
            if (input.getInt("gen") == 1) genXmlReport(mydb, reportId, true);
            File f = new File(AppUtils.AppPath + "/reports/" + reportId + ".xml");
            if (cn != null) {
                if (f.lastModified() == cn.getTime()) root = (Element) cn.getValue();
            }
            if (root == null) {
                if (!f.exists()) {
                    if (!f.getParentFile().exists()) f.getParentFile().mkdirs();
                    genXmlReport(mydb, reportId, true);
                }
                if (!f.exists()) return returnMsg("���?����", faultview, output);
                SAXBuilder builder = new SAXBuilder();
                Document doc = builder.build(f);
                root = doc.getRootElement();
                pool.put(reportId, root, f.lastModified());
            }
            String reportName = XMLUtils.getAttribute(root, "reportName");
            String mainDataSource = XMLUtils.getAttribute(root, "mainDataSource").toLowerCase();
            String resultDataSource = XMLUtils.getAttribute(root, "resultDataSource").toLowerCase();
            String addForm = XMLUtils.getAttribute(root, "addForm").toLowerCase();
            String resultTable = XMLUtils.getAttribute(root, "resultTable");
            output.setParam("resultTable", resultTable);
            int hasAddRole = 0;
            int hasDelRole = 0;
            int hasUpdateRole = 0;
            String rootKey = "";
            String parentKey = "";
            String parentKeyValue = "";
            String bWidth = "500";
            String bHeight = "500";
            if (addForm.length() > 0) {
                ResultSet rs = mydb.executeQuery("select * from jvc_form where formId='" + addForm + "'");
                if (rs.next()) {
                    parentKey = RecordSetUtils.getString(rs, "parentKey");
                    rootKey = RecordSetUtils.getString(rs, "rootKey");
                    bWidth = RecordSetUtils.getString(rs, "bWidth");
                    bHeight = RecordSetUtils.getString(rs, "bHeight");
                    String primaryKey = RecordSetUtils.getString(rs, "primaryKey");
                    output.setParam("bWidth", bWidth);
                    output.setParam("BHeight", bHeight);
                    if (primaryKey.length() > 0) {
                        String addRole = XMLUtils.getAttribute(root, "addRole");
                        String delRole = XMLUtils.getAttribute(root, "delRole");
                        String updateRole = XMLUtils.getAttribute(root, "updateRole");
                        if (input.hasRole(addRole)) hasAddRole = 1;
                        if (input.hasRole(delRole)) hasDelRole = 1;
                        if (input.hasRole(updateRole)) hasUpdateRole = 1;
                        output.setParam("hasAddRole", hasAddRole);
                        output.setParam("hasDelRole", hasDelRole);
                        output.setParam("hasUpdateRole", hasUpdateRole);
                    }
                }
            }
            String mainToField = XMLUtils.getAttribute(root, "mainToField");
            String resultToField = XMLUtils.getAttribute(root, "resultToField");
            String dbName = XMLUtils.getAttribute(root, "dbName");
            int reportType = XMLUtils.getAttributeInt(root, "reportType");
            if (dbName.length() > 0) reportDB = new MyDB(dbName);
            int pageCount = input.getInt2("ds_" + resultDataSource + ".recordsperpage", XMLUtils.getAttributeInt(root, "pageCount"));
            if (input.getInt("jvc.excel") == 1) pageCount = -1;
            output.setParam("autoNum", XMLUtils.getAttributeInt(root, "autoNum"));
            output.setParam("reportName", reportName);
            output.setParam("resultDataSource", resultDataSource);
            output.setParam("mainDataSource", mainDataSource);
            output.setParam("reportType", reportType);
            JVCResult result_sub = XMLUtils.toJVCResult(root, "result_sub");
            output.setParam("res", result_sub);
            output.setParam("rs_main", XMLUtils.toJVCResult(root, "result_main"));
            JVCResult result_sum = XMLUtils.toJVCResult(root, "result_sum");
            output.setParam("result_sum", result_sum);
            JVCResult condition = XMLUtils.toJVCResult(root, "condition");
            output.setParam("condition", condition);
            List list = XMLUtils.getList(root, "datasource");
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                Element el = (Element) it.next();
                String datasourceId = XMLUtils.getString(el, "dsId");
                if (resultDataSource.equalsIgnoreCase(datasourceId)) {
                    if (input.getInt("curpage") > 0) input.setParam("ds_" + resultDataSource + ".curpage", input.getInt("curpage"));
                    input.setParam("ds_" + resultDataSource + ".recordsperpage", pageCount);
                } else input.setParam("ds_" + resultDataSource + ".recordsperpage", -1);
                String sql = XMLUtils.getString(el, "dsSql");
                if (!sql.trim().toLowerCase().startsWith("select")) {
                    output.setParam("ds_" + datasourceId, new JVCResult(sql));
                    continue;
                }
                QueryAction qa = new QueryAction();
                if (parentKey != null && rootKey != null && parentKey.length() > 0 && rootKey.length() > 0) {
                    parentKeyValue = input.getParam("report_" + datasourceId + "." + parentKey);
                    if (parentKeyValue.equals("")) parentKeyValue = rootKey;
                    input.setParam("report_" + datasourceId + "." + parentKey, parentKeyValue);
                }
                input.setParam("sql", sql);
                input.setParam("where", XMLUtils.getString(el, "dsWhere"));
                input.setParam("group", XMLUtils.getString(el, "dsGroup"));
                input.setParam("order", XMLUtils.getString(el, "dsOrder"));
                input.setParam("name", "ds_" + datasourceId);
                input.setParam("prestr", "report_" + datasourceId);
                input.setParam("resulttype", "mutl");
                if (mainDataSource.equalsIgnoreCase(datasourceId)) {
                    input.setParam("resulttype", "single");
                    input.setParam("report_" + datasourceId + "." + mainToField, input.getParam("linkReport.Param", "decode"));
                }
                if (resultDataSource.equalsIgnoreCase(datasourceId)) {
                    if (resultToField.length() > 0) {
                        String aResultToField[] = resultToField.split(",");
                        String aParam[] = input.getParam("linkReport.Param", "decode").split("[|]");
                        if (aParam.length >= aResultToField.length) for (int i = 0; i < aResultToField.length; i++) {
                            if (parentKey.equalsIgnoreCase(aResultToField[i]) && aParam[i].length() > 0) {
                                parentKeyValue = aParam[i];
                                input.setParam("report_" + datasourceId + "." + aResultToField[i], aParam[i]);
                            } else if (!parentKey.equalsIgnoreCase(aResultToField[i])) input.setParam("report_" + datasourceId + "." + aResultToField[i], aParam[i]);
                        }
                    }
                    List listCondition = XMLUtils.getList(root, "condition");
                    for (Iterator itCondition = listCondition.iterator(); itCondition.hasNext(); ) {
                        Element eCondition = (Element) itCondition.next();
                        String conditionId = XMLUtils.getString(eCondition, "conditionId");
                        String toField = XMLUtils.getString(eCondition, "toField");
                        if (toField.length() > 0) {
                            String componentDefaultValue = XMLUtils.getString(eCondition, "componentDefaultValue");
                            int componentId = XMLUtils.getInt(eCondition, "componentId");
                            if (input.existParam("f." + conditionId)) componentDefaultValue = input.getParam("f." + conditionId).trim(); else componentDefaultValue = input.getParse(componentDefaultValue);
                            String paramName = "report_" + datasourceId + "." + toField + "." + XMLUtils.getString(eCondition, "operatorId") + "." + XMLUtils.getString(eCondition, "fieldType");
                            if (componentId == 2) paramName += "." + conditionId;
                            input.setParam(paramName, componentDefaultValue);
                        }
                    }
                }
                if (dbName.length() > 0) qa.Excute(input, output, reportDB); else qa.Excute(input, output, mydb);
                if (resultDataSource.equals(datasourceId) && result_sum.getResult().size() > 0) {
                    String fromSQl = sql.substring(sql.toLowerCase().indexOf("from"), sql.length());
                    String tempField = "";
                    Table rs_sum = new Table(result_sum);
                    while (rs_sum.next()) {
                        if (tempField.length() != 0) tempField += ",";
                        tempField += rs_sum.getField("SumType") + "(" + rs_sum.getField("FieldName") + ") as " + "jvc_sum_" + rs_sum.getField("sumID");
                    }
                    input.setParam("sql", "select " + tempField + " " + fromSQl);
                    input.setParam("group", "");
                    input.setParam("order", "");
                    input.setParam("name", "jvc_sum");
                    input.setParam("prestr", "report_" + datasourceId);
                    input.setParam("resulttype", "single");
                    if (dbName.length() > 0) qa.Excute(input, output, reportDB); else qa.Excute(input, output, mydb);
                }
            }
            boolean hasDateJs = false;
            StringBuffer sbJs = new StringBuffer("");
            sbJs.append("<script language=\"javascript\" src=\"js/dblookup/select.js\"></script>");
            List listCondition = XMLUtils.getList(root, "condition");
            boolean hasCondition = listCondition.size() > 0;
            StringBuffer sbButton = new StringBuffer("");
            StringBuffer sbCondition = new StringBuffer("<input type=hidden name=cmd value='show'>");
            sbButton.append("<input type='hidden' name='linkReport.Param' value='" + input.getParam("linkReport.Param") + "'> ");
            if (hasCondition) {
                sbButton.append("<input type='hidden' name='curpage" + "' value='1'>");
                sbButton.append("&nbsp;&nbsp;<input type='submit' class='btnSearch' value='��ѯ'>");
                sbButton.append("&nbsp;&nbsp;<input type='reset' class='btnReset' value='����'>");
                sbButton.append("&nbsp;&nbsp;<input type='button' class='btnPrint' value='��ӡ' onclick='window.print();'>");
                if (reportType == 0) sbButton.append("&nbsp;&nbsp;<input type='button' class='btnSave' value='����' onclick=\"document.frm_query.cmd.value='excel';document.frm_query.submit();document.frm_query.cmd.value='show'\">");
            } else {
                sbButton.append("&nbsp;&nbsp;<input type='button' class='btnPrint' value='��ӡ' onclick='window.print();'>");
                if (reportType == 0) sbButton.append("&nbsp;&nbsp;<input type='button' class='btnSave' value='����' onclick=\"document.frm_query.cmd.value='excel';document.frm_query.submit();document.frm_query.cmd.value='show'\">");
            }
            if (rootKey != null && parentKey != null && rootKey.length() > 0 && parentKey.length() > 0 && !rootKey.equals(parentKeyValue)) sbButton.append("&nbsp;&nbsp;<input type='button' class='btnNew' value='��һ��' onclick=\"document.frm_query['linkReport.Param'].value='" + input.getParam(parentKey) + "'; document.frm_query.submit();\" >");
            if (hasAddRole == 1 && addForm != null && addForm.length() > 0) {
                String addformurl = "jvc.form.page?cmd=ShowAddFormFrame&cmd1=ShowAddForm&query.formId=" + addForm + "&formType=0";
                if (parentKey != null && parentKey.length() > 0) addformurl += "&" + parentKey + "=" + parentKeyValue;
                sbButton.append("&nbsp;&nbsp;<input type='button' class='btnNew' value='����' onclick=\"if(window.showModalDialog('" + addformurl + "','','scroll:yes;center:yes;help:no;status:no;dialogWidth:" + bWidth + "px;dialogHeight:" + bHeight + "px')){document.frm_query['curpage'].value='" + input.getParam("ds_" + resultDataSource + ".curpage") + "';document.frm_query.submit();}\" >");
            }
            output.setParam("FormID", addForm);
            output.setParam("primaryKey", mydb.getString("select primaryKey from jvc_form where formId='" + addForm + "'", ""));
            output.setParam("linkTable", mydb.getString("select linkTable from jvc_form where formId='" + addForm + "'", ""));
            sbCondition.append("<table><tr>");
            for (Iterator itCondition = listCondition.iterator(); itCondition.hasNext(); ) {
                Element eCondition = (Element) itCondition.next();
                String caption = XMLUtils.getString(eCondition, "componentCaption");
                String componentWidth = XMLUtils.getString(eCondition, "componentWidth");
                String componentDS = XMLUtils.getString(eCondition, "componentDS");
                String conditionId = XMLUtils.getString(eCondition, "conditionId");
                if (!input.hasRole(XMLUtils.getString(eCondition, "RoleID"))) continue;
                boolean isRowEnd = XMLUtils.getInt(eCondition, "isRowEnd") == 1;
                String componentDefaultValue = XMLUtils.getString(eCondition, "componentDefaultValue");
                if (input.existParam("f." + conditionId)) componentDefaultValue = input.getParam("f." + conditionId); else componentDefaultValue = input.getParse(componentDefaultValue);
                String componentParam = XMLUtils.getString(eCondition, "componentParam");
                switch(XMLUtils.getInt(eCondition, "componentId")) {
                    case 0:
                        sbCondition.append("<td align=right noWrap>");
                        sbCondition.append("&nbsp;" + caption + "��&nbsp;");
                        sbCondition.append("</td><td noWrap>");
                        sbCondition.append("<input name='f." + conditionId + "' size=" + componentWidth + " value=\"" + input.getParam2("f." + conditionId, componentDefaultValue) + "\" />");
                        output.setParam("f." + conditionId, input.getParam2("f." + conditionId, componentDefaultValue));
                        break;
                    case 1:
                        sbCondition.append("<td align=right noWrap>");
                        sbCondition.append("&nbsp;" + caption + "��&nbsp;");
                        sbCondition.append("</td><td noWrap>");
                        sbCondition.append("<select name='f." + conditionId + "' >");
                        if (!componentParam.equals("")) {
                            sbCondition.append("<option value=''>" + componentParam + "</option>");
                        }
                        sbCondition.append(new Options(output, "ds_" + componentDS, input.getParam("f." + conditionId)));
                        sbCondition.append("</select>");
                        output.setParam("f." + conditionId, input.getParam2("f." + conditionId, componentDefaultValue));
                        break;
                    case 2:
                        sbCondition.append("<td align=right noWrap>");
                        sbCondition.append("<input type='checkbox' name='f." + conditionId + "'  value='" + componentParam + "' " + (input.getParam("f." + conditionId).equals("") ? "" : "checked") + " />");
                        sbCondition.append("</td><td align=left noWrap>");
                        sbCondition.append("&nbsp;" + caption + "&nbsp;");
                        output.setParam("f." + conditionId, (input.getParam("f." + conditionId).equals("") ? "" : "checked"));
                        break;
                    case 3:
                        hasDateJs = true;
                        sbCondition.append("<td align=right noWrap>");
                        sbCondition.append("&nbsp;" + caption + "��&nbsp;");
                        sbCondition.append("</td><td noWrap>");
                        sbCondition.append("<input style='width:6em' name='f." + conditionId + "' id='f_" + conditionId + "' value=\"" + input.getParam2("f." + conditionId, componentDefaultValue) + "\" />\n");
                        sbCondition.append("<img onclick='setday(this,f_" + conditionId + ");' src='js/date/calendar.gif' style='cursor: hand'>");
                        output.setParam("f." + conditionId, input.getParam2("f." + conditionId, componentDefaultValue));
                        break;
                    default:
                        break;
                }
                sbCondition.append("</td>");
                if (isRowEnd) sbCondition.append("</tr>");
            }
            if (hasCondition && reportType == 0 && pageCount >= 0) sbCondition.append("<td noWrap>ÿҳ��¼��</td><td><input name=\"" + "ds_" + resultDataSource + ".recordsperpage" + "\" value=\"" + pageCount + "\" size=\"3\" ></td></tr>");
            if (hasDateJs) sbJs.append("<script language=\"javascript\" src=\"js/date/calendar.js\"></script>");
            sbCondition.append("<input type=\"hidden\" name=\"reportID\" value=\"" + reportId + "\">");
            sbCondition.append("<input type=\"hidden\" name=\"ds_" + resultDataSource + ".order\" value=\"" + input.getParam("ds_" + resultDataSource + ".order") + "\">");
            sbCondition.append("<input type=\"hidden\" name=\"ds_" + resultDataSource + ".orderfield\" value=\"" + input.getParam("ds_" + resultDataSource + ".orderfield") + "\">");
            sbCondition.append("<input type=\"hidden\" name=\"ds_" + resultDataSource + ".orderdir\" value=\"" + input.getParam("ds_" + resultDataSource + ".orderdir") + "\">");
            sbCondition.append("</tr></table>");
            output.setParam("queryform", sbCondition.toString());
            output.setParam("querybutton", sbButton.toString());
            output.setParam("queryjs", sbJs.toString());
            if (reportDB != null) reportDB.close();
            return successview;
        } catch (Exception e) {
            output.setParam("message", "�������");
            logger.error(e);
        }
        if (reportDB != null) reportDB.close();
        return faultview;
    }

    private String returnMsg(String Message, String view, ActionContent output) {
        output.setParam("message", Message);
        if (reportDB != null) reportDB.close();
        return view;
    }

    public static void main(String[] args) {
        ActionContent input = new ActionContent();
        input.setParam("reportId", "test");
        new JvcXMLReportAction().Excute(input, new ActionContent(), new MyDB());
    }
}
