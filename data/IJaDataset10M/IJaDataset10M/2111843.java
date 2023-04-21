package shellkk.qiq.gui.alg;

import javax.datamining.JDMException;
import javax.datamining.rule.Rule;
import shellkk.qiq.gui.Dialog;
import shellkk.qiq.jdm.modeldetail.tree.DecisionTreeNode;
import shellkk.qiq.jdm.modeldetail.tree.RegressionNode;
import shellkk.qiq.jdm.supervised.regression.RegressionModelImpl;

public class RegressionTreeDialog implements Dialog {

    protected RegressionModelImpl model;

    protected shellkk.qiq.jdm.modeldetail.tree.RegressionTree tree;

    protected RegressionNode node;

    protected Rule rule;

    public Integer getHeight() {
        return 600;
    }

    public String getTitle() {
        return "Regression Tree";
    }

    public Integer getWidth() {
        return 800;
    }

    public boolean isModal() {
        return false;
    }

    public String getName() {
        return "regtreeDialog";
    }

    public void onClose() {
        tree = null;
        node = null;
        rule = null;
    }

    public void onOpen() throws Exception {
    }

    public void showNode() throws Exception {
        rule = node.getRule();
    }

    public String getNodeTitle(DecisionTreeNode node) throws JDMException {
        if (node == null) {
            return null;
        }
        if (node.getParent() == null) {
            return "root";
        }
        if (node.isNumericAttr()) {
            String text = node.getAttrName();
            if (node.getAttrLowerBound() != null) {
                text = node.getAttrLowerBound() + " < " + text;
            }
            if (node.getAttrUpperBound() != null) {
                text = text + " <= " + node.getAttrUpperBound();
            }
            return text;
        } else {
            String text = node.getAttrName();
            if (node.getAttrValues().size() == 1) {
                text = text + " is " + node.getAttrValues().get(0);
            } else {
                text = text + " in " + node.getAttrValues().toString();
            }
            return text;
        }
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public RegressionModelImpl getModel() {
        return model;
    }

    public void setModel(RegressionModelImpl model) {
        this.model = model;
    }

    public shellkk.qiq.jdm.modeldetail.tree.RegressionTree getTree() {
        return tree;
    }

    public void setTree(shellkk.qiq.jdm.modeldetail.tree.RegressionTree tree) {
        this.tree = tree;
    }

    public RegressionNode getNode() {
        return node;
    }

    public void setNode(RegressionNode node) {
        this.node = node;
    }
}
