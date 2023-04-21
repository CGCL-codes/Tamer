package org.subethamail.core.lists.i;

import java.io.Serializable;
import java.util.List;

/**
 * Results of a fulltext search.
 * 
 * @author Jeff Schnitzer
 */
public class SearchResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /** */
    int total;

    List<SearchHit> hits;

    protected SearchResult() {
    }

    /** */
    public SearchResult(int total, List<SearchHit> hits) {
        this.total = total;
        this.hits = hits;
    }

    /** */
    public List<SearchHit> getHits() {
        return this.hits;
    }

    /** */
    public int getTotal() {
        return this.total;
    }

    /** */
    public String toString() {
        return this.getClass().getName() + " {total=" + this.total + ", hits=" + this.hits + "}";
    }
}
