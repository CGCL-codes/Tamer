package org.zkoss.zorum.bean;

import javax.persistence.Embeddable;

@Embeddable
public class CommentId implements java.io.Serializable {

    private int _commentId;

    private int _discussionId;

    public int getCommentId() {
        return _commentId;
    }

    public void setCommentId(int commentId) {
        _commentId = commentId;
    }

    public int getDiscussionId() {
        return _discussionId;
    }

    public void setDiscussionId(int discussionId) {
        _discussionId = discussionId;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof CommentId)) return false;
        CommentId castOther = (CommentId) other;
        return (getCommentId() == castOther.getCommentId()) && (getDiscussionId() == castOther.getDiscussionId());
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + getCommentId();
        result = 37 * result + getDiscussionId();
        return result;
    }
}
