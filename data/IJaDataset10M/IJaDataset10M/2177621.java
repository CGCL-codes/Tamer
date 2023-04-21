package org.hip.vif.bom.impl.test;

import java.math.BigDecimal;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.vif.bom.CompletionAuthorReviewerHome;
import org.hip.vif.bom.ResponsibleHome;
import org.hip.vif.bom.impl.BOMHelper;
import junit.framework.TestCase;

/**
 * 
 * Created on 29.05.2003
 * @author Luthiger
 */
public class CompletionAuthorReviewerHomeImplTest extends TestCase {

    DataHouseKeeper data;

    /**
	 * Constructor for CompletionAuthorReviewerHomeImplTest.
	 * @param arg0
	 */
    public CompletionAuthorReviewerHomeImplTest(String arg0) {
        super(arg0);
        data = DataHouseKeeper.getInstance();
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        data.deleteAllFromCompletionAuthorReviewer();
    }

    public void testSetAuthorReviewer() {
        Long lAuthorID = new Long(22);
        Long lReviewerID = new Long(23);
        BigDecimal lContributionID = new BigDecimal(33);
        try {
            CompletionAuthorReviewerHome lAuthorReviewerHome = (CompletionAuthorReviewerHome) BOMHelper.getCompletionAuthorReviewerHome();
            int lCount = lAuthorReviewerHome.getCount();
            lAuthorReviewerHome.setAuthor(lAuthorID, lContributionID);
            assertEquals("count authors 1", lCount + 1, lAuthorReviewerHome.getCount());
            KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(ResponsibleHome.KEY_MEMBER_ID, lAuthorID);
            lKey.setValue(CompletionAuthorReviewerHome.KEY_COMPLETION_ID, lContributionID);
            DomainObject lAuthorReviewer = lAuthorReviewerHome.findByKey(lKey);
            assertEquals("is author", ResponsibleHome.VALUE_AUTHOR.toString(), lAuthorReviewer.get(ResponsibleHome.KEY_TYPE).toString());
            lAuthorReviewerHome.setReviewer(lReviewerID, lContributionID);
            assertEquals("count authors 2", lCount + 2, lAuthorReviewerHome.getCount());
            lKey = new KeyObjectImpl();
            lKey.setValue(ResponsibleHome.KEY_MEMBER_ID, lReviewerID);
            lKey.setValue(CompletionAuthorReviewerHome.KEY_COMPLETION_ID, lContributionID);
            lAuthorReviewer = lAuthorReviewerHome.findByKey(lKey);
            assertEquals("is reviewer", ResponsibleHome.VALUE_REVIEWER.toString(), lAuthorReviewer.get(ResponsibleHome.KEY_TYPE).toString());
        } catch (Exception exc) {
            fail(exc.getMessage());
        }
    }
}
