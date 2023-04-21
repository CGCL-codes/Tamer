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
import java.util.Iterator;
import java.util.Vector;
import oracle.CartridgeServices.ContextManager;
import oracle.CartridgeServices.CountException;
import oracle.CartridgeServices.InvalidKeyException;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.ARRAY;
import oracle.sql.StructDescriptor;
import org.omg.PortableInterceptor.SUCCESSFUL;

/**
 *
 * @author asales
 */
public class JdbmsJackSumTableImpl extends JdbmsJackSum implements SQLData {

    public static String ORACLE_JACKSUM_IMPLEMENT_NAME = "JACKSUMALGOTABLEIMPL";

    public static String ORACLE_OBJECT_JACKSUM_TYPE_SET_TABLE_NAME = "T_JACKSUM_ALGOS";

    public static String ORACLE_JACKSUM_OBJECT_TYPE_SET = "JACKSUM_ALGO_TYPESET";

    public static String ORACLE_JACKSUM_OBJECT_TYPE = "JACKSUM_ALGO_TYPE";

    public static int ORACLE_JACKSUM_OBJECT_TYPE_NB_COLUMNS = 7;

    String sql_type;

    private BigDecimal key;

    private static StoredCtx sctx;

    static final BigDecimal SUCCESS = new BigDecimal(0);

    static final BigDecimal ERROR = new BigDecimal(1);

    private static ResultSet rset;

    private static int nbProcessedAlgoEntries = 0;

    private static Connection conn = null;

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

    public static BigDecimal ODCITableStart(STRUCT[] sctx) throws Exception {
        conn = DriverManager.getConnection("jdbc:default:connection:");
        System.out.println("Entering in ODCITableStart ...");
        Statement stmt = conn.createStatement();
        rset = stmt.executeQuery("SELECT * FROM " + ORACLE_OBJECT_JACKSUM_TYPE_SET_TABLE_NAME + " where 1=0");
        StoredCtx ctx = new StoredCtx(rset);
        int key = 0;
        fillAlgos();
        try {
            key = ContextManager.setContext(ctx);
        } catch (CountException ce) {
            return ERROR;
        }
        Object[] impAttr = new Object[1];
        impAttr[0] = new BigDecimal(key);
        System.out.println("Building Structure Descriptor ...");
        StructDescriptor sd = new StructDescriptor(ORACLE_JACKSUM_IMPLEMENT_NAME, conn);
        sctx[0] = new STRUCT(sd, conn, impAttr);
        System.out.println("Built stored context.");
        System.out.println("SUccessfully started ODCIStartTable.");
        return SUCCESS;
    }

    public BigDecimal ODCITableFetch(BigDecimal nrows, ARRAY[] outSet) throws SQLException {
        System.out.println("Starting to fetch ODCITableFetch ...");
        StoredCtx ctx;
        try {
            ctx = (StoredCtx) ContextManager.getContext(key.intValue());
        } catch (InvalidKeyException ik) {
            return ERROR;
        }
        int nrowsval = nrows.intValue();
        System.out.println("nrowsval = " + nrowsval);
        System.out.println("nrows = " + nrows);
        Vector v = new Vector(nbAlgorithms);
        StructDescriptor outDesc = StructDescriptor.createDescriptor(ORACLE_JACKSUM_OBJECT_TYPE, conn);
        Object[] out_attr = new Object[ORACLE_JACKSUM_OBJECT_TYPE_NB_COLUMNS];
        System.out.println("nbProcessedRosterEntries : " + nbProcessedAlgoEntries);
        System.out.println("nbAlgos : " + nbAlgorithms);
        if (nbProcessedAlgoEntries < nbAlgorithms) {
            System.out.println("Fetching Algos ...");
            System.out.println("Nb Processed : " + nbProcessedAlgoEntries);
            Iterator<JackSumAlgo> algIter = availableAlgos.iterator();
            JackSumAlgo lAlgo;
            while (algIter.hasNext()) {
                System.out.println("Fetching one more algo (" + nbProcessedAlgoEntries + "/" + nbAlgorithms + ").");
                lAlgo = algIter.next();
                out_attr[0] = (Object) new Integer(nbProcessedAlgoEntries);
                out_attr[1] = (Object) new String(lAlgo.getDescription());
                out_attr[2] = (Object) new String(lAlgo.getEncoding());
                out_attr[3] = (Object) new Integer(lAlgo.getGroupId());
                out_attr[4] = (Object) new String(lAlgo.getName());
                out_attr[5] = (Object) new String(lAlgo.getSeparator());
                out_attr[6] = (Object) new Long(lAlgo.getValue());
                v.add((Object) new STRUCT(outDesc, conn, out_attr));
                nbProcessedAlgoEntries++;
            }
        }
        nrows = new BigDecimal(0);
        nrowsval = 0;
        Object out_arr[] = v.toArray();
        ArrayDescriptor ad = new ArrayDescriptor(ORACLE_JACKSUM_OBJECT_TYPE_SET, conn);
        outSet[0] = new ARRAY(ad, conn, out_arr);
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
        nbProcessedAlgoEntries = 0;
        return SUCCESS;
    }
}
