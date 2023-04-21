package com.rapidminer.operator.clustering.clusterer;

import java.util.Map;
import com.rapidminer.operator.clustering.HierarchicalClusterNode;

/**
 * This class gives the distance update function for the SingleLinkClustering of the 
 * agglomerative clustering.
 * @author Sebastian Land
 */
public class SingleLinkageMethod extends AbstractLinkageMethod {

    public SingleLinkageMethod(DistanceMatrix matrix, int[] clusterIds) {
        super(matrix, clusterIds);
    }

    @Override
    public void updateDistances(DistanceMatrix matrix, int updatedRow, int unionedRow, Map<Integer, HierarchicalClusterNode> clusterMap) {
        for (int y = 0; y < matrix.getHeight(); y++) {
            matrix.set(updatedRow, y, Math.min(matrix.get(updatedRow, y), matrix.get(unionedRow, y)));
        }
    }
}
