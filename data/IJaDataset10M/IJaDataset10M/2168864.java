package com.apelon.matchpack.client;

import com.apelon.common.log4j.Categories;
import com.apelon.common.sql.SQL;
import com.apelon.common.xml.XML;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.sql.*;

/**
 * Encapsulates functions used to retrieve information about the silos contained
 * in the match_db table in the ApelonKB.
 *
 * @author: Pete DiBetta
 * @version 1.0
 *
 * @since MatchDTS 1.0
 */
public class SiloSQL {

    /**
   * Database connection.  Two valid types.
   * <ul>
   * <li>"socket" - For production environments to utilize QuincyDTS</li>
   * <li>"jdbc"   - For testing.  Makes debugging easier</li>
   * </ul>
   *
   * @see com.apelon.common.client.ServerConnectionJDBC
   * @see com.apelon.common.client.ServerConnectionSocket
   */
    private Connection conn;

    /**
   * This variable is used to hold the final SQL statement used to perform
   * a query against match_db.  The statement is initialized through a call to
   * the {@link #openStatements()}.
   */
    private PreparedStatement silo_query = null;

    /**
   * Sole Constructor
   */
    public SiloSQL(Connection conn) {
        this.conn = conn;
    }

    /**
   * Not used.  Completes <code>PreparedStatement</code> objects.  Currently not
   * being used.  May be removed pending finalization of this class.
   */
    private void openStatements() throws SQLException {
    }

    /**
   * Retrieves the name, id pairs of all silos contained in ApelonKB.
   *
   * @return    String result set conforming to 'siloSets' defined in result.dtd.
   *
   * @since     MatchDTS 1.0
   */
    public String siloQuery(Document doc) {
        StringBuffer query = new StringBuffer("select DISTINCT SPEC_NAME, SPEC_ID from DTS_MATCH_SPEC where ");
        Element siloQuery = doc.getDocumentElement();
        NodeList nl = siloQuery.getChildNodes();
        String node = nl.item(0).getNodeName();
        if (node.toUpperCase().trim().equals("NAME")) {
            StringBuffer name = new StringBuffer(nl.item(0).getFirstChild().getNodeValue());
            query.append("UPPER(SPEC_NAME) = UPPER('").append(name).append("')");
        } else {
            StringBuffer id = new StringBuffer(nl.item(0).getFirstChild().getNodeValue());
            query.append("SPEC_ID = '").append(id).append("'");
        }
        StringBuffer siloInfo = new StringBuffer();
        StringBuffer siloResult = new StringBuffer();
        try {
            Statement silo_query = this.conn.createStatement();
            ResultSet res = silo_query.executeQuery(query.toString());
            int numResults = 0;
            while (res.next()) {
                int index = 1;
                String spec_name = res.getString(index++);
                int spec_id = res.getInt(index++);
                XML.asStartTag(siloInfo, "silo_info");
                XML.asTagValue(siloInfo, "name", spec_name);
                XML.asTagValue(siloInfo, "id", String.valueOf(spec_id));
                XML.asEndTag(siloInfo, "silo_info");
                numResults++;
            }
            res.close();
            silo_query.close();
            if (numResults > 0) {
                XML.createProlog(siloResult, "silo", com.apelon.matchpack.dtd.DTD.RESULT);
                XML.asStartTag(siloResult, "silo defined = \"true\"");
                siloResult.append(siloInfo.toString());
            } else {
                XML.createProlog(siloResult, "silo", com.apelon.matchpack.dtd.DTD.RESULT);
                XML.asStartTag(siloResult, "silo defined = \"false\"");
            }
            siloResult.append(XML.asEndTag("silo"));
            Categories.dataDb().info("silo xml: " + siloResult.toString());
        } catch (DOMException ex) {
            Categories.dataXml().error(ex);
        } catch (SQLException ex) {
            Categories.dataDb().error(ex);
        } catch (Exception ex) {
            Categories.dataServer().error(ex);
        } finally {
            return siloResult.toString();
        }
    }

    /**
   * Retrieves the name, id pairs of all silos contained in ApelonKB.
   *
   * @return    String result set conforming to 'siloSets' defined in result.dtd.
   *
   * @since     MatchDTS 1.0
   */
    public String siloSets() {
        StringBuffer siloSet = new StringBuffer();
        StringBuffer siloSets = new StringBuffer();
        boolean tableExist = true;
        if (!SQL.checkTableExists(this.conn, "DTS_MATCH_SPEC")) {
            Categories.dataIo().info("DTS_MATCH_SPEC table doesn't exist in knowledgebase.");
            tableExist = false;
        }
        StringBuffer query = new StringBuffer("select DISTINCT SPEC_NAME, SPEC_ID " + " from DTS_MATCH_SPEC ORDER BY SPEC_ID");
        int numResults = 0;
        try {
            if (tableExist) {
                Statement silo_query = this.conn.createStatement();
                ResultSet res = silo_query.executeQuery(query.toString());
                while (res.next()) {
                    int index = 1;
                    String spec_name = res.getString(index++);
                    int spec_id = res.getInt(index++);
                    XML.asStartTag(siloSet, "silo_set");
                    XML.asTagValue(siloSet, "name", spec_name);
                    XML.asTagValue(siloSet, "id", String.valueOf(spec_id));
                    XML.asEndTag(siloSet, "silo_set");
                    numResults++;
                }
                res.close();
                silo_query.close();
            }
            if (numResults > 0) {
                XML.createProlog(siloSets, "silo_sets", com.apelon.matchpack.dtd.DTD.RESULT);
                XML.asStartTag(siloSets, "silo_sets empty = \"false\"");
                siloSets.append(siloSet.toString());
            } else {
                XML.createProlog(siloSets, "silo_sets", com.apelon.matchpack.dtd.DTD.RESULT);
                siloSets.append(XML.asStartTag("silo_sets empty = \"true\""));
            }
            XML.asEndTag(siloSets, "silo_sets");
            Categories.dataXml().info("silo_sets xml: " + siloSets.toString());
        } catch (DOMException ex) {
            Categories.dataXml().error(ex);
        } catch (SQLException ex) {
            Categories.dataDb().error(ex);
        } catch (Exception ex) {
            Categories.dataServer().error(ex);
        } finally {
            return siloSets.toString();
        }
    }
}
