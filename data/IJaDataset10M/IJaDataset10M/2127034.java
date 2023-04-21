package net.sf.lucis.core.support;

import java.io.IOException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Scorer;

/**
 * A collector that simply counts the total number of hits and the maximum score.
 * @author Andres Rodriguez
 */
public class CountingCollector extends Collector {

    private float maxScore;

    private int totalHits;

    private Scorer scorer;

    /** Constructor. */
    public CountingCollector() {
        reset();
    }

    /** Resets the collector. */
    public void reset() {
        maxScore = Float.MIN_VALUE;
        totalHits = 0;
        scorer = null;
    }

    /** Returns how many hits matched the search. */
    public int getTotalHits() {
        return totalHits;
    }

    @Override
    public void setScorer(Scorer scorer) {
        this.scorer = scorer;
    }

    @Override
    public void collect(int doc) throws IOException {
        totalHits++;
        if (scorer != null) {
            if (scorer.advance(doc) == DocIdSetIterator.NO_MORE_DOCS) {
                scorer = null;
            } else {
                maxScore = Math.max(maxScore, scorer.score());
            }
        }
    }

    @Override
    public void setNextReader(IndexReader reader, int docBase) {
    }

    @Override
    public boolean acceptsDocsOutOfOrder() {
        return true;
    }

    /**
	 * Returns the maximum collected score.
	 * @return The maximum collected score.
	 */
    public final float getMaxScore() {
        return maxScore;
    }
}
