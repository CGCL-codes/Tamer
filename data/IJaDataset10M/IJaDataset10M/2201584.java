package edu.mit.lcs.haystack.server.extensions.infoextraction.data;

import java.util.Iterator;
import java.util.Vector;
import edu.mit.lcs.haystack.server.extensions.infoextraction.cluster.ICluster;
import edu.mit.lcs.haystack.server.extensions.wrapperinduction.dom.INode;

/**
 * @author yks
 * 
 * Alignment of two trees, (using dynamic programming techniques to cache
 * subtree alignments)
 */
public class TreeAlignmentPoint extends DefaultPoint {

    public static int cacheHits = 0;

    public static int totalHits = 0;

    public void clearStats() {
        cacheHits = 0;
    }

    protected static SequenceAligner aligner = new SequenceAligner();

    public TreeAlignmentPoint() {
        super(null);
    }

    /**
     * @param arg
     */
    public TreeAlignmentPoint(Object arg) {
        super(arg);
    }

    private boolean virtualPoint = false;

    public boolean isCentroid() {
        return virtualPoint;
    }

    public IPoint centroid(ICluster c) {
        TreeAlignmentPoint centroid = new TreeAlignmentPoint();
        centroid.virtualPoint = true;
        centroid.setAssociation(c);
        return centroid;
    }

    boolean useOptimization = true;

    boolean membersEqual(ICluster A, ICluster B) {
        if (A.numMembers() == B.numMembers()) {
            Iterator aIt = A.iterator();
            Vector mB = B.getMembers();
            while (aIt.hasNext()) {
                IPoint pa = (IPoint) aIt.next();
                if (!mB.contains(pa)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * compares the similarity of a point with a cluster. returns the average
     * similarity between this point and the cluster
     */
    public double p2cDistance(ICluster B) {
        Iterator it = B.getMembers().iterator();
        double distSum = 0;
        int count = 0;
        while (it.hasNext()) {
            IPoint p = (IPoint) it.next();
            if (p == this) {
                continue;
            }
            distSum += this.p2pDistance(p);
            count++;
        }
        if (count == 0) {
            return 0;
        } else {
            return distSum / (double) count;
        }
    }

    /**
     * compares the similarity of a point with a cluster. returns the average
     * similarity between this point and the cluster
     */
    public double c2pDistance(IPoint B) {
        ICluster A = (ICluster) this.getAssociation();
        Iterator it = A.iterator();
        double distSum = 0;
        int count = 0;
        while (it.hasNext()) {
            TreeAlignmentPoint p = (TreeAlignmentPoint) it.next();
            if (p == this) {
                continue;
            }
            distSum += p.p2pDistance(B);
            count++;
        }
        if (count == 0) {
            return 0;
        } else {
            return distSum / (double) count;
        }
    }

    /**
     * compares two virtual centroid points.
     */
    public double c2cDistance(ICluster B) {
        double distSum = 0;
        int count = 0;
        ICluster A = (ICluster) this.getAssociation();
        if (membersEqual(A, B)) {
            return 1.0;
        }
        Iterator itA = A.iterator();
        while (itA.hasNext()) {
            TreeAlignmentPoint pa = (TreeAlignmentPoint) itA.next();
            if (pa == this) {
                continue;
            }
            Iterator itB = B.iterator();
            while (itB.hasNext()) {
                IPoint pb = (IPoint) itB.next();
                if (pb == this) {
                    continue;
                }
                distSum += pa.p2pDistance(pb);
                count++;
            }
        }
        if (count == 0) {
            return 0;
        } else {
            return distSum / (double) count;
        }
    }

    public double p2pDistance(IPoint B) {
        if (pointCollection.hasDistance((IPoint) this, B)) {
            return pointCollection.getDistance(this, B);
        }
        double dist = 1 - this.p2pSimilarity(B);
        if (dist < 0 || dist < 1e-15) {
            dist = 0;
        }
        this.pointCollection.setDistance(this, B, dist);
        return dist;
    }

    private INode getNode(IRelatablePoint node) {
        return (INode) node.getData();
    }

    /**
     * returns true if the given node is a leaf
     * 
     * @param node
     * @return
     */
    private boolean isLeaf(IRelatablePoint node) {
        return (this.getNode(node).getChildNodes().getLength() == 0);
    }

    public double p2pSimilarity(IPoint B) {
        IRelatablePoint n1 = this;
        IRelatablePoint n2 = (IRelatablePoint) B;
        TreeMapping mapping = aligner.getBestAlignment(n1, n2);
        return mapping.getNormalizedScore();
    }

    public double distance(IPoint B) {
        if (B.hasAssociation() && B.isCentroid()) {
            if (this.hasAssociation() && this.isCentroid()) {
                return c2cDistance((ICluster) B.getAssociation());
            } else {
                return p2cDistance((ICluster) B.getAssociation());
            }
        } else {
            if (this.hasAssociation() && this.isCentroid()) {
                return c2pDistance(B);
            } else {
                return p2pDistance(B);
            }
        }
    }

    public void setPointCollection(IPointCollection ipc) {
        this.pointCollection = ipc;
    }
}
