package org.systemsbiology.apmlparser.v2;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import org.systemsbiology.apmlparser.v2.datatype.*;
import org.apache.log4j.Logger;

/**
 * Implementation for listening to events representing clusters of Features from APMLReader.
 * This default implementation will create a FeatureCluster from every Cluster that is reported.
 * The FeatureCluster will have references to the actual Feature objects, not just the IDs.
 */
public class DefaultAlignedFeatureClustersListener extends BaseDefaultClustersListener implements ClustersListener {

    static Logger _log = Logger.getLogger(DefaultAlignedFeatureClustersListener.class);

    protected List<AlignedFeatureCluster> alignedFeatureClusterList;

    protected Map<Integer, AlignedFeature> idAlignedFeatureMap;

    public DefaultAlignedFeatureClustersListener(int id, String description, Map<Integer, AlignedFeature> idAlignedFeatureMap) {
        super(id, description);
        this.idAlignedFeatureMap = idAlignedFeatureMap;
        alignedFeatureClusterList = new ArrayList<AlignedFeatureCluster>();
    }

    protected AlignedFeature lookupAlignedFeature(int id) {
        return idAlignedFeatureMap.get(id);
    }

    public void reportCluster(Cluster cluster) {
        _log.debug("Reporting cluster " + cluster.getId());
        AlignedFeatureCluster alignedFeatureCluster = new AlignedFeatureCluster(cluster);
        for (int alignedFeatureId : cluster.getRefIds()) alignedFeatureCluster.addAlignedFeatureForExistingId(lookupAlignedFeature(alignedFeatureId));
        alignedFeatureClusterList.add(alignedFeatureCluster);
    }

    public List<AlignedFeatureCluster> getAlignedFeatureClusters() {
        return alignedFeatureClusterList;
    }

    /**
     * Creates a list of generic Cluster objects
     * @return
     */
    public List<Cluster> createGenericClusterList() {
        _log.debug("Creating generic cluster list from AlignedFeatureCluster list");
        List<Cluster> result = new ArrayList<Cluster>();
        for (AlignedFeatureCluster cluster : alignedFeatureClusterList) result.add(cluster);
        return result;
    }
}
