package uk.ac.shef.wit.simmetrics.similaritymetrics.costfunctions;

import uk.ac.shef.wit.simmetrics.similaritymetrics.costfunctions.AbstractAffineGapCost;
import java.io.Serializable;

/**
 * Package: costfunctions Description: AffineGap1_1Over3 implements a Affine Gap
 * cost function.
 * 
 * Date: 29-Mar-2004 Time: 17:03:22
 * 
 * @author Sam Chapman <a href="http://www.dcs.shef.ac.uk/~sam/">Website</a>, <a
 *         href="mailto:sam@dcs.shef.ac.uk">Email</a>.
 * @version 1.1
 */
public final class AffineGap1_1Over3 extends AbstractAffineGapCost implements Serializable {

    /**
	 * returns the name of the cost function.
	 * 
	 * @return the name of the cost function
	 */
    public final String getShortDescriptionString() {
        return "SubCost01";
    }

    /**
	 * get cost between characters.
	 * 
	 * @param stringToGap
	 *            - the string to get the cost of a gap
	 * @param stringIndexStartGap
	 *            - the index within the string to test a start gap from
	 * @param stringIndexEndGap
	 *            - the index within the string to test a end gap to
	 * @return the cost of a Gap G
	 */
    public final float getCost(final String stringToGap, final int stringIndexStartGap, final int stringIndexEndGap) {
        if (stringIndexStartGap >= stringIndexEndGap) {
            return 0.0f;
        } else {
            return 1.0f + (((stringIndexEndGap - 1) - stringIndexStartGap) * (1.0f / 3.0f));
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
        return 0.0f;
    }
}
