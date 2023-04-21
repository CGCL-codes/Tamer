package org.apache.commons.collections.iterators;

import java.util.Iterator;
import org.apache.commons.collections.functors.UniquePredicate;

/** 
 * A FilterIterator which only returns "unique" Objects.  Internally,
 * the Iterator maintains a Set of objects it has already encountered,
 * and duplicate Objects are skipped.
 *
 * @since Commons Collections 2.1
 * @version $Revision: 646777 $ $Date: 2008-04-10 13:33:15 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Morgan Delagrange
 */
public class UniqueFilterIterator extends FilterIterator {

    /**
     *  Constructs a new <code>UniqueFilterIterator</code>.
     *
     *  @param iterator  the iterator to use
     */
    public UniqueFilterIterator(Iterator iterator) {
        super(iterator, UniquePredicate.getInstance());
    }
}
