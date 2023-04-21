package org.apache.commons.collections.bag;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import org.apache.commons.collections.Bag;

/**
 * Implements <code>Bag</code>, using a <code>HashMap</code> to provide the
 * data storage. This is the standard implementation of a bag.
 * <p>
 * A <code>Bag</code> stores each object in the collection together with a
 * count of occurrences. Extra methods on the interface allow multiple copies
 * of an object to be added or removed at once. It is important to read the
 * interface javadoc carefully as several methods violate the
 * <code>Collection</code> interface specification.
 *
 * @since Commons Collections 3.0 (previously in main package v2.0)
 * @version $Revision: 646777 $ $Date: 2008-04-10 13:33:15 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Chuck Burdick
 * @author Stephen Colebourne
 */
public class HashBag extends AbstractMapBag implements Bag, Serializable {

    /** Serial version lock */
    private static final long serialVersionUID = -6561115435802554013L;

    /**
     * Constructs an empty <code>HashBag</code>.
     */
    public HashBag() {
        super(new HashMap());
    }

    /**
     * Constructs a bag containing all the members of the given collection.
     * 
     * @param coll  a collection to copy into this bag
     */
    public HashBag(Collection coll) {
        this();
        addAll(coll);
    }

    /**
     * Write the bag out using a custom routine.
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        super.doWriteObject(out);
    }

    /**
     * Read the bag in using a custom routine.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        super.doReadObject(new HashMap(), in);
    }
}
