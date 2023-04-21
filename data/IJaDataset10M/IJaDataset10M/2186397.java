package org.jfonia.connect5.relations;

import org.jfonia.connect5.basics.MutableValueNode;
import org.jfonia.connect5.basics.ValueNode;
import org.jfonia.connect5.basics.Node;

/**
 *
 * @author Rik Bauwens
 */
public abstract class UnidirectionalABCRelation<R, S, T> implements Relation {

    protected ValueNode<R> a;

    protected ValueNode<S> b;

    protected MutableValueNode<T> c;

    public UnidirectionalABCRelation(ValueNode<R> a, ValueNode<S> b, MutableValueNode<T> c) {
        this.a = a;
        this.b = b;
        this.c = c;
        switchOn();
    }

    public void switchOn() {
        a.addObserver(this);
        b.addObserver(this);
        updateC();
    }

    public void switchOff() {
        a.removeObserver(this);
        b.removeObserver(this);
    }

    public abstract void updateC();

    public void onNotify(Object source) {
        if (source == a || source == b) updateC(); else if (source == c) ; else throw new RuntimeException("Unknown source");
    }
}
