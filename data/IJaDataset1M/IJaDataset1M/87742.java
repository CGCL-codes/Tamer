package enigma.util;

import java.util.Stack;

public abstract class Pool {

    protected abstract Object makeObject();

    protected abstract void destroyObject(Object obj);

    protected abstract boolean activateObject(Object obj);

    protected abstract boolean passivateObject(Object obj);

    private Stack pool = new Stack();

    public synchronized Object borrowObject() {
        Object obj;
        while (!pool.isEmpty()) {
            obj = pool.pop();
            if (activateObject(obj)) {
                return obj;
            }
            destroyObject(obj);
        }
        obj = makeObject();
        return obj == null ? null : activateObject(obj) ? obj : null;
    }

    public synchronized void returnObject(Object obj) {
        if (passivateObject(obj)) {
            pool.push(obj);
        } else {
            destroyObject(obj);
        }
    }

    public synchronized void addObject() {
        Object obj = makeObject();
        if (obj != null) {
            pool.push(obj);
        }
    }

    public synchronized void removeObject() {
        if (!pool.empty()) {
            Object obj = pool.pop();
            destroyObject(obj);
        }
    }

    public synchronized void invalidateObject(Object obj) {
        passivateObject(obj);
        destroyObject(obj);
    }

    public synchronized void clear() {
        while (!pool.empty()) {
            Object obj = pool.pop();
            destroyObject(obj);
        }
    }

    public int getNumIdle() {
        return pool.size();
    }
}
