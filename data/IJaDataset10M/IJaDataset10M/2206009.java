package de.hbrs.inf.atarrabi.test.selenium;

/**
 * Tests the summary page.
 * 
 * @author Lukas Brabanski
 *
 */
public class WizardSummarypageTest extends AtatrrabiSeleniumTest {

    /**
	 * Verifies the summary page.
	 * 
	 * @return boolean for a fail or a correct test
	 */
    public boolean verifySummary() {
        try {
            assert session().getValue("wizardForm:messageToPaTextarea").contains(getTestString());
            session().click("wizardForm:submitButton");
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
            return false;
        }
        return true;
    }
}
