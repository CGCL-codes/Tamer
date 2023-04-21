package edu.mit.lcs.haystack.server.extensions.wrapperinduction;

import java.util.ArrayList;
import edu.mit.lcs.haystack.server.extensions.wrapperinduction.dom.INode;

/**
 * Represents a mapping between two Tree objects.
 * 
 * @author Andrew Hogue
 */
public class Mapping {

    protected ArrayList pairs;

    /**
     * Creates a new mapping.
     * 
     * @param totalSize
     *            represents the combined size of the two subtrees being mapped.
     */
    public Mapping() {
        this.pairs = new ArrayList();
    }

    public Mapping(Pair[] pairs) {
        this.pairs = new ArrayList();
        for (int i = 0; i < pairs.length; i++) {
            this.pairs.add(pairs[i]);
        }
    }

    protected Mapping(ArrayList pairs) {
        this.pairs = pairs;
    }

    /**
     * Creates a new mapping with a single pair, with the given values
     */
    public Mapping(INode n1, INode n2, int cost) {
        this(new Pair[] { new Pair(n1, n2, cost) });
    }

    /**
     * Returns the cost of this mapping as per the static cost variables in
     * TreeDistance.
     */
    public int getCost() {
        int cost = 0;
        for (int i = 0; i < pairs.size(); i++) {
            cost += ((Pair) pairs.get(i)).cost;
        }
        return cost;
    }

    public int getTotalSize() {
        int totalSize = 0;
        for (int i = 0; i < pairs.size(); i++) {
            Pair p = (Pair) (pairs.get(i));
            INode n1 = p.node1;
            INode n2 = p.node2;
            if (n1 != null) totalSize += n1.getSize();
            if (n2 != null) totalSize += n2.getSize();
        }
        return totalSize;
    }

    /**
     * Returns the normalized cost of the mapping, that is,
     * (2*cost)/(totalSize).
     */
    public double getNormalizedCost() {
        if (this.getTotalSize() == 0) return 1;
        return (double) (((double) (this.getCost())) / ((double) (this.getTotalSize())));
    }

    /**
     * Adds a mapping from n1 to n2
     */
    public void add(INode n1, INode n2, int cost) {
        pairs.add(new Pair(n1, n2, cost));
    }

    public void add(Pair pair) {
        pairs.add(pair);
    }

    public Pair getPair(int n) {
        if (n < 0 || n >= pairs.size()) return null;
        return (Pair) this.pairs.get(n);
    }

    /**
     * Returns the pairs contained in this mapping.
     */
    public Pair[] getPairs() {
        return (Pair[]) this.pairs.toArray(new Pair[0]);
    }

    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("Mapping (cost " + getCost() + ", normalized: " + getNormalizedCost() + "):\n");
        for (int i = 0; i < pairs.size(); i++) {
            out.append("\t" + pairs.get(i) + "\n");
        }
        return out.toString();
    }

    /**
     * Returns a new copy of this mapping.
     */
    public Mapping cloneMapping() {
        return new Mapping((ArrayList) this.pairs.clone());
    }

    /**
     * Merges the two mappings, eliminating redundant pairs
     */
    public static Mapping merge(Mapping m1, Mapping m2) {
        Pair[] pairs1 = m1.getPairs();
        Pair[] pairs2 = m2.getPairs();
        Pair[] toReturn = new Pair[pairs1.length + pairs2.length];
        for (int i = 0; i < pairs1.length; i++) {
            toReturn[i] = pairs1[i];
        }
        for (int i = 0; i < pairs2.length; i++) {
            toReturn[pairs1.length + i] = pairs2[i];
        }
        return new Mapping(toReturn);
    }

    public boolean containsDeletions() {
        for (int i = 0; i < pairs.size(); i++) {
            if (((Pair) pairs.get(i)).node2 == null) return true;
        }
        return false;
    }

    public boolean containsInsertions() {
        for (int i = 0; i < pairs.size(); i++) {
            if (((Pair) pairs.get(i)).node1 == null) return true;
        }
        return false;
    }
}
