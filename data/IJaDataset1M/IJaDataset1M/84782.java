package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

/**
 * Subclass of FilteredTermEnum for enumerating a single term.
 * <p>
 * This can be used by {@link MultiTermQuery}s that need only visit one term,
 * but want to preserve MultiTermQuery semantics such as
 * {@link MultiTermQuery#rewriteMethod}.
 */
public class SingleTermEnum extends FilteredTermEnum {

    private Term singleTerm;

    private boolean endEnum = false;

    /**
   * Creates a new <code>SingleTermEnum</code>.
   * <p>
   * After calling the constructor the enumeration is already pointing to the term,
   * if it exists.
   */
    public SingleTermEnum(IndexReader reader, Term singleTerm) throws IOException {
        super();
        this.singleTerm = singleTerm;
        setEnum(reader.terms(singleTerm));
    }

    @Override
    public float difference() {
        return 1.0F;
    }

    @Override
    protected boolean endEnum() {
        return endEnum;
    }

    @Override
    protected boolean termCompare(Term term) {
        if (term.equals(singleTerm)) {
            return true;
        } else {
            endEnum = true;
            return false;
        }
    }
}
