package org.hip.vif.util.test;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import junit.framework.TestCase;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.exc.VException;
import org.hip.vif.bom.Completion;
import org.hip.vif.bom.CompletionAuthorReviewerHome;
import org.hip.vif.bom.CompletionHome;
import org.hip.vif.bom.ParticipantHome;
import org.hip.vif.bom.Question;
import org.hip.vif.bom.QuestionAuthorReviewerHome;
import org.hip.vif.bom.QuestionHome;
import org.hip.vif.bom.ResponsibleHome;
import org.hip.vif.bom.Text;
import org.hip.vif.bom.TextAuthorReviewerHome;
import org.hip.vif.bom.TextHome;
import org.hip.vif.bom.impl.WorkflowAwareContribution;
import org.hip.vif.bom.impl.test.DataHouseKeeper;
import org.hip.vif.search.test.IndexHouseKeeper;
import org.hip.vif.util.StaleRequestRemover;

/**
 * @author Luthiger
 * Created: 13.10.2010
 */
public class StaleRequestRemoverTest extends TestCase {

    private static final long ONE_DAY = 1000 * 60 * 60 * 24;

    private DataHouseKeeper data;

    public StaleRequestRemoverTest(String name) {
        super(name);
        data = DataHouseKeeper.getInstance();
    }

    protected void setUp() throws Exception {
        super.setUp();
        IndexHouseKeeper.redirectDocRoot(false);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        IndexHouseKeeper.deleteTestIndexDir();
        data.deleteAllInAll();
    }

    public void testRemoveStaleRequests() throws Exception {
        String[] lMemberIDs = data.create2Members();
        String lQuestionID1 = data.createQuestion("Question 1", "1:2");
        String lQuestionID2 = data.createQuestion("Question 2", "1:2.3");
        setQuestionState(lQuestionID2);
        String lCompletionID1 = data.createCompletion("Completion 1", lQuestionID1);
        String lCompletionID2 = data.createCompletion("Completion 2", lQuestionID1);
        setCompletionState(lCompletionID2);
        String lTextID1 = data.createText("Text 1", "Foo, Jane");
        String lTextID2 = data.createText("Text 2", "Doe, Jon");
        setTextState(lTextID2);
        long lNow = System.currentTimeMillis();
        createAuthorReviewerEntry(data.getQuestionAuthorReviewerHome().create(), QuestionAuthorReviewerHome.KEY_QUESTION_ID, lQuestionID1, lMemberIDs[0], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - ONE_DAY));
        createAuthorReviewerEntry(data.getQuestionAuthorReviewerHome().create(), QuestionAuthorReviewerHome.KEY_QUESTION_ID, lQuestionID2, lMemberIDs[0], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - ONE_DAY));
        createAuthorReviewerEntry(data.getQuestionAuthorReviewerHome().create(), QuestionAuthorReviewerHome.KEY_QUESTION_ID, lQuestionID1, lMemberIDs[1], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - (3 * ONE_DAY)));
        createAuthorReviewerEntry(data.getQuestionAuthorReviewerHome().create(), QuestionAuthorReviewerHome.KEY_QUESTION_ID, lQuestionID2, lMemberIDs[1], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - (3 * ONE_DAY)));
        createAuthorReviewerEntry(data.getCompletionAuthorReviewerHome().create(), CompletionAuthorReviewerHome.KEY_COMPLETION_ID, lCompletionID1, lMemberIDs[0], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - ONE_DAY));
        createAuthorReviewerEntry(data.getCompletionAuthorReviewerHome().create(), CompletionAuthorReviewerHome.KEY_COMPLETION_ID, lCompletionID2, lMemberIDs[0], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - ONE_DAY));
        createAuthorReviewerEntry(data.getCompletionAuthorReviewerHome().create(), CompletionAuthorReviewerHome.KEY_COMPLETION_ID, lCompletionID1, lMemberIDs[1], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - (3 * ONE_DAY)));
        createAuthorReviewerEntry(data.getCompletionAuthorReviewerHome().create(), CompletionAuthorReviewerHome.KEY_COMPLETION_ID, lCompletionID2, lMemberIDs[1], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - (3 * ONE_DAY)));
        createTextAuthorReviewerEntry(lTextID1, lMemberIDs[0], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - ONE_DAY));
        createTextAuthorReviewerEntry(lTextID2, lMemberIDs[0], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - ONE_DAY));
        createTextAuthorReviewerEntry(lTextID1, lMemberIDs[1], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - (3 * ONE_DAY)));
        createTextAuthorReviewerEntry(lTextID2, lMemberIDs[1], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - (3 * ONE_DAY)));
        assertEquals(4, data.getQuestionAuthorReviewerHome().getCount());
        assertEquals(4, data.getCompletionAuthorReviewerHome().getCount());
        assertEquals(4, data.getTextAuthorReviewerHome().getCount());
        KeyObject lKeyReviewer = new KeyObjectImpl();
        lKeyReviewer.setValue(ResponsibleHome.KEY_TYPE, ResponsibleHome.Type.REVIEWER.getValue());
        assertEquals(4, data.getQuestionAuthorReviewerHome().getCount(lKeyReviewer));
        assertEquals(4, data.getCompletionAuthorReviewerHome().getCount(lKeyReviewer));
        assertEquals(4, data.getTextAuthorReviewerHome().getCount(lKeyReviewer));
        StaleRequestRemoverSub lRemover = new StaleRequestRemoverSub(new Date(lNow - (2 * ONE_DAY)));
        lRemover.removeStaleRequests();
        assertEquals(4, data.getQuestionAuthorReviewerHome().getCount());
        assertEquals(4, data.getCompletionAuthorReviewerHome().getCount());
        assertEquals(4, data.getTextAuthorReviewerHome().getCount());
        assertEquals(3, data.getQuestionAuthorReviewerHome().getCount(lKeyReviewer));
        assertEquals(3, data.getCompletionAuthorReviewerHome().getCount(lKeyReviewer));
        assertEquals(3, data.getTextAuthorReviewerHome().getCount(lKeyReviewer));
        KeyObject lKeyRefused = new KeyObjectImpl();
        lKeyRefused.setValue(ResponsibleHome.KEY_TYPE, ResponsibleHome.Type.REVIEWER_REFUSED.getValue());
        assertEquals(1, data.getQuestionAuthorReviewerHome().getCount(lKeyRefused));
        assertEquals(1, data.getCompletionAuthorReviewerHome().getCount(lKeyRefused));
        assertEquals(1, data.getTextAuthorReviewerHome().getCount(lKeyRefused));
    }

    public void testCreateNewRequests1() throws Exception {
        String[] lMemberIDs = data.create2Members();
        String lQuestionID = data.createQuestion("Question 2", "1:2.3");
        setQuestionState(lQuestionID);
        String lCompletionID = data.createCompletion("Completion 2", lQuestionID);
        setCompletionState(lCompletionID);
        String lTextID = data.createText("Text 2", "Doe, Jon");
        setTextState(lTextID);
        for (String lMemberID : lMemberIDs) {
            createParticipant(lMemberID);
        }
        long lNow = System.currentTimeMillis();
        createAuthorReviewerEntry(data.getQuestionAuthorReviewerHome().create(), QuestionAuthorReviewerHome.KEY_QUESTION_ID, lQuestionID, lMemberIDs[0], ResponsibleHome.Type.AUTHOR.getValue(), (lNow - (3 * ONE_DAY)));
        createAuthorReviewerEntry(data.getQuestionAuthorReviewerHome().create(), QuestionAuthorReviewerHome.KEY_QUESTION_ID, lQuestionID, lMemberIDs[1], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - (3 * ONE_DAY)));
        createAuthorReviewerEntry(data.getCompletionAuthorReviewerHome().create(), CompletionAuthorReviewerHome.KEY_COMPLETION_ID, lCompletionID, lMemberIDs[0], ResponsibleHome.Type.AUTHOR.getValue(), (lNow - (3 * ONE_DAY)));
        createAuthorReviewerEntry(data.getCompletionAuthorReviewerHome().create(), CompletionAuthorReviewerHome.KEY_COMPLETION_ID, lCompletionID, lMemberIDs[1], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - (3 * ONE_DAY)));
        createTextAuthorReviewerEntry(lTextID, lMemberIDs[0], ResponsibleHome.Type.AUTHOR.getValue(), (lNow - (3 * ONE_DAY)));
        createTextAuthorReviewerEntry(lTextID, lMemberIDs[1], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - (3 * ONE_DAY)));
        KeyObject lKeyReviewer = new KeyObjectImpl();
        lKeyReviewer.setValue(ResponsibleHome.KEY_TYPE, ResponsibleHome.Type.REVIEWER.getValue());
        assertEquals(1, data.getQuestionAuthorReviewerHome().getCount(lKeyReviewer));
        assertEquals(1, data.getCompletionAuthorReviewerHome().getCount(lKeyReviewer));
        assertEquals(1, data.getTextAuthorReviewerHome().getCount(lKeyReviewer));
        StaleRequestRemoverSub lRemover = new StaleRequestRemoverSub(new Date(lNow - (2 * ONE_DAY)));
        lRemover.removeStaleRequests();
        lRemover.createNewRequests();
        assertEquals(0, data.getQuestionAuthorReviewerHome().getCount(lKeyReviewer));
        assertEquals(0, data.getCompletionAuthorReviewerHome().getCount(lKeyReviewer));
        assertEquals(0, data.getTextAuthorReviewerHome().getCount(lKeyReviewer));
    }

    public void testCreateNewRequests2() throws Exception {
        String[] lMemberIDs = data.create3Members();
        String lQuestionID = data.createQuestion("Question 2", "1:2.3");
        setQuestionState(lQuestionID);
        String lCompletionID = data.createCompletion("Completion 2", lQuestionID);
        setCompletionState(lCompletionID);
        String lTextID = data.createText("Text 2", "Doe, Jon");
        setTextState(lTextID);
        for (String lMemberID : lMemberIDs) {
            createParticipant(lMemberID);
        }
        long lNow = System.currentTimeMillis();
        createAuthorReviewerEntry(data.getQuestionAuthorReviewerHome().create(), QuestionAuthorReviewerHome.KEY_QUESTION_ID, lQuestionID, lMemberIDs[0], ResponsibleHome.Type.AUTHOR.getValue(), (lNow - (3 * ONE_DAY)));
        createAuthorReviewerEntry(data.getQuestionAuthorReviewerHome().create(), QuestionAuthorReviewerHome.KEY_QUESTION_ID, lQuestionID, lMemberIDs[1], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - (3 * ONE_DAY)));
        createAuthorReviewerEntry(data.getCompletionAuthorReviewerHome().create(), CompletionAuthorReviewerHome.KEY_COMPLETION_ID, lCompletionID, lMemberIDs[0], ResponsibleHome.Type.AUTHOR.getValue(), (lNow - (3 * ONE_DAY)));
        createAuthorReviewerEntry(data.getCompletionAuthorReviewerHome().create(), CompletionAuthorReviewerHome.KEY_COMPLETION_ID, lCompletionID, lMemberIDs[1], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - (3 * ONE_DAY)));
        createTextAuthorReviewerEntry(lTextID, lMemberIDs[0], ResponsibleHome.Type.AUTHOR.getValue(), (lNow - (3 * ONE_DAY)));
        createTextAuthorReviewerEntry(lTextID, lMemberIDs[1], ResponsibleHome.Type.REVIEWER.getValue(), (lNow - (3 * ONE_DAY)));
        KeyObject lKeyReviewer = new KeyObjectImpl();
        lKeyReviewer.setValue(ResponsibleHome.KEY_TYPE, ResponsibleHome.Type.REVIEWER.getValue());
        assertEquals(1, data.getQuestionAuthorReviewerHome().getCount(lKeyReviewer));
        assertEquals(1, data.getCompletionAuthorReviewerHome().getCount(lKeyReviewer));
        assertEquals(1, data.getTextAuthorReviewerHome().getCount(lKeyReviewer));
        StaleRequestRemoverSub lRemover = new StaleRequestRemoverSub(new Date(lNow - (2 * ONE_DAY)));
        lRemover.removeStaleRequests();
        lRemover.createNewRequests();
        if (data.getQuestionAuthorReviewerHome().getCount(lKeyReviewer) == 1) {
            lKeyReviewer.setValue(ResponsibleHome.KEY_MEMBER_ID, new Long(lMemberIDs[2]));
            assertEquals(1, data.getQuestionAuthorReviewerHome().getCount(lKeyReviewer));
            assertEquals(1, data.getCompletionAuthorReviewerHome().getCount(lKeyReviewer));
            assertEquals(1, data.getTextAuthorReviewerHome().getCount(lKeyReviewer));
        }
    }

    private void createParticipant(String inMemberID) throws VException, SQLException {
        DomainObject lParticipant = data.getParticipantHome().create();
        lParticipant.set(ParticipantHome.KEY_MEMBER_ID, new Long(inMemberID));
        lParticipant.set(ParticipantHome.KEY_GROUP_ID, DataHouseKeeper.DFT_GROUP_ID);
        Timestamp lInit = new Timestamp(1000);
        lParticipant.set(ParticipantHome.KEY_SUSPEND_FROM, lInit);
        lParticipant.set(ParticipantHome.KEY_SUSPEND_TO, lInit);
        lParticipant.insert(true);
    }

    private void setQuestionState(String inQuestionID) throws VException, SQLException {
        Question lQuestion = data.getQuestionHome().getQuestion(inQuestionID);
        lQuestion.set(QuestionHome.KEY_STATE, new Long(WorkflowAwareContribution.S_WAITING_FOR_REVIEW));
        lQuestion.update(true);
    }

    private void setCompletionState(String inCompletionID) throws VException, SQLException {
        Completion lCompletion = data.getCompletionHome().getCompletion(inCompletionID);
        lCompletion.set(CompletionHome.KEY_STATE, new Long(WorkflowAwareContribution.S_WAITING_FOR_REVIEW));
        lCompletion.update(true);
    }

    private void setTextState(String inTextID) throws VException, SQLException {
        Text lText = data.getTextHome().getText(inTextID, 0);
        lText.set(TextHome.KEY_STATE, new Long(WorkflowAwareContribution.S_WAITING_FOR_REVIEW));
        lText.update(true);
    }

    private void createAuthorReviewerEntry(DomainObject lEntry, String inFieldName, String inContributionID, String inMemberID, Integer inType, long inTime) throws VException, SQLException {
        lEntry.set(inFieldName, new Long(inContributionID));
        lEntry.set(ResponsibleHome.KEY_MEMBER_ID, new Long(inMemberID));
        lEntry.set(ResponsibleHome.KEY_TYPE, inType);
        lEntry.set(ResponsibleHome.KEY_CREATED, new Timestamp(inTime));
        lEntry.insert(true);
    }

    private void createTextAuthorReviewerEntry(String inTextID, String inMemberID, Integer inType, long inTime) throws VException, SQLException {
        DomainObject lEntry = data.getTextAuthorReviewerHome().create();
        lEntry.set(TextAuthorReviewerHome.KEY_TEXT_ID, new Long(inTextID));
        lEntry.set(TextAuthorReviewerHome.KEY_VERSION, new Long(0));
        lEntry.set(ResponsibleHome.KEY_MEMBER_ID, new Long(inMemberID));
        lEntry.set(ResponsibleHome.KEY_TYPE, inType);
        lEntry.set(ResponsibleHome.KEY_CREATED, new Timestamp(inTime));
        lEntry.insert(true);
    }

    private class StaleRequestRemoverSub extends StaleRequestRemover {

        public StaleRequestRemoverSub(Date inStaleDate) {
            super(inStaleDate);
        }

        @Override
        public void removeStaleRequests() throws Exception {
            super.removeStaleRequests();
        }

        @Override
        protected void sendRequestExpirationNotification(Long inReviewerID) throws Exception {
        }

        @Override
        public void createNewRequests() throws Exception {
            super.createNewRequests();
        }
    }
}
