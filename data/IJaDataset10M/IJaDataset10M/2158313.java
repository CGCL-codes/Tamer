package net.taylor.portal.entity.dashboard.editor;

import net.taylor.portal.entity.dashboard.Panel;
import net.taylor.richfaces.EntityTreeNode;
import net.taylor.richfaces.FolderTreeNode;
import net.taylor.richfaces.TreeNode;

/**
 * The implementation of a RichFaces TreeNode.
 *
 * @author jgilbert
 * @generated
 */
public class PanelTreeNode extends EntityTreeNode<Panel> {

    /** @generated */
    public PanelTreeNode(TreeNode parent, Panel entity) {
        super(parent, entity);
    }

    /** @NOT generated */
    public String getText() {
        return getInstance().getLabel();
    }

    /** @generated */
    public static class PanelFolderTreeNode extends FolderTreeNode<Panel> {

        /** @generated */
        public PanelFolderTreeNode(TreeNode parent, String text, CardinalityType type) {
            super(parent, text, type);
        }

        /** @generated */
        public EntityTreeNode addChild(Panel newInstance) {
            return new PanelTreeNode(this, newInstance);
        }

        /** @generated */
        public void removeChild2(Panel instance) {
            removeChild(instance.getId());
        }
    }
}
