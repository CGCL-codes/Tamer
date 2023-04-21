package uk.ac.ebi.intact.application.dataConversion.psiDownload.xmlGenerator;

import junit.framework.Test;
import junit.framework.TestSuite;
import uk.ac.ebi.intact.application.dataConversion.PsiVersion;
import uk.ac.ebi.intact.application.dataConversion.psiDownload.PsiDownloadTest;
import uk.ac.ebi.intact.application.dataConversion.psiDownload.UserSessionDownload;
import uk.ac.ebi.intact.application.dataConversion.psiDownload.xmlGenerator.psi1.Experiment2xmlPSI1;
import uk.ac.ebi.intact.application.dataConversion.psiDownload.xmlGenerator.psi2.Experiment2xmlPSI2;
import uk.ac.ebi.intact.application.dataConversion.psiDownload.xmlGenerator.psi25.Experiment2xmlPSI25;

/**
 * Test the behaviour of the Experiment2xmlFactory.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id: Experiment2xmlFactoryTest.java 5295 2006-07-06 17:32:20Z baranda $
 */
public class Experiment2xmlFactoryTest extends PsiDownloadTest {

    /**
     * Returns this test suite. Reflection is used here to add all the testXXX() methods to the suite.
     */
    public static Test suite() {
        return new TestSuite(Experiment2xmlFactoryTest.class);
    }

    public void testGetInstance() {
        UserSessionDownload session = null;
        try {
            Experiment2xmlFactory.getInstance(session);
            fail("You should no be allowed to give null to a Factory.");
        } catch (Exception e) {
        }
    }

    public void testGetInstancePsi1() {
        UserSessionDownload session = new UserSessionDownload(PsiVersion.getVersion1());
        Experiment2xmlI bsi = Experiment2xmlFactory.getInstance(session);
        assertNotNull(bsi);
        assertTrue(bsi instanceof Experiment2xmlPSI1);
    }

    public void testGetInstancePsi2() {
        UserSessionDownload session = new UserSessionDownload(PsiVersion.getVersion2());
        Experiment2xmlI bsi = Experiment2xmlFactory.getInstance(session);
        assertNotNull(bsi);
        assertTrue(bsi instanceof Experiment2xmlPSI2);
    }

    public void testGetInstancePsi25() {
        UserSessionDownload session = new UserSessionDownload(PsiVersion.getVersion25());
        Experiment2xmlI bsi = Experiment2xmlFactory.getInstance(session);
        assertNotNull(bsi);
        assertTrue(bsi instanceof Experiment2xmlPSI25);
    }
}
