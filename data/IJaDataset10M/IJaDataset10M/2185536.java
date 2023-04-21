package dr.app.treestat.statistics;

import dr.evolution.tree.NodeRef;
import dr.evolution.tree.Tree;
import dr.evolution.util.*;
import java.util.*;
import java.lang.reflect.Array;

/**
 *
 * @version $Id: TMRCASummaryStatistic.java,v 1.3 2006/05/09 10:24:27 rambaut Exp $
 *
 * @author Alexei Drummond
 */
public class CladeMeanAttributeStatistic extends AbstractTreeSummaryStatistic {

    private CladeMeanAttributeStatistic() {
        this.attributeName = "";
    }

    public void setTaxonList(TaxonList taxonList) {
        this.taxonList = taxonList;
    }

    public void setString(String value) {
        this.attributeName = value;
    }

    public double[] getSummaryStatistic(Tree tree) {
        NodeRef node;
        if (taxonList == null) {
            node = tree.getRoot();
        } else {
            try {
                Set<String> leafSet = Tree.Utils.getLeavesForTaxa(tree, taxonList);
                node = Tree.Utils.getCommonAncestorNode(tree, leafSet);
                if (node == null) throw new RuntimeException("No clade found that contains " + leafSet);
            } catch (Tree.MissingTaxonException e) {
                throw new RuntimeException("Missing taxon!");
            }
        }
        sumAttribute = 0.0;
        nodeCount = 0;
        getAttribute(tree, node);
        return new double[] { sumAttribute / nodeCount };
    }

    private double sumAttribute;

    private int nodeCount;

    private void getAttribute(Tree tree, NodeRef node) {
        if (!tree.isExternal(node)) {
            for (int i = 0; i < tree.getChildCount(node); i++) {
                getAttribute(tree, tree.getChild(node, i));
            }
        }
        Object item = tree.getNodeAttribute(node, attributeName);
        if (item != null && item instanceof Number) {
            sumAttribute += ((Number) item).doubleValue();
            nodeCount++;
        } else {
        }
    }

    public String getSummaryStatisticName() {
        if (taxonList != null) {
            return attributeName + "(" + taxonList.getId() + ")";
        } else {
            return attributeName + "(root)";
        }
    }

    public String getSummaryStatisticDescription() {
        if (taxonList != null) {
            return "Gets the mean of an attribute for a clade defined by a taxon set";
        }
        return "Gets the mean of an attribute across the tree.";
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
            return new CladeMeanAttributeStatistic();
        }

        public String getSummaryStatisticName() {
            return "Mean Clade Attribute";
        }

        public String getSummaryStatisticDescription() {
            return "Extracts the mean of a named attribute for a clade defined by a taxon set";
        }

        public String getSummaryStatisticReference() {
            return "-";
        }

        public String getValueName() {
            return "The attribute name:";
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

        public boolean allowsWholeTree() {
            return true;
        }

        public boolean allowsTaxonList() {
            return true;
        }

        public boolean allowsString() {
            return true;
        }
    };

    private String attributeName = null;

    private TaxonList taxonList = null;
}
