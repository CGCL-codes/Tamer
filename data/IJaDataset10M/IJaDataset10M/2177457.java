package Watermill.relational;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import org.apache.log4j.Logger;
import Watermill.interfaces.Identifier;
import Watermill.interfaces.Value;
import Watermill.kernel.Chrono;
import Watermill.kernel.Msg;
import Watermill.kernel.WatermillDbConnectionException;
import Watermill.kernel.WatermillException;
import Watermill.kernel.constraints.GlobalConstraint;
import Watermill.kernel.constraints.GlobalConstraints;
import Watermill.kernel.constraints.LocalConstraint;
import Watermill.kernel.pools.SAPPool;
import Watermill.util.ValueFactory;

/**
 * PsqlDb is the postgres implementation
 * for Db interface
 *
 * @author Julien Lafaye (julien.lafaye@cnam.fr)
 */
public class PsqlDb extends SqlDb {

    private static final Logger logger = Logger.getLogger(PsqlDb.class);

    static final long serialVersionUID = 7969464794313949374L;

    PreparedStatement pstmt_aip;

    PreparedStatement pstmt_get_val;

    Connection con_sv;

    Connection con_aip;

    private static final String driver = "org.postgresql.Driver";

    public PsqlDb(String n, String h, String p, String u) throws WatermillException {
        super(n, h, p, u);
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new WatermillDbConnectionException("Unable to load driver:" + e.getMessage());
        }
        name = n;
    }

    public PsqlDb() {
        connection = null;
    }

    public boolean testDb(String dbName) throws WatermillException {
        try {
            Connection conn = getConnection();
            Statement s = conn.createStatement();
            String query = "SELECT COUNT(datname) " + "FROM pg_catalog.pg_database " + "WHERE datname='" + dbName + "'";
            ResultSet rs = s.executeQuery(query);
            rs.next();
            boolean b = (rs.getInt(1) > 0);
            s.close();
            conn.close();
            return b;
        } catch (SQLException e) {
            throw new WatermillDbConnectionException(e.getMessage());
        }
    }

    /**
	 * Prepare a statement to get a list of keys ordered by dependencies.
	 */
    public Statement getKeyFlowStatement() throws Exception {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
        Statement s = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        s.setFetchSize(50);
        return s;
    }

    /**
	 *  Store in a relation pairs of identifiers having same dependencies
	 */
    protected void buildPairs(String name, String depMatrixName, GlobalConstraints gcs) {
        int arity = gcs.size();
        boolean cont = true;
        boolean[] membership1 = new boolean[arity];
        boolean[] membership2 = new boolean[arity];
        int alive = 1;
        try {
            Connection c = getNewConnection();
            c.setAutoCommit(false);
            ResultSet flow = getKeyFlow(depMatrixName, gcs);
            PreparedStatement pstmt = c.prepareStatement("INSERT INTO " + name + " VALUES(?,?)");
            Object key1, key2;
            String pat1, pat2;
            try {
                boolean b;
                b = flow.next();
                key1 = flow.getObject(1);
                pat1 = flow.getString(2);
                b = flow.next();
                key2 = flow.getObject(1);
                pat2 = flow.getString(2);
                while (b) {
                    if (pat1.equals(pat2)) {
                        pstmt.setObject(1, key1);
                        pstmt.setObject(2, key2);
                        Chrono.setMode(Chrono.QUERY_MODE);
                        pstmt.executeUpdate();
                        Chrono.setMode(Chrono.CPU_MODE);
                        b = flow.next();
                        if (b) {
                            key1 = flow.getString(1);
                            pat1 = flow.getString(2);
                        }
                    } else {
                        pat1 = pat2;
                        key1 = key2;
                    }
                    b = flow.next();
                    if (b) {
                        key2 = flow.getString(1);
                        pat2 = flow.getString(2);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                logger.debug("Flow exhausted!");
            }
            pstmt.close();
            c.commit();
        } catch (Exception e) {
            e.printStackTrace();
            logger.fatal(e);
        }
    }

    /**
	 * Create an empty dependency matrix
	 */
    protected void createDependencyMatrix(String matrixName, LocalConstraint lc, GlobalConstraints gcs) {
        Statement stm = null;
        String query = "";
        int arity = lc.countInvolved(gcs);
        if (arity <= 0) {
            arity = 1;
        }
        logger.debug("Entering createDependencyMatrix(" + name + ")");
        dropTable(matrixName);
        try {
            query = "CREATE  TABLE " + matrixName + " (" + "key1 varchar(100) PRIMARY KEY," + "pattern BIT(" + arity + ") DEFAULT 0::bit(" + arity + "))";
            executeQuery(query);
        } catch (Exception e) {
            logger.fatal(e);
        }
    }

    /**
	 * Create an empty dependency matrix
	 * @deprecated
	 */
    protected void createDependencyMatrix(String name, GlobalConstraints gcs) {
        Statement stm = null;
        String query = "";
        int arity = gcs.size();
        if (arity <= 0) {
            arity = 1;
        }
        logger.debug("Entering createDependencyMatrix(" + name + ")");
        dropTable(name);
        try {
            query = "CREATE  TABLE " + name + " (" + "key1 varchar(100) PRIMARY KEY," + "pattern BIT(" + arity + ") DEFAULT 0::bit(" + arity + "))";
            executeQuery(query);
        } catch (Exception e) {
            logger.fatal(e);
        }
    }

    protected void getDependencyMatrix(String matrixName, LocalConstraint lc, GlobalConstraints gcs) {
        int i = 1;
        int arity = lc.countInvolved(gcs);
        try {
            for (GlobalConstraint gc : gcs) {
                if (lc.isInvolved(gc)) {
                    String query = "UPDATE " + name + " SET " + "pattern=(pattern | (1::bit(" + arity + ") << " + (i - 1) + ")) " + "WHERE key1 IN (" + gc.rKeyString() + ")";
                    logger.info("Adding constraint " + i + " into dependency matrix ");
                    executeQuery(query);
                }
            }
        } catch (Exception e2) {
            logger.fatal(e2);
        }
    }

    /**
	 * Store dependencies in a relation
	 */
    protected void getDependencyMatrixOld(String name, GlobalConstraints gcs) {
        int i = 1;
        int arity = gcs.size();
        try {
            for (Enumeration e = gcs.elements(); e.hasMoreElements(); i++) {
                GlobalConstraint gc = (GlobalConstraint) (e.nextElement());
                String query = "UPDATE " + name + " SET " + "pattern=(pattern | (1::bit(" + arity + ") << " + (i - 1) + ")) " + "WHERE key1 IN (" + gc.rKeyString() + ")";
                logger.info("Adding constraint " + i + " into dependency matrix ");
                executeQuery(query);
            }
        } catch (Exception e2) {
            logger.fatal(e2);
        }
    }

    /**
	 * Insert keys and modifies patterns in the dependency matrix according
	 * to the constraints.
	 * @deprecated
	 */
    protected void getDependencyMatrix(String name, GlobalConstraints gcs) {
        int i = 1;
        int arity = gcs.size();
        String query = "SELECT initAndPopulateDependencyMatrix(?,?,?,?,?,?)";
        try {
            Chrono.setMode(Chrono.QUERY_MODE);
            PreparedStatement pstmt = getConnection().prepareStatement(query);
            int count = 1;
            GlobalConstraint gc;
            Enumeration e = gcs.elements();
            while (e.hasMoreElements()) {
                gc = (GlobalConstraint) (e.nextElement());
                pstmt.setString(1, gc.keyName);
                pstmt.setString(2, gc.tableName);
                pstmt.setString(3, gc.rConditionString());
                pstmt.setString(4, name);
                pstmt.setInt(5, i);
                pstmt.setInt(6, arity);
                ResultSet rs = pstmt.executeQuery();
                rs.next();
                logger.debug("Matrix populated for constraint " + gc + " " + rs.getInt(1) + " keys added");
                if ((count % 10) == 0) logger.debug(count + " constraints processed");
                count++;
                Chrono.setMode(Chrono.CPU_MODE);
            }
        } catch (SQLException e) {
            logger.info("Falling back to good old not optimized method");
            initializeDependencyMatrixOld(name, gcs);
            getDependencyMatrixOld(name, gcs);
        } catch (Exception e) {
            logger.fatal(e);
        }
    }

    public void initializeDependencyMatrix(String name, GlobalConstraints gcs) {
        return;
    }

    public void initializeDependencyMatrixOld(String name, GlobalConstraints gcs) {
        String insert = "";
        String select = "";
        ResultSet rs;
        GlobalConstraint constraint = (GlobalConstraint) gcs.firstElement();
        select = "SELECT " + constraint.keyName + " " + "FROM " + constraint.tableName + " " + "WHERE ";
        insert = "INSERT INTO " + name + "(key1) VALUES(?)";
        int i = 0;
        try {
            Connection c = getConnection();
            Statement s = c.createStatement();
            PreparedStatement ps = c.prepareStatement(insert);
            GlobalConstraint gc;
            String query = "";
            Enumeration e = gcs.elements();
            Chrono.setMode(Chrono.QUERY_MODE);
            while (e.hasMoreElements()) {
                gc = (GlobalConstraint) (e.nextElement());
                query = select + gc.rConditionString();
                rs = s.executeQuery(select + gc.rConditionString());
                while (rs.next()) {
                    try {
                        ps.setString(1, rs.getString(1));
                        ps.executeUpdate();
                        i++;
                    } catch (Exception f) {
                        logger.debug(f);
                    }
                    if ((i % 1000) == 0) {
                        logger.info(i + " keys added");
                    }
                }
            }
            Chrono.setMode(Chrono.CPU_MODE);
        } catch (Exception e) {
            logger.fatal(e);
        }
    }

    public void addIntoPairs(String name, String k1, String k2) {
        try {
            if (pstmt_aip == null) {
                con_aip = getNewConnection();
                con_aip.setAutoCommit(false);
                pstmt_aip = con_aip.prepareStatement("INSERT INTO " + name + " VALUES(?,?)");
            }
            pstmt_aip.setString(1, k1);
            pstmt_aip.setString(2, k2);
            Chrono.setMode(Chrono.QUERY_MODE);
            pstmt_aip.execute();
            Chrono.setMode(Chrono.CPU_MODE);
        } catch (SQLException e) {
            logger.debug("Unable to add pair " + e);
        } catch (Exception e) {
            logger.fatal(e);
        }
    }

    public Value getValue(Identifier iid) {
        try {
            RIdentifier id = (RIdentifier) iid;
            Statement stm = getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            RKeyValue kv = id.getKeyValue();
            RKey k = kv.getKey();
            String keyAttribute = k.getName();
            String keyTable = k.getTable();
            String keyValue = kv.getKeyRef();
            String valueAttribute = id.getName();
            Chrono.setMode(Chrono.QUERY_MODE);
            String queryString = "SELECT " + valueAttribute + " " + "FROM " + keyTable + " " + "WHERE " + keyAttribute + "='" + keyValue + "'";
            ResultSet rs = stm.executeQuery(queryString);
            rs.next();
            Value v = ValueFactory.createValue(rs.getString(1));
            stm.close();
            Chrono.setMode(Chrono.CPU_MODE);
            return v;
        } catch (Exception e) {
            logger.debug(e);
            return null;
        }
    }

    public void setValue(Identifier iid, Value v) {
        try {
            if (con_sv == null) {
                con_sv = getNewConnection();
                con_sv.setAutoCommit(false);
                logger.debug("con_sv commit mode=" + con_sv.getAutoCommit());
            }
            RIdentifier id = (RIdentifier) iid;
            Statement stm = con_sv.createStatement();
            RKeyValue kv = id.getKeyValue();
            RKey k = kv.getKey();
            String keyAttribute = k.getName();
            String keyTable = k.getTable();
            String keyValue = kv.getKeyRef();
            String valueAttribute = id.getName();
            String queryString = "UPDATE " + keyTable + " " + "SET " + valueAttribute + "=" + v + " " + "WHERE " + keyAttribute + "='" + keyValue + "'";
            Chrono.setMode(Chrono.QUERY_MODE);
            stm.execute(queryString);
            Chrono.setMode(Chrono.CPU_MODE);
        } catch (Exception e) {
            logger.fatal(e);
        }
    }

    public int resetPairReading(String name) {
        int count = 0;
        pairReading = null;
        try {
            Statement s = getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            s.setFetchSize(50);
            count = getPairCount(name);
            String query = "SELECT key1,key2 FROM " + name;
            Chrono.setMode(Chrono.QUERY_MODE);
            pairReading = s.executeQuery(query);
            Chrono.setMode(Chrono.CPU_MODE);
        } catch (Exception e) {
            logger.fatal(e);
        }
        return count;
    }

    public void endWatermark() {
        try {
            con_sv.commit();
            con_sv.close();
        } catch (Exception e) {
            logger.debug(e);
        }
        try {
            pstmt_aip.close();
            con_aip.commit();
            con_aip.close();
        } catch (Exception f) {
            logger.debug(f.toString());
        }
        try {
            pairReading.close();
        } catch (Exception g) {
            logger.debug(g.toString());
        }
    }

    /**
	 * Prepare an empty table of Pairs
	 * @param name Name of the target Pairs relation
	 */
    public void createPairs(String name) {
        dropTable(name);
        try {
            Statement stm = getConnection().createStatement();
            String query = "CREATE TABLE " + name + " (" + "key1 varchar(100)," + "key2 varchar(100)," + "PRIMARY KEY (key1,key2))";
            logger.info("CREATION PAIRS:" + query);
            Chrono.setMode(Chrono.QUERY_MODE);
            stm.execute(query);
            Chrono.setMode(Chrono.CPU_MODE);
        } catch (Exception e) {
            logger.fatal(e);
        }
    }

    List<String> listDatabases() {
        final List<String> result = new ArrayList<String>();
        try {
            Set<String> docBlackList = new HashSet<String>();
            docBlackList.add("postgres");
            docBlackList.add("template0");
            docBlackList.add("template1");
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery("select datname from pg_database");
            while (rset.next()) {
                String document = rset.getString(1);
                if (!docBlackList.contains(document)) {
                    result.add(rset.getString(1));
                }
            }
            rset.close();
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }
}
