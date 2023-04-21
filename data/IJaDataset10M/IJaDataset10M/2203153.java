package snap;

import beast.core.Description;
import beast.core.Input;
import beast.core.Input.Validate;
import beast.evolution.tree.Node;
import beast.core.parameter.RealParameter;
import beast.evolution.tree.Tree;

@Description("Represents population size associated with each node in a tree.")
public class GammaParameter extends RealParameter {

    public Input<Tree> m_pTree = new Input<Tree>("tree", "tree associated with this (array) parameter", Validate.REQUIRED);

    public Input<Boolean> m_bInitFromTree = new Input<Boolean>("initFromTree", "whether to initialize from starting tree values (if true), or vice versa (if false)");

    public Input<String> m_pPattern = new Input<String>("pattern", "pattern of metadata element associated with this parameter in the tree");

    public GammaParameter() {
    }

    static Tree m_tree;

    @Override
    public void initAndValidate() throws Exception {
        if (lowerValueInput.get() != null) {
            m_fLower = lowerValueInput.get();
        } else {
            m_fLower = Double.NEGATIVE_INFINITY;
        }
        if (upperValueInput.get() != null) {
            m_fUpper = upperValueInput.get();
        } else {
            m_fUpper = Double.POSITIVE_INFINITY;
        }
        m_tree = m_pTree.get();
        if (m_bInitFromTree.get() == true) {
            values = new Double[m_tree.getNodeCount()];
            m_tree.getMetaData(m_tree.getRoot(), values, m_pPattern.get());
            m_nDimension.setValue(new Integer(values.length), this);
        } else {
            values = new Double[m_tree.getNodeCount()];
            m_nDimension.setValue(new Integer(values.length), this);
            String sValue = m_pValues.get();
            sValue = sValue.replaceAll("^\\s+", "");
            sValue = sValue.replaceAll("\\s+$", "");
            String[] sValues = sValue.split("\\s+");
            for (int i = 0; i < values.length; i++) {
                values[i] = new Double(sValues[i % sValues.length]);
            }
            m_tree.setMetaData(m_tree.getRoot(), values, m_pPattern.get());
        }
        m_bIsDirty = new boolean[m_nDimension.get()];
        m_pTree.setValue(null, this);
    }

    @Override
    public GammaParameter copy() {
        GammaParameter copy = new GammaParameter();
        copy.m_sID = m_sID;
        copy.values = new Double[values.length];
        System.arraycopy(values, 0, copy.values, 0, values.length);
        copy.m_bIsDirty = new boolean[values.length];
        copy.m_fLower = m_fLower;
        copy.m_fUpper = m_fUpper;
        copy.index = index;
        copy.m_pTree = m_pTree;
        copy.m_pPattern = m_pPattern;
        return copy;
    }

    public void prepare() {
        Tree tree = (Tree) m_tree.getCurrent();
        Node node = tree.getRoot();
        syncTree(node, values, m_pPattern.get());
    }

    void syncTree(Node node, Double[] fValues, String sPattern) {
        node.setMetaData(sPattern, fValues[Math.abs(node.getNr())]);
        if (!node.isLeaf()) {
            syncTree(node.getLeft(), fValues, sPattern);
            if (node.getRight() != null) {
                syncTree(node.getRight(), fValues, sPattern);
            }
        }
    }
}
