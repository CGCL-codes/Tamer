package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.gui.view.thread.PostTableCellRenderer;
import org.xblackcat.rojac.i18n.Message;

/**
 * Delegate class to identify replies amount on the post for a table renderer.
 *
 * @author xBlackCat
 */
final class PostReplies extends APostProxy {

    public PostReplies(Post post) {
        super(post);
    }

    @Override
    protected void setValue(PostTableCellRenderer renderer, boolean ignored) {
        String replies;
        int repliesAmount = post.getPostAmount();
        if (post.getThreadRoot() != post) {
            if (repliesAmount == 0) {
                replies = "";
            } else {
                replies = String.valueOf(repliesAmount);
            }
        } else {
            Thread t = (Thread) post;
            int unreadPosts = t.getUnreadPosts();
            int unreadReplies = t.getUnreadReplies();
            if (unreadPosts == 0) {
                replies = String.valueOf(repliesAmount);
            } else if (unreadPosts == repliesAmount) {
                if (unreadReplies == 0) {
                    replies = String.valueOf(repliesAmount);
                } else {
                    replies = Message.View_Navigation_Item_ForumInfo_Full.get(unreadReplies, unreadPosts, repliesAmount);
                }
            } else {
                if (unreadReplies == 0) {
                    replies = Message.View_Navigation_Item_ForumInfo.get(unreadPosts, repliesAmount);
                } else {
                    replies = Message.View_Navigation_Item_ForumInfo_Full.get(unreadReplies, unreadPosts, repliesAmount);
                }
            }
        }
        renderer.setText(replies);
    }
}
