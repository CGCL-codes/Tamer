package net.sourceforge.oracle.jutils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import oracle.CartridgeServices.ContextManager;
import oracle.CartridgeServices.CountException;
import oracle.CartridgeServices.InvalidKeyException;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

/**
 *
 * @author asales
 */
public class JdbmsXmppRostersTableImpl extends JdbmsXmpp implements SQLData {

    String sql_type;

    private BigDecimal key;

    private static StoredCtx sctx;

    static final BigDecimal SUCCESS = new BigDecimal(0);

    static final BigDecimal ERROR = new BigDecimal(1);

    private static ResultSet rset;

    private static int nbProcessedRosterEntries = 0;

    private static String ORACLE_OBJECT_XMPP_ROSTER_TYPE_SET_TABLE_NAME = "T_XMPP_ROSTERS";

    private static String ORACLE_OBJECT_XMPP_IMPLEMENT_NAME = "XMPPROSTERSTABLEIMPL";

    private static String ORACLE_XMPP_OBJECT_TYPE = "XMPPROSTERTYPE";

    private static String ORACLE_XMPP_OBJECT_TYPE_SET = "XMPPROSTERTYPESET";

    private static int ORACLE_XMPP_OBJECT_TYPE_NB_COLUMNS = 16;

    public String getSQLTypeName() throws SQLException {
        return sql_type;
    }

    public void readSQL(SQLInput stream, String typeName) throws SQLException {
        sql_type = typeName;
        key = stream.readBigDecimal();
    }

    public void writeSQL(SQLOutput stream) throws SQLException {
        stream.writeBigDecimal(key);
    }

    public static BigDecimal ODCITableStart(STRUCT[] sctx, String iHost, int iPort, String iService, String iLogin, String iPasswd) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:default:connection:");
        System.out.println("Entering in ODCITableStart ...");
        Statement stmt = conn.createStatement();
        rset = stmt.executeQuery("SELECT * FROM " + ORACLE_OBJECT_XMPP_ROSTER_TYPE_SET_TABLE_NAME + " where 1=0");
        StoredCtx ctx = new StoredCtx(rset);
        connect(iHost, iPort, iService);
        try {
            login(iLogin, iPasswd);
            sendPresenceAndStartRosterListener();
            System.out.println("Found " + nbRosters + " Rosters Entries found.");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        int key = 0;
        try {
            key = ContextManager.setContext(ctx);
        } catch (CountException ce) {
            return ERROR;
        }
        Object[] impAttr = new Object[1];
        impAttr[0] = new BigDecimal(key);
        System.out.print("Building Structure Descriptor ...");
        StructDescriptor sd = new StructDescriptor(ORACLE_OBJECT_XMPP_IMPLEMENT_NAME, conn);
        sctx[0] = new STRUCT(sd, conn, impAttr);
        System.out.print("Built stored context.");
        System.out.print("SUccessfully started ODCIStartTable.");
        return SUCCESS;
    }

    public BigDecimal ODCITableFetch(BigDecimal nrows, ARRAY[] outSet) throws SQLException {
        System.out.println("Starting to fetch ODCITableFetch ...");
        Connection conn = DriverManager.getConnection("jdbc:default:connection:");
        StoredCtx ctx;
        try {
            ctx = (StoredCtx) ContextManager.getContext(key.intValue());
        } catch (InvalidKeyException ik) {
            return ERROR;
        }
        int nrowsval = nrows.intValue();
        System.out.println("nrowsval = " + nrowsval);
        System.out.println("nrows = " + nrows);
        Vector v = new Vector(nbRosters);
        StructDescriptor outDesc = StructDescriptor.createDescriptor(ORACLE_XMPP_OBJECT_TYPE, conn);
        Object[] out_attr = new Object[ORACLE_XMPP_OBJECT_TYPE_NB_COLUMNS];
        System.out.println("nbProcessedRosterEntries : " + nbProcessedRosterEntries);
        System.out.println("nbRosters : " + nbRosters);
        if (nbProcessedRosterEntries < nbRosters) {
            Iterator<XmppRoster> rostIter = RosterEntriesList.iterator();
            XmppRoster lRoster;
            while (rostIter.hasNext()) {
                lRoster = rostIter.next();
                out_attr[0] = (Object) new Integer(nbProcessedRosterEntries + 1);
                if (lRoster.getName() == null) {
                    out_attr[1] = (Object) new String("");
                } else {
                    out_attr[1] = (Object) new String(lRoster.getName());
                }
                if (lRoster.getUser() == null) {
                    out_attr[2] = (Object) new String("");
                } else {
                    out_attr[2] = (Object) new String(lRoster.getUser());
                }
                if (lRoster.getMode() == null) {
                    out_attr[3] = (Object) new String("");
                } else {
                    out_attr[3] = (Object) new String(lRoster.getMode());
                }
                if (lRoster.getStatus() == null) {
                    out_attr[4] = (Object) new String("");
                } else {
                    out_attr[4] = (Object) new String(lRoster.getStatus());
                }
                out_attr[5] = (Object) new Integer(lRoster.getAvailable());
                if (lRoster.getPacketId() == null) {
                    out_attr[6] = (Object) new String("");
                } else {
                    out_attr[6] = (Object) new String(lRoster.getPacketId());
                }
                out_attr[7] = (Object) new Integer(lRoster.getPriority());
                if (lRoster.getTo() == null) {
                    out_attr[8] = (Object) new String("");
                } else {
                    out_attr[8] = (Object) new String(lRoster.getTo());
                }
                if (lRoster.getType() == null) {
                    out_attr[9] = (Object) new String("");
                } else {
                    out_attr[9] = (Object) new String(lRoster.getType());
                }
                if (lRoster.getTypeName() == null) {
                    out_attr[10] = (Object) new String("");
                } else {
                    out_attr[10] = (Object) new String(lRoster.getTypeName());
                }
                if (lRoster.getXmlns() == null) {
                    out_attr[11] = (Object) new String("");
                } else {
                    out_attr[11] = (Object) new String(lRoster.getXmlns());
                }
                if (lRoster.getErrorMessage() == null) {
                    out_attr[12] = (Object) new String("");
                } else {
                    out_attr[12] = (Object) new String(lRoster.getErrorMessage());
                }
                out_attr[13] = (Object) new Integer(lRoster.getErrorCode());
                if (lRoster.getErrorCondition() == null) {
                    out_attr[14] = (Object) new String("");
                } else {
                    out_attr[14] = (Object) new String(lRoster.getErrorCondition());
                }
                if (lRoster.getErrorType() == null) {
                    out_attr[15] = (Object) new String("");
                } else {
                    out_attr[15] = (Object) new String(lRoster.getErrorType());
                }
                nbProcessedRosterEntries++;
                v.add((Object) new STRUCT(outDesc, conn, out_attr));
            }
            nrows = new BigDecimal(0);
            nrowsval = 0;
            Object out_arr[] = v.toArray();
            ArrayDescriptor ad = new ArrayDescriptor(ORACLE_XMPP_OBJECT_TYPE_SET, conn);
            outSet[0] = new ARRAY(ad, conn, out_arr);
        }
        getConnection().disconnect();
        return SUCCESS;
    }

    public BigDecimal ODCITableClose() throws SQLException {
        StoredCtx ctx;
        try {
            ctx = (StoredCtx) ContextManager.clearContext(key.intValue());
        } catch (InvalidKeyException ik) {
            return ERROR;
        }
        Statement stmt = ctx.getResultSet().getStatement();
        ctx.getResultSet().close();
        if (stmt != null) stmt.close();
        nbProcessedRosterEntries = 0;
        getConnection().disconnect();
        return SUCCESS;
    }
}
