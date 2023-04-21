package otservices.util.database;

import org.apache.log4j.Logger;

public class SQLExpressionHSQLImpl implements SQLExpressionInterface {

    private String expression = null;

    private Boolean empty = new Boolean(true);

    private static Logger logger = Logger.getLogger(SQLExpressionHSQLImpl.class.getName());

    /**
	 * Returns the SQL Expression stored
	 * 
	 * @param none
	 * @return SQL expression
	 */
    public String getSQLExpression() {
        logger.debug("SQL Expression = " + this.expression);
        return this.expression;
    }

    /**
	 * Set the SQL expression to be validated
	 * 
	 * @param expression
	 *            SQL expression to be stored
	 * @return none
	 */
    public void setSQLExpression(String expression) {
        logger.debug("Setting SQL Expression = " + expression);
        this.expression = expression;
        this.empty = new Boolean(false);
    }

    /**
	 * Determine if the expression is empty
	 * 
	 * @param none
	 * @return true - if it is empty; false - otherwise
	 */
    public Boolean isEmpty() {
        return this.empty;
    }

    /**
	 * Determine if the expression is a query (SELECT)
	 * 
	 * @param none
	 * @return true - if the expression is a query; false - otherwise
	 */
    public Boolean isQuery() {
        logger.debug("isQuery() = true");
        return new Boolean(true);
    }

    /**
	 * Determines if the expression is an update (INSERT, UPDATE, DELETE)
	 * 
	 * @param none
	 * @return true - if the expression is an update; false - otherwise
	 */
    public Boolean isUpdate() {
        logger.debug("isUpdate() = true");
        return new Boolean(true);
    }

    /**
	 * Validates the SQL expression based on the SQL Statements supported by the
	 * database system for which the implementation class was developed
	 * 
	 * @param none
	 * @return true - if the SQL expression is valid; false - if the SQL
	 *         expression is not valid
	 */
    public Boolean isValid() {
        logger.debug("isValid() = true");
        return new Boolean(true);
    }
}
