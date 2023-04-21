package com.siteeval.system;

import com.siteeval.common.DbConnect;
import com.siteeval.common.Globa;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import java.util.Date;
import java.util.Vector;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2005-11-4
 * Time: 15:52:58
 * To change this template use Options | File Templates.
 */
public class SysLog {

    /**ȫ�ֱ���*/
    private Globa globa;

    private DbConnect db;

    private ResultSet rs;

    Document doc;

    private Date datOccur;

    public SysLog() {
    }

    public SysLog(Globa globa) {
        this.globa = globa;
        db = globa.db;
    }

    public SysLog(Globa globa, boolean b) {
        this.globa = globa;
        db = globa.db;
        if (b) globa.setDynamicProperty(this);
    }

    String strTableName = "t_sy_log";

    private String retrunQuery(String tWhere, String startdate, String enddate) {
        StringBuffer strSql = new StringBuffer(tWhere);
        if (startdate != null && !startdate.equals("")) {
            strSql.append(" and '" + startdate + "'<=dOccurDate");
        }
        if (enddate != null && !enddate.equals("")) {
            strSql.append(" and '" + enddate + " 23:59:59'>=dOccurDate");
        }
        strSql.append(" order by dOccurDate DESC");
        return strSql.toString();
    }

    public SysLog show(String where) {
        try {
            String strSql = "select * from  " + strTableName + "  ".concat(String.valueOf(String.valueOf(where)));
            ResultSet rs = db.executeQuery(strSql);
            if (rs != null && rs.next()) return load(rs); else return null;
        } catch (Exception ee) {
            return null;
        }
    }

    public SysLog load(ResultSet rs) {
        SysLog theBean = new SysLog();
        try {
            theBean.setStrId(rs.getString("strId"));
            theBean.setdOccurDate(rs.getString("dOccurDate"));
            theBean.setStrOperator(rs.getString("strOperator"));
            theBean.setStrContent(rs.getString("strContent"));
            theBean.setStrCode(rs.getString("strCode"));
            theBean.setStrOperatorIp(rs.getString("strOperatorIp"));
            theBean.setStrOther(rs.getString("strOther"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return theBean;
    }

    public int getCount(String tWhere, String startdate, String enddate) {
        int count = 0;
        String where = retrunQuery(tWhere, startdate, enddate);
        try {
            String sql = "SELECT count(strId) FROM " + strTableName + "  ";
            if (where.length() > 0) {
                where = where.toLowerCase();
                if (where.indexOf("order") > 0) where = where.substring(0, where.lastIndexOf("order"));
                sql = String.valueOf(sql) + String.valueOf(where);
            }
            ResultSet rs = db.executeQuery(sql);
            if (rs.next()) count = rs.getInt(1);
            rs.close();
            return count;
        } catch (Exception ee) {
            ee.printStackTrace();
            return count;
        }
    }

    public Vector list(String tWhere, String startdate, String enddate, int startRow, int rowCount) {
        Vector beans = new Vector();
        String where = retrunQuery(tWhere, startdate, enddate);
        try {
            String sql = "SELECT *  FROM  " + strTableName;
            if (where.length() > 0) sql = String.valueOf(sql) + String.valueOf(where);
            Statement s = db.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (startRow != 0 && rowCount != 0) s.setMaxRows((startRow + rowCount) - 1);
            ResultSet rs = s.executeQuery(sql);
            if (rs != null && rs.next()) {
                if (startRow != 0 && rowCount != 0) rs.absolute(startRow);
                do {
                    SysLog theBean = new SysLog();
                    theBean = load(rs);
                    beans.addElement(theBean);
                } while (rs.next());
            }
            rs.close();
            s.close();
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return beans;
    }

    private String dateFormat2(java.util.Date date) {
        if (date == null) return "";
        java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(date);
    }

    public boolean delete(String where) {
        try {
            String sql = "DELETE FROM " + strTableName + "  ".concat(where);
            System.out.println("" + sql);
            db.executeUpdate(sql);
            return true;
        } catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
    }

    public boolean del() {
        try {
            db.executeUpdate("DELETE FROM " + strTableName + " ");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * �����־�����ļ�
     * @return the backup file's URL
     */
    public byte[] backup(String operatorId, String startdate, String enddate) {
        String sql = "SELECT strId,strOperator,dOccurDate,strContent,strCode,strOperatorIp,strOther  FROM  " + strTableName;
        String where = retrunQuery(operatorId, startdate, enddate);
        if (where.length() > 0) sql = String.valueOf(sql) + String.valueOf(where);
        java.sql.ResultSet rs = db.executeQuery(sql);
        String logInfo = "";
        String path = globa.application.getRealPath(globa.getPropValue("PATH_LOG_BACKUP", "logs"));
        java.io.File dir = new java.io.File(path);
        if (!dir.exists()) dir.mkdirs();
        path += java.io.File.separator;
        path += System.currentTimeMillis() + "log.xml";
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
            logInfo = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n";
            logInfo += "<logs>\n";
            while (rs.next()) {
                logInfo += "<log>\n";
                logInfo += "<id>" + rs.getString("strId") + "</id>\n";
                logInfo += "<operator>" + rs.getString("strOperator") + "</operator>\n";
                logInfo += "<occurDate>" + dateFormat2(new java.util.Date(rs.getTimestamp("dOccurDate").getTime())) + "</occurDate>\n";
                logInfo += "<code>" + rs.getString("strCode") + "</code>\n";
                logInfo += "<operatorIp>" + rs.getString("strOperatorIp") + "</operatorIp>\n";
                logInfo += "<other>" + rs.getString("strOther") + "</other>\n";
                logInfo += "</log>\n";
            }
            logInfo += "</logs>";
            bos.write(logInfo.getBytes("GBK"));
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        try {
            return logInfo.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return logInfo.getBytes();
        }
    }

    /**
     * ����һ��DOM����
     * ����path���XML�ļ��ľ��·��
     */
    private Document newDom(String path) {
        Document doc;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(path);
            doc.normalize();
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String strId;

    String dOccurDate;

    private String strOperator;

    private String strContent;

    private String strCode;

    private String strOperatorIp;

    private String strOther;

    public String getStrTableName() {
        return strTableName;
    }

    public void setStrTableName(String strTableName) {
        this.strTableName = strTableName;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getdOccurDate() {
        return dOccurDate;
    }

    public void setdOccurDate(String dOccurDate) {
        this.dOccurDate = dOccurDate;
    }

    public String getStrOperator() {
        return strOperator;
    }

    public void setStrOperator(String strOperator) {
        this.strOperator = strOperator;
    }

    public String getStrContent() {
        return strContent;
    }

    public void setStrContent(String strContent) {
        this.strContent = strContent;
    }

    public String getStrCode() {
        return strCode;
    }

    public void setStrCode(String strCode) {
        this.strCode = strCode;
    }

    public String getStrOperatorIp() {
        return strOperatorIp;
    }

    public void setStrOperatorIp(String strOperatorIp) {
        this.strOperatorIp = strOperatorIp;
    }

    public String getStrOther() {
        return strOther;
    }

    public void setStrOther(String strOther) {
        this.strOther = strOther;
    }
}
