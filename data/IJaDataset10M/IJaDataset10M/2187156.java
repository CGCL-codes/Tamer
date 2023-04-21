package org.juddi.datastore.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.juddi.datatype.IdentifierBag;
import org.juddi.datatype.KeyedReference;
import org.juddi.datatype.request.FindQualifiers;
import org.juddi.util.Config;
import org.juddi.util.jdbc.ConnectionManager;
import org.juddi.util.jdbc.Transaction;

/**
 * @author Steve Viens (sviens@users.sourceforge.net)
 */
class FindBusinessByIdentifierQuery {

    private static Log log = LogFactory.getLog(FindBusinessByIdentifierQuery.class);

    static String selectSQL;

    static {
        StringBuffer sql = new StringBuffer(200);
        sql.append("SELECT B.BUSINESS_KEY,B.LAST_UPDATE ");
        sql.append("FROM BUSINESS_ENTITY B,BUSINESS_IDENTIFIER I ");
        selectSQL = sql.toString();
    }

    /**
   * Select ...
   *
   * @param connection JDBC connection
   * @throws java.sql.SQLException
   */
    public static Vector select(IdentifierBag identifierBag, Vector keysIn, FindQualifiers qualifiers, Connection connection) throws java.sql.SQLException {
        if ((keysIn != null) && (keysIn.size() == 0)) return keysIn;
        Vector keysOut = new Vector();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer(selectSQL);
        appendWhere(sql, identifierBag, qualifiers);
        appendIn(sql, keysIn);
        appendOrderBy(sql, qualifiers);
        try {
            log.debug("select from BUSINESS_ENTITY & BUSINESS_IDENTIFIER tables:\n\n\t" + sql.toString() + "\n");
            statement = connection.prepareStatement(sql.toString());
            resultSet = statement.executeQuery();
            while (resultSet.next()) keysOut.addElement(resultSet.getString(1));
            return keysOut;
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                log.warn("An Exception was encountered while attempting to close " + "the Find BusinessEntity ResultSet: " + e.getMessage(), e);
            }
            try {
                statement.close();
            } catch (Exception e) {
                log.warn("An Exception was encountered while attempting to close " + "the Find BusinessEntity Statement: " + e.getMessage(), e);
            }
        }
    }

    /**
   *
   */
    private static void appendWhere(StringBuffer sql, IdentifierBag identifierBag, FindQualifiers qualifiers) {
        sql.append("WHERE B.BUSINESS_KEY = I.BUSINESS_KEY ");
        if (identifierBag != null) {
            Vector keyedRefVector = identifierBag.getKeyedReferenceVector();
            if (keyedRefVector != null) {
                int vectorSize = keyedRefVector.size();
                if (vectorSize > 0) {
                    sql.append("AND (");
                    for (int i = 0; i < vectorSize; i++) {
                        KeyedReference keyedRef = (KeyedReference) keyedRefVector.elementAt(i);
                        String name = keyedRef.getKeyName();
                        String value = keyedRef.getKeyValue();
                        if ((name != null) && (value != null)) {
                            sql.append("(I.KEY_NAME = '").append(name).append("' AND I.KEY_VALUE = '").append(value).append("')");
                            if (i + 1 < vectorSize) sql.append(" OR ");
                        }
                    }
                    sql.append(") ");
                }
            }
        }
    }

    /**
   * Utility method used to construct SQL "IN" statements such as
   * the following SQL example:
   *
   *   SELECT * FROM TABLE WHERE MONTH IN ('jan','feb','mar')
   *
   * @param sql StringBuffer to append the final results to
   * @param keysIn Vector of Strings used to construct the "IN" clause
   */
    private static void appendIn(StringBuffer sql, Vector keysIn) {
        if (keysIn == null) return;
        sql.append("AND B.BUSINESS_KEY IN (");
        int keyCount = keysIn.size();
        for (int i = 0; i < keyCount; i++) {
            String key = (String) keysIn.elementAt(i);
            sql.append("'").append(key).append("'");
            if ((i + 1) < keyCount) sql.append(",");
        }
        sql.append(") ");
    }

    /**
   *
   */
    private static void appendOrderBy(StringBuffer sql, FindQualifiers qualifiers) {
        sql.append("ORDER BY ");
        if (qualifiers == null) sql.append("B.LAST_UPDATE DESC"); else if (qualifiers.sortByDateAsc) sql.append("B.LAST_UPDATE ASC"); else sql.append("B.LAST_UPDATE DESC");
    }

    /***************************************************************************/
    public static void main(String[] args) throws Exception {
        Config.setStringProperty("juddi.useConnectionPool", "true");
        Connection conn = null;
        try {
            conn = ConnectionManager.aquireConnection();
            test(conn);
        } finally {
            if (conn != null) conn.close();
        }
    }

    public static void test(Connection connection) throws Exception {
        IdentifierBag identifierBag = new IdentifierBag();
        Vector keyedRefVector = new Vector();
        keyedRefVector.addElement(new KeyedReference("af176f34-00ca-4d93-97f8-2b62aa3a75e5", "blah, blah, blah"));
        keyedRefVector.addElement(new KeyedReference("54a827e0-1b51-4381-8775-ad02c377bb25", "Haachachachacha"));
        keyedRefVector.addElement(new KeyedReference("xxxxxxxxxxxxxxxx", "xxxxxxxxxxx"));
        identifierBag.setKeyedReferenceVector(keyedRefVector);
        Vector keysIn = new Vector();
        keysIn.add("824c73af-22e0-4816-9689-2429101c7727");
        keysIn.add("c311085b-3277-470d-8ce9-07b81c484e4b");
        keysIn.add("6b368a5a-6a62-4f23-a002-f11e22780a91");
        keysIn.add("45994713-d3c3-40d6-87b5-6ce51f36001c");
        keysIn.add("901b15c5-799c-4387-8337-a1a35fceb791");
        keysIn.add("80fdae14-0e5d-4ea6-8eb8-50fde422056d");
        keysIn.add("e1996c33-c436-4004-9e3e-14de191bcc6b");
        keysIn.add("f715d9ff-d4eb-4073-92e8-2411cd5d0d68");
        Transaction txn = new Transaction();
        if (connection != null) {
            try {
                txn.begin(connection);
                select(identifierBag, keysIn, null, connection);
                select(identifierBag, null, null, connection);
                txn.commit();
            } catch (Exception ex) {
                try {
                    txn.rollback();
                } catch (java.sql.SQLException sqlex) {
                    sqlex.printStackTrace();
                }
                throw ex;
            }
        }
    }
}
