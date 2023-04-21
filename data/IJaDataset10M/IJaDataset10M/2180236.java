package com.angel.dao.generic.query.clauses;

/**
 * @author William.
 * @since 16/September/2009.
 *
 */
public interface GroupByClause extends QueryClause {

    public boolean hasQueryGroupsParams();

    public GroupByClause add(String alias, String property);

    public GroupByClause add(String property);
}
