package com.dcivision.dms.client.parser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.Utility;
import com.dcivision.framework.bean.SysUserDefinedIndex;
import com.dcivision.framework.bean.SysUserDefinedIndexDetail;
import com.dcivision.framework.dao.SysUserDefinedIndexDAObject;
import com.dcivision.framework.dao.SysUserDefinedIndexDetailDAObject;
import com.dcivision.framework.xml.XMLUtility;

/**
 * <p>Class Name:       SysUserDefinedIndexParser.java    </p>
 * <p>Description:      the class is to generate XML</p>
 *
 *    @author           Jenny Li
 *    @company          DCIVision Limited
 *    @creation date    18/02/2003
 *    @version          $Revision: 1.5 $
 */
public class SysUserDefinedIndexParser extends XMLUtility {

    public static final String REVISION = "$Revision: 1.5 $";

    List userDefineIDList = new ArrayList();

    SessionContainer ctx = null;

    Connection conn = null;

    String dataFilePath = null;

    public SysUserDefinedIndexParser() {
    }

    public SysUserDefinedIndexParser(String file) {
        super(file);
    }

    public List getUserDefineIDList() {
        return this.userDefineIDList;
    }

    public void setUserDefineIDList(List userDefineIDList) {
        this.userDefineIDList = userDefineIDList;
    }

    public SessionContainer getCtx() {
        return this.ctx;
    }

    public void setCtx(SessionContainer ctx) {
        this.ctx = ctx;
    }

    public Connection getConn() {
        return this.conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public String getDataFilePath() {
        return (this.dataFilePath);
    }

    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    public void parse(InputSource inputSource) throws IOException, SAXException {
        try {
            ContentHandler handler = this.getContentHandler();
            handler.startDocument();
            SysUserDefinedIndexDetailDAObject UserDefinedIndexDetailDAO = new SysUserDefinedIndexDetailDAObject(ctx, conn);
            SysUserDefinedIndexDAObject userDefinedIndexDAO = new SysUserDefinedIndexDAObject(ctx, conn);
            List userDetailList = new ArrayList();
            PrintStream out = new PrintStream(new FileOutputStream(dataFilePath));
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<file_list xmlns=\"http://tempuri.org/profile.xsd\">");
            if (!Utility.isEmpty(userDefineIDList)) {
                for (int i = 0; i < userDefineIDList.size(); i++) {
                    Integer userDefineID = (Integer) userDefineIDList.get(i);
                    SysUserDefinedIndex sysUserDefinedIndex = (SysUserDefinedIndex) userDefinedIndexDAO.getObjectByID(userDefineID);
                    if (!Utility.isEmpty(sysUserDefinedIndex)) {
                        userDetailList = UserDefinedIndexDetailDAO.getListBySysUserDefinedIndexID(sysUserDefinedIndex.getID());
                        out.println(blank(1) + "<profile id=\"" + sysUserDefinedIndex.getID() + "\">");
                        out.println(blank(2) + "<profile_name>" + sysUserDefinedIndex.getUserDefinedType() + "</profile_name>");
                        out.println(blank(2) + "<parent_id>" + sysUserDefinedIndex.getParentID() + "</parent_id>");
                        out.println(blank(2) + "<description>" + sysUserDefinedIndex.getDescription() + "</description>");
                        out.println(blank(2) + "<gen_key_template>" + sysUserDefinedIndex.getGenKeyTemplate() + "</gen_key_template>");
                        out.println(blank(2) + "<gen_num_max>" + sysUserDefinedIndex.getGenKeyNumMax() + "</gen_num_max>");
                        out.println(blank(2) + "<create_date>" + sysUserDefinedIndex.getCreateDate() + "</create_date>");
                        out.println(blank(2) + "<update_date>" + sysUserDefinedIndex.getUpdateDate() + "</update_date>");
                        if (!Utility.isEmpty(userDetailList)) {
                            out.println(blank(2) + "<profile_details>");
                            for (int j = 0; j < userDetailList.size(); j++) {
                                SysUserDefinedIndexDetail userDetail = (SysUserDefinedIndexDetail) userDetailList.get(j);
                                out.println(blank(3) + "<define_index id=\"" + userDetail.getID() + "\">");
                                out.println(blank(4) + "<field_name>" + userDetail.getFieldName() + "</field_name>");
                                out.println(blank(4) + "<field_type>" + userDetail.getFieldType() + "</field_type>");
                                out.println(blank(4) + "<mandatory>" + userDetail.getMandatory() + "</mandatory>");
                                out.println(blank(4) + "<default>true</default>");
                                out.println(blank(4) + "<validation_rule></validation_rule>");
                                out.println(blank(3) + "</define_index>");
                            }
                            out.println(blank(2) + "</profile_details>");
                            out.println(blank(1) + "</profile>");
                        }
                    }
                }
            }
            out.println("</file_list>");
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public StringBuffer blank(int i) {
        String s = "   ";
        StringBuffer ss = new StringBuffer();
        for (int k = 0; k <= i; k++) {
            ss.append(s);
        }
        return ss;
    }
}
