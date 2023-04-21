package org.apache.solr.search;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ConstantScoreQuery;
import java.io.IOException;

/**
 * @version $Id: ConstantScorePrefixQuery.java 741262 2009-02-05 19:41:55Z shalin $
 */
public class ConstantScorePrefixQuery extends Query {

    private final Term prefix;

    public ConstantScorePrefixQuery(Term prefix) {
        this.prefix = prefix;
    }

    /** Returns the prefix  for this query */
    public Term getPrefix() {
        return prefix;
    }

    public Query rewrite(IndexReader reader) throws IOException {
        Query q = new ConstantScoreQuery(new PrefixFilter(prefix));
        q.setBoost(getBoost());
        return q;
    }

    /** Prints a user-readable version of this query. */
    public String toString(String field) {
        StringBuilder buffer = new StringBuilder();
        if (!prefix.field().equals(field)) {
            buffer.append(prefix.field());
            buffer.append(":");
        }
        buffer.append(prefix.text());
        buffer.append('*');
        if (getBoost() != 1.0f) {
            buffer.append("^");
            buffer.append(Float.toString(getBoost()));
        }
        return buffer.toString();
    }

    /** Returns true if <code>o</code> is equal to this. */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstantScorePrefixQuery)) return false;
        ConstantScorePrefixQuery other = (ConstantScorePrefixQuery) o;
        return this.prefix.equals(other.prefix) && this.getBoost() == other.getBoost();
    }

    /** Returns a hash code value for this object.*/
    public int hashCode() {
        int h = prefix.hashCode() ^ Float.floatToIntBits(getBoost());
        h ^= (h << 14) | (h >>> 19);
        return h;
    }
}
