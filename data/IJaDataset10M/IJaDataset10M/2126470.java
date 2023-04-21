package net.sf.crepido.base;

import java.util.*;

public final class Option<E> {

    private E value;

    public static final Option none = new Option();

    private Option() {
    }

    private Option(E value) {
        this.value = value;
    }

    public boolean isSome() {
        return this != Option.none;
    }

    public boolean isNone() {
        return this == Option.none;
    }

    public E value() {
        if (this == Option.none) {
            throw new NoSuchElementException();
        }
        return this.value;
    }

    public static <E> Option<E> some(E value) {
        return new Option<E>(value);
    }
}
