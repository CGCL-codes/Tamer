package org.unitils.dbmaintainer.sequences;

import org.apache.commons.configuration.Configuration;
import org.unitils.core.UnitilsException;
import org.unitils.dbmaintainer.dbsupport.DatabaseTask;
import org.unitils.dbmaintainer.handler.StatementHandlerException;
import java.sql.SQLException;
import java.util.Set;

/**
 * Implementation of {@link SequenceUpdater}. All sequences and identity columns that have a value lower than the value
 * defined by {@link #PROPKEY_LOWEST_ACCEPTABLE_SEQUENCE_VALUE} are set to this value.
 *
 * @author Filip Neven
 */
public class DefaultSequenceUpdater extends DatabaseTask implements SequenceUpdater {

    public static final String PROPKEY_LOWEST_ACCEPTABLE_SEQUENCE_VALUE = "sequenceUpdater.sequencevalue.lowestacceptable";

    protected long lowestAcceptableSequenceValue;

    /**
     * Initializes the lowest acceptable sequence value using the given configuration object
     *
     * @param configuration
     */
    protected void doInit(Configuration configuration) {
        lowestAcceptableSequenceValue = configuration.getLong(PROPKEY_LOWEST_ACCEPTABLE_SEQUENCE_VALUE);
    }

    /**
     * Updates all database sequences and identity columns to a sufficiently high value, so that test data be inserted
     * easily.
     *
     * @throws StatementHandlerException
     */
    public void updateSequences() throws StatementHandlerException {
        try {
            if (dbSupport.supportsSequences()) {
                incrementSequencesWithLowValue();
            }
            if (dbSupport.supportsIdentityColumns()) {
                incrementIdentityColumnsWithLowValue();
            }
        } catch (SQLException e) {
            throw new UnitilsException("Error while updating sequences", e);
        }
    }

    /**
     * Increments all sequences whose value is too low.
     *
     * @throws SQLException
     * @throws StatementHandlerException
     */
    private void incrementSequencesWithLowValue() throws SQLException, StatementHandlerException {
        Set<String> sequenceNames = dbSupport.getSequenceNames();
        for (String sequenceName : sequenceNames) {
            if (dbSupport.getNextValueOfSequence(sequenceName) < lowestAcceptableSequenceValue) {
                dbSupport.incrementSequenceToValue(sequenceName, lowestAcceptableSequenceValue);
            }
        }
    }

    /**
     * Increments the next value for identity columns whose next value is too low
     * 
     * @throws SQLException
     */
    private void incrementIdentityColumnsWithLowValue() throws SQLException {
        Set<String> tableNames = dbSupport.getTableNames();
        for (String tableName : tableNames) {
            Set<String> primaryKeyColumnNames = dbSupport.getPrimaryKeyColumnNames(tableName);
            if (primaryKeyColumnNames.size() == 1) {
                String primaryKeyColumnName = primaryKeyColumnNames.iterator().next();
                dbSupport.incrementIdentityColumnToValue(tableName, primaryKeyColumnName, lowestAcceptableSequenceValue);
            }
        }
    }
}
