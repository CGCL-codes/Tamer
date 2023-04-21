package de.cue4net.eventservice.model.email.dao;

import de.cue4net.eventservice.model.email.EmailConfiguration;
import de.cue4net.eventservice.tests.model.AbstractEventserviceTest;

/**
 * EmailConfigurationDAOTest
 *
 * @author Thorsten Vogel
 * @version $Id: EmailConfigurationDAOTest.java,v 1.2 2008-06-05 12:19:08 keino Exp $
 */
public class EmailConfigurationDAOTest extends AbstractEventserviceTest {

    public void testInitDao() {
        try {
            emailConfigurationDAO.initDao();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testGetConfig() {
        EmailConfiguration config = emailConfigurationDAO.getConfig();
        assertNotNull(config);
        assertNotNull(config.getMaximumSendRetries());
        assertNotNull(config.getSendMailNumberOfEmailsPerRun());
        assertNotNull(config.getSendMailTaskTimeout());
    }

    public void testUpdateConfig() {
        EmailConfiguration config = emailConfigurationDAO.getConfig();
        config.setMaximumSendRetries(7);
        config.setSendMailNumberOfEmailsPerRun(999);
        config.setSendMailTaskTimeout(123);
        emailConfigurationDAO.updateConfig(config);
        EmailConfiguration newConfig = emailConfigurationDAO.getConfig();
        assertEquals(7, newConfig.getMaximumSendRetries().intValue());
        assertEquals(999, newConfig.getSendMailNumberOfEmailsPerRun().intValue());
        assertEquals(123, newConfig.getSendMailTaskTimeout().intValue());
    }

    public void testGetDefaultEmailConfiguration() {
        EmailConfiguration config = emailConfigurationDAO.getDefaultEmailConfiguration();
        assertNotNull(config);
        assertNotNull(config.getMaximumSendRetries());
        assertNotNull(config.getSendMailNumberOfEmailsPerRun());
        assertNotNull(config.getSendMailTaskTimeout());
    }
}
