package org.hip.vif.util.test;

import java.math.BigDecimal;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.vif.bom.Question;
import org.hip.vif.bom.QuestionHome;
import org.hip.vif.bom.SubscriptionHome;
import org.hip.vif.bom.impl.test.DataHouseKeeper;
import org.hip.vif.bom.impl.SubscriberCollector;
import junit.framework.TestCase;

/**
 * @author Benno Luthiger
 * Created on Feb 17, 2004
 */
public class SubscriberCollectorTest extends TestCase {

    DataHouseKeeper data;

    /**
	 * Constructor for SubscriberCollectorTest.
	 * @param name
	 */
    public SubscriberCollectorTest(String name) {
        super(name);
        data = DataHouseKeeper.getInstance();
    }

    protected void setUp() throws Exception {
        super.setUp();
        data.redirectDocRoot();
    }

    protected void tearDown() throws Exception {
        data.deleteAllInAll();
    }

    public void testVisitDomainObject() throws Exception {
        String lMail1 = "mail1@test.org";
        String lMail2 = "mail2@test.org";
        BigDecimal lMemberID1 = new BigDecimal(data.createMember("user1", lMail1));
        BigDecimal lMemberID2 = new BigDecimal(data.createMember("user2", lMail2));
        BigDecimal lQuestionID = new BigDecimal(data.createQuestion("Does this work?", "4.21"));
        data.createSubscription(lQuestionID, lMemberID1, false);
        data.createSubscription(lQuestionID, lMemberID2, true);
        KeyObject lKey = new KeyObjectImpl();
        lKey.setValue(QuestionHome.KEY_ID, lQuestionID);
        Question lQuestion = (Question) data.getQuestionHome().findByKey(lKey);
        SubscriberCollector lCollector = new SubscriberCollector(false);
        lCollector.visitQuestion(lQuestion);
        assertEquals("visited 1", lMail1 + ", " + lMail2, lCollector.getMailAddresses());
        lCollector = new SubscriberCollector(true);
        lCollector.visitQuestion(lQuestion);
        assertEquals("visited 2", lMail1, lCollector.getMailAddresses());
        lCollector.updateSelectionFilter(false);
        lCollector.visitQuestion(lQuestion);
        assertEquals("visited 3", lMail1 + ", " + lMail2, lCollector.getMailAddresses());
    }

    public void testCheckFor() throws Exception {
        String lMail1 = "mail1@test.org";
        String lMail2 = "mail2@test.org";
        String lMail3 = "mail3@test.org";
        String lSeparator = ", ";
        SubscriptionHome lHome = data.getSubscriptionHome();
        int lCount = lHome.getCount();
        BigDecimal lMemberID1 = new BigDecimal(data.createMember("user1", lMail1));
        BigDecimal lMemberID2 = new BigDecimal(data.createMember("user2", lMail2));
        BigDecimal lMemberID3 = new BigDecimal(data.createMember("user3", lMail3));
        BigDecimal lQuestionID = new BigDecimal(data.createQuestion("Does this work?", "4.21"));
        data.createSubscription(lQuestionID, lMemberID1, false);
        data.createSubscription(lQuestionID, lMemberID2, false);
        data.createSubscription(lQuestionID, lMemberID3, false);
        assertEquals("count", lCount + 3, lHome.getCount());
        KeyObject lKey = new KeyObjectImpl();
        lKey.setValue(QuestionHome.KEY_ID, lQuestionID);
        Question lQuestion = (Question) data.getQuestionHome().findByKey(lKey);
        SubscriberCollector lCollector = new SubscriberCollector(false);
        lCollector.visitQuestion(lQuestion);
        assertEquals("visited 1", lMail1 + lSeparator + lMail2 + lSeparator + lMail3, lCollector.getMailAddresses());
        lCollector.checkFor(lMail2);
        assertEquals("visited 2", lMail1 + lSeparator + lMail3, lCollector.getMailAddresses());
        lCollector = new SubscriberCollector(false);
        lCollector.visitQuestion(lQuestion);
        lCollector.checkFor(lMail1);
        assertEquals("visited 3", lMail2 + lSeparator + lMail3, lCollector.getMailAddresses());
        lCollector = new SubscriberCollector(false);
        lCollector.visitQuestion(lQuestion);
        lCollector.checkFor(lMail3);
        assertEquals("visited 4", lMail1 + lSeparator + lMail2, lCollector.getMailAddresses());
        lCollector = new SubscriberCollector(false);
        lCollector.visitQuestion(lQuestion);
        lCollector.checkFor("nothing");
        assertEquals("visited 5", lMail1 + lSeparator + lMail2 + lSeparator + lMail3, lCollector.getMailAddresses());
    }
}
