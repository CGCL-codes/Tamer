package org.exist.util.serializer;

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.StackKeyedObjectPool;
import org.exist.storage.serializers.Serializer;

/**
 * @author wolf
 *
 */
public class SerializerPool extends StackKeyedObjectPool {

    private static final SerializerPool instance = new SerializerPool(new SerializerObjectFactory(), 10, 1);

    public static final SerializerPool getInstance() {
        return instance;
    }

    /**
     * 
     */
    public SerializerPool(KeyedPoolableObjectFactory factory, int max, int init) {
        super(factory, max, init);
    }

    public synchronized Object borrowObject(Object key) {
        try {
            return super.borrowObject(key);
        } catch (Exception e) {
            throw new IllegalStateException("Error while creating serializer: " + e.getMessage());
        }
    }

    public DOMStreamer borrowDOMStreamer(Serializer delegate) {
        try {
            ExtendedDOMStreamer serializer = (ExtendedDOMStreamer) borrowObject(DOMStreamer.class);
            serializer.setSerializer(delegate);
            return serializer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void returnObject(Object obj) {
        if (obj == null) return;
        try {
            super.returnObject(obj.getClass(), obj);
        } catch (Exception e) {
            throw new IllegalStateException("Error while returning serializer: " + e.getMessage());
        }
    }
}
