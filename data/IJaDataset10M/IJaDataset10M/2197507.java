package com.shudes.container;

import java.util.*;

public class Container<P, C> implements Iterable<C> {

    protected P parent;

    protected Collection<C> children;

    public Container(P p) {
        this.parent = p;
        this.children = newChildrenCollection();
    }

    public P getParent() {
        return parent;
    }

    public Collection<C> getChildren() {
        return Collections.unmodifiableCollection(this.children);
    }

    public Set<C> getChildrenSet() {
        return Collections.unmodifiableSet(new HashSet<C>(this.children));
    }

    public void addChild(C child) {
        children.add(child);
    }

    public Iterator<C> iterator() {
        return children.iterator();
    }

    public Container<P, C> newContainer() {
        return new Container<P, C>(getParent());
    }

    protected Collection<C> newChildrenCollection() {
        return new ArrayList<C>();
    }
}
