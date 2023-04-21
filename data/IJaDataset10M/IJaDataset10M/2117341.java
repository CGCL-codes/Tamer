package org.encog.ml.kmeans;

/**
 * The centers of each cluster.
 */
public class Centroid {

    /**
	 * The center for each dimension in the input.
	 */
    private final double[] centers;

    /**
	 * The cluster.
	 */
    private KMeansCluster cluster;

    /**
	 * Construct the centroid.
	 * 
	 * @param theCenters
	 *            The centers.
	 */
    public Centroid(final double[] theCenters) {
        this.centers = theCenters;
    }

    /**
	 * Calculate the centroid.
	 */
    public final void calcCentroid() {
        final int numDP = this.cluster.size();
        final double[] temp = new double[this.centers.length];
        for (int i = 0; i < numDP; i++) {
            for (int j = 0; j < temp.length; j++) {
                temp[j] += this.cluster.get(i).getData(j);
            }
        }
        for (int i = 0; i < temp.length; i++) {
            this.centers[i] = temp[i] / numDP;
        }
        this.cluster.calcSumOfSquares();
    }

    /**
	 * @return The centers.
	 */
    public final double[] getCenters() {
        return this.centers;
    }

    /**
	 * @return The clusters.
	 */
    public final KMeansCluster getCluster() {
        return this.cluster;
    }

    /**
	 * Set the cluster.
	 * 
	 * @param c
	 *            The cluster.
	 */
    public final void setCluster(final KMeansCluster c) {
        this.cluster = c;
    }
}
