package TestingClasses;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import java.util.Vector;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;
import businessClasses.TicketComment;
import databaseClasses.CommentController;

public class JUnit4DCCommentControllerTester {

    @Test
    public void testCreateNewComment() {
        TicketComment ticketComment = new TicketComment();
        ticketComment.setTicketId(1);
        ticketComment.setOwner("Tester");
        ticketComment.setContent("testing content");
        assertTrue("Failed in Create New Comment!", (CommentController.createNewComment(ticketComment)));
    }

    @Test
    public void testGetTicketComments() {
        Vector<TicketComment> ticketComments = CommentController.getTicketComments(1);
        assertNotNull(ticketComments);
        assertTrue("Failed in Get ticket comments!", (ticketComments.size() > 0));
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(JUnit4DCCommentControllerTester.class);
    }
}
