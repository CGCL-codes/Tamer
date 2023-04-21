package org.testorrery.examples;

public class Pair<LeftValueType, RightValueType> {

    public LeftValueType left;

    public RightValueType right;

    public Pair(LeftValueType left, RightValueType right) {
        this.left = left;
        this.right = right;
    }

    public String toString() {
        return "[" + left.toString() + "," + right.toString() + "]";
    }
}
