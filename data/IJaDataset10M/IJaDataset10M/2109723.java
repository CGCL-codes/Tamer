package org.pwr.odwa.server.engine;

import java.util.ArrayList;

/**
 * Interface which keeps creating database query abstract 
 *
 * @author Maciek Kupczak macku30@gmail.com
 * @author Mateusz Lis mateusz.lis@gmail.com
 */
public interface SQLQuery {

    /**
	 * Add result field to the query
	 * 
	 * @param field
	 *            name of field, which will be added
	 */
    public void addResField(String field);

    /**
	 * Adds FIRST element to the from clause (if it is not the first one, it
	 * will be ignored, just because you have to use version with join)
	 * 
	 * @param fromField
	 *            field that is going to be addded
	 */
    public void addToFromClause(String fromField);

    /**
	 * Adds element to from clause. If it is first one, join operator will be
	 * ignored.
	 * 
	 * @param fromField
	 *            field that is going to be added
	 * @param op
	 *            Join operator (for more than one field
	 * @param on1
	 *            first field that is going to be joined on
	 * @param on2
	 *            as the above
	 */
    public void addToFromClause(String fromField, SQLJoinOperator op, String on1, String on2);

    /**
	 * Adds WHERE clause to the query. If clause already exists, it will replace
	 * it.
	 * 
	 * @param clause
	 *            (MySQLWhereClause, otherwise will throw ClassCastException)
	 * 
	 */
    public void addToWhereClause(WhereClause clause);

    /**
	 * Add measure result field to the query
	 * 
	 * @param field
	 *            name of measure, which will be added
	 */
    public void addMeasureResField(String field);

    /**
	 * Adds element to GROUP BY clause in the query
	 * 
	 * @param clause
	 *            element
	 */
    public void addToGroupByClause(String clause);

    /**Returns SQL query string
	 * 
	 * @return
	 */
    public String getQuery();
}
