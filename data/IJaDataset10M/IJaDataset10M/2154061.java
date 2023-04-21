package grammarscope.browser.filter.tree;

import grammarscope.browser.RelationFilter;
import grammarscope.browser.utils.RandomColor;
import grammarscope.parser.MutableGrammaticalRelation;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

/**
 * Tree cell renderer
 * 
 * @author Bernard Bou
 */
public class TreeRenderer extends TreeCell implements TreeCellRenderer {

    private static final long serialVersionUID = 1L;

    /**
	 * Relation filter
	 */
    private final RelationFilter theRelationFilter;

    /**
	 * Display long names
	 */
    public static boolean displayLongName = true;

    /**
	 * Constructor
	 * 
	 * @param theseRelationFilter
	 *            relation filter
	 */
    public TreeRenderer(final RelationFilter theseRelationFilter) {
        this.theRelationFilter = theseRelationFilter;
    }

    @SuppressWarnings("boxing")
    @Override
    public Component getTreeCellRendererComponent(final JTree thisTree, final Object thisValue, @SuppressWarnings("hiding") final boolean isSelected, final boolean expanded, final boolean leaf, final int row, @SuppressWarnings("hiding") final boolean hasFocus) {
        final TreeRelationNode thisNode = (TreeRelationNode) thisValue;
        final MutableGrammaticalRelation thisRelation = thisNode.theRelation;
        final MutableGrammaticalRelation thisParentRelation = thisRelation.getParent();
        this.theTree = thisTree;
        final String thisName = TreeRenderer.displayLongName ? thisRelation.getLongName() : thisRelation.getShortName();
        Boolean thisFlag = this.theRelationFilter.theDisplayMap.get(thisRelation.getId());
        if (thisFlag == null) {
            thisFlag = thisParentRelation == null ? Boolean.FALSE : this.theRelationFilter.theDisplayMap.get(thisParentRelation.getId());
            this.theRelationFilter.theDisplayMap.put(thisRelation.getId(), thisFlag);
        }
        Color thisColor = this.theRelationFilter.theColorMap.get(thisRelation.getId());
        if (thisColor == null) {
            thisColor = new RandomColor(thisParentRelation == null ? null : this.theRelationFilter.theColorMap.get(thisParentRelation.getId()));
            this.theRelationFilter.theColorMap.put(thisRelation.getId(), thisColor);
        }
        setText(thisName);
        setColor(thisColor);
        setTicked(thisFlag == null ? false : thisFlag);
        setIsSelected(isSelected);
        setHasFocus(hasFocus);
        setToolTipText(thisRelation.getTreeName(false, false));
        return this;
    }
}
