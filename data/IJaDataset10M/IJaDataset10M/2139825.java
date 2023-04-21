package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import edu.ncsu.csc.itrust.enums.TransactionType;

/**
 * Test Diagnosis Trends page
 * 
 * @author student
 *
 */
public class ViewDiagnosisStatisticsTest extends iTrustHTTPTest {

    /**
	 * Sets up the test. Clears the tables then adds necessary data
	 */
    protected void setUp() throws Exception {
        super.setUp();
        gen.clearAllTables();
        gen.standardData();
        gen.patient_hcp_vists();
        gen.hcp_diagnosis_data();
    }

    public void testViewDiagnosisTrends_PHAView1() throws Exception {
        WebConversation wc = login("7000000001", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertEquals("iTrust - PHA Home", wr.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 7000000001L, 0L, "");
        wr = wr.getLinkWith("Diagnosis Trends").click();
        WebForm form = wr.getFormWithID("formSelectFlow");
        form.getScriptableObject().setParameterValue("viewSelect", "trends");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        form = wr.getFormWithID("formMain");
        form.getScriptableObject().setParameterValue("icdCode", "72.00");
        form.getScriptableObject().setParameterValue("zipCode", "27695");
        form.getScriptableObject().setParameterValue("startDate", "06/28/2011");
        form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 7000000001L, 0L, "");
        WebTable table = wr.getTableWithID("diagnosisStatisticsTable");
        assertTrue(table.getCellAsText(1, 2).contains("0"));
        assertTrue(table.getCellAsText(1, 3).contains("2"));
    }

    public void testViewDiagnosisTrends_LHCPObserveIncrease() throws Exception {
        WebConversation wc = login("9000000008", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertEquals("iTrust - HCP Home", wr.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000008L, 0L, "");
        wr = wr.getLinkWith("Diagnosis Trends").click();
        WebForm form = wr.getFormWithID("formSelectFlow");
        form.getScriptableObject().setParameterValue("viewSelect", "trends");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        form = wr.getFormWithID("formMain");
        form.getScriptableObject().setParameterValue("icdCode", "487.00");
        form.getScriptableObject().setParameterValue("zipCode", "27695");
        form.getScriptableObject().setParameterValue("startDate", "08/28/2011");
        form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 9000000008L, 0L, "");
        WebTable table = wr.getTableWithID("diagnosisStatisticsTable");
        long local1 = Long.parseLong(table.getCellAsText(1, 2));
        long region1 = Long.parseLong(table.getCellAsText(1, 3));
        wr = wr.getLinkWith("Document Office Visit").click();
        WebForm patientForm = wr.getForms()[0];
        patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "25");
        patientForm.getButtons()[1].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-uap/documentOfficeVisit.jsp", wr.getURL().toString());
        form = wr.getForms()[0];
        form.getButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals("iTrust - Document Office Visit", wr.getTitle());
        form = wr.getFormWithID("mainForm");
        form.setParameter("visitDate", "09/28/2011");
        form.setParameter("notes", "I like diet-coke");
        form.getButtonWithID("update").click();
        wr = wc.getCurrentPage();
        assertTrue(wr.getText().contains("Information Successfully Updated"));
        assertLogged(TransactionType.OFFICE_VISIT_CREATE, 9000000008L, 25L, "Office visit");
        form = wr.getFormWithID("diagnosisForm");
        form.setParameter("ICDCode", "487.00");
        form.getButtons()[0].click();
        wr = wc.getCurrentPage();
        assertTrue(wr.getText().contains("Diagnosis information successfully updated."));
        wr = wr.getLinkWith("Diagnosis Trends").click();
        form = wr.getFormWithID("formSelectFlow");
        form.getScriptableObject().setParameterValue("viewSelect", "trends");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        form = wr.getFormWithID("formMain");
        form.getScriptableObject().setParameterValue("icdCode", "487.00");
        form.getScriptableObject().setParameterValue("zipCode", "27606");
        form.getScriptableObject().setParameterValue("startDate", "08/28/2011");
        form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 9000000008L, 0L, "");
        table = wr.getTableWithID("diagnosisStatisticsTable");
        long local2 = Long.parseLong(table.getCellAsText(1, 2));
        long region2 = Long.parseLong(table.getCellAsText(1, 3));
        assertEquals(local1 + 1, local2);
        assertEquals(region1 + 1, region2);
    }

    public void testViewDiagnosisTrends_InvalidZip() throws Exception {
        WebConversation wc = login("9000000008", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertEquals("iTrust - HCP Home", wr.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000008L, 0L, "");
        wr = wr.getLinkWith("Diagnosis Trends").click();
        WebForm form = wr.getFormWithID("formSelectFlow");
        form.getScriptableObject().setParameterValue("viewSelect", "trends");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        form = wr.getFormWithID("formMain");
        form.getScriptableObject().setParameterValue("icdCode", "487.00");
        form.getScriptableObject().setParameterValue("zipCode", "276");
        form.getScriptableObject().setParameterValue("startDate", "08/28/2011");
        form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 9000000008L, 0L, "");
        assertTrue(wr.getText().contains("Information not valid"));
        assertTrue(wr.getText().contains("Zip Code must be 5 digits!"));
    }

    public void testViewDiagnosisTrends_InvalidDates() throws Exception {
        WebConversation wc = login("9000000000", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertEquals("iTrust - HCP Home", wr.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
        wr = wr.getLinkWith("Diagnosis Trends").click();
        WebForm form = wr.getFormWithID("formSelectFlow");
        form.getScriptableObject().setParameterValue("viewSelect", "trends");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        form = wr.getFormWithID("formMain");
        form.getScriptableObject().setParameterValue("icdCode", "84.50");
        form.getScriptableObject().setParameterValue("zipCode", "27519");
        form.getScriptableObject().setParameterValue("startDate", "09/28/2011");
        form.getScriptableObject().setParameterValue("endDate", "08/28/2011");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 9000000000L, 0L, "");
        assertTrue(wr.getText().contains("Information not valid"));
        assertTrue(wr.getText().contains("Start date must be before end date!"));
    }

    public void testViewDiagnosisTrends_SameRegionCount() throws Exception {
        WebConversation wc = login("9000000000", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertEquals("iTrust - HCP Home", wr.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
        wr = wr.getLinkWith("Diagnosis Trends").click();
        WebForm form = wr.getFormWithID("formSelectFlow");
        form.getScriptableObject().setParameterValue("viewSelect", "trends");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        form = wr.getFormWithID("formMain");
        form.getScriptableObject().setParameterValue("icdCode", "84.50");
        form.getScriptableObject().setParameterValue("zipCode", "27695");
        form.getScriptableObject().setParameterValue("startDate", "06/28/2011");
        form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 9000000000L, 0L, "");
        WebTable table = wr.getTableWithID("diagnosisStatisticsTable");
        long region1 = Long.parseLong(table.getCellAsText(1, 3));
        form = wr.getFormWithID("formMain");
        form.getScriptableObject().setParameterValue("icdCode", "84.50");
        form.getScriptableObject().setParameterValue("zipCode", "27606");
        form.getScriptableObject().setParameterValue("startDate", "06/28/2011");
        form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 9000000000L, 0L, "");
        table = wr.getTableWithID("diagnosisStatisticsTable");
        long region2 = Long.parseLong(table.getCellAsText(1, 3));
        assertEquals(region1, region2);
    }

    public void testViewDiagnosisTrends_SameDateStartEnd() throws Exception {
        WebConversation wc = login("9000000000", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertEquals("iTrust - HCP Home", wr.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
        wr = wr.getLinkWith("Diagnosis Trends").click();
        WebForm form = wr.getFormWithID("formSelectFlow");
        form.getScriptableObject().setParameterValue("viewSelect", "trends");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        form = wr.getFormWithID("formMain");
        form.getScriptableObject().setParameterValue("icdCode", "84.50");
        form.getScriptableObject().setParameterValue("zipCode", "27519");
        form.getScriptableObject().setParameterValue("startDate", "09/28/2011");
        form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 9000000000L, 0L, "");
        WebTable table = wr.getTableWithID("diagnosisStatisticsTable");
        assertTrue(table.getCellAsText(1, 2).contains("0"));
        assertTrue(table.getCellAsText(1, 3).contains("0"));
    }

    public void testViewDiagnosisTrends_RegionNotLess() throws Exception {
        WebConversation wc = login("9000000008", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertEquals("iTrust - HCP Home", wr.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000008L, 0L, "");
        wr = wr.getLinkWith("Diagnosis Trends").click();
        WebForm form = wr.getFormWithID("formSelectFlow");
        form.getScriptableObject().setParameterValue("viewSelect", "trends");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        form = wr.getFormWithID("formMain");
        form.getScriptableObject().setParameterValue("icdCode", "487.00");
        form.getScriptableObject().setParameterValue("zipCode", "27607");
        form.getScriptableObject().setParameterValue("startDate", "08/28/2011");
        form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 9000000008L, 0L, "");
        WebTable table = wr.getTableWithID("diagnosisStatisticsTable");
        long local = Long.parseLong(table.getCellAsText(1, 2));
        long region = Long.parseLong(table.getCellAsText(1, 3));
        assertTrue(local <= region);
    }

    public void testViewDiagnosisTrends_NoDiagnosisSelected() throws Exception {
        WebConversation wc = login("7000000001", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertEquals("iTrust - PHA Home", wr.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 7000000001L, 0L, "");
        wr = wr.getLinkWith("Diagnosis Trends").click();
        WebForm form = wr.getFormWithID("formSelectFlow");
        form.getScriptableObject().setParameterValue("viewSelect", "trends");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        form = wr.getFormWithID("formMain");
        form.getScriptableObject().setParameterValue("icdCode", "");
        form.getScriptableObject().setParameterValue("zipCode", "27695");
        form.getScriptableObject().setParameterValue("startDate", "06/28/2011");
        form.getScriptableObject().setParameterValue("endDate", "09/28/2011");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        assertLogged(TransactionType.DIAGNOSIS_TRENDS_VIEW, 7000000001L, 0L, "");
        assertTrue(wr.getText().contains("Information not valid"));
        assertTrue(wr.getText().contains("ICDCode must be valid diagnosis!"));
    }

    public void viewDiagnosisEpidemics_NoEpidemicRecords() throws Exception {
        WebConversation wc = login("9000000000", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertEquals("iTrust - HCP Home", wr.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
        wr = wr.getLinkWith("Diagnosis Trends").click();
        WebForm form = wr.getFormWithID("formSelectFlow");
        form.getScriptableObject().setParameterValue("viewSelect", "epidemics");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        form = wr.getFormWithID("formMain");
        form.getScriptableObject().setParameterValue("icdCode", "84.50");
        form.getScriptableObject().setParameterValue("zipCode", "38201");
        form.getScriptableObject().setParameterValue("startDate", "06/02/2010");
        form.getScriptableObject().setParameterValue("threshold", "110");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        assertLogged(TransactionType.DIAGNOSIS_EPIDEMICS_VIEW, 9000000000L, 0L, "");
        assertTrue(wr.getText().contains("There is no epidemic occurring in the region."));
    }

    public void viewDiagnosisEpidemics_YesEpidemic() throws Exception {
        gen.influenza_epidemic();
        WebConversation wc = login("9000000007", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertEquals("iTrust - HCP Home", wr.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 9000000007L, 0L, "");
        wr = wr.getLinkWith("Diagnosis Trends").click();
        WebForm form = wr.getFormWithID("formSelectFlow");
        form.getScriptableObject().setParameterValue("viewSelect", "epidemics");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        form = wr.getFormWithID("formMain");
        form.getScriptableObject().setParameterValue("icdCode", "487.00");
        form.getScriptableObject().setParameterValue("zipCode", "27607");
        form.getScriptableObject().setParameterValue("startDate", "11/02/2011");
        form.getScriptableObject().setParameterValue("threshold", "");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        assertLogged(TransactionType.DIAGNOSIS_EPIDEMICS_VIEW, 9000000007L, 0L, "");
        assertTrue(wr.getText().contains("THERE IS AN EPIDEMIC OCCURRING IN THIS REGION!"));
    }

    public void viewDiagnosisEpidemics_NoEpidemic() throws Exception {
        gen.influenza_epidemic();
        WebConversation wc = login("7000000001", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertEquals("iTrust - PHA Home", wr.getTitle());
        assertLogged(TransactionType.HOME_VIEW, 7000000001L, 0L, "");
        wr = wr.getLinkWith("Diagnosis Trends").click();
        WebForm form = wr.getFormWithID("formSelectFlow");
        form.getScriptableObject().setParameterValue("viewSelect", "epidemics");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        form = wr.getFormWithID("formMain");
        form.getScriptableObject().setParameterValue("icdCode", "487.00");
        form.getScriptableObject().setParameterValue("zipCode", "27607");
        form.getScriptableObject().setParameterValue("startDate", "02/15/2010");
        form.getScriptableObject().setParameterValue("threshold", "");
        form.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-pha/viewDiagnosisStatistics.jsp", wr.getURL().toString());
        assertLogged(TransactionType.DIAGNOSIS_EPIDEMICS_VIEW, 7000000001L, 0L, "");
        assertTrue(wr.getText().contains("There is no epidemic occurring in the region."));
    }
}
