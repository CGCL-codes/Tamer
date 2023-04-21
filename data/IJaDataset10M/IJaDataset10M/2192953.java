package gumbo.core.life.impl;

import gumbo.core.life.ObjectPool;
import gumbo.core.struct.IdentityHashSet;
import gumbo.core.struct.StructUtils;
import gumbo.core.util.AssertUtils;
import gumbo.core.util.Cleaner;
import gumbo.core.util.CoreUtils;
import gumbo.core.util.Maker;
import java.util.Set;

/**
 * Default implementation of ObjectPool. The pool is thread-safe, with each
 * thread maintaining its own separate pool.
 * <p>
 * Derived from com.ardor3d.math.ObjectPool and
 * org.apache.commons.pool.ObjectPool.
 * @param <T> The pool object type.
 */
public class ObjectPoolImpl<T> extends DisposableImpl.IdentityEquality implements ObjectPool<T> {

    /**
	 * Creates a new instance, with DEFAULT_MAX_SIZE.
	 * @param maker Shared exposed object maker. Never null.
	 */
    public ObjectPoolImpl(Maker.FromNone<? extends T> maker) {
        this(maker, null, DEFAULT_MAX_SIZE);
    }

    /**
	 * Creates a new instance, with DEFAULT_MAX_SIZE. returned objects will be
	 * cleaned.
	 * @param maker Shared exposed object maker. Never null.
	 * @param cleaner Shared exposed object cleaner. Never null.
	 */
    public ObjectPoolImpl(Maker.FromNone<? extends T> maker, Cleaner<T> cleaner) {
        this(maker, cleaner, DEFAULT_MAX_SIZE);
    }

    /**
	 * Creates a new instance.
	 * @param maker Shared exposed object maker. Never null.
	 * @param cleaner Shared exposed object cleaner. None if null.
	 * @param maxSize Maximum pool size (>0).
	 */
    public ObjectPoolImpl(Maker.FromNone<? extends T> maker, Cleaner<T> cleaner, int maxSize) {
        AssertUtils.assertNonNullArg(maker);
        if (maxSize <= 0) AssertUtils.throwIllegalArg("MaxSize must be >0. maxSize=" + maxSize);
        _maker = maker;
        _cleaner = cleaner;
        _maxSize = maxSize;
    }

    @Override
    public final T borrowObject() {
        Set<T> cache = _cache.get();
        if (cache.isEmpty()) {
            Set<T> pool = _pool.get();
            if (pool.size() >= _maxSize) {
                throw new IllegalStateException("Pool size at the maximum. max=" + _maxSize + "\n" + "Perhaps there is a memory leak. this=" + this);
            }
            T newObj;
            try {
                newObj = _maker.make();
                AssertUtils.assertNonNullState(newObj);
            } catch (final Exception ex) {
                throw new IllegalStateException("Can't create a new pool object. maker=" + _maker + "\n", ex);
            }
            pool.add(newObj);
            return newObj;
        } else {
            T oldObj = StructUtils.findFirst(cache);
            cache.remove(oldObj);
            if (CoreUtils.isDisposed(oldObj)) {
                throw new IllegalStateException("Pool object was disposed. obj=" + " obj=" + CoreUtils.string(oldObj, oldObj.toString()) + "\nReturned objects must be ceded, not shared.");
            }
            return oldObj;
        }
    }

    @Override
    public final void returnObject(T obj) {
        AssertUtils.assertNonNullArg(obj);
        if (CoreUtils.isDisposed(obj)) {
            discardObject(obj);
        } else {
            Set<T> pool = _pool.get();
            if (!pool.contains(obj)) {
                throw new IllegalStateException("Return object not in pool." + " obj=" + CoreUtils.string(obj, obj.toString()));
            }
            if (_cleaner != null) {
                _cleaner.clean(obj);
            }
            Set<T> cache = _cache.get();
            boolean changed = cache.add(obj);
            if (!changed) {
                throw new IllegalStateException("Return object is in pool." + " obj=" + CoreUtils.string(obj, obj.toString()) + "\nReturned objects must be ceded, not shared.");
            }
        }
    }

    @Override
    public final void discardObject(T obj) {
        AssertUtils.assertNonNullArg(obj);
        boolean changed;
        Set<T> pool = _pool.get();
        changed = pool.remove(obj);
        if (!changed) {
            throw new IllegalStateException("Discard object not in pool." + " obj=" + CoreUtils.string(obj, obj.toString()));
        }
        Set<T> cache = _cache.get();
        changed = cache.remove(obj);
        if (changed) {
            throw new IllegalStateException("Discard object is in pool." + " obj=" + CoreUtils.string(obj, obj.toString()) + "\nReturned objects must be ceded, not shared.");
        }
    }

    protected void implDispose() {
        super.implDispose();
        CoreUtils.disposeAll(_pool.get());
        _pool = null;
        CoreUtils.disposeAll(_cache.get());
        _cache = null;
    }

    private int _maxSize;

    private Maker.FromNone<? extends T> _maker;

    private Cleaner<T> _cleaner = null;

    /** Pool includes borrowed and returned objects (i.e. all objects
	 * related to this pool). */
    private ThreadLocal<Set<T>> _pool = new ThreadLocal<Set<T>>() {

        @Override
        protected Set<T> initialValue() {
            return new IdentityHashSet<T>();
        }
    };

    /** Cache includes only returned objects (i.e. objects that are available
	 * for borrowing). */
    private ThreadLocal<Set<T>> _cache = new ThreadLocal<Set<T>>() {

        @Override
        protected Set<T> initialValue() {
            return new IdentityHashSet<T>();
        }
    };

    /**
	 * Default maximum pool size.
	 */
    public static final int DEFAULT_MAX_SIZE = 16;
}
