package org.apache.lucene.search;

import java.io.IOException;

/**
 * A {@link Scorer} which wraps another scorer and caches the score of the
 * current document. Successive calls to {@link #score()} will return the same
 * result and will not invoke the wrapped Scorer's score() method, unless the
 * current document has changed.<br>
 * This class might be useful due to the changes done to the {@link Collector}
 * interface, in which the score is not computed for a document by default, only
 * if the collector requests it. Some collectors may need to use the score in
 * several places, however all they have in hand is a {@link Scorer} object, and
 * might end up computing the score of a document more than once.
 */
public class ScoreCachingWrappingScorer extends Scorer {

    private Scorer scorer;

    private int curDoc = -1;

    private float curScore;

    /** Creates a new instance by wrapping the given scorer. */
    public ScoreCachingWrappingScorer(Scorer scorer) {
        super(scorer.getSimilarity());
        this.scorer = scorer;
    }

    @Override
    protected boolean score(Collector collector, int max, int firstDocID) throws IOException {
        return scorer.score(collector, max, firstDocID);
    }

    @Override
    public Similarity getSimilarity() {
        return scorer.getSimilarity();
    }

    @Override
    public float score() throws IOException {
        int doc = scorer.docID();
        if (doc != curDoc) {
            curScore = scorer.score();
            curDoc = doc;
        }
        return curScore;
    }

    @Override
    public int docID() {
        return scorer.docID();
    }

    @Override
    public int nextDoc() throws IOException {
        return scorer.nextDoc();
    }

    @Override
    public void score(Collector collector) throws IOException {
        scorer.score(collector);
    }

    @Override
    public int advance(int target) throws IOException {
        return scorer.advance(target);
    }
}
