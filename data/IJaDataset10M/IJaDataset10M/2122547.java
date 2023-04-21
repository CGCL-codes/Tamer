package de.ui.sushi.graph;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {

    private static int counter = 0;

    private final int hashCode;

    public final T data;

    public final List<Node<T>> starting;

    public final List<Node<T>> ending;

    public Node(T data) {
        this.hashCode = counter++;
        this.data = data;
        this.starting = new ArrayList<Node<T>>();
        this.ending = new ArrayList<Node<T>>();
    }

    /** Override default implementation to get reproducable results */
    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}
