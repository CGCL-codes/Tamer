package org.enerj.apache.commons.collections.set;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * Serializable subclass of AbstractSetDecorator.
 * 
 * @author Stephen Colebourne
 * @since Commons Collections 3.1
 */
public abstract class AbstractSerializableSetDecorator extends AbstractSetDecorator implements Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 1229469966212206107L;

    /**
     * Constructor.
     */
    protected AbstractSerializableSetDecorator(Set set) {
        super(set);
    }

    /**
     * Write the set out using a custom routine.
     * 
     * @param out  the output stream
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(collection);
    }

    /**
     * Read the set in using a custom routine.
     * 
     * @param in  the input stream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        collection = (Collection) in.readObject();
    }
}
