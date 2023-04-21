package uk.ac.shef.wit.simmetrics.similaritymetrics;

import uk.ac.shef.wit.simmetrics.tokenisers.InterfaceTokeniser;
import uk.ac.shef.wit.simmetrics.tokenisers.TokeniserWhitespace;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Package: uk.ac.shef.wit.simmetrics.similaritymetrics.matchingcoefficient
 * Description: uk.ac.shef.wit.simmetrics.similaritymetrics.matchingcoefficient
 * implements a
 * 
 * Date: 02-Apr-2004 Time: 14:31:40
 * 
 * @author Sam Chapman <a href="http://www.dcs.shef.ac.uk/~sam/">Website</a>, <a
 *         href="mailto:sam@dcs.shef.ac.uk">Email</a>.
 * @version 1.1
 */
public final class MatchingCoefficient extends AbstractStringMetric implements Serializable {

    /**
	 * a constant for calculating the estimated timing cost.
	 */
    private final float ESTIMATEDTIMINGCONST = 2.0e-4f;

    /**
	 * private tokeniser for tokenisation of the query strings.
	 */
    private final InterfaceTokeniser tokeniser;

    /**
	 * constructor - default (empty).
	 */
    public MatchingCoefficient() {
        tokeniser = new TokeniserWhitespace();
    }

    /**
	 * constructor.
	 * 
	 * @param tokeniserToUse
	 *            - the tokeniser to use should a different tokeniser be
	 *            required
	 */
    public MatchingCoefficient(final InterfaceTokeniser tokeniserToUse) {
        tokeniser = tokeniserToUse;
    }

    /**
	 * returns the string identifier for the metric .
	 * 
	 * @return the string identifier for the metric
	 */
    public String getShortDescriptionString() {
        return "MatchingCoefficient";
    }

    /**
	 * returns the long string identifier for the metric.
	 * 
	 * @return the long string identifier for the metric
	 */
    public String getLongDescriptionString() {
        return "Implements the Matching Coefficient algorithm providing a similarity measure between two strings";
    }

    /**
	 * gets a div class xhtml similarity explaining the operation of the metric.
	 * 
	 * @param string1
	 *            string 1
	 * @param string2
	 *            string 2
	 * 
	 * @return a div class html section detailing the metric operation.
	 */
    public String getSimilarityExplained(String string1, String string2) {
        return null;
    }

    /**
	 * gets the estimated time in milliseconds it takes to perform a similarity
	 * timing.
	 * 
	 * @param string1
	 *            string 1
	 * @param string2
	 *            string 2
	 * 
	 * @return the estimated time in milliseconds taken to perform the
	 *         similarity measure
	 */
    public float getSimilarityTimingEstimated(final String string1, final String string2) {
        final float str1Tokens = tokeniser.tokenizeToArrayList(string1).size();
        final float str2Tokens = tokeniser.tokenizeToArrayList(string2).size();
        return (str2Tokens * str1Tokens) * ESTIMATEDTIMINGCONST;
    }

    /**
	 * gets the similarity of the two strings using MatchingCoefficient.
	 * 
	 * @param string1
	 * @param string2
	 * @return a value between 0-1 of the similarity
	 */
    public float getSimilarity(final String string1, final String string2) {
        final ArrayList<String> str1Tokens = tokeniser.tokenizeToArrayList(string1);
        final ArrayList<String> str2Tokens = tokeniser.tokenizeToArrayList(string2);
        final int totalPossible = Math.max(str1Tokens.size(), str2Tokens.size());
        return getUnNormalisedSimilarity(string1, string2) / (float) totalPossible;
    }

    /**
	 * gets the un-normalised similarity measure of the metric for the given
	 * strings.
	 * 
	 * @param string1
	 * @param string2
	 * @return returns the score of the similarity measure (un-normalised)
	 */
    public float getUnNormalisedSimilarity(String string1, String string2) {
        final ArrayList<String> str1Tokens = tokeniser.tokenizeToArrayList(string1);
        final ArrayList<String> str2Tokens = tokeniser.tokenizeToArrayList(string2);
        int totalFound = 0;
        for (Object str1Token : str1Tokens) {
            final String sToken = (String) str1Token;
            boolean found = false;
            for (Object str2Token : str2Tokens) {
                final String tToken = (String) str2Token;
                if (sToken.equals(tToken)) {
                    found = true;
                }
            }
            if (found) {
                totalFound++;
            }
        }
        return (float) totalFound;
    }
}
