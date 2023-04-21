package org.squabble.web.comment;

import java.io.Serializable;

public class TagCommand implements Serializable {

    private static final long serialVersionUID = 3780338297579073758L;

    private Long commentId;

    private String tags;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
