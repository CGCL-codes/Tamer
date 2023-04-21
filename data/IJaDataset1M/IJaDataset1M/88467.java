package unbbayes.datamining.clustering;

import java.util.Hashtable;
import java.util.Random;
import unbbayes.datamining.datamanipulation.Attribute;
import unbbayes.datamining.datamanipulation.Instance;
import unbbayes.datamining.datamanipulation.InstanceSet;
import unbbayes.datamining.distance.IDistance;

/**
 * Implements </b>K-Means Classical</b> data clustering method.
 * 
 * @version $Revision: 602 $ $Date: 2006-09-26 10:33:27 -0400 (Tue, 26 Sep 2006) $
 * 
 * @author <a href="mailto:hugoiver@yahoo.com.br">Hugo Iver V. Gon&ccedil;alves</a>
 * @author <a href="mailto:rodbra@pop.com.br">Rodrigo C. M. Coimbra</a>
 * 
 * modified by <a href="mailto:emersoft@conecttanet.com.br">Emerson Lopes Machado
 * for working with UnBMiner
 */
public class KmeansClassical {

    /** A matrix containing in each row the coordinates of final clusters. */
    private double[][] clusters;

    /**
	 * Distance matrix of data points to clusters. Each row is a cluster and
	 * each column a data point in the same order they appear in the input
	 * matrix.
	 */
    private double[][] distanceMatrix;

    /**
	 * Data assignment to clusters. Each row is a cluster and each column a data
	 * point in the same order they appear in the input matrix.
	 */
    private int[][] assignmentMatrix;

    /**
	 * Method for calculate distance between two vectors.
	 */
    private IDistance distance;

    /** The current dataset */
    private InstanceSet instanceSet;

    /** Number of desired clusters */
    private int k;

    /** Number of instances in the instance set */
    private int numInstances;

    /**
	 * Creates a new <code>KmeansClassical</code>
	 * 
	 * @param distance The distance method
	 * @param instanceSet The input dataset
	 */
    public KmeansClassical() {
    }

    /**
	 * @see neuralnetworktoolkit.clustering.IClustering#clusterize(double[][],
	 *      int, double)
	 */
    public int[][] clusterize(IDistance distance, InstanceSet instanceSet, int k, double error) {
        boolean go;
        boolean modified = false;
        double localError = Double.MAX_VALUE;
        double instantError = Double.MAX_VALUE;
        this.distance = distance;
        this.instanceSet = instanceSet;
        this.k = k;
        numInstances = instanceSet.numInstances();
        clusters = initialize();
        do {
            go = false;
            distanceMatrix = calculateDistanceMatrix();
            assignmentMatrix = assignPoints();
            clusters = calculateCentroids();
            instantError = calculateError();
            if (Double.compare(1.001, localError / instantError) < 0) {
                localError = instantError;
                go = true;
            }
        } while (go);
        return null;
    }

    /**
	 * Initializes the clusters matrix.
	 * 
	 * @param k
	 *            Number of clusters.
	 * @param input
	 *            Data to clusterize.
	 * 
	 * @return Clusters matrix with initial values.
	 */
    private double[][] initialize() {
        Random randomizer;
        Hashtable<String, Integer> hash;
        int filled;
        int key;
        double[][] result;
        int numAttributes = instanceSet.numAttributes();
        Instance instance;
        numAttributes = instanceSet.numAttributes();
        randomizer = new Random();
        hash = new Hashtable<String, Integer>();
        result = new double[k][numAttributes];
        filled = 0;
        while (filled < k) {
            key = randomizer.nextInt(numInstances);
            if (!hash.containsKey(String.valueOf(key))) {
                instance = instanceSet.getInstance(key);
                for (int att = 0; att < numAttributes; att++) {
                    if (instanceSet.getAttribute(att).isNumeric()) {
                        result[filled][att] = instance.floatValue(att);
                    } else {
                        result[filled][att] = instance.getValue(att);
                    }
                }
                hash.put(String.valueOf(key), new Integer(key));
                filled++;
            }
        }
        return result;
    }

    /**
	 * Calculates distance matrix.
	 * 
	 * @param input
	 *            Data to clusterize.
	 * 
	 * @return Distance matrix.
	 */
    private double[][] calculateDistanceMatrix() {
        double[][] result = new double[k][numInstances];
        double[][] input = new double[k][numInstances];
        Instance instance;
        int numAttributes = instanceSet.numAttributes();
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < numInstances; j++) {
                instance = instanceSet.getInstance(j);
                for (int att = 0; att < numAttributes; att++) {
                    if (instanceSet.getAttribute(att).isNominal()) {
                        input[j][att] = instance.getValue(att);
                    } else {
                        input[j][att] = instance.floatValue(att);
                    }
                }
                result[i][j] = distance.distanceValue(clusters[i], input[j]);
            }
        }
        return result;
    }

    /**
	 * Selects assign points to clusters.
	 * 
	 * @return Assign points.
	 */
    private int[][] assignPoints() {
        int selected = 0;
        int[][] result;
        double least;
        result = new int[distanceMatrix.length][distanceMatrix[0].length];
        for (int j = 0; j < distanceMatrix[0].length; j++) {
            least = Double.MAX_VALUE;
            for (int i = 0; i < distanceMatrix.length; i++) {
                if (distanceMatrix[i][j] < least) {
                    least = distanceMatrix[i][j];
                    selected = i;
                }
            }
            result[selected][j] = 1;
        }
        return result;
    }

    /**
	 * Calculates new centroids values.
	 * 
	 * @param input
	 *            Data to clusterize.
	 * 
	 * @return New centroids values.
	 */
    private double[][] calculateCentroids() {
        double[][] result = new double[assignmentMatrix.length][input[0].length];
        double[] sum = new double[input[0].length];
        int membersCounter;
        for (int i = 0; i < assignmentMatrix.length; i++) {
            for (int j = 0; j < sum.length; j++) {
                sum[j] = 0;
            }
            membersCounter = 0;
            for (int j = 0; j < assignmentMatrix[0].length; j++) {
                if (assignmentMatrix[i][j] == 1) {
                    membersCounter++;
                    for (int k = 0; k < input[0].length; k++) {
                        sum[k] += input[j][k];
                    }
                }
            }
            for (int j = 0; j < input[0].length; j++) {
                sum[j] /= membersCounter;
                result[i][j] = sum[j];
            }
        }
        return result;
    }

    /**
	 * Calculates error in clustering.
	 * 
	 * @param input Data to clusterize.
	 * 
	 * @return Error in clustering.
	 */
    private double calculateError() {
        double result = 0;
        for (int i = 0; i < assignmentMatrix.length; i++) {
            for (int j = 0; j < assignmentMatrix[0].length; j++) {
                if (assignmentMatrix[i][j] == 1) {
                    result += Math.pow(distance.distanceValue(input[j], clusters[i]), 2);
                }
            }
        }
        return result;
    }
}
