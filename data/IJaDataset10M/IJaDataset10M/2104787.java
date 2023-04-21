package com.gantzgulch.generics;

public class Pair<T, U> {

    private T left;

    private U right;

    public Pair() {
        this(null, null);
    }

    public Pair(T left, U right) {
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public void setLeft(T left) {
        this.left = left;
    }

    public U getRight() {
        return right;
    }

    public void setRight(U right) {
        this.right = right;
    }
}
