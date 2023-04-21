package org.apache.solr.search;

import org.apache.lucene.search.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;

class LuceneQueryOptimizer {

    private LinkedHashMap cache;

    private float threshold;

    /** Construct an optimizer that caches and uses filters for required {@link
   * TermQuery}s whose boost is zero.
   * @param cacheSize the number of QueryFilters to cache
   * @param threshold the fraction of documents which must contain term
   */
    public LuceneQueryOptimizer(final int cacheSize, float threshold) {
        this.cache = new LinkedHashMap(cacheSize, 0.75f, true) {

            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > cacheSize;
            }
        };
        this.threshold = threshold;
    }

    public TopDocs optimize(BooleanQuery original, Searcher searcher, int numHits, Query[] queryOut, Filter[] filterOut) throws IOException {
        BooleanQuery query = new BooleanQuery();
        BooleanQuery filterQuery = null;
        for (BooleanClause c : (List<BooleanClause>) original.clauses()) {
            Query q = c.getQuery();
            if (c.isRequired() && q.getBoost() == 0.0f && q instanceof TermQuery && (searcher.docFreq(((TermQuery) q).getTerm()) / (float) searcher.maxDoc()) >= threshold) {
                if (filterQuery == null) filterQuery = new BooleanQuery();
                filterQuery.add(q, BooleanClause.Occur.MUST);
            } else {
                query.add(c);
            }
        }
        Filter filter = null;
        if (filterQuery != null) {
            synchronized (cache) {
                filter = (Filter) cache.get(filterQuery);
            }
            if (filter == null) {
                filter = new CachingWrapperFilter(new QueryWrapperFilter(filterQuery));
                synchronized (cache) {
                    cache.put(filterQuery, filter);
                }
            }
        }
        if (queryOut != null && filterOut != null) {
            queryOut[0] = query;
            filterOut[0] = filter;
            return null;
        } else {
            return searcher.search(query, filter, numHits);
        }
    }
}
