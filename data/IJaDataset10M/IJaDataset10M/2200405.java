package dr.app.treestat.statistics;

import dr.evolution.tree.NodeRef;
import dr.evolution.tree.Tree;
import dr.evolution.util.TaxonList;
import java.util.Arrays;

/**
 *
 * @version $Id: NodeHeights.java,v 1.2 2005/09/28 13:50:56 rambaut Exp $
 *
 * @author Alexei Drummond
 */
public class NodeHeights implements TreeSummaryStatistic {

    private NodeHeights() {
    }

    public int getStatisticDimensions(Tree tree) {
        return tree.getInternalNodeCount();
    }

    public String getStatisticLabel(Tree tree, int i) {
        return "Height " + Integer.toString(i + 1);
    }

    public double[] getSummaryStatistic(Tree tree) {
        int internalNodeCount = tree.getInternalNodeCount();
        double[] stats = new double[internalNodeCount];
        for (int i = 0; i < internalNodeCount; i++) {
            NodeRef node = tree.getInternalNode(i);
            stats[i] = tree.getNodeHeight(node);
        }
        Arrays.sort(stats);
        return stats;
    }

    public void setTaxonList(TaxonList taxonList) {
        throw new UnsupportedOperationException("not implemented in this statistic");
    }

    public void setInteger(int value) {
        throw new UnsupportedOperationException("not implemented in this statistic");
    }

    public void setDouble(double value) {
        throw new UnsupportedOperationException("not implemented in this statistic");
    }

    public void setString(String value) {
        throw new UnsupportedOperationException("not implemented in this statistic");
    }

    public String getSummaryStatisticName() {
        return FACTORY.getSummaryStatisticName();
    }

    public String getSummaryStatisticDescription() {
        return FACTORY.getSummaryStatisticDescription();
    }

    public String getSummaryStatisticReference() {
        return FACTORY.getSummaryStatisticReference();
    }

    public boolean allowsPolytomies() {
        return FACTORY.allowsPolytomies();
    }

    public boolean allowsNonultrametricTrees() {
        return FACTORY.allowsNonultrametricTrees();
    }

    public boolean allowsUnrootedTrees() {
        return FACTORY.allowsUnrootedTrees();
    }

    public Category getCategory() {
        return FACTORY.getCategory();
    }

    public static final Factory FACTORY = new Factory() {

        public TreeSummaryStatistic createStatistic() {
            return new NodeHeights();
        }

        public String getSummaryStatisticName() {
            return "Node Heights";
        }

        public String getSummaryStatisticDescription() {
            return "The height of each internal node in the tree.";
        }

        public String getSummaryStatisticReference() {
            return "-";
        }

        public boolean allowsPolytomies() {
            return true;
        }

        public boolean allowsNonultrametricTrees() {
            return true;
        }

        public boolean allowsUnrootedTrees() {
            return false;
        }

        public Category getCategory() {
            return Category.GENERAL;
        }
    };
}
