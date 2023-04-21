package jvc.util.db;

import java.io.File;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import jvc.util.AppUtils;
import jvc.util.FileUtils;
import jvc.util.RecordSetUtils;
import jvc.util.StringUtils;
import jvc.util.compress.UnZip;
import jvc.web.action.report.JvcXMLReportAction;
import jvc.web.module.Field;

public class TableUtils {

    public static boolean ExportTable(String tableName) {
        try {
            XMLOutputter out = new XMLOutputter("  ", true, "GBK");
            out.setEncoding("GBK");
            java.io.FileWriter writer = new java.io.FileWriter(AppUtils.AppPath + "/uploadtemp/" + tableName + ".xml");
            Document document = new Document(TableToElement(tableName));
            out.output(document, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static Element TableToElement(String tableName) {
        return TableToElement(tableName, "");
    }

    public static Element TableToElement(String tableName, String where) {
        MyDB mydb = new MyDB();
        Element e = new Element(tableName);
        try {
            ResultSet rs = mydb.getConn().getMetaData().getColumns(null, null, tableName, null);
            e.addContent(RecordSetUtils.toXMLList("field", rs));
            rs.close();
            rs = mydb.executeQuery("select * from " + tableName + "  " + where);
            e.addContent(RecordSetUtils.toXMLList("data", rs));
            rs.close();
            rs = mydb.getConn().getMetaData().getIndexInfo(null, null, tableName, true, false);
            e.addContent(RecordSetUtils.toXMLList("key", rs));
            rs.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        mydb.close();
        return e;
    }

    public static boolean ExportTables(String tableName[]) {
        try {
            Element e = new Element("tables");
            for (int i = 0; i < tableName.length; i++) {
                e.addContent(TableToElement(tableName[i]));
            }
            XMLOutputter out = new XMLOutputter("  ", true, "GBK");
            out.setEncoding("GBK");
            java.io.FileWriter writer = new java.io.FileWriter(AppUtils.AppPath + "/uploadtemp/" + "data" + ".xml");
            Document document = new Document(e);
            out.output(document, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private static boolean AlterTable(MyDB mydb, String tableName, Element root, Map fieldmap) throws Exception {
        List list = root.getChild("fields").getChildren("field");
        String sql = "alter table " + tableName + "";
        boolean needAlter = false;
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Element el = (Element) it.next();
            String columnName = el.getAttributeValue("column_name");
            if (isExsitColumn(mydb, tableName, columnName)) continue;
            if (needAlter) sql += ",";
            sql += " add ";
            needAlter = true;
            switch(StringUtils.toInt(el.getAttributeValue("data_type"))) {
                case Types.VARCHAR:
                    sql += el.getAttributeValue("column_name") + " " + el.getAttributeValue("type_name") + " (" + el.getAttributeValue("column_size") + ")";
                    break;
                case Types.LONGVARCHAR:
                    if (mydb.getDatabaseProductName().equalsIgnoreCase("MySQL")) sql += el.getAttributeValue("column_name") + " " + "text"; else sql += el.getAttributeValue("column_name") + " " + el.getAttributeValue("type_name");
                    break;
                default:
                    sql += el.getAttributeValue("column_name") + " " + el.getAttributeValue("type_name");
                    break;
            }
            if (it.hasNext()) sql += ",";
        }
        if (needAlter) mydb.executeUpdate(sql);
        return true;
    }

    public static Map getKeyMap(MyDB mydb, String tableName) {
        Map keymap = new HashMap();
        try {
            ResultSet rs = mydb.getConn().getMetaData().getIndexInfo(null, null, tableName, true, false);
            while (rs.next()) if (rs.getString("INDEX_NAME") != null && rs.getString("COLUMN_NAME") != null && rs.getString("INDEX_NAME").length() > 0 && rs.getString("COLUMN_NAME").length() > 0 && !rs.getString("INDEX_NAME").equals(rs.getString("COLUMN_NAME"))) {
                keymap.put(rs.getString("COLUMN_NAME"), rs.getString("COLUMN_NAME"));
            }
        } catch (Exception e) {
        }
        return keymap;
    }

    private static boolean CreateTable(MyDB mydb, String tableName, Element root, Map fieldmap, Map keymap) throws Exception {
        List list = root.getChild("fields").getChildren("field");
        String sql = "create table " + tableName + "(";
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Element el = (Element) it.next();
            fieldmap.put(el.getAttributeValue("column_name").toLowerCase(), el.getAttributeValue("data_type"));
            switch(StringUtils.toInt(el.getAttributeValue("data_type"))) {
                case Types.VARCHAR:
                    sql += el.getAttributeValue("column_name") + " " + el.getAttributeValue("type_name") + " (" + el.getAttributeValue("column_size") + ")";
                    break;
                case Types.LONGVARCHAR:
                    if (mydb.getDatabaseProductName().equalsIgnoreCase("MySQL")) sql += el.getAttributeValue("column_name") + " " + "text"; else sql += el.getAttributeValue("column_name") + " " + el.getAttributeValue("type_name");
                    break;
                default:
                    sql += el.getAttributeValue("column_name") + " " + el.getAttributeValue("type_name");
                    break;
            }
            if (it.hasNext()) sql += ",";
        }
        list = root.getChild("keys").getChildren("key");
        boolean bHasKey = false;
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Element el = (Element) it.next();
            if (el.getAttributeValue("index_name").length() > 0 && el.getAttributeValue("column_name").length() > 0 && !el.getAttributeValue("index_name").equals(el.getAttributeValue("column_name"))) {
                if (!bHasKey) {
                    sql += " ,PRIMARY KEY (";
                    bHasKey = true;
                } else sql += ",";
                keymap.put(el.getAttributeValue("column_name").toLowerCase(), el.getAttributeValue("data_type"));
                sql += el.getAttributeValue("column_name");
            }
        }
        if (keymap.size() == 0) keymap = fieldmap;
        if (isExsitTable(mydb, tableName)) {
            return AlterTable(mydb, tableName, root, fieldmap);
        }
        if (bHasKey) sql += ")";
        sql += ")";
        mydb.executeUpdate(sql);
        return true;
    }

    private static int getDBType(int fieldType) {
        int type = Field.FT_STRING;
        switch(fieldType) {
            case Types.INTEGER:
                type = Field.FT_INT;
                break;
            case Types.DOUBLE:
                type = Field.FT_DOUBLE;
                break;
            case Types.TIMESTAMP:
                type = Field.FT_DATETIME;
                break;
            case Types.BIGINT:
                type = Field.FT_LONG;
                break;
            case Types.DATE:
                type = Field.FT_DATE;
                break;
            case Types.TIME:
                type = Field.FT_TIME;
                break;
        }
        return type;
    }

    private static boolean ImportData(MyDB mydb, String tableName, Element root, Map fieldmap, Map keymap) throws Exception {
        List list = root.getChild("datas").getChildren("data");
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            UpdateDB update = new UpdateDB(tableName);
            for (Iterator itdata = ((Element) it.next()).getAttributes().iterator(); itdata.hasNext(); ) {
                Attribute el = (Attribute) itdata.next();
                int type = getDBType(StringUtils.toInt((String) (fieldmap.get(el.getName())), 0));
                if (keymap.size() == 0) keymap = fieldmap;
                if (keymap.containsKey(el.getName())) update.setKey(type, el.getName(), el.getValue());
                update.AddField(type, el.getName(), el.getValue());
            }
            update.Excute(mydb, true);
        }
        return true;
    }

    public static boolean ImportTable(MyDB mydb, String tableName, Element root) {
        try {
            mydb.beginTrans();
            Map fieldmap = new HashMap();
            Map keymap = new HashMap();
            CreateTable(mydb, tableName, root, fieldmap, keymap);
            ImportData(mydb, tableName, root, fieldmap, keymap);
            mydb.commit();
        } catch (Exception e) {
            mydb.rollback();
            e.printStackTrace();
        }
        return true;
    }

    public static boolean ImportTables(String FileName) {
        MyDB mydb = new MyDB();
        boolean re = ImportTables(mydb, FileName);
        mydb.close();
        return re;
    }

    public static boolean ImportTables(MyDB mydb, String FileName) {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new File(FileName));
            Element root = doc.getRootElement();
            List list = root.getChildren();
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                Element el = (Element) it.next();
                ImportTable(mydb, el.getName(), el);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return true;
    }

    public static boolean isExsitColumn(MyDB mydb, String tableName, String columnName) {
        boolean re = false;
        try {
            ResultSet rs = mydb.getConn().getMetaData().getColumns(null, null, tableName, null);
            while (rs.next()) {
                if (columnName.equals(rs.getString("COLUMN_NAME"))) {
                    re = true;
                    break;
                }
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }

    public static boolean isExsitTable(MyDB mydb, String tableName) {
        String types[] = new String[1];
        types[0] = "TABLE";
        boolean re = false;
        try {
            ResultSet rs = mydb.getConn().getMetaData().getTables(null, null, null, types);
            while (rs.next()) {
                if (tableName.equals(rs.getString("TABLE_NAME"))) {
                    re = true;
                    break;
                }
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }

    public static boolean ExportDataBase() {
        MyDB mydb = new MyDB();
        String types[] = new String[1];
        types[0] = "TABLE";
        try {
            ResultSet rs = mydb.getConn().getMetaData().getTables(null, null, null, types);
            Element e = new Element("tables");
            while (rs.next()) {
                e.addContent(TableToElement(rs.getString("TABLE_NAME")));
            }
            XMLOutputter out = new XMLOutputter("  ", true, "GBK");
            out.setEncoding("GBK");
            java.io.FileWriter writer = new java.io.FileWriter(AppUtils.AppPath + "/config/" + "initdb" + ".xml");
            Document document = new Document(e);
            out.output(document, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mydb.close();
        return true;
    }

    public static boolean ExportReportData(String reportID) {
        boolean re = false;
        try {
            java.io.FileWriter writer = new java.io.FileWriter(AppUtils.AppPath + "/uploadtemp/" + reportID + ".xml");
            ExportReportData(reportID, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
        }
        return re;
    }

    public static boolean ImportReportData(MyDB mydb, String reportfile) {
        try {
            UnZip u = new UnZip();
            u.setMode(1);
            String tempdir = AppUtils.AppPath + "\\config\\report_import\\" + reportfile.replaceAll(".zip", "") + "\\";
            FileUtils.delete(tempdir);
            u.unZip(AppUtils.AppPath + "\\config\\report_import\\" + reportfile, tempdir);
            File fdir = new File(tempdir);
            File[] fs = fdir.listFiles();
            for (int i = 0; i < fs.length; i++) ImportTables(mydb, fs[i].getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(AppUtils.AppPath + "\\config\\report_import\\" + reportfile);
        }
        return true;
    }

    public static boolean ExportReportData(String reportID, Writer writer) {
        MyDB mydb = new MyDB();
        String types[] = new String[1];
        types[0] = "TABLE";
        try {
            Element e = new Element("tables");
            e.addContent(TableToElement("jvc_report", " where reportID='" + reportID + "'"));
            e.addContent(TableToElement("jvc_report_condition", " where reportID='" + reportID + "'"));
            e.addContent(TableToElement("jvc_report_result", " where reportID='" + reportID + "'"));
            e.addContent(TableToElement("jvc_report_sum", " where reportID='" + reportID + "'"));
            JvcXMLReportAction ra = new JvcXMLReportAction();
            e.addContent(TableToElement("jvc_report_datasource", "where " + ra.getReportDatasource(mydb, reportID) + ""));
            String addForm = mydb.getString("select addForm from jvc_report where reportID='" + reportID + "'", "");
            if (addForm.length() > 0) {
                e.addContent(TableToElement("jvc_form", " where formId='" + addForm + "'"));
                e.addContent(TableToElement("jvc_form_field", " where formId='" + addForm + "'"));
            }
            XMLOutputter out = new XMLOutputter("  ", true, "GBK");
            out.setEncoding("GBK");
            Document document = new Document(e);
            out.output(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mydb.close();
        return true;
    }

    public static String getMaxID(String tablename, String fieldname, String format) {
        int re = getMaxID(tablename, fieldname);
        return StringUtils.format(re, format);
    }

    public static int getMaxID(String tablename, String fieldname) {
        MyDB mydb = new MyDB();
        int re = (mydb.getInt("select max(" + fieldname + ")+1 from " + tablename, 1));
        mydb.close();
        return re;
    }

    public static void main(String[] args) {
        ExportDataBase();
    }
}
