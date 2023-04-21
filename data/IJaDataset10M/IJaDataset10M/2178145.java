package hebClustering.vectorSpace;

import hebClustering.*;
import hebClustering.documentTypes.Document;
import hebClustering.vectorSpace.distances.*;
import hebClustering.weights.IWeightFunc;
import hebClustering.weights.Tf_IdfWeight;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *	A representation of a metric space, that holds vectors, and a set of clusters that divide them.
 *
 *	@see <a href="http://en.wikipedia.org/wiki/Metric_space" target="_blank">Metric space</a>
 */
public class MetricSpace implements Iterable<IVector> {

    private int dimension;

    private ClusterSet clusters;

    private Set<IVector> vectors;

    /**
	 * 	Creates a new metric space given a document set.<br>
	 * 	For each document in the set, the builder creates a document vector, and addes it to the space.
	 *  
	 * @param dimension - The dimension of the space.
	 * @param documentSet - A set of documents. 
	 */
    public MetricSpace(int dimension, Set<Document> documentSet) {
        this.dimension = dimension;
        clusters = new ClusterSet();
        vectors = new HashSet<IVector>();
        IWeightFunc weight = new Tf_IdfWeight();
        for (Document d : documentSet) {
            IVector currentVector = new DocumentVector(d, weight, dimension);
            addVector(currentVector);
        }
    }

    /**
	 * Adds a vector to the space.
	 * 
	 * @param v - The vector to be added.
	 */
    public void addVector(IVector v) {
        vectors.add(v);
    }

    /**
	 * Removes a vector from the space.
	 * 
	 * @param v - The vector to be removed.
	 */
    public void removeVector(IVector v) {
        vectors.remove(v);
    }

    /**
	 * Get the dimension of the space.
	 * 
	 * @return The dimension.
	 */
    public int getDimension() {
        return dimension;
    }

    /**
	 * Returns a set of the clusters in the space.
	 * 
	 * @return Set of clusters.
	 */
    public ClusterSet getClusters() {
        return clusters;
    }

    /**
	 * Set the clusters of the space.
	 * 
	 * @param clusterSet - A set of clusters.
	 */
    public void setClusters(ClusterSet clusterSet) {
        clusters = clusterSet;
        clusters.removeAllEmptyClusters();
    }

    /**
	 * Get a cluster that contains a specific vector.
	 * 
	 * @param vector - The vector to be searched.
	 * 
	 * @return The cluster that contains the requested vector, or null if it is not contained in any cluster. 
	 */
    public Cluster getCluster(IVector vector) {
        for (Cluster cluster : clusters) {
            if (cluster.contains(vector)) return cluster;
        }
        return null;
    }

    /**
	 * Returns a string representation of the space.
	 * 
	 * @return A String representation.
	 */
    public String toString() {
        StringBuffer output = new StringBuffer();
        output.append("Metric Space:\r\n\tDimension: " + getDimension() + "\r\n\tNum of vectors: " + vectors.size() + "\r\n\tClusters:\r\n\t\t");
        output.append("Partionioned into " + clusters.size() + " Clusters\r\n");
        output.append(clusters.toString());
        return output.toString();
    }

    /**
	 * Get the vectors of the space.
	 * 
	 * @return A set of the vectors of the space.
	 */
    public Set<IVector> getVectors() {
        Set<IVector> vecSet = new HashSet<IVector>();
        vecSet.addAll(vectors);
        return vecSet;
    }

    /**
	 * Prints the distances between all the vectors in the space.
	 *  
	 * @param distance - A distance function to be used.
	 */
    public String printDebug(IDistance distance) {
        String out = "";
        IVector[] vecArr = new IVector[vectors.size()];
        vectors.toArray(vecArr);
        for (int i = 0; i < vecArr.length; i++) {
            for (int j = i; j < vecArr.length; j++) {
                if (vecArr[i] != vecArr[j]) out += "The distance between " + vecArr[i].toString() + " and " + vecArr[j].toString() + " is " + distance.calc(vecArr[i], vecArr[j]) + "\r\r\n";
            }
        }
        return out;
    }

    /**
	 * Get the number of vectors in the space.
	 * 
	 * @return The number of vectors.
	 */
    public int numOfVectors() {
        return vectors.size();
    }

    @Override
    public Iterator<IVector> iterator() {
        return vectors.iterator();
    }

    /**
	 * Add a list of vectors to the space.
	 * 
	 * @param lst - A list of vectors to be added to the space.
	 */
    public void addAllVectors(List<IVector> lst) {
        for (IVector v : this) {
            lst.add(v);
        }
    }
}
