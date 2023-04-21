package it.essepuntato.facility.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A class defining facilities for collections.
 * 
 * @author Silvio Peroni 
 */
public class CollectionFacility {

    /**
	 * It returns a copy of an input collection.
	 * 
	 * @param c a collection to be copied.
	 * @return a copy of the input collection.
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Collection copy(Collection c) {
        Collection result = new ArrayList();
        Iterator ite = c.iterator();
        while (ite.hasNext()) {
            result.add(ite.next());
        }
        return result;
    }
}
