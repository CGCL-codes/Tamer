package org.gamegineer.common.persistence.schemes.serializable;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamClass;
import net.jcip.annotations.NotThreadSafe;
import org.gamegineer.common.persistence.schemes.serializable.services.persistencedelegateregistry.IPersistenceDelegateRegistry;

/**
 * A stream used for deserializing objects previously serialized using an
 * {@code ObjectOutputStream}.
 * 
 * <p>
 * After deserializing an object, the stream will query its persistence delegate
 * registry for an associated persistence delegate and give it an opportunity to
 * substitute the deserialized object with a compatible object.
 * </p>
 * 
 * <p>
 * To contribute a persistence delegate for a specific class, register it with
 * the {@code IPersistenceDelegateRegistry} passed to the input stream.
 * </p>
 */
@NotThreadSafe
public final class ObjectInputStream extends java.io.ObjectInputStream {

    /** The persistence delegate registry. */
    private final IPersistenceDelegateRegistry persistenceDelegateRegistry_;

    /**
     * Initializes a new instance of the {@code ObjectInputStream} class.
     * 
     * @param in
     *        The input stream from which to read; must not be {@code null}.
     * @param persistenceDelegateRegistry
     *        The persistence delegate registry; must not be {@code null}.
     * 
     * @throws java.io.IOException
     *         If an I/O error occurs while reading the stream header.
     * @throws java.io.StreamCorruptedException
     *         If the stream header is incorrect.
     * @throws java.lang.NullPointerException
     *         If {@code in} or {@code persistenceDelegateRegistry} is {@code
     *         null}.
     */
    public ObjectInputStream(final InputStream in, final IPersistenceDelegateRegistry persistenceDelegateRegistry) throws IOException {
        super(in);
        assertArgumentNotNull(persistenceDelegateRegistry, "persistenceDelegateRegistry");
        persistenceDelegateRegistry_ = persistenceDelegateRegistry;
        enableResolveObject(true);
    }

    @Override
    protected Class<?> resolveClass(final ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        final IPersistenceDelegate delegate = persistenceDelegateRegistry_.getPersistenceDelegate(desc.getName());
        if (delegate != null) {
            final Class<?> serializedClass = delegate.resolveClass(this, desc);
            if (serializedClass != null) {
                return serializedClass;
            }
        }
        return super.resolveClass(desc);
    }

    @Override
    protected Object resolveObject(final Object obj) throws IOException {
        Object object = obj;
        while (true) {
            final IPersistenceDelegate delegate = persistenceDelegateRegistry_.getPersistenceDelegate(object.getClass().getName());
            final Object resolvedObject = (delegate != null) ? delegate.resolveObject(object) : super.resolveObject(object);
            if (object != resolvedObject) {
                object = resolvedObject;
            } else {
                break;
            }
        }
        return object;
    }
}
