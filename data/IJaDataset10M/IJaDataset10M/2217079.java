package com.completex.objective.components.persistency;

/**
 * @author Gennady Krizhevsky
 */
public interface UpdateQueryFactory {

    UpdateQuery newQuery();

    UpdateQuery newQuery(String sql);

    /**
     * Creates new update query using examplePoForSetClause persistent object as an example. All set fields will be used
     * in SET clause of the query.
     *
     * @param examplePoForSetClause
     * @return new Update Query
     */
    UpdateQuery newQueryByExample(PersistentObject examplePoForSetClause);

    /**
     * Creates new update query using examplePoForSetClause and examplePoForWhereClause persistent objects as examples.
     * Fields set in examplePoForSetClause will be used in SET clause of the query.
     * Fields set in examplePoForWhereClause will be used in WHERE clause of the query.
     *
     * @param examplePoForSetClause
     * @param examplePoForWhereClause
     * @return new Update Query
     */
    UpdateQuery newQueryByExample(PersistentObject examplePoForSetClause, PersistentObject examplePoForWhereClause);
}
