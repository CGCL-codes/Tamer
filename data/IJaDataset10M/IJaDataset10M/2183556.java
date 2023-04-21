package mpr.openGPX.lib;

import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Martin Preishuber
 *

?	 - neutral / not rated yet
1	 - poor
1.5  - fairly poor
2	 - below average
2.5	 - not so bad
3	 - average 
3.5	 - not bad at all
4	 - better than average
4.5	 - very good
5	 - awesome

 */
public class GCVote {

    public String userName = "";

    public String cacheId = "";

    public float voteMedian = 0.0f;

    public float voteAverage = 0.0f;

    public int voteCount = 0;

    public int voteUser = 0;

    public String waypoint = "";

    public int vote1 = 0;

    public int vote2 = 0;

    public int vote3 = 0;

    public int vote4 = 0;

    public int vote5 = 0;

    private String mRawVotes = "";

    private static final String RAWVOTEPATTERN = "(\\(\\d\\.\\d\\:\\d+\\))";

    /**
	 * 
	 * @param intVotes
	 * @param intTotal
	 * @return
	 */
    private String getStars(int intVotes, int intTotal) {
        final int intMaxStars = 10;
        String strStars = "";
        for (int i = 0; i < ((float) intVotes / (float) intTotal) * intMaxStars; i++) {
            strStars = strStars.concat("=");
        }
        return strStars;
    }

    /**
	 * 
	 * @return
	 */
    public TreeMap<Float, Integer> getRawVotes() {
        final TreeMap<Float, Integer> rawVotes = new TreeMap<Float, Integer>();
        final Matcher m = Pattern.compile(RAWVOTEPATTERN).matcher(this.mRawVotes);
        while (m.find()) {
            final String pair = m.group().replace("(", "").replace(")", "");
            final String[] splitted = pair.split(":");
            final Float vote = Float.parseFloat(splitted[0]);
            final Integer count = Integer.parseInt(splitted[1]);
            rawVotes.put(vote, count);
        }
        return rawVotes;
    }

    /**
	 * 
	 * @param value
	 */
    public void setRawVotes(String value) {
        this.mRawVotes = value;
    }

    /**
	 * 
	 */
    @Override
    public String toString() {
        StringBuilder sbResult = new StringBuilder();
        String strStars;
        sbResult.append(String.format("Average: %.2f (Median %.2f)\n\n", this.voteAverage, this.voteMedian));
        strStars = this.getStars(this.vote1, this.voteCount);
        sbResult.append(String.format("Vote 1: %s (%d)\n", strStars, this.vote1));
        strStars = this.getStars(this.vote2, this.voteCount);
        sbResult.append(String.format("Vote 2: %s (%d)\n", strStars, this.vote2));
        strStars = this.getStars(this.vote3, this.voteCount);
        sbResult.append(String.format("Vote 3: %s (%d)\n", strStars, this.vote3));
        strStars = this.getStars(this.vote4, this.voteCount);
        sbResult.append(String.format("Vote 4: %s (%d)\n", strStars, this.vote4));
        strStars = this.getStars(this.vote5, this.voteCount);
        sbResult.append(String.format("Vote 5: %s (%d)\n\n", strStars, this.vote5));
        sbResult.append(String.format("Total of %d votes", this.voteCount));
        return sbResult.toString();
    }
}
