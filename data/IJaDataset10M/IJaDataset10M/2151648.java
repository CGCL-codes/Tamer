package com.gusto.engine.clusterant.algorithms.antsclustering;

import com.gusto.engine.clusterant.model.Point;

/**
 * <p>A neighbor of a {@link Point} on the {@link Plan}, 
 * with a weight that represents the proximity.</p>
 * 
 * @author amokrane.belloui@gmail.com
 *
 */
public class Neighbor {

    private Object object;

    private double weight;

    public Neighbor() {
        super();
    }

    public String toString() {
        return "Neighbor " + object + "(" + weight + ")";
    }

    public Neighbor(Object object, double weight) {
        super();
        this.object = object;
        this.weight = weight;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
