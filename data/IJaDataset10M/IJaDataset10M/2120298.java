package org.objectstyle.cayenne.dba.postgres;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.objectstyle.cayenne.CayenneRuntimeException;
import org.objectstyle.cayenne.access.DataNode;
import org.objectstyle.cayenne.access.QueryLogger;
import org.objectstyle.cayenne.dba.oracle.OraclePkGenerator;
import org.objectstyle.cayenne.map.DbEntity;
import org.objectstyle.cayenne.map.DbKeyGenerator;

/**
 * Default PK generator for PostgreSQL that uses sequences for PK generation.
 */
public class PostgresPkGenerator extends OraclePkGenerator {

    protected String createSequenceString(DbEntity ent) {
        StringBuffer buf = new StringBuffer();
        buf.append("CREATE SEQUENCE ").append(sequenceName(ent)).append(" INCREMENT ").append(pkCacheSize(ent)).append(" START 200");
        return buf.toString();
    }

    /**
     * Generates primary key by calling Oracle sequence corresponding to the
     * <code>dbEntity</code>. Executed SQL looks like this:
     * 
     * <pre>
     *     SELECT nextval(pk_table_name)
     * </pre>
     */
    protected int pkFromDatabase(DataNode node, DbEntity ent) throws Exception {
        DbKeyGenerator pkGenerator = ent.getPrimaryKeyGenerator();
        String pkGeneratingSequenceName;
        if (pkGenerator != null && DbKeyGenerator.ORACLE_TYPE.equals(pkGenerator.getGeneratorType()) && pkGenerator.getGeneratorName() != null) pkGeneratingSequenceName = pkGenerator.getGeneratorName(); else pkGeneratingSequenceName = sequenceName(ent);
        Connection con = node.getDataSource().getConnection();
        try {
            Statement st = con.createStatement();
            try {
                String sql = "SELECT nextval('" + pkGeneratingSequenceName + "')";
                QueryLogger.logQuery(sql, Collections.EMPTY_LIST);
                ResultSet rs = st.executeQuery(sql);
                try {
                    if (!rs.next()) {
                        throw new CayenneRuntimeException("Error generating pk for DbEntity " + ent.getName());
                    }
                    return rs.getInt(1);
                } finally {
                    rs.close();
                }
            } finally {
                st.close();
            }
        } finally {
            con.close();
        }
    }

    /**
     * Fetches a list of existing sequences that might match Cayenne generated ones.
     */
    protected List getExistingSequences(DataNode node) throws SQLException {
        Connection con = node.getDataSource().getConnection();
        try {
            Statement sel = con.createStatement();
            try {
                String sql = "SELECT relname FROM pg_class WHERE relkind='S'";
                QueryLogger.logQuery(sql, Collections.EMPTY_LIST);
                ResultSet rs = sel.executeQuery(sql);
                try {
                    List sequenceList = new ArrayList();
                    while (rs.next()) {
                        sequenceList.add(rs.getString(1));
                    }
                    return sequenceList;
                } finally {
                    rs.close();
                }
            } finally {
                sel.close();
            }
        } finally {
            con.close();
        }
    }
}
