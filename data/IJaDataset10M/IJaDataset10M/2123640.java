package com.google.code.datastrut;

public interface Navigable<Type> {

    ReverseIterator<Type> getReverseIterator();
}
