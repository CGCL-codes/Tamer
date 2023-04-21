package uk.ac.shef.wit.text.gazetteer.simmetric;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import java.util.*;
import java.io.IOException;

/**
 * <p>
 * This class suggests similar strings given a user input and a pool of candidate strings using String Similarity Metrics,
 * http://www.dcs.shef.ac.uk/~sam/stringmetrics.html. It doesn't apply a threshold, instead, it applies Jonathan Butter's
 * noise removal algorithm to eleminate noisy suggestions and returns the filtered output. A typical usage scenario is search engine
 * "more-like-this" keyword suggestion.
 * </p>
 *
 * @author: Jonathan Butters
 * @author: Ziqi Zhang
 * @Organisation: University of Sheffield
 */
public class SuggestionPoolJDB extends SuggestionPool {

    private double cutoff;

    /**
     * Constructs an instance, which uses Levenshtein similarity algorithm to compare two strings, and cuts off
     * all noise detected.
     * @param pool list of reference strings
     */
    public SuggestionPoolJDB(final Set<String> pool) {
        this(new Levenshtein(), 1.0, pool);
    }

    /**
     * Constructs an instance
     * @param metric the similarity metric to apply
     * @param cutoffPercentage percentage of noise to cut.
     * @param pool list of reference strings
     */
    public SuggestionPoolJDB(final AbstractStringMetric metric, final double cutoffPercentage, final Set<String> pool) {
        this.simMetric = metric;
        cutoff = cutoffPercentage;
        this.pool = pool;
    }

    @Override
    public List<SimilarString> selectSimilar(final String in) {
        List<SimilarString> sPool = getSimilarity(pool, in);
        return removeNoise(sPool, calcJDBCutoff(sPool, cutoff));
    }

    private List<SimilarString> removeNoise(final List<SimilarString> pool, final double cutoff) {
        Iterator<SimilarString> it = pool.iterator();
        while (it.hasNext()) {
            if (it.next().getSimilarity() < cutoff) it.remove();
        }
        Collections.sort(pool);
        return pool;
    }

    private List<SimilarString> getSimilarity(final Set<String> pool, final String search) {
        List<SimilarString> results = new ArrayList<SimilarString>();
        for (String c : pool) results.add(new SimilarString(c, simMetric.getSimilarity(search.toLowerCase(), c.toLowerCase())));
        return results;
    }

    private double calcJDBCutoff(final List<SimilarString> pool, final double percentage) {
        double XminusXbar;
        double SumXminusXbar = 0;
        double sigma = 4;
        if (percentage > 0.98) {
            double number = (percentage - 0.5);
            sigma = (100 * number) - 46;
        }
        if (percentage < 0.98 && percentage > 0.50) {
            double number = (percentage - 0.5);
            sigma = (-1) * (Math.sqrt((4.84 - (10 * number))) - 2.2);
        }
        if (percentage < 0.50 && percentage > 0.02) {
            double number = ((0.5 - percentage));
            sigma = (Math.sqrt((4.84 - (10 * number))) - 2.2);
        }
        if (percentage < 0.02) {
            sigma = -4;
        }
        double summation = 0;
        for (SimilarString ss : pool) summation = summation + ss.getSimilarity();
        double mean = summation / pool.size();
        for (SimilarString ss : pool) {
            double coefficient = ss.getSimilarity();
            XminusXbar = (coefficient - mean);
            XminusXbar = XminusXbar * XminusXbar;
            SumXminusXbar = SumXminusXbar + XminusXbar;
        }
        double SD = (SumXminusXbar / pool.size());
        SD = (float) Math.sqrt(SD);
        return (sigma * SD) + mean;
    }
}
