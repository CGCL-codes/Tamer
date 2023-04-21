package org.gdbms.engine.customQuery;

import org.gdbms.engine.data.DataSource;
import org.gdbms.engine.data.ExecutionException;
import org.gdbms.engine.instruction.Expression;
import org.gdbms.engine.strategies.OperationDataSource;

/**
 * Interface to implement by the custom queries
 *
 * @author Fernando Gonz�lez Cort�s
 */
public interface CustomQuery {

    /**
     * Executes the custom query
     *
     * @param tables tables involved in the query
     * @param values values passed to the query
     *
     * @return DataSource result of the query
     *
     * @throws ExecutionException if the custom query execution fails
     */
    public OperationDataSource evaluate(DataSource[] tables, Expression[] values) throws ExecutionException;

    /**
     * Gets the query name. Must ve a valid SQL identifier (i.e.: '.' is not
     * allowed)
     *
     * @return query name
     */
    public String getName();
}
