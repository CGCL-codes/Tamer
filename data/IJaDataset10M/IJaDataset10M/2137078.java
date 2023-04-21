package net.sourceforge.javamatch.engine;

/**
 * Class ResultItem contains the match result of a single object and its match 
 * value
 */
public class ResultItem implements Comparable {

    /** The match value of this ResultItem */
    public float matchValue;

    /** The matched object */
    public Object matchedObject;

    /**
	 * Creates a new, empty, ResultItem
	 */
    public ResultItem() {
    }

    /**
	 * Creates a new ResultItem with the given matched object and match value
 	 * @param matchedObject the object that was matched
	 * @param matchValue the return value of the matching of the matched object

	 */
    public ResultItem(Object matchedObject, float matchValue) {
        setMatchValue(matchValue);
        setMatchedObject(matchedObject);
    }

    /**
     * Sets the match value of this ResultItem
	 * @param matchValue the return value of the matching of the matched object
     */
    public void setMatchValue(float matchValue) {
        this.matchValue = matchValue;
    }

    /**
     * Returns the match value of this ResultItem
	 * @return the return value of the matching of the matched object
     */
    public float getMatchValue() {
        return matchValue;
    }

    /**
     * Sets the matched object
	 * @param matchedObject the object that was matched
     */
    public void setMatchedObject(Object matchedObject) {
        this.matchedObject = matchedObject;
    }

    /**
     * Returns the matched object
	 * @return the object that was matched
     */
    public Object getMatchedObject() {
        return matchedObject;
    }

    /**
     * Compares this result item with the given other item
	 * @param otherObject the other object, agains which this object is compared
     */
    public int compareTo(Object otherObject) {
        ResultItem otherItem = (ResultItem) otherObject;
        if (matchValue < otherItem.matchValue) {
            return 1;
        }
        if (matchValue > otherItem.matchValue) {
            return -1;
        }
        return 0;
    }
}
