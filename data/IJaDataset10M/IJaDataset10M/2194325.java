package org.dbmaintain.script.parser.parsingstate;

/**
 * Defines the contract for implementations that define whether a given start is the
 * start of a pl-sql block (e.g. stored procedure) for a specific SQL dialect.
 *
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public interface PlSqlBlockMatcher {

    /**
     * Returns whether the given string is the start of a pl-sql block definition.
     * Only works if the given string doesn't contain redundant whitespace and if it's
     * only the start of the statement, not containing any more data.
     *
     * @param statementWithoutCommentsOrWhitespace
     *         the start of an SQL statement
     * @return true if the given start of an SQL statement indicates the begin of a
     *         pl-sql block definition
     */
    boolean isStartOfPlSqlBlock(StringBuilder statementWithoutCommentsOrWhitespace);
}
