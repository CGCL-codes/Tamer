package ComparisonEngine;

import de.tud.kom.stringmatching.shinglecloud.ShingleCloud;
import de.tud.kom.stringmatching.shinglecloud.ShingleCloudMatch;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kasun
 */
public class ShingleCloudAlgorithm {

    List<ShingleCloudMatch> shingleCloudMatchlist;

    ArrayList<String> mactchingShingleList = new ArrayList<String>();

    String result = "";

    public float getSimilarity(String input1, String input2) {
        ShingleCloud sc = new ShingleCloud(input1);
        sc.setNGramSize(4);
        sc.setMinimumNumberOfOnesInMatch(1);
        sc.setSortMatchesByRating(false);
        sc.match(input2);
        float containmentInHaystack = 0;
        float containmentInNeedle = 0;
        shingleCloudMatchlist = sc.getMatches();
        for (int i = 0; i < sc.getMatches().size(); i++) {
            ShingleCloudMatch match = sc.getMatches().get(i);
            result = result + match.getMatchedShingles() + "~";
            mactchingShingleList.add(result);
            containmentInHaystack += match.getContainmentInHaystack();
            containmentInNeedle += match.getContainmentInNeedle();
        }
        float plagiarismValue = containmentInHaystack + containmentInNeedle;
        return plagiarismValue;
    }

    public String getList() {
        return this.result;
    }

    public ArrayList<String> GetShingleList() {
        return this.mactchingShingleList;
    }
}
