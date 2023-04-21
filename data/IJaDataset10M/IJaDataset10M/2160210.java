package com.herestudio.config;

import junit.framework.TestCase;
import com.herestudio.HahereConstant;

public class ApplicationContextHolderTest extends TestCase {

    public void testForceRefresh() {
        ApplicationContextHolder applicationContextHolder = new ApplicationContextHolder();
        applicationContextHolder.addConfigFile(HahereConstant.DAO_CONFIG_FILE);
        assertTrue(applicationContextHolder.containsConfigFile(HahereConstant.DAO_CONFIG_FILE));
        try {
            applicationContextHolder.forceRefresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(applicationContextHolder.getApplicationContext().getBean("userDAO"));
    }
}
