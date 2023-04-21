package org.deri.iris.rdb.storage;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IConcreteTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;
import org.deri.iris.rdb.utils.RdbUtils;
import org.deri.iris.storage.IRelation;

/**
 * This relation may return duplicate tuples.
 */
public class SimpleRdbRelation extends AbstractRdbRelation {

    public static final String LEFT_TABLE_ALIAS = "rightTable";

    private String tableName;

    private int arity;

    private RdbUniverseRelation universe;

    private PreparedStatement insertStatement;

    private PreparedStatement getTupleStatement;

    private PreparedStatement sizeStatement;

    private PreparedStatement containsStatement;

    public SimpleRdbRelation(Connection connection, String tableName, int arity) throws SQLException {
        super(connection);
        this.tableName = tableName;
        this.arity = arity;
        this.universe = RdbUniverseRelation.getInstance(connection);
    }

    @Override
    public int getArity() {
        return arity;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public boolean add(ITuple tuple) {
        return add(tuple, false);
    }

    private boolean add(ITuple tuple, boolean batched) {
        if (tuple.size() != getArity()) {
            return false;
        }
        try {
            createInsertStatement();
            int i = 1;
            for (ITerm term : tuple) {
                int id = addToUniverse(term);
                insertStatement.setInt(i, id);
                i++;
            }
            if (batched) {
                logger.debug("Add to batch " + insertStatement);
                insertStatement.addBatch();
            } else {
                logger.debug("Executing " + insertStatement);
                return insertStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 23001) {
                logger.debug("Tuple " + tuple + " already exists in the relation");
            } else {
                logger.error("Failed to execute query (error code: " + e.getErrorCode() + ")", e);
            }
        }
        return false;
    }

    private void createInsertStatement() throws SQLException {
        if (insertStatement == null) {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("INSERT INTO " + getTableName());
            sqlBuilder.append("(");
            List<String> attributeList = getAttributes();
            String attributes = RdbUtils.join(attributeList, ", ");
            sqlBuilder.append(attributes);
            sqlBuilder.append(") VALUES (");
            List<String> questionMarkList = new ArrayList<String>();
            for (int i = 0; i < getArity(); i++) {
                questionMarkList.add("?");
            }
            String questionMarks = RdbUtils.join(questionMarkList, ", ");
            sqlBuilder.append(questionMarks);
            sqlBuilder.append(")");
            Connection connection = getConnection();
            insertStatement = connection.prepareStatement(sqlBuilder.toString());
        }
    }

    private int addToUniverse(ITerm term) {
        if (!term.isGround() || !(term instanceof IConcreteTerm)) {
            return -1;
        }
        universe.add(term);
        return universe.getId(term);
    }

    @Override
    public boolean addAll(IRelation relation) {
        if (relation instanceof IRdbRelation) {
            IRdbRelation otherRelation = (IRdbRelation) relation;
            try {
                return addAll(otherRelation);
            } catch (SQLException e) {
            }
            return addAllOneByOne(otherRelation);
        }
        logger.debug("Adding tuples one by one");
        boolean allAdded = false;
        int size = relation.size();
        for (int i = 0; i < size; i++) {
            allAdded |= add(relation.get(i));
        }
        return allAdded;
    }

    public boolean addAll(IRdbRelation otherRelation) throws SQLException {
        if (otherRelation instanceof RdbUniverseRelation) {
            logger.warn("Attempted to copy universe relation");
            return false;
        }
        if (otherRelation.getArity() != getArity()) {
            if (logger.isWarnEnabled() && otherRelation.size() > 0) {
                logger.warn("Arity of source relation ({}) did not match target relation ({})", otherRelation.getTableName(), getTableName());
            }
            return false;
        }
        List<String> attributeList = getAttributes();
        String attributes = RdbUtils.join(attributeList, ", ");
        String selectClause = createSelectJoinClause(otherRelation);
        String sqlFormat = "INSERT INTO %s(%s) %s";
        String sql = String.format(sqlFormat, getTableName(), attributes.toString(), selectClause);
        Connection connection = getConnection();
        CallableStatement call = null;
        try {
            logger.debug("Adding tuples from " + otherRelation.getTableName() + " to " + getTableName());
            call = connection.prepareCall(sql);
            logger.debug("Executing " + call);
            int rowCount = call.executeUpdate();
            return rowCount > 0;
        } catch (SQLException e) {
            logger.error("Failed to add tuples from " + otherRelation.getTableName() + " to " + getTableName(), e);
            throw e;
        }
    }

    private boolean addAllOneByOne(IRdbRelation relation) {
        boolean allAdded = false;
        CloseableIterator<ITuple> iterator = relation.iterator();
        while (iterator.hasNext()) {
            ITuple tuple = iterator.next();
            allAdded |= add(tuple);
        }
        iterator.close();
        return allAdded;
    }

    private String createSelectJoinClause(IRdbRelation otherRelation) {
        List<String> joinParts = new ArrayList<String>();
        List<String> isNullParts = new ArrayList<String>();
        List<String> leftAttributeList = new ArrayList<String>();
        List<String> rightAttributeList = getAttributes();
        for (int i = 0; i < getArity(); i++) {
            String leftAttribute = LEFT_TABLE_ALIAS + "." + rightAttributeList.get(i);
            String rightAttribute = getTableName() + "." + rightAttributeList.get(i);
            leftAttributeList.add(leftAttribute);
            String joinPart = leftAttribute + " = " + rightAttribute;
            joinParts.add(joinPart);
            String isNullPart = rightAttribute + " IS NULL";
            isNullParts.add(isNullPart);
        }
        StringBuilder joinBuilder = new StringBuilder();
        if (joinParts.size() > 0) {
            String joinConditions = RdbUtils.join(joinParts, " AND ");
            String whereConditions = RdbUtils.join(isNullParts, " AND ");
            String joinFormat = "LEFT JOIN %s ON %s WHERE %s";
            String join = String.format(joinFormat, getTableName(), joinConditions, whereConditions);
            joinBuilder.append(join);
        }
        String join = joinBuilder.toString();
        String leftAttributes = RdbUtils.join(leftAttributeList, ", ");
        String sqlFormat = "SELECT DISTINCT %s FROM %s %s";
        String sql = String.format(sqlFormat, leftAttributes.toString(), otherRelation.getTableName() + " AS " + LEFT_TABLE_ALIAS, join);
        return sql;
    }

    @Override
    public int size() {
        ResultSet resultSet = null;
        try {
            createSizeStatement();
            logger.debug("Executing " + sizeStatement);
            resultSet = sizeStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("size");
            }
        } catch (SQLException e) {
            logger.error("Failed to execute query", e);
        } finally {
            RdbUtils.closeResultSet(resultSet);
        }
        return 0;
    }

    private void createSizeStatement() throws SQLException {
        if (sizeStatement == null) {
            String queryFormat = "SELECT COUNT(*) AS size FROM %s";
            String query = String.format(queryFormat, getTableName());
            Connection connection = getConnection();
            sizeStatement = connection.prepareStatement(query);
        }
    }

    private List<String> getAttributes() {
        List<String> attributes = new ArrayList<String>();
        for (int i = 1; i <= getArity(); i++) {
            attributes.add(IRdbRelation.ATTRIBUTE_PREFIX + i);
        }
        return attributes;
    }

    @Override
    public ITuple get(int index) {
        ResultSet resultSet = null;
        try {
            createGetTupleStatement();
            getTupleStatement.setInt(1, index + 1);
            logger.debug("Executing " + getTupleStatement);
            resultSet = getTupleStatement.executeQuery();
            List<Integer> termIds = new ArrayList<Integer>();
            if (resultSet.next()) {
                List<ITerm> terms = new ArrayList<ITerm>();
                for (String attribute : getAttributes()) {
                    int termId = resultSet.getInt(attribute);
                    termIds.add(termId);
                    ITerm term = universe.getTerm(termId);
                    if (term != null) {
                        terms.add(term);
                    } else {
                        return null;
                    }
                }
                return Factory.BASIC.createTuple(terms);
            }
        } catch (SQLException e) {
            logger.error("Failed to execute query " + insertStatement, e);
        } finally {
            RdbUtils.closeResultSet(resultSet);
        }
        return null;
    }

    private void createGetTupleStatement() throws SQLException {
        if (getTupleStatement == null) {
            String sqlFormat = "SELECT * FROM " + "(SELECT *, ROWNUM AS num FROM %s) WHERE num = ?";
            String sql = String.format(sqlFormat, getTableName());
            Connection connection = getConnection();
            try {
                getTupleStatement = connection.prepareStatement(sql);
            } catch (SQLException e) {
                logger.error("Failed to prepare statement for query " + sql, e);
                throw e;
            }
        }
    }

    @Override
    public boolean contains(ITuple tuple) {
        if (tuple.size() != getArity()) {
            return false;
        }
        ResultSet resultSet = null;
        try {
            createContainsStatement();
            for (int i = 0; i < tuple.size(); i++) {
                ITerm term = tuple.get(i);
                int termId = universe.getId(term);
                if (termId == -1) {
                    return false;
                }
                containsStatement.setInt(i + 1, termId);
            }
            resultSet = containsStatement.executeQuery();
            logger.debug("Executing " + containsStatement);
            if (resultSet.next()) {
                int size = resultSet.getInt("size");
                return size > 0;
            }
        } catch (SQLException e) {
            logger.error("Failed to execute query", e);
        } finally {
            RdbUtils.closeResultSet(resultSet);
        }
        return false;
    }

    private void createContainsStatement() throws SQLException {
        if (containsStatement == null) {
            List<String> attributes = getAttributes();
            StringBuilder whereClause = new StringBuilder();
            for (int i = 0; i < attributes.size(); i++) {
                String attribute = attributes.get(i);
                if (i > 0) {
                    whereClause.append(" AND ");
                }
                whereClause.append(attribute + " = ?");
            }
            String sqlFormat = "SELECT COUNT(*) AS size FROM %s WHERE %s";
            String sql = String.format(sqlFormat, getTableName(), whereClause);
            Connection connection = getConnection();
            containsStatement = connection.prepareStatement(sql);
        }
    }

    @Override
    public void drop() {
        close();
    }

    @Override
    public String toString() {
        return RdbUtils.toString(this);
    }

    @Override
    public void close() {
        RdbUtils.closeStatement(getTupleStatement);
        getTupleStatement = null;
        RdbUtils.closeStatement(insertStatement);
        insertStatement = null;
        RdbUtils.closeStatement(sizeStatement);
        sizeStatement = null;
        RdbUtils.closeStatement(containsStatement);
        containsStatement = null;
    }

    @Override
    public CloseableIterator<ITuple> iterator() {
        try {
            return new TupleIterator(getConnection(), this);
        } catch (SQLException e) {
            return null;
        }
    }
}
