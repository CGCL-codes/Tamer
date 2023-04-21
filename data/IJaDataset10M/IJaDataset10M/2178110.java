package gate.creole.annic.apache.lucene.search;

import java.io.IOException;
import gate.creole.annic.apache.lucene.index.*;

abstract class PhraseScorer extends Scorer {

    private Weight weight;

    protected byte[] norms;

    protected float value;

    private boolean firstTime = true;

    private boolean more = true;

    protected PhraseQueue pq;

    protected PhrasePositions first, last;

    private float freq;

    PhraseScorer(Weight weight, TermPositions[] tps, java.util.Vector positions, Similarity similarity, byte[] norms, Searcher searcher) throws IOException {
        super(similarity);
        this.norms = norms;
        this.weight = weight;
        this.value = weight.getValue();
        this.searcher = searcher;
        for (int i = 0; i < tps.length; i++) {
            PhrasePositions pp = new PhrasePositions(tps[i], ((Integer) positions.get(i)).intValue());
            if (last != null) {
                last.next = pp;
            } else first = pp;
            last = pp;
        }
        pq = new PhraseQueue(tps.length);
    }

    PhraseScorer(Weight weight, TermPositions[] tps, Similarity similarity, byte[] norms) throws IOException {
        super(similarity);
        this.norms = norms;
        this.weight = weight;
        this.value = weight.getValue();
        for (int i = 0; i < tps.length; i++) {
            PhrasePositions pp = new PhrasePositions(tps[i], i);
            if (last != null) {
                last.next = pp;
            } else first = pp;
            last = pp;
        }
        pq = new PhraseQueue(tps.length);
    }

    public int doc() {
        return first.doc;
    }

    public boolean next(Searcher searcher) throws IOException {
        this.searcher = searcher;
        if (firstTime) {
            init();
            firstTime = false;
        } else if (more) {
            more = last.next();
        }
        return doNext();
    }

    private boolean doNext() throws IOException {
        while (more) {
            while (more && first.doc < last.doc) {
                more = first.skipTo(last.doc);
                firstToLast();
            }
            if (more) {
                freq = phraseFreq();
                if (freq == 0.0f) more = last.next(); else return true;
            }
        }
        return false;
    }

    public float score(Searcher searcher) throws IOException {
        this.searcher = searcher;
        float raw = getSimilarity().tf(freq) * value;
        return raw * Similarity.decodeNorm(norms[first.doc]);
    }

    public boolean skipTo(int target) throws IOException {
        for (PhrasePositions pp = first; more && pp != null; pp = pp.next) {
            more = pp.skipTo(target);
        }
        if (more) sort();
        return doNext();
    }

    protected abstract float phraseFreq() throws IOException;

    private void init() throws IOException {
        for (PhrasePositions pp = first; more && pp != null; pp = pp.next) more = pp.next();
        if (more) sort();
    }

    private void sort() {
        pq.clear();
        for (PhrasePositions pp = first; pp != null; pp = pp.next) pq.put(pp);
        pqToList();
    }

    protected final void pqToList() {
        last = first = null;
        while (pq.top() != null) {
            PhrasePositions pp = (PhrasePositions) pq.pop();
            if (last != null) {
                last.next = pp;
            } else first = pp;
            last = pp;
            pp.next = null;
        }
    }

    protected final void firstToLast() {
        last.next = first;
        last = first;
        first = first.next;
        last.next = null;
    }

    public Explanation explain(final int doc) throws IOException {
        Explanation tfExplanation = new Explanation();
        while (next(this.searcher) && doc() < doc) {
        }
        float phraseFreq = (doc() == doc) ? freq : 0.0f;
        tfExplanation.setValue(getSimilarity().tf(phraseFreq));
        tfExplanation.setDescription("tf(phraseFreq=" + phraseFreq + ")");
        return tfExplanation;
    }

    public String toString() {
        return "scorer(" + weight + ")";
    }
}
