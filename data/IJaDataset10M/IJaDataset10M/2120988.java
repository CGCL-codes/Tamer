package com.novasurv.turtle.backend.project;

import java.io.Serializable;

/**
 * Represents a general comment made on the project. This may be expanded in the future
 * to explicitly track code snippets and source file, but for now all of that information
 * will be embedded into the comment itself.
 *
 * @author Jason Dobies
 */
public class CommentItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String comment;

    public CommentItem() {
    }

    public CommentItem(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
