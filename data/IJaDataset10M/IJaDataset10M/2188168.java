package org.enerj.apache.commons.collections.buffer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import org.enerj.apache.commons.collections.Buffer;
import org.enerj.apache.commons.collections.Unmodifiable;
import org.enerj.apache.commons.collections.iterators.UnmodifiableIterator;

/**
 * Decorates another <code>Buffer</code> to ensure it can't be altered.
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 155406 $ $Date: 2005-02-26 12:55:26 +0000 (Sat, 26 Feb 2005) $
 * 
 * @author Stephen Colebourne
 */
public final class UnmodifiableBuffer extends AbstractBufferDecorator implements Unmodifiable, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 1832948656215393357L;

    /**
     * Factory method to create an unmodifiable buffer.
     * <p>
     * If the buffer passed in is already unmodifiable, it is returned.
     * 
     * @param buffer  the buffer to decorate, must not be null
     * @return an unmodifiable Buffer
     * @throws IllegalArgumentException if buffer is null
     */
    public static Buffer decorate(Buffer buffer) {
        if (buffer instanceof Unmodifiable) {
            return buffer;
        }
        return new UnmodifiableBuffer(buffer);
    }

    /**
     * Constructor that wraps (not copies).
     * 
     * @param buffer  the buffer to decorate, must not be null
     * @throws IllegalArgumentException if buffer is null
     */
    private UnmodifiableBuffer(Buffer buffer) {
        super(buffer);
    }

    /**
     * Write the collection out using a custom routine.
     * 
     * @param out  the output stream
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(collection);
    }

    /**
     * Read the collection in using a custom routine.
     * 
     * @param in  the input stream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        collection = (Collection) in.readObject();
    }

    public Iterator iterator() {
        return UnmodifiableIterator.decorate(getCollection().iterator());
    }

    public boolean add(Object object) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection coll) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object object) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection coll) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection coll) {
        throw new UnsupportedOperationException();
    }

    public Object remove() {
        throw new UnsupportedOperationException();
    }
}
