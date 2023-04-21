package org.dcopolis.util;

import java.util.*;

public abstract class GraphGenerator<V extends Vertex> implements Iterable<Graph<V>> {

    int n;

    public GraphGenerator(int n) {
        this.n = n;
    }
}
