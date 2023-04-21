package com.stromberglabs.cluster.checker;

import com.stromberglabs.cluster.Cluster;

/**
 * This interface just returns whether or not the current iteration of the
 * clusterer had a small enough change to be considered "done". Often there is
 * some tolerance level that is acceptable depending on the application, either
 * a very few number of items switched clusters, or the clusters moved less than
 * a certain fixed distance.
 * 
 * @author Andrew
 *
 */
public interface ClusterChecker {

    /**
	 * Given the current set of clusters, return whether or not the clusterer
	 * should execute another iteration
	 * 
	 * @param clusters
	 * @return
	 */
    public boolean recalculateClusters(Cluster[] clusters);
}
