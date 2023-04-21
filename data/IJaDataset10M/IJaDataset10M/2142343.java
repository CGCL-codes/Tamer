package xbird.util.pool;

import xbird.util.concurrent.collections.NonBlockingStack;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public abstract class StackObjectPool<V> implements ObjectPool<V> {

    protected final NonBlockingStack<V> stack;

    public StackObjectPool() {
        this(0);
    }

    public StackObjectPool(int initEntries) {
        this.stack = new NonBlockingStack<V>();
        for (int i = 0; i < initEntries; i++) {
            stack.push(createObject());
        }
    }

    protected abstract V createObject();

    public V borrowObject() {
        final V pooled = stack.pop();
        if (pooled != null) {
            return pooled;
        }
        V created = createObject();
        return created;
    }

    public boolean returnObject(V value) {
        stack.push(value);
        return true;
    }

    public void clear() {
        stack.clear();
    }
}
