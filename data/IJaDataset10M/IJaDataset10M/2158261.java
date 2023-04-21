package com.angel.architecture.daos;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.angel.architecture.BaseArchitectureTestCase;
import com.angel.architecture.daos.factory.ArchitectureDAOFactory;

/**
 * @author William
 *
 */
public class ArchitectureDAOTestCase extends BaseArchitectureTestCase {

    @Autowired
    private ArchitectureDAOFactory architectureDAOFactory;

    @Test
    public void testInitializeSpringApplicationContext() {
        assertNotNull("Action dao must be injected in arcchitecture dao factory.", architectureDAOFactory.getActionDAO());
        assertNotNull("Configuration parameter dao must be injected in arcchitecture dao factory.", architectureDAOFactory.getConfigurationParameterDAO());
        assertNotNull("Language dao must be injected in arcchitecture dao factory.", architectureDAOFactory.getLanguageDAO());
        assertNotNull("Role dao must be injected in arcchitecture dao factory.", architectureDAOFactory.getRoleDAO());
        assertNotNull("User dao must be injected in arcchitecture dao factory.", architectureDAOFactory.getUserDAO());
        assertNotNull("User role dao must be injected in arcchitecture dao factory.", architectureDAOFactory.getUserRoleDAO());
    }
}
