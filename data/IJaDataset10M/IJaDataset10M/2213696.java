package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.Term;
import org.apache.lucene.document.Document;

/** An abstract base class for search implementations.
 * Implements the main search methods.
 * 
 * <p>Note that you can only access Hits from a Searcher as long as it is
 * not yet closed, otherwise an IOException will be thrown. 
 */
public abstract class Searcher implements Searchable {

    /** Returns the documents matching <code>query</code>. 
   * @throws BooleanQuery.TooManyClauses
   */
    public final Hits search(Query query) throws IOException {
        return search(query, (Filter) null);
    }

    /** Returns the documents matching <code>query</code> and
   * <code>filter</code>.
   * @throws BooleanQuery.TooManyClauses
   */
    public Hits search(Query query, Filter filter) throws IOException {
        return new Hits(this, query, filter);
    }

    /** Returns documents matching <code>query</code> sorted by
   * <code>sort</code>.
   * @throws BooleanQuery.TooManyClauses
   */
    public Hits search(Query query, Sort sort) throws IOException {
        return new Hits(this, query, null, sort);
    }

    /** Returns documents matching <code>query</code> and <code>filter</code>,
   * sorted by <code>sort</code>.
   * @throws BooleanQuery.TooManyClauses
   */
    public Hits search(Query query, Filter filter, Sort sort) throws IOException {
        return new Hits(this, query, filter, sort);
    }

    /** Expert: Low-level search implementation with arbitrary sorting.  Finds
   * the top <code>n</code> hits for <code>query</code>, applying
   * <code>filter</code> if non-null, and sorting the hits by the criteria in
   * <code>sort</code>.
   *
   * <p>Applications should usually call {@link
   * Searcher#search(Query,Filter,Sort)} instead.
   * @throws BooleanQuery.TooManyClauses
   */
    public TopFieldDocs search(Query query, Filter filter, int n, Sort sort) throws IOException {
        return search(createWeight(query), filter, n, sort);
    }

    /** Lower-level search API.
   *
   * <p>{@link HitCollector#collect(int,float)} is called for every non-zero
   * scoring document.
   *
   * <p>Applications should only use this if they need <i>all</i> of the
   * matching documents.  The high-level search API ({@link
   * Searcher#search(Query)}) is usually more efficient, as it skips
   * non-high-scoring hits.
   * <p>Note: The <code>score</code> passed to this method is a raw score.
   * In other words, the score will not necessarily be a float whose value is
   * between 0 and 1.
   * @throws BooleanQuery.TooManyClauses
   */
    public void search(Query query, HitCollector results) throws IOException {
        search(query, (Filter) null, results);
    }

    /** Lower-level search API.
   *
   * <p>{@link HitCollector#collect(int,float)} is called for every non-zero
   * scoring document.
   * <br>HitCollector-based access to remote indexes is discouraged.
   *
   * <p>Applications should only use this if they need <i>all</i> of the
   * matching documents.  The high-level search API ({@link
   * Searcher#search(Query)}) is usually more efficient, as it skips
   * non-high-scoring hits.
   *
   * @param query to match documents
   * @param filter if non-null, a bitset used to eliminate some documents
   * @param results to receive hits
   * @throws BooleanQuery.TooManyClauses
   */
    public void search(Query query, Filter filter, HitCollector results) throws IOException {
        search(createWeight(query), filter, results);
    }

    /** Expert: Low-level search implementation.  Finds the top <code>n</code>
   * hits for <code>query</code>, applying <code>filter</code> if non-null.
   *
   * <p>Called by {@link Hits}.
   *
   * <p>Applications should usually call {@link Searcher#search(Query)} or
   * {@link Searcher#search(Query,Filter)} instead.
   * @throws BooleanQuery.TooManyClauses
   */
    public TopDocs search(Query query, Filter filter, int n) throws IOException {
        return search(createWeight(query), filter, n);
    }

    /** Returns an Explanation that describes how <code>doc</code> scored against
   * <code>query</code>.
   *
   * <p>This is intended to be used in developing Similarity implementations,
   * and, for good performance, should not be displayed with every hit.
   * Computing an explanation is as expensive as executing the query over the
   * entire index.
   */
    public Explanation explain(Query query, int doc) throws IOException {
        return explain(createWeight(query), doc);
    }

    /** The Similarity implementation used by this searcher. */
    private Similarity similarity = Similarity.getDefault();

    /** Expert: Set the Similarity implementation used by this Searcher.
   *
   * @see Similarity#setDefault(Similarity)
   */
    public void setSimilarity(Similarity similarity) {
        this.similarity = similarity;
    }

    /** Expert: Return the Similarity implementation used by this Searcher.
   *
   * <p>This defaults to the current value of {@link Similarity#getDefault()}.
   */
    public Similarity getSimilarity() {
        return this.similarity;
    }

    /**
   * creates a weight for <code>query</code>
   * @return new weight
   */
    protected Weight createWeight(Query query) throws IOException {
        return query.weight(this);
    }

    public int[] docFreqs(Term[] terms) throws IOException {
        int[] result = new int[terms.length];
        for (int i = 0; i < terms.length; i++) {
            result[i] = docFreq(terms[i]);
        }
        return result;
    }

    public abstract void search(Weight weight, Filter filter, HitCollector results) throws IOException;

    public abstract void close() throws IOException;

    public abstract int docFreq(Term term) throws IOException;

    public abstract int maxDoc() throws IOException;

    public abstract TopDocs search(Weight weight, Filter filter, int n) throws IOException;

    public abstract Document doc(int i) throws IOException;

    public abstract Query rewrite(Query query) throws IOException;

    public abstract Explanation explain(Weight weight, int doc) throws IOException;

    public abstract TopFieldDocs search(Weight weight, Filter filter, int n, Sort sort) throws IOException;
}
