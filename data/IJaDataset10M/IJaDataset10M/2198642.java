package cunei.confusion;

import java.util.ArrayList;
import java.util.Collection;
import cunei.collections.Lattice;
import cunei.collections.MergedSet;

public class ConfusionLattice extends Lattice<ConfusionNode> {

    protected static class ConfusionSet extends MergedSet<ConfusionNode> {

        public ConfusionSet() {
            super();
        }
    }

    public ConfusionLattice() {
        super(ConfusionSet.class);
    }

    public ConfusionLattice clone() {
        return (ConfusionLattice) super.clone();
    }

    public Collection<ConfusionPath> getPaths(int start, int end, boolean isComplete, boolean hasEmptyTarget) {
        Collection<ConfusionPath> result = new ArrayList<ConfusionPath>();
        for (int mid = start + 1; mid < end; mid++) {
            Collection<ConfusionNode> headNodes = get(mid, end);
            if (headNodes != null) {
                int coverage = end - mid;
                Collection<ConfusionPath> tailPaths = getPaths(start, mid, isComplete, hasEmptyTarget);
                for (ConfusionPath tailPath : tailPaths) for (ConfusionNode headNode : headNodes) if (isComplete && headNode.hasCompleteSource() && headNode.hasCompleteTarget() || hasEmptyTarget && headNode.hasEmptyTarget()) result.add(new ConfusionPath(coverage, headNode, tailPath));
            }
        }
        Collection<ConfusionNode> nodes = get(start, end);
        if (nodes != null) {
            int coverage = end - start;
            for (ConfusionNode node : nodes) if (isComplete && node.hasCompleteSource() && node.hasCompleteTarget() || hasEmptyTarget && node.hasEmptyTarget()) result.add(new ConfusionPath(coverage, node));
        }
        return result;
    }
}
