package net.taylor.worklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.persistence.EntityManager;
import net.taylor.worklist.entity.Comment;
import net.taylor.worklist.entity.Task;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

/**
 * View and add comments to a task.
 * 
 * @author jgilbert01
 */
@Name("taskCommentAction")
@Scope(ScopeType.CONVERSATION)
public class TaskCommentAction {

    @Logger
    private Log log;

    @In
    protected Task currentTask;

    @In
    private EntityManager bpmEntityManager;

    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String saveComment() {
        log.debug("saveTaskComment: #0", comment);
        if (comment != null && comment.length() > 0) {
            Comment c = new Comment();
            c.setText(comment);
            c.setUser(Identity.instance().getCredentials().getUsername());
            currentTask.addComment(c);
        }
        comment = null;
        bpmEntityManager.flush();
        return null;
    }

    public List<Comment> getComments() {
        List<Comment> comments = new ArrayList<Comment>();
        if (currentTask != null) {
            comments.addAll(currentTask.getComments());
            Collections.sort(comments, new Comparator<Comment>() {

                public int compare(Comment lh, Comment rh) {
                    return lh.getTimestamp().compareTo(rh.getTimestamp());
                }
            });
        }
        return comments;
    }
}
