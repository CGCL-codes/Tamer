package tests.GUI.GUIChairMaintenance;

import static org.junit.Assert.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import Repository.Entities.Decision;
import Repository.RepositoryChairMaintenance.IPaperFeedback;
import Repository.RepositoryChairMaintenance.PaperFeedback;

public class CommentClassJUnit {

    String SessionId = "0";

    Logic.LogicChairMaintenance.Chair.Chair c = null;

    tests.GUI.GUIChairMaintenance.CommentClass a = new tests.GUI.GUIChairMaintenance.CommentClass(SessionId, c);

    @Test
    public void testCommentClass() {
        Assert.assertEquals(SessionId, a.getSessionId());
        Assert.assertEquals(c, a.getChair());
    }

    @Test
    public void testRefresh() {
        tests.GUI.GUIChairMaintenance.CommentClass b;
        b = new tests.GUI.GUIChairMaintenance.CommentClass(SessionId, c);
        a.initializeD();
        a.refresh();
        Assert.assertEquals(b, a);
    }

    @Test
    public void test1initializeComponent() {
        Assert.assertEquals(true, a.test1initialazeComponent());
    }

    @Test
    public void test1addComponent() {
        tests.GUI.GUIChairMaintenance.CommentClass b;
        b = new tests.GUI.GUIChairMaintenance.CommentClass(SessionId, c);
        Assert.assertEquals(true, a.test1addComponent(b));
    }

    @Test
    public void testpreviousButton_actionPerformed() {
        ActionEvent e = new ActionEvent(a, 0, SessionId);
        Assert.assertEquals(true, a.test1previousButton_actionPerformed(e));
        Assert.assertEquals(true, a.test2previousButton_actionPerformed(e));
    }

    @Test
    public void testnextButton_actionPerformed() {
        ActionEvent e = new ActionEvent(a, 0, SessionId);
        Assert.assertEquals(true, a.test1nextButton_actionPerformed(e));
        Assert.assertEquals(true, a.test2nextButton_actionPerformed(e));
    }

    @Test
    public void testgetCameraArray() {
        LinkedList<IPaperFeedback> p = new LinkedList<IPaperFeedback>();
        PaperFeedback p1 = new PaperFeedback();
        PaperFeedback p2 = new PaperFeedback();
        PaperFeedback p3 = new PaperFeedback();
        p1.setPaperId(0);
        p1.setPaperId(1);
        p1.setPaperId(2);
        p1.setAcceptanceDecision(Decision.Accepted);
        p1.setAcceptanceDecision(Decision.Rejected);
        p1.setAcceptanceDecision(Decision.Undefined);
        p.add(p1);
        p.add(p2);
        p.add(p3);
        Assert.assertEquals(true, a.test1getCameraArray(p));
        Assert.assertEquals(true, a.test2getCameraArray(p));
    }

    @Test
    public void testgetReviewerName() {
        String arevName = a.getReviewerList().get(0).getFirstName() + " " + a.getReviewerList().get(0).getLastName();
        tests.GUI.GUIChairMaintenance.CommentClass b = new tests.GUI.GUIChairMaintenance.CommentClass(SessionId, c);
        String brevName = b.test1getReviewerName(0);
        Assert.assertEquals(a, b);
        Assert.assertEquals(null, a.test2getReviewerName(a.getReviewerListSize() + 1));
        Assert.assertEquals(null, a.test3getReviewerName(-1));
    }

    @Test
    public void tesinitializeData() {
        Assert.assertEquals(true, a.test1initializeData());
        Assert.assertEquals(true, a.test2initializeData());
        Assert.assertEquals(true, a.test3initializeData());
        Assert.assertEquals(true, a.test4initializeData());
    }
}
