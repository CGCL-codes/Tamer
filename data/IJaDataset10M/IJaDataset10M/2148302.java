package seepeople;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.concordion.integration.junit3.ConcordionTestCase;
import com.simplefun.seepeople.ContactOpportunityDao;
import com.simplefun.seepeople.KeepInTouch;

public class ClosenessDisplayedTest extends ConcordionTestCase {

    private ContactOpportunityDao dao;

    public int getNumberOfContactVisitsSinceLastContacted(String contactName, int visitsSinceLastContacted) {
        setUpDaoToReturnNumberOfVisitsForContact(visitsSinceLastContacted, contactName);
        KeepInTouch keepInTouch = new KeepInTouch();
        keepInTouch.setDao(dao);
        keepInTouch.setContactName(contactName);
        return keepInTouch.getVisitsSinceLastContacted();
    }

    private void setUpDaoToReturnNumberOfVisitsForContact(int visitsSinceLastContacted, String contactName) {
        dao = mock(ContactOpportunityDao.class);
        when(dao.getNumberOfOpportunitiesToContactSinceLastContacted(contactName)).thenReturn(visitsSinceLastContacted);
    }
}
