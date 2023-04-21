package com.tt.bnct.domain;

/**
 * Edge for metabolic graphs.
 * 
 * @author Samuel Yung
 * @param <V> A metabolic vertex.
 */
public interface MetabolicEdge<V extends MetabolicVertex> {

    /**
     * Return source vertex.
     * 
     * @return source vertex
     */
    V getSource();

    /**
     * Return target vertex.
     * 
     * @return target vertex
     */
    V getTarget();

    String getInteractionType();
}
