package com.jklas.search.util;

public class Pair<A, B> {

    private A first;

    private B second;

    public Pair(A a, B b) {
        setFirst(a);
        setSecond(b);
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    public void setFirst(A a) {
        this.first = a;
    }

    public void setSecond(B b) {
        this.second = b;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!obj.getClass().equals(getClass())) return false;
        Pair<?, ?> other = (Pair<?, ?>) obj;
        Object otherA = other.getFirst();
        Object otherB = other.getSecond();
        return otherA.equals(getFirst()) && otherB.equals(getSecond());
    }

    @Override
    public int hashCode() {
        return (getFirst().hashCode() + 1) * (getSecond().hashCode() + 1);
    }
}
