package spock.util.concurrent;

import java.util.concurrent.*;
import org.spockframework.util.ThreadSafe;

/**
 * Implementation underlying class <tt>BlockingVariables</tt>. Should not be
 * used directly.
 *
 * @author Peter Niederwieser
 */
@ThreadSafe
class BlockingVariablesImpl {

    private final int timeout;

    private final TimeUnit unit;

    private final ConcurrentHashMap<String, BlockingVariable<Object>> map = new ConcurrentHashMap<String, BlockingVariable<Object>>();

    public BlockingVariablesImpl(int timeout, TimeUnit unit) {
        this.timeout = timeout;
        this.unit = unit;
    }

    public Object get(String name) throws InterruptedException {
        BlockingVariable<Object> entry = new BlockingVariable<Object>(timeout, unit);
        BlockingVariable<Object> oldEntry = map.putIfAbsent(name, entry);
        if (oldEntry == null) return entry.get(); else return oldEntry.get();
    }

    public void put(String name, Object value) {
        BlockingVariable<Object> entry = new BlockingVariable<Object>(timeout, unit);
        BlockingVariable<Object> oldEntry = map.putIfAbsent(name, entry);
        if (oldEntry == null) entry.set(value); else oldEntry.set(value);
    }
}
