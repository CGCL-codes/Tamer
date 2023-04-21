package shellkk.qiq.jdm.clustering;

import java.util.ArrayList;
import java.util.Collection;
import javax.datamining.JDMException;
import javax.datamining.MiningFunction;
import javax.datamining.clustering.Cluster;
import javax.datamining.clustering.ClusteringModel;
import javax.datamining.clustering.ClusteringModelProperty;
import javax.datamining.rule.Rule;
import shellkk.qiq.jdm.base.ModelImpl;

public class ClusteringModelImpl extends ModelImpl implements ClusteringModel {

    protected int numberOfClusters;

    protected int numberOfLevels;

    @Override
    protected ClusteringModelImpl create() {
        return new ClusteringModelImpl();
    }

    public ClusteringModelImpl getCopy() {
        ClusteringModelImpl copy = (ClusteringModelImpl) super.getCopy();
        copy.setNumberOfClusters(numberOfClusters);
        copy.setNumberOfLevels(numberOfLevels);
        return copy;
    }

    public int getNumberOfClusters() {
        return numberOfClusters;
    }

    public void setNumberOfClusters(int numberOfClusters) {
        this.numberOfClusters = numberOfClusters;
    }

    public int getNumberOfLevels() {
        return numberOfLevels;
    }

    public void setNumberOfLevels(int numberOfLevels) {
        this.numberOfLevels = numberOfLevels;
    }

    public Cluster getCluster(int identifier) throws JDMException {
        ClusteringModelDetail md = (ClusteringModelDetail) modelDetail;
        for (Cluster cluster : md.getClusters()) {
            if (cluster.getClusterId() == identifier) {
                return cluster;
            }
        }
        return null;
    }

    public Collection getClusters() throws JDMException {
        ClusteringModelDetail md = (ClusteringModelDetail) modelDetail;
        ArrayList all = new ArrayList(md.getClusters());
        return all;
    }

    public Collection getLeafClusters() throws JDMException {
        ClusteringModelDetail md = (ClusteringModelDetail) modelDetail;
        ArrayList all = new ArrayList(md.getClusters());
        for (Cluster cluster : md.getClusters()) {
            if (cluster.isLeaf()) {
                all.add(cluster);
            }
        }
        return all;
    }

    public Collection getRootClusters() throws JDMException {
        ClusteringModelDetail md = (ClusteringModelDetail) modelDetail;
        ArrayList all = new ArrayList(md.getClusters());
        for (Cluster cluster : md.getClusters()) {
            if (cluster.isRoot()) {
                all.add(cluster);
            }
        }
        return all;
    }

    public Collection getRules() throws JDMException {
        ClusteringModelDetail md = (ClusteringModelDetail) modelDetail;
        ArrayList all = new ArrayList(md.getClusters());
        for (Cluster cluster : md.getClusters()) {
            Rule rule = cluster.getRule();
            if (rule != null) {
                all.add(rule);
            }
        }
        return all;
    }

    public Double getSimilarity(int clusterIdentifier1, int clusterIdentifier2) throws JDMException {
        ClusteringModelDetail md = (ClusteringModelDetail) modelDetail;
        return md.getSimilarity(clusterIdentifier1, clusterIdentifier2);
    }

    public boolean hasProperty(ClusteringModelProperty property) {
        ClusteringModelDetail md = (ClusteringModelDetail) modelDetail;
        return md.hasProperty(property);
    }

    public MiningFunction getMiningFunction() {
        return MiningFunction.clustering;
    }
}
