package uk.ac.shef.wit.aleph.algorithm.graphlab.kernel;

import no.uib.cipr.matrix.Vector;

public class KernelCosine implements Kernel {

    public double computeSimilarity(final Vector vector1, final Vector vector2) {
        return vector1.dot(vector2) / (vector1.norm(Vector.Norm.Two) * vector2.norm(Vector.Norm.Two));
    }
}
