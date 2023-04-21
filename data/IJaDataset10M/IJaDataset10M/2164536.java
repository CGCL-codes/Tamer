package org.programmerplanet.intracollab.model.search;

import java.util.Date;
import org.programmerplanet.intracollab.model.Comment;

/**
 * A <code>SearchResult</code> based on a <code>Comment</code>.
 * 
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2010 Joseph Fifield
 */
public class CommentSearchResult implements SearchResult {

    private Comment comment;

    public CommentSearchResult(Comment comment) {
        this.comment = comment;
    }

    /**
	 * @see org.programmerplanet.intracollab.model.search.SearchResult#getDate()
	 */
    public Date getDate() {
        return comment.getCreated();
    }

    /**
	 * @see org.programmerplanet.intracollab.model.search.SearchResult#getDescription()
	 */
    public String getDescription() {
        return "Ticket #" + comment.getTicket().getId() + " commented on by " + comment.getCreatedBy();
    }

    /**
	 * @see org.programmerplanet.intracollab.model.search.SearchResult#getText()
	 */
    public String getText() {
        return comment.getContent();
    }
}
