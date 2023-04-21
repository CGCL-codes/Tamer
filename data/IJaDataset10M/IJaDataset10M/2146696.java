package org.vikamine.gui.subgroup.view.currentSubgroup;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.vikamine.app.VIKAMINE;
import org.vikamine.kernel.subgroup.selectors.SGNominalSelector;
import org.vikamine.kernel.subgroup.target.BooleanFormulaTarget;
import org.vikamine.kernel.subgroup.target.NumericTarget;
import org.vikamine.kernel.subgroup.target.SelectorTarget;

/**
 * @author Tobias Vogele
 */
public class SubgroupRenderer extends DefaultTreeCellRenderer {

    /** generated */
    private static final long serialVersionUID = -92868957595882225L;

    private final String emptyTargetText = VIKAMINE.I18N.getString("vikamine.subgroupTree.noTarget");

    public SubgroupRenderer() {
        setLeafIcon(null);
        setClosedIcon(null);
        setOpenIcon(null);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object nodeValue, boolean sel, boolean expanded, boolean leaf, int row, boolean hasNowFocus) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodeValue;
        Object value = node.getUserObject();
        String text;
        if (value instanceof SGNominalSelector) {
            text = ((SGNominalSelector) value).getDescription();
        } else if (value instanceof SelectorTarget) {
            text = ((SelectorTarget) value).getSelector().getDescription();
        } else if (value instanceof BooleanFormulaTarget) {
            text = ((BooleanFormulaTarget) value).getDescription();
        } else if (value == null) {
            text = emptyTargetText;
        } else if (value instanceof NumericTarget) {
            text = ((NumericTarget) value).getDescription();
        } else {
            assert false : "unknown user-object: " + value;
            text = "?";
        }
        return super.getTreeCellRendererComponent(tree, text, sel, expanded, leaf, row, hasNowFocus);
    }
}
