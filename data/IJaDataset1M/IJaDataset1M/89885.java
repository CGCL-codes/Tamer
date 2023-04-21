package ktree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import purity.Purity;

/**
 *
 * @author chris
 */
public class TreePurityVisitor implements ClusterVisitor {

    TreePurityVisitor(String labelFile) throws Exception {
        labels = Purity.loadLabels(labelFile);
        clusterList = new ArrayList<Set<Integer>>();
        clusters = 0;
        allsize = 0;
    }

    public void accept(Vector[] v, int count) throws Exception {
        if (count == 0) {
            return;
        }
        Set<Integer> currCluster = new HashSet<Integer>();
        for (int i = 0; i < count; ++i) {
            currCluster.add(((Document) v[i]).getId());
        }
        clusterList.add(currCluster);
        ++clusters;
        allsize += count;
    }

    public double overallOneLessEntropy() throws Exception {
        int totalLabelCount = Purity.totalLabelCount(labels);
        int totalDocs = 0;
        double overallOneLessEntropy = 0.0;
        for (Set<Integer> cluster : clusterList) {
            Map<Integer, Integer> perLabelCount = new HashMap<Integer, Integer>();
            int labelCount = 0;
            for (Integer docid : cluster) {
                Integer doclabel = labels.get(docid);
                if (doclabel != null) {
                    Integer val = perLabelCount.get(doclabel);
                    if (val == null) {
                        val = 0;
                    }
                    perLabelCount.put(doclabel, ++val);
                    ++labelCount;
                }
            }
            if (labelCount != 0) {
                double oneLessEntropy = Purity.oneLessEntropy(perLabelCount, totalLabelCount);
                overallOneLessEntropy += oneLessEntropy * cluster.size();
                totalDocs += cluster.size();
            }
        }
        overallOneLessEntropy /= totalDocs;
        return overallOneLessEntropy;
    }

    public double averageClusterSize() {
        return allsize / (double) clusters;
    }

    Map<Integer, Integer> labels;

    List<Set<Integer>> clusterList;

    int clusters;

    int allsize;
}
