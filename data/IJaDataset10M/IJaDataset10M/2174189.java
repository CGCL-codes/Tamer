package oracle.toplink.essentials.internal.identitymaps;

import java.lang.ref.*;
import java.util.Vector;

/**
 * <p><b>Purpose</b>: Container class for storing objects in an IdentityMap.
 * The weak cache key uses a weak reference to allow garbage collection of its object.
 * The cache key itself however will remain and thus should cleaned up every no and then.
 * <p><b>Responsibilities</b>:<ul>
 * <li> Hold key and object.
 * <li> Maintain and update the current writeLockValue.
 * </ul>
 * @since TOPLink/Java 1.0
 */
public class WeakCacheKey extends CacheKey {

    /** Reference is maintained weak to allow garbage collection */
    protected WeakReference reference;

    /**
     * Initialize the newly allocated instance of this class.
     * @param primaryKey contains values extracted from the object
     * @param writeLockValue is the write lock value, null if optimistic locking not being used for this object.
     * @param readTime the time TopLInk read the cache key
     */
    public WeakCacheKey(Vector primaryKey, Object object, Object writeLockValue, long readTime) {
        super(primaryKey, object, writeLockValue, readTime);
    }

    public Object getObject() {
        return getReference().get();
    }

    public WeakReference getReference() {
        return reference;
    }

    public void setObject(Object object) {
        setReference(new WeakReference(object));
    }

    protected void setReference(WeakReference reference) {
        this.reference = reference;
    }
}
