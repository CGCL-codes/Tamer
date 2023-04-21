package jdbm.helper;

import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator for objects which have been serialized into byte arrays.
 * In effect, it wraps another Comparator which compares object and provides
 * transparent deserialization from byte array to object.
 *
 * @author <a href="mailto:boisvert@intalio.com">Alex Boisvert</a>
 * @version $Id: ObjectBAComparator.java,v 1.1 2002/05/31 06:33:20 boisvert Exp $
 */
public final class ObjectBAComparator implements Comparator, Serializable {

    /**
     * Version id for serialization.
     */
    static final long serialVersionUID = 1L;

    /**
     * Wrapped comparator.
     */
    private Comparator _comparator;

    /**
     * Construct an ObjectByteArrayComparator which wraps an Object Comparator.
     *
     * @param comparator Object comparator.
     */
    public ObjectBAComparator(Comparator comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Argument 'comparator' is null");
        }
        _comparator = comparator;
    }

    /**
     * Compare two objects.
     *
     * @param obj1 First object
     * @param obj2 Second object
     * @return 1 if obj1 > obj2, 0 if obj1 == obj2, -1 if obj1 < obj2
     */
    public int compare(Object obj1, Object obj2) {
        if (obj1 == null) {
            throw new IllegalArgumentException("Argument 'obj1' is null");
        }
        if (obj2 == null) {
            throw new IllegalArgumentException("Argument 'obj2' is null");
        }
        try {
            obj1 = Serialization.deserialize((byte[]) obj1);
            obj2 = Serialization.deserialize((byte[]) obj2);
            return _comparator.compare(obj1, obj2);
        } catch (IOException except) {
            throw new WrappedRuntimeException(except);
        } catch (ClassNotFoundException except) {
            throw new WrappedRuntimeException(except);
        }
    }

    /**
     * Compare two byte arrays.
     */
    public static int compareByteArray(byte[] thisKey, byte[] otherKey) {
        int len = Math.min(thisKey.length, otherKey.length);
        for (int i = 0; i < len; i++) {
            if (thisKey[i] >= 0) {
                if (otherKey[i] >= 0) {
                    if (thisKey[i] < otherKey[i]) {
                        return -1;
                    } else if (thisKey[i] > otherKey[i]) {
                        return 1;
                    }
                } else {
                    return -1;
                }
            } else {
                if (otherKey[i] >= 0) {
                    return 1;
                } else {
                    if (thisKey[i] < otherKey[i]) {
                        return -1;
                    } else if (thisKey[i] > otherKey[i]) {
                        return 1;
                    }
                }
            }
        }
        if (thisKey.length == otherKey.length) {
            return 0;
        }
        if (thisKey.length < otherKey.length) {
            return -1;
        }
        return 1;
    }
}
