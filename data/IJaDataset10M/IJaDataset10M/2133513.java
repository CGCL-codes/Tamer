package guineu.modules.dataanalysis.clustering.farthestfirst;

import guineu.modules.dataanalysis.clustering.ClusteringAlgorithm;
import guineu.modules.dataanalysis.clustering.VisualizationType;
import guineu.parameters.ParameterSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.clusterers.Clusterer;
import weka.clusterers.FarthestFirst;
import weka.core.Instance;
import weka.core.Instances;

public class FarthestFirstClusterer implements ClusteringAlgorithm {

    private ParameterSet parameters;

    private int numberOfGroups = 0;

    public FarthestFirstClusterer() {
        parameters = new FarthestFirstClustererParameters();
    }

    public String toString() {
        return "Farthest First";
    }

    public ParameterSet getParameterSet() {
        return parameters;
    }

    public List<Integer> getClusterGroups(Instances dataset) {
        List<Integer> clusters = new ArrayList<Integer>();
        String[] options = new String[2];
        Clusterer clusterer = new FarthestFirst();
        int numberOfGroups = parameters.getParameter(FarthestFirstClustererParameters.numberOfGroups).getValue();
        options[0] = "-N";
        options[1] = String.valueOf(numberOfGroups);
        try {
            ((FarthestFirst) clusterer).setOptions(options);
            clusterer.buildClusterer(dataset);
            Enumeration e = dataset.enumerateInstances();
            while (e.hasMoreElements()) {
                clusters.add(clusterer.clusterInstance((Instance) e.nextElement()));
            }
            this.numberOfGroups = clusterer.numberOfClusters();
        } catch (Exception ex) {
            Logger.getLogger(FarthestFirstClusterer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clusters;
    }

    public String getHierarchicalCluster(Instances dataset) {
        return null;
    }

    public VisualizationType getVisualizationType() {
        return parameters.getParameter(FarthestFirstClustererParameters.visualization).getValue();
    }

    public int getNumberOfGroups() {
        return this.numberOfGroups;
    }
}
