package algo.graph.staticflow.maxflow;

import ds.graph.Edge;
import ds.graph.Node;

/**
 *
 * @author Jan-Philipp Kappmeier
 */
public class PushRelabelHighestLabelGlobalGapRelabelling extends PushRelabelHighestLabelGlobalRelabelling {

    int gaps;

    int gapNodes;

    @Override
    protected void discharge(Node v) {
        assert excess.get(v) > 0;
        assert v.id() != sink.id();
        do {
            final int nodeDistance = distanceLabels.get(v);
            int i;
            for (i = current.get(v); i < residualGraph.getLast(v); ++i) {
                final Edge e = residualGraph.getEdge(i);
                if (residualGraph.getResidualCapacity(e) > 0 && distanceLabels.get(e.end()) == nodeDistance - 1 && push(e) == 0) break;
            }
            if (i == residualGraph.getLast(v)) {
                relabel(v);
                if (distanceLabels.get(v) == n) break;
                if (activeBuckets.get(nodeDistance) == null && inactiveBuckets.get(nodeDistance) == null) gap(nodeDistance);
                if (distanceLabels.get(v) == n) throw new IllegalStateException("here a break should be somehow");
            } else {
                current.set(v, i);
                inactiveBuckets.addInactive(nodeDistance, v);
                break;
            }
        } while (true);
    }

    /**
	 * Gap relabeling (maybe move to bucket?)
	 * @param emptyBucket
	 * @return
	 */
    protected int gap(int emptyBucket) {
        gaps++;
        int r = emptyBucket - 1;
        int cc;
        for (int l = emptyBucket + 1; l <= activeBuckets.getdMax(); l++) {
            for (Node node = inactiveBuckets.get(l); node != null; node = inactiveBuckets.next(node)) {
                distanceLabels.set(node, n);
                gapNodes++;
            }
            inactiveBuckets.set(l, null);
        }
        cc = (activeBuckets.getMinIndex() > r) ? 1 : 0;
        activeBuckets.setMaxIndex(r);
        return cc;
    }

    public int getGapNodes() {
        return gapNodes;
    }

    public int getGaps() {
        return gaps;
    }
}
