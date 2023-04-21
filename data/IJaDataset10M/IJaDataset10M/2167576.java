package edu.ncsu.csc.itrust.http;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;
import edu.ncsu.csc.itrust.enums.TransactionType;

public class EditPatientTest extends iTrustHTTPTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gen.clearAllTables();
        gen.standardData();
    }

    public void testCauseOfDeathValidation() throws Exception {
        WebConversation wc = login("9000000000", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
        wr = wr.getLinkWith("Patient Information").click();
        WebForm patientForm = wr.getForms()[0];
        patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
        patientForm.getButtons()[1].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-uap/editPatient.jsp", wr.getURL().toString());
        WebForm editPatientForm = wr.getForms()[0];
        editPatientForm.setParameter("dateOfDeathStr", "");
        editPatientForm.getButtons()[2].click();
        wr = wc.getCurrentPage();
        assertTrue(wr.getText().contains("This form has not been validated correctly. The following field are not properly filled in: [Cause of Death cannot be specified without Date of Death!]"));
        assertNotLogged(TransactionType.DEMOGRAPHICS_EDIT, 9000000000L, 2L, "");
    }

    public void testMisspellings() throws Exception {
        WebConversation wc = login("9000000000", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
        wr = wr.getLinkWith("Patient Information").click();
        WebForm patientForm = wr.getForms()[0];
        patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
        patientForm.getButtons()[1].click();
        wr = wc.getCurrentPage();
        assertEquals(ADDRESS + "auth/hcp-uap/editPatient.jsp", wr.getURL().toString());
        assertFalse(wr.getText().contains("Mother MIDs"));
    }

    public void testViewDemographicsTest() throws Exception {
        WebConversation wc = login("9000000000", "pw");
        WebResponse wr = wc.getCurrentPage();
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
        wr = wr.getLinkWith("Patient Information").click();
        WebForm patientForm = wr.getForms()[0];
        patientForm.getScriptableObject().setParameterValue("UID_PATIENTID", "2");
        patientForm.getButtons()[1].click();
        wr = wc.getCurrentPage();
        patientForm = wr.getFormWithID("editForm");
        patientForm.getScriptableObject().setParameterValue("email", "history@gmail.com");
        patientForm.getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        wr.getFormWithID("viewHistory").getSubmitButtons()[0].click();
        wr = wc.getCurrentPage();
        assertTrue(wr.getText().contains("history@gmail.com"));
    }
}
