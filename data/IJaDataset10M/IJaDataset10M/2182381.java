package uk.org.ogsadai.converters.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A ResultSet handler that uses strategy objects to handle the SQL
 * column types occurring within a ResultSet. The handling of other
 * aspects of a ResultSet is deferred to a sub-class.
 * </p><p>
 * The class is constructed using a <code>ResultSet</code> object and
 * a <code>ColumnStrategyFactory</code> object. During this process,
 * the ResultSet meta-data is analysed to determine which SQL column
 * types are present. The factory is then used to create an
 * appropriate <code>ColumnStrategy</code> objects for each column,
 * and these strategies are used for processing the ResultSet data
 * during the conversion process. 
 * </p><p>
 * This class makes use of the <em>strategy</em> and <em>abstract
 * factory</em> design patterns.
 * 
 * @author The OGSA-DAI Project Team
 *
 * @see ColumnStrategy
 * @see ColumnStrategyFactory
 */
public abstract class StrategicResultSetHandler implements ResultSetHandler {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh,  2002 - 2007.";

    /** Column strategies for each column. */
    private final ColumnStrategy[] mStrategies;

    /**
     * Creates a new result set handler that will use strategies for handling
     * the result set fields.
     * 
     * @param rs
     *     ResultSet to handle
     * @param factory
     *     Factory to use for creating column strategies.
     * @throws ResultSetHandlerException
     *     If a problem occurs when accessing the ResultSet.
     */
    public StrategicResultSetHandler(final ResultSet rs, final ColumnStrategyFactory factory) throws ResultSetHandlerException {
        try {
            mStrategies = new ColumnStrategy[rs.getMetaData().getColumnCount()];
            for (int i = 0; i < mStrategies.length; i++) {
                int type = rs.getMetaData().getColumnType(i + 1);
                mStrategies[i] = factory.createColumnStrategy(type);
            }
        } catch (SQLException e) {
            throw new ResultSetHandlerException(e);
        }
    }

    public final void field(final StringBuffer output, final ResultSet resultSet, final int column) throws ResultSetHandlerException {
        try {
            mStrategies[column - 1].convertField(output, resultSet, column);
        } catch (Exception e) {
            throw new ResultSetHandlerException(new ResultSetHandlerColumnException(column, e));
        }
    }
}
