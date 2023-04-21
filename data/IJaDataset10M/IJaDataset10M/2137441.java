package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import com.meterware.httpunit.TableRow;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * Use Case 6
 * Test designatedand ViewHCPCase 
 * @author student
 * @ author student
 *
 */
public class DesignateAndViewHCPUseCaseTest extends iTrustHTTPTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gen.clearAllTables();
        gen.standardData();
        gen.patient_hcp_vists();
    }

    /**
	 * Test testReportSeenHCPs0
	 * @throws Exception
	 */
    public void testReportSeenHCPs0() throws Exception {
        WebConversation wc = login("2", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertEquals("iTrust - Patient Home", wr.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
        wr = wr.getLinkWith("My Providers").click();
        assertEquals("iTrust - My Providers", wr.getTitle());
        WebTable table = (WebTable) wr.getElementWithID("hcp_table");
        TableRow rows[] = table.getRows();
        assertEquals("| HCP Name | Specialty | Address | Date of Office Visit | Designated HCP?", rows[0].getText());
        assertEquals("| Gandalf Stormcrow | none | 4321 My Road St PO BOX 2 CityName, NY 12345-1234 | 09/14/2009 |", rows[1].getText());
        assertEquals("| Mary Shelley | surgeon | 1313 Cautionary Tale Raleigh, NC 27601 | 07/03/2008 |", rows[2].getText());
        assertEquals("| Lauren Frankenstein | pediatrician | 333 Dark Lane Raleigh, NC 27605 | 06/02/2008 |", rows[3].getText());
        assertEquals("| Jason Frankenstein | surgeon | 333 Dark Lane Raleigh, NC 27603 | 05/01/2008 |", rows[4].getText());
        assertEquals("| Kelly Doctor | surgeon | 4321 My Road St PO BOX 2 CityName, NY 12345-1234 | 06/10/2007 |", rows[5].getText());
    }

    public void testReportSeenHCPs1() throws Exception {
        WebConversation wc = login("2", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertEquals("iTrust - Patient Home", wr.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
        wr = wr.getLinkWith("My Providers").click();
        assertEquals("iTrust - My Providers", wr.getTitle());
        WebForm form = wr.getFormWithName("mainForm");
        form.getScriptableObject().setParameterValue("removeID", "Jason Frankenstein");
        wr = wr.getForms()[0].submit();
        assertLogged(TransactionType.LHCP_VIEW, 2L, 0L, "");
        WebTable table = (WebTable) wr.getElementWithID("hcp_table");
        TableRow rows[] = table.getRows();
        assertEquals("| Jason Frankenstein | surgeon | 333 Dark Lane Raleigh, NC 27603 | 05/01/2008 |", rows[4].getText());
    }

    public void testReportSeenHCPs2() throws Exception {
        WebConversation wc = login("2", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertEquals("iTrust - Patient Home", wr.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 2L, 0L, "");
        wr = wr.getLinkWith("My Providers").click();
        assertEquals("iTrust - My Providers", wr.getTitle());
        WebForm form = wr.getFormWithID("searchForm");
        form.setParameter("filter_name", "Frank");
        form.setParameter("filter_specialty", "pediatrician");
        wr = form.submit();
        assertEquals("iTrust - My Providers", wr.getTitle());
        WebTable table = (WebTable) wr.getElementWithID("hcp_table");
        TableRow rows[] = table.getRows();
        assertEquals("| Lauren Frankenstein | pediatrician | 333 Dark Lane Raleigh, NC 27605 | 06/02/2008 |", rows[1].getText());
    }
}
