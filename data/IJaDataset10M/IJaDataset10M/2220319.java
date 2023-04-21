package macaw.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import macaw.businessLayer.MacawCurationAPI;
import macaw.businessLayer.MacawRetrievalAPI;
import macaw.persistenceLayer.demo.DemonstrationRetrievalService;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.persistenceLayer.production.ProductionRetrievalService;
import macaw.system.MacawException;
import macaw.system.SessionProperties;
import macaw.system.StartupOptions;

public class RunAllMacawTestCases extends TestSuite {

    public static final boolean USE_DEMO = false;

    private static boolean servicesInitialised = false;

    public static MacawCurationAPI curationService = null;

    public static MacawRetrievalAPI retrievalService = null;

    public RunAllMacawTestCases() {
    }

    public static MacawCurationAPI getMacawCurationService() throws MacawException {
        initialiseServices();
        return curationService;
    }

    public static MacawRetrievalAPI getMacawRetrievalService() throws MacawException {
        initialiseServices();
        return retrievalService;
    }

    public static void initialiseServices() throws MacawException {
        if (servicesInitialised == false) {
            servicesInitialised = true;
            if (RunAllMacawTestCases.USE_DEMO == true) {
                SessionProperties sessionProperties = new SessionProperties();
                DemonstrationRetrievalService service = new DemonstrationRetrievalService(sessionProperties);
                curationService = service;
                retrievalService = service;
            } else {
                SessionProperties sessionProperties = new SessionProperties();
                StartupOptions startupOptions = sessionProperties.getStartupOptions();
                startupOptions.setDbUser("root");
                startupOptions.setDbPassword("H3rm3s");
                curationService = new ProductionCurationService(sessionProperties);
                retrievalService = new ProductionRetrievalService(sessionProperties);
            }
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(macaw.test.curation.TestBasket.class);
        suite.addTestSuite(macaw.test.curation.TestBasketVariableReference.class);
        suite.addTestSuite(macaw.test.retrieval.TestRetrieveVariables.class);
        suite.addTestSuite(macaw.test.curation.TestCurateRawVariables.class);
        suite.addTestSuite(macaw.test.curation.TestStudyMetaData.class);
        suite.addTestSuite(macaw.test.curation.TestCurateUsers.class);
        suite.addTestSuite(macaw.test.retrieval.TestRetrieveUsers.class);
        suite.addTestSuite(macaw.test.curation.TestCurateDerivedVariables.class);
        suite.addTestSuite(macaw.test.curation.TestCurateListChoices.class);
        suite.addTestSuite(macaw.test.curation.TestCurateOntologyTerms.class);
        suite.addTestSuite(macaw.test.curation.TestCurateValueLabels.class);
        suite.addTestSuite(macaw.test.curation.TestOntologyTermFilter.class);
        suite.addTestSuite(macaw.test.curation.TestSupportingDocumentFilter.class);
        return suite;
    }
}
