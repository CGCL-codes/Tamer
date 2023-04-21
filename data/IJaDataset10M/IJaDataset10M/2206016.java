package edu.umd.cs.piccolox.util;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PPickPath;

/**
 * Experimental class for detecting occlusions.
 * 
 * @author Jesse Grosjean
 */
public class POcclusionDetection {

    /**
     * Traverse from the bottom right of the scene graph (top visible node) up
     * the tree determining which parent nodes are occluded by their children
     * nodes. Note that this is only detecting a subset of occlusions (parent,
     * child), others such as overlapping siblings or cousins are not detected.
     */
    public void detectOccusions(PNode n, PBounds parentBounds) {
        detectOcclusions(n, new PPickPath(null, parentBounds));
    }

    public void detectOcclusions(PNode n, PPickPath pickPath) {
        if (n.fullIntersects(pickPath.getPickBounds())) {
            pickPath.pushTransform(n.getTransformReference(false));
            int count = n.getChildrenCount();
            for (int i = count - 1; i >= 0; i--) {
                PNode each = (PNode) n.getChild(i);
                if (n.getOccluded()) {
                    each.setOccluded(true);
                } else {
                    detectOcclusions(each, pickPath);
                }
            }
            if (!n.getOccluded()) {
                if (n.intersects(pickPath.getPickBounds())) {
                    if (n.isOpaque(pickPath.getPickBounds())) {
                        PNode p = n.getParent();
                        while (p != null && !p.getOccluded()) {
                            p.setOccluded(true);
                        }
                    }
                }
            }
            pickPath.popTransform(n.getTransformReference(false));
        }
    }
}
