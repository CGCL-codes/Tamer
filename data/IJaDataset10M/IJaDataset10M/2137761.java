package nuts.core.orm.restriction;

import java.util.Collection;

/**
 */
public interface Restrictions {

    /**
	 * AND = "AND";
	 */
    public static final String AND = "AND";

    /**
	 * OR = "OR";
	 */
    public static final String OR = "OR";

    /**
	 * @return conjunction
	 */
    String getConjunction();

    /**
	 * @param conjunction the conjunction to set
	 */
    void setConjunction(String conjunction);

    /**
	 * setConjunctionToAnd
	 */
    void setConjunctionToAnd();

    /**
	 * setConjunctionToOr
	 */
    void setConjunctionToOr();

    /**
	 * add AND expression 
	 * @return this
	 */
    Restrictions and();

    /**
	 * add OR expression 
	 * @return this
	 */
    Restrictions or();

    /**
	 * add open paren ( 
	 * @return this
	 */
    Restrictions open();

    /**
	 * add close paren ) 
	 * @return this
	 */
    Restrictions close();

    /**
	 * add "column IS NULL" expression
	 * @param column column 
	 * @return this
	 */
    Restrictions isNull(String column);

    /**
	 * add "column IS NOT NULL" expression 
	 * @param column column 
	 * @return this
	 */
    Restrictions isNotNull(String column);

    /**
	 * add "column = value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
    Restrictions equalTo(String column, Object value);

    /**
	 * add "column &lt;&gt; value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
    Restrictions notEqualTo(String column, Object value);

    /**
	 * add "column &gt; value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
    Restrictions greaterThan(String column, Object value);

    /**
	 * add "column %gt;= value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
    Restrictions greaterThanOrEqualTo(String column, Object value);

    /**
	 * add "column &lt; value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
    Restrictions lessThan(String column, Object value);

    /**
	 * add "column &lt;= value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
    Restrictions lessThanOrEqualTo(String column, Object value);

    /**
	 * add "column LIKE value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
    Restrictions like(String column, Object value);

    /**
	 * add "column LIKE %value%" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
    Restrictions match(String column, Object value);

    /**
	 * add "column LIKE value%" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
    Restrictions leftMatch(String column, Object value);

    /**
	 * add "column LIKE %value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
    Restrictions rightMatch(String column, Object value);

    /**
	 * add "column NOT LIKE value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
    Restrictions notLike(String column, Object value);

    /**
	 * add "column = compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
    Restrictions equalToColumn(String column, String compareColumn);

    /**
	 * add "column &lt;&gt; compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
    Restrictions notEqualToColumn(String column, String compareColumn);

    /**
	 * add "column %gt; compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
    Restrictions greaterThanColumn(String column, String compareColumn);

    /**
	 * add "column &gt;= compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
    Restrictions greaterThanOrEqualToColumn(String column, String compareColumn);

    /**
	 * add "column &lt; compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
    Restrictions lessThanColumn(String column, String compareColumn);

    /**
	 * add "column %lt;= compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
    Restrictions lessThanOrEqualToColumn(String column, String compareColumn);

    /**
	 * add "column IN (value1, value2 ...)" expression
	 * @param column column 
	 * @param values values
	 * @return this
	 */
    Restrictions in(String column, Object[] values);

    /**
	 * add "column IN (value1, value2 ...)" expression
	 * @param column column 
	 * @param values values
	 * @return this
	 */
    Restrictions in(String column, Collection values);

    /**
	 * add "column NOT IN (value1, value2 ...)" expression
	 * @param column column 
	 * @param values values
	 * @return this
	 */
    Restrictions notIn(String column, Object[] values);

    /**
	 * add "column NOT IN (value1, value2 ...)" expression
	 * @param column column 
	 * @param values values
	 * @return this
	 */
    Restrictions notIn(String column, Collection values);

    /**
	 * add "column BETWEEN (value1, value2)" expression
	 * @param column column 
	 * @param value1 value from
	 * @param value2 value to
	 * @return this
	 */
    Restrictions between(String column, Object value1, Object value2);

    /**
	 * add "column NOT BETWEEN (value1, value2)" expression
	 * @param column column 
	 * @param value1 value from
	 * @param value2 value to
	 * @return this
	 */
    Restrictions notBetween(String column, Object value1, Object value2);
}
