package algs.blog.multithread.nearestNeighbor.onehelper;

import algs.model.IMultiPoint;
import algs.model.kdtree.DimensionalNode;
import algs.model.kdtree.KDTree;

/**
 * Multi-threaded nearest implementation which employs a separate thread
 * on the first time a double-recursion call to nearest neighbor is needed.
 * 
 * @author George Heineman
 * @version 1.0, 6/1/09
 */
public class OneHelperKDTree extends KDTree {

    /**
	 * Extend parent's constructor.
	 * 
	 * @param md
	 */
    public OneHelperKDTree(int md) {
        super(md);
    }

    /**
	 * Find the nearest point in the KDtree to the given point while using
	 * multiple threads as helpers.
	 * <p>
	 * Only returns null if the tree was initially empty. Otherwise, must
	 * return some point that belongs to the tree.
	 * <p>
	 * If tree is empty or if the target is <code>null</code> then
	 * <code>null</code> is returned.
	 * 
	 * @param   target    the target of the search. 
	 */
    public IMultiPoint nearest(IMultiPoint target) {
        OneHelperKDNode top = ((OneHelperKDNode) getRoot());
        if (top == null || target == null) return null;
        DimensionalNode parent = parent(target);
        IMultiPoint result = parent.point;
        double smallest = target.distance(result);
        double best[] = new double[] { smallest };
        double raw[] = target.raw();
        IMultiPoint betterOne = top.nearest(raw, best);
        if (betterOne != null) {
            return betterOne;
        }
        return result;
    }
}
