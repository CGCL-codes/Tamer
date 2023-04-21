package org.jmlspecs.javacontract.lib.collection;

public interface Sequence<E> {

    E first();

    Sequence<E> insertFront(E e);

    int int_length();

    boolean isEmpty();

    E itemAt(int i);

    E trailer();
}
