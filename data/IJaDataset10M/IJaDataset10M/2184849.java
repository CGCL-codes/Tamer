package atg.repository.beans.wrapper;

import java.util.ListIterator;
import atg.repository.RepositoryItem;
import atg.repository.beans.RepositoryBean;
import atg.repository.tojava.runtime.RepositoryItemObjectFactory;

/**
 * 
 * <p>
 * This class is a wrapper around a ListIterator of RepositoryItems, that wraps
 * everything as data objects. This class should only be used by the generated
 * data objects, not application code.
 * 
 * @author Nathan Abramson
 * @version $Id:
 *          //product/DAS/main/Java/atg/repository/tojava/runtime/ListIteratorWrapper.java#8
 *          $$Change: 396858 $
 * @updated $DateTime: 2006/10/17 10:54:30 $$Author: adamb $
 */
public class ListIteratorWrapper extends IteratorWrapper implements ListIterator {

    /** Class version string */
    public static String CLASS_VERSION = "$Id: //user/adamb/RepositoryBeans/src/main/java/atg/repository/beans/wrapper/ListIteratorWrapper.java#1 $$Change: 396858 $";

    ListIterator mWrappedList;

    /**
   * 
   * Constructor
   */
    public ListIteratorWrapper(ListIterator pWrapped, RepositoryItemObjectFactory pFactory) {
        super(pWrapped, pFactory);
        mWrappedList = pWrapped;
    }

    public boolean hasPrevious() {
        return mWrappedList.hasPrevious();
    }

    public Object previous() {
        return mFactory.wrap((RepositoryItem) (mWrappedList.previous()), false);
    }

    public int nextIndex() {
        return mWrappedList.nextIndex();
    }

    public int previousIndex() {
        return mWrappedList.previousIndex();
    }

    public void set(Object pObject) {
        mWrappedList.set(((RepositoryBean) pObject).backingItem());
    }

    public void add(Object pObject) {
        mWrappedList.add(((RepositoryBean) pObject).backingItem());
    }
}
