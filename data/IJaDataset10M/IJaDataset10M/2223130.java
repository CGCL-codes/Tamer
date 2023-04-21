package net.sf.afluentes.impl;

import net.sf.afluentes.EdgeRemovalListener;

class Edge<T, U> {

    private VertexImpl<T> dependent;

    private EdgeRemovalListener<T, U> listener;

    Edge(VertexImpl<T> dependent, EdgeRemovalListener<T, U> listener) {
        this.dependent = dependent;
        this.listener = listener;
    }

    VertexImpl<T> getDependent() {
        return dependent;
    }

    EdgeRemovalListener<T, U> getListener() {
        return listener;
    }
}
