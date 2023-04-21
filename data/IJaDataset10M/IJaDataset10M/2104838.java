package nl.utwente.ewi.stream.network.attributes.bp;

import java.sql.Timestamp;
import nl.utwente.ewi.stream.network.attributes.Buffer;
import nl.utwente.ewi.stream.network.attributes.BufferPredicate;

/**
 * A fixed tuple buffer predicate enforces a fixed tuple interval of tuples (historical data).
 * The buffer will only contain values with an ID between the 'oldest' and the 'newest' ID passed.
 *
 * @author rein
 */
public class FixedTupleBufferPredicate extends BufferPredicate {

    private long oldest, newest;

    public FixedTupleBufferPredicate(long oldest, long newest) {
        this.oldest = oldest;
        this.newest = newest;
    }

    public FixedTupleBufferPredicate(Buffer b, long oldest, long newest) {
        super(b);
        this.oldest = oldest;
        this.newest = newest;
    }

    /**
     * @see BufferPredicate#evaluateBufferPredicate(long lastInsertId, Timestamp createdAt).
     */
    @Override
    public void evaluateBufferPredicate(long lastInsertId, long createdAt) {
        getBuffer().removeElementsWithIDSmallerThan(oldest);
        getBuffer().removeElementsWithIDBiggerThan(newest);
    }

    @Override
    public String getBufferType() {
        return "FixedTuple";
    }

    @Override
    public String getBufferPredicate() {
        return Long.toString(oldest) + "," + Long.toString(newest);
    }
}
