package com.apelon.dts.db;

import java.sql.*;
import java.util.Hashtable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.apelon.common.xml.*;
import com.apelon.common.sql.SQL;
import com.apelon.common.license.db.*;
import java.util.*;
import java.text.*;

/**
 * Execute license and schema requests.
 * <p>The following requests are currently handled:
 * <ul>
 * <li>get license
 * <li>get schema version
 * </ul>
 *
 * @since DTS 3.0
 *
 * Copyright (c) 2006 Apelon, Inc. All rights reserved.
 */
public class DTSCommonDb extends BasicDb implements LicenseRetreiver {

    private String LICENSE_TABLE = "CONTENT_LICENSE";

    private boolean licenseTablePresent = false;

    private static final String TABLE_KEY = "DTS_COMMON_DB";

    /**
  * Set the database connection in {@linkplain  BasicDb BasicDb}
  * and checks to see if the License table exists.
  *
  * @param conn a java.sql.Connection for access to DTS schema.
  *
  * @since DTS 3.0
  */
    public DTSCommonDb(Connection conn) {
        super(conn);
        licenseTablePresent = SQL.checkTableExists(conn, LICENSE_TABLE);
    }

    /**
  * Get the license for a given source.
  *
  * @param root a Xml DOM Element holding data about the query.
  *
  * @return string XML formatted string with license information in <string> tags
  *
  * @see com.apelon.common.license.db.LicenseRetreiver for return format
  *
  * @throws SQLException
  */
    public String retreiveLicense(Element root) throws SQLException {
        return accessSingleLicense(root);
    }

    /**
  * Retrieve all the licenses in the database.
  *
  * @param condition a Xml DOM Element holding data about the query.
  *
  * @return string XML formatted string with license information in <string> tags
  *
  * @see com.apelon.common.license.db.LicenseRetreiver for return format
  *
  * @throws SQLException
  */
    public String retreiveAllLicenses(Element condition) throws SQLException {
        return accessMultipleLicense(condition);
    }

    private String runSingleLicenseTest() throws SQLException {
        StringBuffer license = new StringBuffer();
        appendDtd(license, com.apelon.dts.dtd.common.DTD.COMMON, stringElement());
        String license_text = "<h1>Apelon License</h1> <br> This is a sample license agreement for <h3>QueryExtractor</h3>.";
        appendCdEscElem(license, stringElement(), license_text);
        return license.toString();
    }

    private String runMultipleLicenseTest() throws SQLException {
        StringBuffer license = new StringBuffer();
        appendDtd(license, com.apelon.dts.dtd.common.DTD.COMMON, stringsElement());
        String license_text1 = "<h1>Apelon License1</h1> <br> This is a sample license agreement for <h3>QueryExtractor1</h3>.";
        String license_text2 = "<h1>Apelon License2</h1> <br> This is a sample license agreement for <h3>QueryExtractor2</h3>.";
        XML.asStartTag(stringsElement());
        appendCdEscElem(license, stringElement(), license_text1);
        appendCdEscElem(license, stringElement(), license_text2);
        XML.asEndTag(license, stringsElement());
        return license.toString();
    }

    private String accessSingleLicense(Element elem) throws SQLException {
        String license = "";
        if (licenseTablePresent) {
            Statement stmt = null;
            try {
                String namespace_id = (elem.getFirstChild().getFirstChild()).getNodeValue();
                stmt = conn.createStatement();
                String sql = getDAO().getStatement(TABLE_KEY, "SINGLE_LICENSE");
                sql += namespace_id;
                ResultSet res = stmt.executeQuery(sql);
                if (res.next()) {
                    byte[] lic = res.getBytes(1);
                    if (lic != null) {
                        license = new String(lic);
                    }
                }
            } finally {
                closeStatement(stmt);
            }
        }
        StringBuffer xml_license = new StringBuffer(1024);
        appendDtd(xml_license, com.apelon.dts.dtd.common.DTD.COMMON, stringElement());
        appendCdEscElem(xml_license, stringElement(), license);
        return xml_license.toString();
    }

    private String accessMultipleLicense(Element condition) throws SQLException {
        StringBuffer license_xml = new StringBuffer(1024);
        appendDtd(license_xml, com.apelon.dts.dtd.common.DTD.COMMON, stringsElement());
        XML.asStartTag(license_xml, stringsElement());
        if (licenseTablePresent) {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                String sql = getDAO().getStatement(TABLE_KEY, "MULTIPLE_LICENSE");
                ResultSet res = stmt.executeQuery(sql);
                while (res.next()) {
                    String license = "";
                    byte[] lic_bytes = res.getBytes(1);
                    if (lic_bytes != null) {
                        license = new String(lic_bytes);
                        appendCdEscElem(license_xml, stringElement(), new String(license));
                    }
                }
            } finally {
                closeStatement(stmt);
            }
        }
        XML.asEndTag(license_xml, stringsElement());
        return license_xml.toString();
    }

    /**
   * Append Character Data escaped Element
   */
    protected void appendCdEscElem(StringBuffer document, String elementName, String value) {
        XML.asStartTag(document, elementName);
        document.append(XML.asCdata(value));
        XML.asEndTag(document, elementName);
    }

    protected String licTextTblName() {
        return "license_text";
    }

    protected String contLicColName() {
        return "content_license";
    }

    /**
   * Get the version of the schema currently connected to.
   * @param schemaName a String that is the name of the schema
   * @return String that is the version if present, else null.
   * @throws SQLException Any SQL Error
   */
    public String getSchemaVersion(String schemaName) throws SQLException {
        if (!SQL.checkTableExists(conn, "APELON_VERSION")) {
            if (schemaName.equalsIgnoreCase("dts")) {
                if (SQL.checkTableExists(conn, "DTS_NAMESPACE")) {
                    return "3.0";
                }
            }
            return null;
        }
        ;
        String version = null;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = getDAO().getStatement(TABLE_KEY, "VERSION");
            sql += "'" + schemaName + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                version = rs.getString(1);
            }
        } finally {
            closeStatement(stmt);
        }
        return version;
    }
}
