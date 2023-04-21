package jolie.runtime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ValueVectorLink extends ValueVector implements Cloneable {

    private final VariablePath linkPath;

    @Override
    public ValueVectorLink clone() {
        return new ValueVectorLink(linkPath);
    }

    public Value get(int i) {
        return getLinkedValueVector().get(i);
    }

    public void set(int i, Value value) {
        getLinkedValueVector().set(i, value);
    }

    public ValueVectorLink(VariablePath path) {
        linkPath = path;
    }

    public boolean isLink() {
        return true;
    }

    private ValueVector getLinkedValueVector() {
        return linkPath.getValueVector();
    }

    protected List<Value> values() {
        return getLinkedValueVector().values();
    }
}

class ValueVectorImpl extends ValueVector implements Serializable {

    private final ArrayList<Value> values;

    protected List<Value> values() {
        return values;
    }

    public synchronized Value get(int i) {
        if (i >= values.size()) {
            values.ensureCapacity(i + 1);
            for (int k = values.size(); k <= i; k++) {
                values.add(Value.create());
            }
        }
        return values.get(i);
    }

    public synchronized void set(int i, Value value) {
        if (i >= values.size()) {
            values.ensureCapacity(i + 1);
            for (int k = values.size(); k < i; k++) {
                values.add(Value.create());
            }
            values.add(value);
        } else {
            values.set(i, value);
        }
    }

    public boolean isLink() {
        return false;
    }

    public ValueVectorImpl() {
        values = new ArrayList<Value>(1);
    }
}

public abstract class ValueVector implements Iterable<Value> {

    public static ValueVector create() {
        return new ValueVectorImpl();
    }

    public synchronized Value remove(int i) {
        return values().remove(i);
    }

    public static ValueVector createLink(VariablePath path) {
        return new ValueVectorLink(path);
    }

    public static ValueVector createClone(ValueVector vec) {
        ValueVector retVec = null;
        if (vec.isLink()) {
            retVec = ((ValueVectorLink) vec).clone();
        } else {
            retVec = create();
            for (Value v : vec) retVec.add(Value.createClone(v));
        }
        return retVec;
    }

    public synchronized Value first() {
        return get(0);
    }

    public synchronized boolean isEmpty() {
        return values().isEmpty();
    }

    public Iterator<Value> iterator() {
        return values().iterator();
    }

    public abstract Value get(int i);

    public abstract void set(int i, Value value);

    public synchronized int size() {
        return values().size();
    }

    public synchronized void add(Value value) {
        values().add(value);
    }

    public synchronized void deepCopy(ValueVector vec) {
        for (int i = 0; i < vec.size(); i++) get(i).deepCopy(vec.get(i));
    }

    protected abstract List<Value> values();

    public abstract boolean isLink();
}
