package uk.ac.shef.wit.simmetrics.similaritymetrics.costfunctions;

import java.io.Serializable;

/**
 * Package: costfunctions Description: SubCost1_Minus2 implements a substitution
 * cost function where d(i,j) = 1 if i does not equal j, -2 if i equals j.
 * 
 * Date: 24-Mar-2004 Time: 13:38:12
 * 
 * @author Sam Chapman <a href="http://www.dcs.shef.ac.uk/~sam/">Website</a>, <a
 *         href="mailto:sam@dcs.shef.ac.uk">Email</a>.
 * @version 1.1
 */
public final class SubCost1_Minus2 extends AbstractSubstitutionCost implements Serializable {

    /**
	 * returns the name of the cost function.
	 * 
	 * @return the name of the cost function
	 */
    public final String getShortDescriptionString() {
        return "SubCost1_Minus2";
    }

    /**
	 * get cost between characters where d(i,j) = 1 if i does not equal j, -2 if
	 * i equals j.
	 * 
	 * @param str1
	 *            - the string1 to evaluate the cost
	 * @param string1Index
	 *            - the index within the string1 to test
	 * @param str2
	 *            - the string2 to evaluate the cost
	 * @param string2Index
	 *            - the index within the string2 to test
	 * @return the cost of a given subsitution d(i,j) where d(i,j) = 1 if i!=j,
	 *         -2 if i==j
	 */
    public final float getCost(final String str1, final int string1Index, final String str2, final int string2Index) {
        if (str1.length() <= string1Index || string1Index < 0) {
            return 0;
        }
        if (str2.length() <= string2Index || string2Index < 0) {
            return 0;
        }
        if (str1.charAt(string1Index) == str2.charAt(string2Index)) {
            return 1.0f;
        } else {
            return -2.0f;
        }
    }

    /**
	 * returns the maximum possible cost.
	 * 
	 * @return the maximum possible cost
	 */
    public final float getMaxCost() {
        return 1.0f;
    }

    /**
	 * returns the minimum possible cost.
	 * 
	 * @return the minimum possible cost
	 */
    public final float getMinCost() {
        return -2.0f;
    }
}
