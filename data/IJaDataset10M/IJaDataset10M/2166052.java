package net.taylor.audit.entity.editor;

import net.taylor.audit.entity.Activity;
import net.taylor.richfaces.EntityTreeNode;
import net.taylor.richfaces.FolderTreeNode;
import net.taylor.richfaces.TreeNode;

/**
 * The implementation of a RichFaces TreeNode.
 *
 * @author jgilbert
 * @generated
 */
public class ActivityTreeNode extends EntityTreeNode<Activity> {

    /** @generated */
    public ActivityTreeNode(TreeNode parent, Activity entity) {
        super(parent, entity);
    }

    /** @NOT generated */
    public String getText() {
        return getInstance().getTitle();
    }

    /** @generated */
    public static class ActivityFolderTreeNode extends FolderTreeNode<Activity> {

        /** @generated */
        public ActivityFolderTreeNode(TreeNode parent, String text, CardinalityType type) {
            super(parent, text, type);
        }

        /** @generated */
        public EntityTreeNode addChild(Activity newInstance) {
            return new ActivityTreeNode(this, newInstance);
        }

        /** @generated */
        public void removeChild2(Activity instance) {
            removeChild(instance.getId());
        }
    }
}
