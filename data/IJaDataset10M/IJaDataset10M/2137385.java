package net.lunglet.cluster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import net.lunglet.array4j.matrix.FloatMatrix;
import net.lunglet.array4j.matrix.dense.DenseFactory;
import net.lunglet.array4j.matrix.dense.FloatDenseMatrix;
import net.lunglet.array4j.matrix.util.FloatMatrixUtils;
import org.junit.Test;

public final class KMeansTest {

    @Test
    public void testLocalOptimum() throws InterruptedException, ExecutionException {
        int ndata = 3;
        int dimension = 1;
        int maxCentroids = 2;
        Random rng = new Random(1185202905218L);
        KMeans<FloatMatrix> kmeans = KMeans.create();
        FloatDenseMatrix data = DenseFactory.floatMatrix(dimension, ndata);
        FloatMatrixUtils.fillRandom(data, rng);
        FloatMatrix initialCentroids = kmeans.chooseCentroids(maxCentroids, data);
        double delta = 1.0e-6;
        assertEquals(0.3165713548660278, initialCentroids.get(0, 0), delta);
        assertEquals(0.6212534904479980, initialCentroids.get(0, 1), delta);
        int iterations = 3;
        FloatMatrix centroids = kmeans.train(iterations, initialCentroids, data);
        assertNotSame(initialCentroids, centroids);
        assertEquals(initialCentroids, centroids);
    }

    @Test
    public void testRandom() throws InterruptedException, ExecutionException {
        KMeans<FloatMatrix> kmeans = KMeans.create();
        for (int i = 0; i < 10; i++) {
            Random rng = new Random(System.currentTimeMillis());
            int ndata = 1000 + rng.nextInt(2000);
            int dimension = 1 + rng.nextInt(10);
            int maxCentroids = 1 + rng.nextInt(10);
            long seed = System.currentTimeMillis();
            rng = new Random(seed);
            System.out.println(String.format("%d %d %d %d", seed, ndata, dimension, maxCentroids));
            FloatDenseMatrix data = DenseFactory.floatMatrix(dimension, ndata);
            FloatMatrixUtils.fillRandom(data, rng);
            FloatMatrix centroids = kmeans.chooseCentroids(maxCentroids, data);
            int iterations = 100;
            centroids = kmeans.train(iterations, centroids, data);
        }
    }
}
