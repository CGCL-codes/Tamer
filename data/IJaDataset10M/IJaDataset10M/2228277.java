package net.sf.clairv.search.searcher;

import java.io.IOException;
import net.sf.clairv.index.document.lucene.LuceneDocument;
import net.sf.clairv.search.pattern.ResultPattern;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;

/**
 * A high-level wrapper of the <tt>Hits</tt> object, whose main purpose is to
 * show the results in String form. Generally it is not neccessary to access the
 * fields within the <tt>Hits</tt> object.
 * 
 * @author qiuyin
 * 
 */
public class LocalLuceneQueryResults implements QueryResults {

    private static final long serialVersionUID = -9213951887108270594L;

    protected Query query;

    protected Hits hits;

    protected ResultPattern pattern;

    public LocalLuceneQueryResults(Query query, Hits hits, ResultPattern pattern) {
        this.query = query;
        this.hits = hits;
    }

    public int length() {
        return hits.length();
    }

    public Hit getHit(int index) {
        try {
            Document doc = hits.doc(index);
            return new DefaultHit(new LuceneDocument(doc), pattern.format(query, doc), hits.score(index));
        } catch (IOException e) {
            return null;
        }
    }
}
