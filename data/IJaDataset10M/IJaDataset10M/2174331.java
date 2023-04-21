package org.apache.solr.search.function;

import org.apache.lucene.search.FieldCache;

/**
 * A base class for ValueSource implementations that retrieve values for
 * a single field from the {@link org.apache.lucene.search.FieldCache}.
 *
 * @version $Id: FieldCacheSource.java 794328 2009-07-15 17:21:04Z shalin $
 */
public abstract class FieldCacheSource extends ValueSource {

    protected String field;

    protected FieldCache cache = FieldCache.DEFAULT;

    public FieldCacheSource(String field) {
        this.field = field;
    }

    public FieldCache getFieldCache() {
        return cache;
    }

    public String description() {
        return field;
    }

    public boolean equals(Object o) {
        if (!(o instanceof FieldCacheSource)) return false;
        FieldCacheSource other = (FieldCacheSource) o;
        return this.field.equals(other.field) && this.cache == other.cache;
    }

    public int hashCode() {
        return cache.hashCode() + field.hashCode();
    }

    ;
}
