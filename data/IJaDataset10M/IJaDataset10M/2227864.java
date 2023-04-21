package net.taylor.worklist.entity.editor;

import net.taylor.richfaces.EntityTreeNode;
import net.taylor.richfaces.FolderTreeNode;
import net.taylor.worklist.entity.Comment;
import net.taylor.richfaces.TreeNode;

/**
 * The implementation of a RichFaces TreeNode.
 *
 * @author jgilbert01
 * @version $Id: CommentTreeNode.java,v 1.5 2008/06/04 18:16:19 jgilbert01 Exp $
 * @generated
 */
public class CommentTreeNode extends EntityTreeNode<Comment> {

    /** @generated */
    public CommentTreeNode(TreeNode parent, Comment entity) {
        super(parent, entity);
    }

    /** @NOT generated */
    public String getText() {
        return getInstance().getUser() + " - " + getInstance().getTimestamp();
    }

    protected String getEditorName() {
        return "taskCommentEditor";
    }

    protected String getFinderName() {
        return "taskCommentFinder";
    }

    public String getViewPath() {
        return "bpm/";
    }

    protected String getEditOutcome() {
        return "editTaskComment";
    }

    protected String getViewOutcome() {
        return "viewTaskComment";
    }

    /** @generated */
    public static class CommentFolderTreeNode extends FolderTreeNode<Comment> {

        /** @generated */
        public CommentFolderTreeNode(TreeNode parent, String text, CardinalityType type) {
            super(parent, text, type);
        }

        /** @generated */
        public EntityTreeNode addChild(Comment newInstance) {
            return new CommentTreeNode(this, newInstance);
        }

        /** @generated */
        public void removeChild2(Comment instance) {
            removeChild(instance.getId());
        }

        protected String getEditorName() {
            return "taskCommentEditor";
        }

        protected String getFinderName() {
            return "taskCommentFinder";
        }

        protected String getInstanceName() {
            return "taskComment";
        }

        public String getViewPath() {
            return "bpm/";
        }
    }
}
