package com.jklas.search.index;

import java.util.List;

public interface MasterAndInvertedIndex extends InvertedIndex, MasterIndex {

    /**
	 * Removes a key from the master registry and
	 * from the inverted index.
	 * 
	 * This is the only "remove" method that should be used. 
	 * 
	 * @param key the key of the object to be removed from this index
	 * @param termList 
	 */
    public abstract void consistentRemove(ObjectKey key, List<Term> termList);

    public abstract void consistentRemove(ObjectKey key);

    public abstract void removePosting(Term term, ObjectKey key);

    public abstract int getObjectCount();
}
