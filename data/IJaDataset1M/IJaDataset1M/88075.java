package macaw.test.curation;

import junit.framework.TestCase;
import macaw.businessLayer.MacawCurationAPI;
import macaw.businessLayer.User;
import macaw.persistenceLayer.production.ProductionCurationService;
import macaw.system.Log;
import macaw.test.RunAllMacawTestCases;

public class MacawCurationTestCase extends TestCase {

    protected MacawCurationAPI curationService;

    protected User demoUser;

    protected User admin;

    protected Log log;

    public MacawCurationTestCase(String name) {
        super(name);
        try {
            log = new Log();
            demoUser = User.createDemoUser();
            admin = User.createDemoAdminUser();
            curationService = RunAllMacawTestCases.getMacawCurationService();
        } catch (Exception exception) {
            exception.printStackTrace(System.out);
        }
    }

    protected void setUp() throws Exception {
        RunAllMacawTestCases.curationService.clear(admin);
        if (RunAllMacawTestCases.USE_DEMO == false) {
            ProductionCurationService service = (ProductionCurationService) RunAllMacawTestCases.curationService;
        }
        super.setUp();
    }

    protected void tearDown() throws Exception {
        if (RunAllMacawTestCases.USE_DEMO == false) {
            ProductionCurationService service = (ProductionCurationService) RunAllMacawTestCases.curationService;
        }
        super.tearDown();
    }
}
