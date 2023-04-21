package org.edemocrazy.democracy.web.business;

import java.util.Collection;
import org.edemocrazy.democracy.web.test.AbstractSpringWebContextTest;

public class ApplicationManagerTest extends AbstractSpringWebContextTest {

    ApplicationManager applicationManager;

    /**
     * 
     * @throws java.lang.Exception 
     */
    public void setUp() throws Exception {
        applicationManager = (ApplicationManager) ctx.getBean("applicationManager");
    }

    public void testGetCountries() {
        Collection countries = this.applicationManager.getCountries();
        assertEquals("Should contain sweden,uk", 2, countries.size());
    }
}
