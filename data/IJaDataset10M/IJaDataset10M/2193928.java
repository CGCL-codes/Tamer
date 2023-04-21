package com.jxva.dao.dialect;

import com.jxva.dao.Dialect;
import com.jxva.dao.DialectException;
import com.jxva.log.Logger;

/**
 * for Derby Dialect
 * @author  The Jxva Framework
 * @since   1.0
 * @version 2008-11-27 10:57:42 by Jxva
 */
public class DerbyDialect implements Dialect {

    private static final long serialVersionUID = 4213805280416974787L;

    private static final Logger log = Logger.getLogger(DerbyDialect.class);

    public DerbyDialect() {
        log.info("Derby Dialect initializing...");
    }

    /**
	 *  refrence: http://db.apache.org/derby/docs/10.4/ref/rreffuncrownumber.html
	 * 	ROW_NUMBER function

		The ROW_NUMBER function returns the row number over a named or unnamed window specification.
		
		The ROW_NUMBER function does not take any arguments, and for each row over the window it returns an ever increasing BIGINT. It is normally used to limit the number of rows returned for a query. The LIMIT keyword used in other databases is not defined in the SQL standard, and is not supported.
		
		    * Derby does not currently allow the named or unnamed window specification to be specified in the OVER() clause, but requires an empty parenthesis. This means the function is evaluated over the entire result set.
		    * The ROW_NUMBER function cannot currently be used in a WHERE clause.
		    * Derby does not currently support ORDER BY in subqueries, so there is currently no way to guarantee the order of rows in the SELECT subquery. An optimizer override can be used to force the optimizer to use an index ordered on the desired column(s) if ordering is a firm requirement.
		
		The data type of the returned value is a BIGINT number.
		Syntax
		
		ROW_NUMBER() OVER ()
		
		Example
		
		To limit the number of rows returned from a query to the 10 first rows of table T, use the following query:
		
		SELECT * FROM (
		   SELECT 
		     ROW_NUMBER() OVER () AS R, 
		     T.* 
		   FROM T
		) AS TR 
		   WHERE R <= 10; 


	 */
    public String getSQLWithRowSetLimit(String sql, int start, int end) throws DialectException {
        return sql;
    }

    public boolean supportsLimit() {
        return false;
    }

    public boolean supportsLimitOffset() {
        return false;
    }

    public static void main(String[] args) throws Exception {
        DerbyDialect s = new DerbyDialect();
        System.out.println(s.getSQLWithRowSetLimit("select * from hr_info where name='4' order by msgid", 10, 15));
    }

    public boolean supportsGetGeneratedKeys() {
        return true;
    }
}
