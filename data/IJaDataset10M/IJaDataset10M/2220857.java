package edu.tufts.cs.geometry;

import javax.vecmath.*;

/**
	A <code>Generator</code> object creates or fills a <code>PointSet</code>
	object with data.
*/
public abstract class Generator {

    /**
	Chose the seed automatically, usually determined by the clock. 
	*/
    public final int AUTO_SEED = Integer.MIN_VALUE;

    public final GMatrix generate(int size, int dimension) {
        return generate(size, dimension, AUTO_SEED, null);
    }

    public final GMatrix generate(int size, int dimension, int seed) {
        return generate(size, dimension, seed, null);
    }

    public final GMatrix generate(int size, int dimension, int seed, String parameters) {
        GMatrix data = new GMatrix(size, dimension);
        fill(data, seed, parameters);
        return data;
    }

    public final void fill(GMatrix data) {
        fill(data, AUTO_SEED, null);
    }

    public final void fill(GMatrix data, int seed) {
        fill(data, seed, null);
    }

    public abstract void fill(GMatrix data, int seed, String parameters);
}
