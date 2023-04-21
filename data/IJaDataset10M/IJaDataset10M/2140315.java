package org.apache.lucene.search.hits;

import java.io.IOException;
import java.util.Collection;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollectorSource;
import org.apache.lucene.search.NoMatchDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.ObjectResults.ObjectItem;

/** An extensible list of objects, used to hold any search results
 *  
 * @author  Jos� Luis Oramas Mart�n (Sadiel)
 * @since   lucene 2.0
 */
public class ObjectHits extends CountedHits {

    public static Object singleHitOp(Searcher searcher, HitCollectorSource factory) throws IOException {
        ObjectHits result = new ObjectHits(searcher, new NoMatchDocsQuery(), null, factory);
        return (result != null && result.length() > 0 ? (Collection) result.object(0) : null);
    }

    public ObjectHits(Searcher s, Query q, Filter f, HitCollectorSource c) throws IOException {
        super(s, q, f, c);
    }

    public ExtHit getHit(int n) {
        return new ObjectHit(this, n);
    }

    public Object object(int n) throws IOException {
        return ((ObjectItem) hitDoc(n).scoreDoc).getObject();
    }
}
