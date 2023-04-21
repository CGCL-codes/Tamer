package org.hip.vif.bom.impl.test;

import java.util.Iterator;
import java.util.Vector;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.exc.VException;
import org.hip.vif.bom.SubscriptionHome;
import org.hip.vif.bom.impl.JoinSubscriptionToQuestionHome;
import junit.framework.TestCase;

/**
 * 
 * 
 * @author Benno Luthiger
 * Created on Feb 29, 2004
 */
public class JoinSubscriptionToQuestionHomeTest extends TestCase {

    DataHouseKeeper data;

    private class JoinSubscriptionToQuestionHomeSub extends JoinSubscriptionToQuestionHome {

        public JoinSubscriptionToQuestionHomeSub() {
            super();
        }

        public Vector<Object> createTestObjects() {
            Vector<Object> outTest = new Vector<Object>();
            try {
                KeyObject lKey = new KeyObjectImpl();
                lKey.setValue(SubscriptionHome.KEY_MEMBERID, new Integer(76));
                outTest.add(createSelectString(lKey));
            } catch (VException exc) {
                fail(exc.getMessage());
            }
            return outTest;
        }
    }

    /**
	 * Constructor for JoinSubscriptionToMemberHomeTest.
	 * @param name
	 */
    public JoinSubscriptionToQuestionHomeTest(String name) {
        super(name);
        data = DataHouseKeeper.getInstance();
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testObjects() {
        String lExpected = "";
        if (data.isDBMySQL()) {
            lExpected = "SELECT tblSubscription.MEMBERID, tblSubscription.BLOCAL, tblQuestion.QUESTIONID, tblQuestion.SQUESTIONID, tblQuestion.SQUESTION, tblQuestion.SREMARK, tblQuestion.NSTATE FROM tblSubscription INNER JOIN tblQuestion ON tblSubscription.QUESTIONID = tblQuestion.QUESTIONID WHERE tblSubscription.MEMBERID = 76";
        } else if (data.isDBOracle()) {
            lExpected = "";
        }
        JoinSubscriptionToQuestionHome lSubHome = new JoinSubscriptionToQuestionHomeSub();
        Iterator<Object> lTest = lSubHome.getTestObjects();
        assertEquals("test object", lExpected, (String) lTest.next());
    }
}
