package org.telscenter.sail.webapp.domain.premadecomment.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.sf.sail.webapp.domain.User;
import net.sf.sail.webapp.domain.impl.UserImpl;
import org.telscenter.sail.webapp.domain.premadecomment.PremadeComment;
import org.telscenter.sail.webapp.domain.premadecomment.PremadeCommentList;
import junit.framework.TestCase;

/**
 * @author patrick lawler
 *
 */
public class PremadeCommentListImplTest extends TestCase {

    private User user;

    private String label;

    private Set<PremadeComment> commentList;

    private PremadeComment currentComment;

    private PremadeCommentList premadeCommentList;

    private static final String[] comments = { "great job", "good job", "awesome" };

    @Override
    protected void setUp() {
        premadeCommentList = new PremadeCommentListImpl();
        commentList = new TreeSet<PremadeComment>();
        for (String comment : comments) {
            user = new UserImpl();
            currentComment = new PremadeCommentImpl();
            currentComment.setLabel("good comment");
            currentComment.setOwner(user);
            currentComment.setComment(comment);
            commentList.add(currentComment);
        }
        user = new UserImpl();
        label = "good comments";
        premadeCommentList.setLabel(label);
        premadeCommentList.setOwner(user);
        premadeCommentList.setPremadeCommentList(commentList);
    }

    @Override
    protected void tearDown() {
        user = null;
        label = null;
        currentComment = null;
        commentList = null;
        premadeCommentList = null;
    }

    public void testPremadeCommentList() {
        assertEquals(premadeCommentList.getPremadeCommentList(), commentList);
    }
}
