package com.google.code.datastrut.list;

public interface Indexable<Type> {

    Type getElementAt(int index);

    Type[] toArray();
}
