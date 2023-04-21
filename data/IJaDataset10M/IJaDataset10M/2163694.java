package dr.evomodel.tree;

import dr.evolution.tree.NodeRef;
import dr.evolution.tree.Tree;
import dr.inference.model.BooleanStatistic;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Tests whether 2 (possibly unresolved) trees are compatible
 * *
 *
 * @author Andrew Rambaut
 */
public class CompatibilityStatistic extends BooleanStatistic implements TreeStatistic {

    public CompatibilityStatistic(String name, Tree tree1, Tree tree2) throws Tree.MissingTaxonException {
        super(name);
        this.tree = tree1;
        intersection = new BitSet(tree1.getExternalNodeCount());
        clades = new HashSet<BitSet>();
        getClades(tree1, tree2, tree2.getRoot(), null, clades);
        for (int i = 0; i < tree1.getTaxonCount(); i++) {
            String id = tree1.getTaxonId(i);
            if (tree2.getTaxonIndex(id) == -1) {
                throw new Tree.MissingTaxonException(tree1.getTaxon(i));
            }
        }
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public Tree getTree() {
        return tree;
    }

    public int getDimension() {
        return 1;
    }

    /**
     * @return boolean result of test.
     */
    public boolean getBoolean(int dim) {
        return isCompatible(tree, tree.getRoot(), null);
    }

    private boolean isCompatible(Tree tree, NodeRef node, BitSet leaves) {
        if (tree.isExternal(node)) {
            leaves.set(node.getNumber());
            return true;
        } else {
            BitSet ls = new BitSet(tree.getExternalNodeCount());
            for (int i = 0; i < tree.getChildCount(node); i++) {
                NodeRef node1 = tree.getChild(node, i);
                if (!isCompatible(tree, node1, ls)) {
                    return false;
                }
            }
            if (leaves != null) {
                if (!clades.contains(ls)) {
                    for (BitSet clade : clades) {
                        intersection.clear();
                        intersection.or(clade);
                        intersection.and(ls);
                        int card = intersection.cardinality();
                        if (card != 0 && card != ls.cardinality() && card != clade.cardinality()) {
                            return false;
                        }
                    }
                }
                leaves.or(ls);
            }
        }
        return true;
    }

    private void getClades(Tree referenceTree, Tree tree, NodeRef node, BitSet leaves, Set<BitSet> clades) {
        if (tree.isExternal(node)) {
            String taxonId = tree.getNodeTaxon(node).getId();
            for (int i = 0; i < referenceTree.getExternalNodeCount(); i++) {
                NodeRef n = referenceTree.getExternalNode(i);
                if (taxonId.equals(referenceTree.getNodeTaxon(n).getId())) {
                    leaves.set(n.getNumber());
                }
            }
        } else {
            BitSet ls = new BitSet(tree.getExternalNodeCount());
            for (int i = 0; i < tree.getChildCount(node); i++) {
                NodeRef node1 = tree.getChild(node, i);
                getClades(referenceTree, tree, node1, ls, clades);
            }
            if (leaves != null) {
                leaves.or(ls);
                clades.add(ls);
            }
        }
    }

    private Tree tree;

    private final Set<BitSet> clades;

    private final BitSet intersection;
}
