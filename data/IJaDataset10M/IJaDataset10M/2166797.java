package shellkk.qiq.jdm.algorithm.kmedoids;

import java.util.Map;
import javax.datamining.clustering.ClusteringAlgorithmSettings;

public interface KMedoidsSettings extends ClusteringAlgorithmSettings {

    public Map<String, String> getDistanceProperties();

    public void setDistanceProperties(Map<String, String> props);

    public String getDistanceName();

    public void setDistanceName(String distanceName);

    /**
	 * Returns the maximum number of iterations to train a <i>k</i>-means
	 * clustering model.
	 * 
	 * @return int
	 */
    public int getMaxNumberOfIterations();

    /**
	 * Sets the maximum number of interations while training a <i>k</i>-means
	 * clustering model.
	 * 
	 * The maximum iterations must be a positive integer.
	 * 
	 * @param maxIterations
	 *            The maximum number of iterations.
	 * @return void
	 */
    public void setMaxNumberOfIterations(int maxIterations);

    /**
	 * Returns the minimum error tolerance to train a <i>k</i>-means clustering
	 * model.
	 * 
	 * @return double
	 */
    public double getMinErrorTolerance();

    /**
	 * Sets the minimum percentual change in error between iterations to
	 * consider that the clusters have converged.
	 * 
	 * The minimum error tolerance must be a non-negative number that is less
	 * than 1.
	 * 
	 * @param minErrorTolerance
	 *            The minimum percentual change in error between iterations.
	 * @return void
	 */
    public void setMinErrorTolerance(double minErrorTolerance);
}
