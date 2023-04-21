package org.lindenb.njson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.RandomAccess;

public class ArrayNode extends ComplexNode implements java.util.List<Node>, RandomAccess {

    private java.util.List<Node> array;

    public ArrayNode() {
        this.array = new java.util.ArrayList<Node>();
    }

    public ArrayNode(int capacity) {
        this.array = new java.util.ArrayList<Node>(capacity);
    }

    @Override
    public Type getType() {
        return Type.ARRAY;
    }

    @Override
    public int size() {
        return this.array.size();
    }

    @Override
    public Node get(int index) {
        return this.array.get(index);
    }

    public NilNode getNil(int index) {
        return get(index).asNil();
    }

    public ArrayNode getArray(int index) {
        return get(index).asArray();
    }

    public ObjectNode getObject(int index) {
        return get(index).asObject();
    }

    public StringNode getString(int index) {
        return get(index).asString();
    }

    public BoolNode getBool(int index) {
        return get(index).asBool();
    }

    public IntNode getInt(int index) {
        return get(index).asInt();
    }

    public DecimalNode getDecimal(int index) {
        return get(index).asDecimal();
    }

    @Override
    public boolean isEmpty() {
        return this.array.isEmpty();
    }

    @Override
    public void print(java.io.Writer out) throws java.io.IOException {
        out.write('[');
        for (int i = 0; i < size(); ++i) {
            if (i != 0) out.write(',');
            get(i).print(out);
        }
        out.write(']');
    }

    @Override
    public int hashCode() {
        return array.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || !(o instanceof ArrayNode)) return false;
        return ArrayNode.class.cast(o).array.equals(array);
    }

    @Override
    public Object clone() {
        ArrayNode a = new ArrayNode(size());
        for (int i = 0; i < size(); ++i) {
            a.array.add(Node.class.cast(get(i).clone()));
        }
        return a;
    }

    @Override
    public void add(int index, Node element) {
        this.array.add(index, element);
    }

    @Override
    public boolean add(Node e) {
        return this.array.add(e);
    }

    public boolean add(String s) {
        return this.add(s == null ? new NilNode() : new StringNode(s));
    }

    public boolean add(boolean value) {
        return this.add(new BoolNode(value));
    }

    public boolean add(long value) {
        return this.add(new IntNode(value));
    }

    public boolean add(double value) {
        return this.add(new DecimalNode(value));
    }

    public boolean add(BigInteger value) {
        return this.add(new IntNode(value));
    }

    public boolean add(BigDecimal value) {
        return this.add(new DecimalNode(value));
    }

    @Override
    public boolean addAll(Collection<? extends Node> c) {
        return this.array.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Node> c) {
        return this.array.addAll(index, c);
    }

    @Override
    public void clear() {
        this.array.clear();
    }

    @Override
    public boolean contains(Object o) {
        return this.array.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.array.containsAll(c);
    }

    @Override
    public int indexOf(Object o) {
        return this.array.indexOf(o);
    }

    @Override
    public Iterator<Node> iterator() {
        return this.array.iterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.array.lastIndexOf(o);
    }

    @Override
    public ListIterator<Node> listIterator() {
        return array.listIterator();
    }

    @Override
    public ListIterator<Node> listIterator(int index) {
        return array.listIterator(index);
    }

    @Override
    public boolean remove(Object o) {
        return array.remove(o);
    }

    @Override
    public Node remove(int index) {
        return array.remove(index);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return array.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return array.retainAll(c);
    }

    @Override
    public Node set(int index, Node element) {
        return array.set(index, element);
    }

    @Override
    public ArrayNode subList(int fromIndex, int toIndex) {
        ArrayNode n = new ArrayNode(toIndex - fromIndex);
        n.array.addAll(this.array.subList(fromIndex, toIndex));
        return n;
    }

    @Override
    public Object[] toArray() {
        return this.array.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.array.toArray(a);
    }
}
