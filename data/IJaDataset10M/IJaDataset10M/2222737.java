package com.divosa.security.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.divosa.security.domain.Application;
import com.divosa.security.domain.Group;
import com.divosa.security.exception.ObjectAlreadyExistsException;
import com.divosa.security.exception.ObjectNotFoundException;
import com.divosa.security.exception.RepositoryLayerException;
import com.divosa.security.testassist.SpringApplicationContext;

/**
 * @author test Ottenkamp
 */
public class UserManagementServicesTest {

    private static Logger LOGGER = Logger.getLogger(UserManagementServicesTest.class);

    private static UserManagementService userManagementService = null;

    /**
     * .
     */
    @BeforeClass
    public static void init() {
        new ClassPathXmlApplicationContext(new String[] { "spring/divosa-security-applicationContext-test.xml" });
        userManagementService = (UserManagementService) SpringApplicationContext.getBean("userManagementService");
    }

    @AfterClass
    public static void destroy() {
    }

    @Test
    public void templateMethod() {
        testSomeStuff();
    }

    private void testSomeStuff() {
        newApplication();
        getApplication();
        newGroup();
        getGroup();
        LOGGER.info("UserManagementServicesTest done...");
    }

    private void newApplication() {
        String applicationName = "MSPS";
        try {
            userManagementService.newApplication(applicationName);
        } catch (ObjectAlreadyExistsException e) {
            LOGGER.warn(e.getMessage());
        } catch (RepositoryLayerException e) {
            fail();
            LOGGER.error(e.getMessage());
        }
    }

    private void getApplication() {
        String name = "MSPS";
        try {
            Application application = userManagementService.getApplication(name);
            assertTrue(application.getNaam().equals(name));
            LOGGER.info("Found Application '" + name + "'; id = " + application.getId());
        } catch (ObjectNotFoundException e) {
            fail();
            LOGGER.error(e.getMessage());
        }
    }

    private void newGroup() {
        String name = "amersfoort";
        String css = "amersfoort.css";
        try {
            newGroup(name, css);
        } catch (RepositoryLayerException e) {
            fail();
            LOGGER.error(e.getMessage());
        }
    }

    private void getGroup() {
        String name = "amersfoort";
        try {
            Group group = userManagementService.getGroup(name);
            assertTrue(group.getName().equals(name));
        } catch (ObjectNotFoundException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    private void newGroup(String name, String css) throws RepositoryLayerException {
        Group group = null;
        try {
            group = userManagementService.newGroup(name);
            assertTrue(group.getName().equals(name));
            if (css != null) {
                group.setCss(css);
                userManagementService.saveOrUpdate(group);
            }
        } catch (ObjectAlreadyExistsException e) {
            LOGGER.warn(e.getMessage());
            group = userManagementService.getGroup(name);
            assertNotNull(group);
            assertTrue(group.getName().equals(name));
            if (css != null && !css.equals(group.getCss())) {
                group.setCss(css);
                userManagementService.saveOrUpdate(group);
            }
        }
    }
}
