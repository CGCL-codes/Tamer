package org.jaimz.telex.logging;

import java.util.*;

public class ActionSet implements Iterable<Action> {

    private LinkedHashSet<Action> _set;

    long _totalWeight;

    public ActionSet() {
        _set = new LinkedHashSet<Action>();
        _totalWeight = 0L;
    }

    public boolean add(Action action) {
        boolean result = this._set.add(action);
        if (result) this._totalWeight += action.getWeight();
        return result;
    }

    public boolean addAll(Collection<Action> actions) {
        boolean result = false;
        for (Action a : actions) result = this.add(a);
        return result;
    }

    public boolean remove(Action a) {
        boolean result = this._set.remove(a);
        if (result) this._totalWeight -= a.getWeight();
        return result;
    }

    public boolean contains(Action action) {
        return this._set.contains(action);
    }

    public long getSetWeight() {
        return this._totalWeight;
    }

    public int getCount() {
        return this._set.size();
    }

    public Iterator<Action> iterator() {
        return this._set.iterator();
    }

    public Action[] toArray() {
        Action[] result = new Action[this._set.size()];
        return this._set.toArray(result);
    }

    public ActionSet intersection(ActionSet other) {
        ActionSet result = new ActionSet();
        for (Action a : other) {
            if (this.contains(a)) result.add(a);
        }
        return result;
    }

    public ActionSet union(ActionSet other) {
        ActionSet result = new ActionSet();
        result.add(other);
        result.add(this);
        return result;
    }

    public void add(ActionSet other) {
        for (Action a : other) {
            this.add(a);
        }
    }

    public void subtract(ActionSet other) {
        for (Action a : other) this._set.remove(a);
    }

    public void clear() {
        this._set.clear();
        this._totalWeight = 0L;
    }
}
