package jaxlib.ref;

import jaxlib.col.concurrent.AbstractConcurrentXMap;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: ConcurrentReferenceMap.java 2997 2011-10-06 23:34:10Z joerg_wassmer $
 */
public abstract class ConcurrentReferenceMap<K, V> extends AbstractConcurrentXMap<K, V> {

    /**
   * The default initial number of table slots for this table.
   * Used when not otherwise specified in constructor.
   */
    static final int DEFAULT_INITIAL_CAPACITY = 16;

    /**
   * The maximum capacity, used if a higher value is implicitly
   * specified by either of the constructors with arguments.  MUST
   * be a power of two <= 1<<30 to ensure that entries are indexible
   * using ints.
   */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
   * The maximum number of segments to allow; used to bound
   * constructor arguments.
   */
    static final int MAX_SEGMENTS = 1 << 16;

    /**
   * The default number of concurrency control segments.
   **/
    public static final int DEFAULT_CONCURRENCY = 16;

    /**
   * The default load factor for this table.
   * Used when not otherwise specified in constructor.
   */
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    ConcurrentReferenceMap() {
        super();
    }
}
