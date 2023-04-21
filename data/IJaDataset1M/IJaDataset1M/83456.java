package net.sourceforge.javamatch.query;

import net.sourceforge.javamatch.engine.*;

/**
 * Class NumberEquals is a query that check if a value is equal to the specified 
 * one, when the value is a number
 */
public class NumberEquals extends NumericQuery {

    /** The name of the member of the object that is matched */
    private String memberName;

    /** The value that the instance is compared against */
    private double matchValue;

    /**
	 * Creates a new NumberEquals-query, that checks the given member name for 
	 * the given match value
	 * @param memberName the name of the member that is matched
	 * @param matchValue the value against which is matched
	 */
    public NumberEquals(String memberName, double matchValue) {
        if (memberName == null) {
            throw new NullPointerException("Member name can't be null");
        }
        this.memberName = memberName;
        this.matchValue = matchValue;
    }

    /**
	 * Returns the match value of this match query, when executed on the given 
	 * object. 
	 * @param targetObject the object agains which the query is executed
	 * @return A value between 0 and 1, both inclusive, that indicates how good 
	 *         the object matches this query. 0 means a mismatch, 1 means a full
	 *         match
	 * @throws MatchException when the value could not be retrieved
     */
    public float getMatchValue(Object targetObject) throws MatchException {
        double objectValue = getValue(targetObject, memberName);
        if (Math.abs(objectValue - matchValue) < 0.0001) {
            return 1;
        }
        return 0;
    }
}
