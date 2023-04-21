package puppy.demo.tagRanking;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import util.string.StringAnalysis;

public class RankMerger implements Runnable {

    private Hashtable<String, Double> scores = null;

    private Iterator<Entry<String, Double>> iter = null;

    public RankMerger(Hashtable<String, Double> scores) {
        this.scores = scores;
    }

    @Override
    public void run() {
        System.out.println("Merging results: " + scores.size());
        iter = util.hashing.Sorting.sortHashNumericValuesDouble(scores, false);
        printRank(iter);
        scores.clear();
        iter = null;
    }

    public void printRank(Iterator<Entry<String, Double>> iter) {
        int MAX = 51;
        int i = 1;
        while (iter.hasNext() && i < MAX) {
            Entry<String, Double> element = iter.next();
            if (!StringAnalysis.isStopWord(element.getKey())) {
                System.out.println(i + "\t" + element.getKey() + "\t" + element.getValue());
                i++;
            }
        }
    }
}
