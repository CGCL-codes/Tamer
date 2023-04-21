package org.didicero.base.service.test;

import org.didicero.base.security.DidiceroPrincipal;
import org.didicero.base.test.EJB3Container;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Service test class IntegrityServiceTest for testing with TestNG
 * Check the testng.xml for initialisation of the EJB3Container before running any tests.
 */
public class IntegrityServiceTest {

    private static final Log logger = LogFactory.getLog(IntegrityServiceTest.class);

    @org.testng.annotations.Test
    public void testCheckDatabaseDefaults() {
        try {
            DidiceroPrincipal princ = new DidiceroPrincipal("admin");
            org.didicero.base.service.IntegrityServiceRemote integrityService = (org.didicero.base.service.IntegrityServiceRemote) EJB3Container.getInitialContext(princ, "admin").lookup("IntegrityServiceBean/remote");
            integrityService.checkDatabaseDefaults();
        } catch (Exception ex) {
            logger.warn("Failed test testCheckDatabaseDefaults()", ex);
        }
    }

    @org.testng.annotations.Test
    public void testRecreateDatabaseDefaults() {
    }
}
