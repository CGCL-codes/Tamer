package grammarscope.dependency.filter;

import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.util.Pair;
import grammarscope.dependency.ChangeFirer;
import grammarscope.dependency.RelationFilter;
import grammarscope.dependency.RelationModel;
import java.util.Enumeration;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class TreeModel extends DefaultTreeModel {

    private static final long serialVersionUID = 1L;

    /**
	 * Relation model
	 */
    private RelationModel theRelationModel;

    /**
	 * Relation data
	 */
    private RelationFilter theRelationFilter;

    /**
	 * Change firer
	 */
    private final ChangeFirer theChangeFirer;

    /**
	 * Propagate behaviour
	 */
    public boolean propagate = true;

    /**
	 * Constructor
	 */
    public TreeModel() {
        super(null);
        this.theChangeFirer = new ChangeFirer();
    }

    /**
	 * Set relation model
	 * 
	 * @param thisRelationModel
	 *            relation model
	 */
    public void set(final RelationModel thisRelationModel) {
        this.theRelationModel = thisRelationModel;
        setRoot(TreeModelAdapter.make(thisRelationModel));
    }

    /**
	 * Set relation data
	 * 
	 * @param theseRelationFilter
	 *            relation data
	 */
    public void set(final RelationFilter theseRelationFilter) {
        this.theRelationFilter = theseRelationFilter;
    }

    /**
	 * Get relation data
	 * 
	 * @return relation model
	 */
    public RelationModel getRelationModel() {
        return this.theRelationModel;
    }

    /**
	 * Get relation filter
	 * 
	 * @return relation filter
	 */
    public RelationFilter getRelationFilter() {
        return this.theRelationFilter;
    }

    /**
	 * Fire subtree changed
	 * 
	 * @param thisNode
	 *            subtree root
	 */
    void fireTreeNodeChanged(final TreeNode thisNode) {
        final TreeNode thisParent = thisNode.getParent();
        final Object thisSource = thisParent;
        final Object[] thisPath = this.getPathToRoot(thisParent);
        final int[] theseChildIndices = { thisParent.getIndex(thisNode) };
        final Object[] theChildren = { thisNode };
        fireTreeNodesChanged(thisSource, thisPath, theseChildIndices, theChildren);
    }

    /**
	 * Fire subtree children changed
	 * 
	 * @param thisNode
	 *            subtree root
	 */
    @SuppressWarnings("unchecked")
    void fireTreeChildrenNodesChanged(final TreeNode thisNode) {
        final Object thisSource = thisNode;
        final Object[] thisPath = this.getPathToRoot(thisNode);
        for (final Enumeration<TreeNode> theseChildren = thisNode.children(); theseChildren.hasMoreElements(); ) {
            final TreeNode thisChild = theseChildren.nextElement();
            final int[] theseChildIndices = { thisNode.getIndex(thisChild) };
            final Object[] theChildren = { thisChild };
            fireTreeNodesChanged(thisSource, thisPath, theseChildIndices, theChildren);
            fireTreeChildrenNodesChanged(thisChild);
        }
    }

    /**
	 * Set display flag for all relations
	 * 
	 * @param thisFlag
	 *            display flag
	 */
    @SuppressWarnings("boxing")
    public void setAll(final boolean thisFlag) {
        for (final GrammaticalRelation thisRelation : this.theRelationModel.theRelations) {
            this.theRelationFilter.theMap.put(thisRelation.getShortName(), thisFlag);
        }
        fireTreeChildrenNodesChanged((TreeNode) getRoot());
    }

    /**
	 * Set display flag for specified relations
	 * 
	 * @param thisFlag
	 *            display flag
	 * @param theseIds
	 *            relations to set flag for
	 */
    @SuppressWarnings({ "boxing", "unchecked" })
    public void setGroup(final boolean thisFlag, final String[] theseIds) {
        for (final String thisId : theseIds) {
            this.theRelationFilter.theMap.put(thisId, thisFlag);
            final DefaultMutableTreeNode thisRoot = (DefaultMutableTreeNode) getRoot();
            for (final Enumeration<DefaultMutableTreeNode> thisEnum = thisRoot.breadthFirstEnumeration(); thisEnum.hasMoreElements(); ) {
                final DefaultMutableTreeNode thisNode = thisEnum.nextElement();
                final GrammaticalRelation thisRelation = (GrammaticalRelation) thisNode.getUserObject();
                if (thisRelation.getShortName().equals(thisId)) {
                    fireTreeNodeChanged(thisNode);
                    break;
                }
            }
        }
    }

    @Override
    public void valueForPathChanged(final TreePath thisPath, final Object thisNewValue) {
        final Boolean thisValue = (Boolean) thisNewValue;
        final DefaultMutableTreeNode thisTreeNode = (DefaultMutableTreeNode) thisPath.getLastPathComponent();
        final GrammaticalRelation thisGrammaticalRelation = (GrammaticalRelation) thisTreeNode.getUserObject();
        if (thisValue != null) {
            this.theRelationFilter.theMap.put(thisGrammaticalRelation.getShortName(), thisValue);
            this.theChangeFirer.fireStateChanged(new ChangeEvent(new Pair<GrammaticalRelation, Boolean>(thisGrammaticalRelation, thisValue)));
            if (this.propagate) {
                for (@SuppressWarnings("unchecked") final Enumeration<TreeNode> thisChildEnum = thisTreeNode.children(); thisChildEnum.hasMoreElements(); ) {
                    final TreeNode thisChildTreeNode = thisChildEnum.nextElement();
                    final TreePath thisChildPath = thisPath.pathByAddingChild(thisChildTreeNode);
                    valueForPathChanged(thisChildPath, thisValue);
                }
            }
        }
        nodeChanged(thisTreeNode);
    }

    /**
	 * Add change Listener
	 * 
	 * @param thisListener
	 *            change listener
	 */
    public void addChangeListener(final ChangeListener thisListener) {
        this.theChangeFirer.addChangeListener(thisListener);
    }

    /**
	 * Remove change Listener
	 * 
	 * @param thisListener
	 *            change listener
	 */
    public void removeChangeListener(final ChangeListener thisListener) {
        this.theChangeFirer.removeChangeListener(thisListener);
    }
}
