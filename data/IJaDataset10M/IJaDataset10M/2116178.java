package swarm.engine.statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import swarm.engine.statistics.SingleLinkageClustering.Merge;

public class HeirarchialClusterer<P extends Point> extends Clusterer<P> {

    private SingleLinkageClustering clustering;

    private double splitRadius = 250;

    private int minimumClusterSize = 10;

    public HeirarchialClusterer() {
    }

    public void setSplitRadius(double splitRadius) {
        this.splitRadius = splitRadius;
    }

    public void setMinimumClusterSize(int minimumClusterSize) {
        this.minimumClusterSize = minimumClusterSize;
    }

    @Override
    public void initialize(Collection<P> points) {
        clustering = new SingleLinkageClustering(points);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Cluster<P>> run() {
        List<Merge> res = clustering.run();
        if (res.isEmpty()) return Collections.emptyList();
        Merge prevMerge = res.get(0);
        for (Merge merge : res) {
            int nClusters = 0;
            for (Collection<Point> l : prevMerge) {
                if (l.size() > minimumClusterSize) nClusters++;
            }
            if (prevMerge.getDistance() < splitRadius || nClusters >= maxClusters) {
                break;
            }
            prevMerge = merge;
        }
        List<Cluster<P>> ret = new ArrayList<Cluster<P>>(prevMerge.size());
        for (Collection<Point> l : prevMerge) {
            if (l.size() <= minimumClusterSize) continue;
            ret.add(new Cluster<P>((Collection<P>) l));
        }
        clustering = null;
        return ret;
    }
}
