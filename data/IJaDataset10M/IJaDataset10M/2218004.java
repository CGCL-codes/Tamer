package nz.ac.vuw.ecs.kcassell.cluster;

import java.util.Collection;

public interface ClustererIfc<V> {

    /**
     * Form clusters by combining objects.  When to stop clustering will
     * be clusterer dependent.  For example, an agglomerative clusterer might
     * cluster until a single group is formed, or alternatively, until a
     * user-specified criterion is reached.
     * @return a collection of newly formed clusters
     */
    public Collection<V> cluster();

    /**
     * Form clusters by combining objects.
     * @param iteration the total number of cluster steps that should be
     * performed
     * @return a collection of newly formed clusters
     */
    public Collection<V> cluster(int iteration);

    public Collection<V> getClusters();
}
