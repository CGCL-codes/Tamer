package org.hip.vif.bom.impl.test;

import java.util.Iterator;
import java.util.Vector;
import junit.framework.TestCase;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.exc.VException;
import org.hip.vif.bom.QuestionHome;
import org.hip.vif.bom.impl.JoinQuestionToCompletionAndContributorsHome;

/**
 * @author Luthiger
 * Created 20.09.2009
 */
public class JoinQuestionToCompletionAndContributorsHomeTest extends TestCase {

    public JoinQuestionToCompletionAndContributorsHomeTest(String name) {
        super(name);
        DataHouseKeeper.getInstance();
    }

    public void testObject() throws Exception {
        JoinQuestionToCompletionAndContributorsHome lHome = new JoinQuestionToCompletionAndContributorsHomeSub();
        Iterator<Object> lTest = lHome.getTestObjects();
        String lExpected = "SELECT tblQuestion.QUESTIONID, tblQuestion.GROUPID, tblCompletion.COMPLETIONID, tblCompletion.SCOMPLETION, tblCompletion.QUESTIONID, tblCompletion.DTMUTATION, tblCompletion.NSTATE, tblCompletionAuthorReviewer.MEMBERID, tblCompletionAuthorReviewer.NTYPE, tblMember.SNAME, tblMember.SFIRSTNAME FROM tblQuestion INNER JOIN tblCompletion ON tblQuestion.QUESTIONID = tblCompletion.QUESTIONID INNER JOIN tblCompletionAuthorReviewer ON tblCompletion.COMPLETIONID = tblCompletionAuthorReviewer.COMPLETIONID INNER JOIN tblMember ON tblCompletionAuthorReviewer.MEMBERID = tblMember.MEMBERID WHERE tblQuestion.GROUPID = 8";
        assertEquals("test object", lExpected, (String) lTest.next());
    }

    private class JoinQuestionToCompletionAndContributorsHomeSub extends JoinQuestionToCompletionAndContributorsHome {

        @Override
        protected Vector<Object> createTestObjects() {
            Vector<Object> out = new Vector<Object>();
            try {
                KeyObject lKey = new KeyObjectImpl();
                lKey.setValue(QuestionHome.KEY_GROUP_ID, new Long(8));
                out.add(createSelectString(lKey));
            } catch (VException exc) {
                fail(exc.getMessage());
            }
            return out;
        }
    }
}
