package org.databene.jdbacl.dialect;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import org.databene.commons.ArrayBuilder;
import org.databene.jdbacl.DBUtil;
import org.databene.jdbacl.DatabaseDialect;
import org.databene.jdbacl.model.DBSequence;
import org.databene.jdbacl.sql.Query;

/**
 * {@link DatabaseDialect} implementation for the Firebird database.<br/>
 * <br/>
 * Created at 09.03.2009 07:13:35
 * @since 0.5.8
 * @author Volker Bergmann
 */
public class FirebirdDialect extends DatabaseDialect {

    private static final String DATE_PATTERN = "''yyyy-MM-dd''";

    private static final String TIME_PATTERN = "''HH:mm:ss''";

    Pattern randomPKNamePattern = Pattern.compile("INTEG_\\d+");

    Pattern randomUKNamePattern = Pattern.compile("RDB\\$\\w+");

    Pattern randomFKNamePattern = Pattern.compile("INTEG_\\d+");

    Pattern randomIndexNamePattern = Pattern.compile("RDB\\$\\w+");

    public FirebirdDialect() {
        super("firebird", true, true, DATE_PATTERN, TIME_PATTERN);
    }

    public String getJDBCDriverClass() {
        return "org.firebirdsql.jdbc.FBDriver";
    }

    @Override
    public boolean isDefaultCatalog(String catalog, String user) {
        return true;
    }

    @Override
    public boolean isDefaultSchema(String schema, String user) {
        return true;
    }

    @Override
    public boolean isSequenceBoundarySupported() {
        return false;
    }

    @Override
    public void createSequence(String name, long initialValue, Connection connection) throws SQLException {
        DBUtil.executeUpdate(renderCreateSequence(name), connection);
        DBUtil.executeUpdate(renderSetSequenceValue(name, initialValue), connection);
    }

    @Override
    public String renderCreateSequence(DBSequence sequence) {
        String result = renderCreateSequence(sequence.getName());
        BigInteger start = sequence.getStart();
        if (start != null && isNotOne(start)) result += "; " + renderSetSequenceValue(sequence.getName(), start.longValue()) + ";";
        return result;
    }

    public String renderCreateSequence(String name) {
        return "CREATE GENERATOR " + name;
    }

    @Override
    public String renderDropSequence(String sequenceName) {
        return "drop generator " + sequenceName;
    }

    @Override
    public String renderFetchSequenceValue(String sequenceName) {
        return "select gen_id(" + sequenceName + ", 1) from RDB$DATABASE;";
    }

    @Override
    public DBSequence[] querySequences(Connection connection) throws SQLException {
        String query = "select RDB$GENERATOR_NAME, RDB$GENERATOR_ID, RDB$SYSTEM_FLAG, RDB$DESCRIPTION " + "from RDB$GENERATORS where RDB$GENERATOR_NAME NOT LIKE '%$%'";
        ResultSet resultSet = null;
        try {
            resultSet = DBUtil.executeQuery(query, connection);
            ArrayBuilder<DBSequence> builder = new ArrayBuilder<DBSequence>(DBSequence.class);
            while (resultSet.next()) builder.add(new DBSequence(resultSet.getString(1).trim(), null));
            return builder.toArray();
        } finally {
            DBUtil.closeResultSetAndStatement(resultSet);
        }
    }

    @Override
    public void setNextSequenceValue(String sequenceName, long value, Connection connection) throws SQLException {
        DBUtil.executeUpdate(renderSetSequenceValue(sequenceName, value), connection);
    }

    public String renderSetSequenceValue(String sequenceName, long value) {
        return "SET GENERATOR " + sequenceName + " TO " + (value - 1);
    }

    @Override
    public boolean isDeterministicPKName(String pkName) {
        return !randomPKNamePattern.matcher(pkName).matches();
    }

    @Override
    public boolean isDeterministicUKName(String ukName) {
        return !randomUKNamePattern.matcher(ukName).matches();
    }

    @Override
    public boolean isDeterministicFKName(String fkName) {
        return !randomFKNamePattern.matcher(fkName).matches();
    }

    @Override
    public boolean isDeterministicIndexName(String indexName) {
        return !randomIndexNamePattern.matcher(indexName).matches();
    }

    @Override
    public void restrictRownums(int firstRowIndex, int rowCount, Query query) {
        throw new UnsupportedOperationException("FirebirdDialect.applyRownumRestriction() is not implemented");
    }
}
