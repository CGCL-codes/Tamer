package net.java.dev.joode.util;

import java.util.HashMap;

/**
 * For example, a pool of Matraces.
 * The user will require different sizes of matrices. A 2D pool is capable of
 * storing and retriving objects that are indexed in a two dimensional way.
 * 
 * @see Pool
 * @see Pool1D
 */
public abstract class Pool2D<T> {

    private final HashMap<Integer, Pool1D<T>> pools = new HashMap<Integer, Pool1D<T>>();

    /**
     * returns a reference to an object. After the caller is finished using the
     * instance, release should be called passing the aquired object reference as an argument.
     * Failing to release the object will force the object to be tidied up using normal
     * garbage collection routines.
     */
    public final T aquire(final int i, final int j) {
        Pool1D<T> pool = pools.get(i);
        if (pool == null) {
            pool = new Pool1D<T>() {

                /**
                 * {@inheritDoc}
                 */
                @Override
                protected T construct(int index) {
                    return Pool2D.this.constuct(i, j);
                }

                /**
                 * {@inheritDoc}
                 */
                @Override
                protected int getIndex(T object) {
                    return getSecondIndex(object);
                }

                /**
                 * {@inheritDoc}
                 */
                @Override
                protected void reset(T object) {
                    Pool2D.this.reset(object);
                }
            };
            pools.put(i, pool);
        }
        return pool.aquire(j);
    }

    /**
     * returns an object that was previously 'aquired' back to the object pool.
     * Calling release on an object allready released, or continuing to use an object
     * after it has been released, will cause strange bugs, as the object could be modified by any other
     * part of the codebase.
     */
    public final void release(T object) {
        Pool1D<T> pool = pools.get(getFirstIndex(object));
        assert pool != null : "object was not aquired";
        pool.release(object);
    }

    /**
     * subclasses should overide this method will a construction call
     * to the required pool, for the given index.
     */
    protected abstract T constuct(int i, int j);

    /**
     * subclasses must override this implementation to
     * return the first index of the object for pooling. If this pool was to store
     * arbitary matraces, the index would be the first dimention of the matrix (becuae the pool
     * is indexed by size)
     */
    protected abstract int getFirstIndex(T object);

    /**
     * subclasses must override this implementation to
     * return the second index of the object for pooling. If this pool was to store
     * arbitary matraces, the index would be the second dimention of the matrix (becuae the pool
     * is indexed by size)
     */
    protected abstract int getSecondIndex(T object);

    protected abstract void reset(T object);

    public int size() {
        int size = 0;
        for (Pool1D<?> pool : pools.values()) {
            size += pool.size();
        }
        return size;
    }
}
