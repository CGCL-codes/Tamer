package org.personalsmartspace.cm.db.impl;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.personalsmartspace.cm.api.pss3p.ContextDBException;
import org.personalsmartspace.cm.db.api.platform.DeviceRole;

/**
 * @author <a href="mailto:nliampotis@users.sourceforge.net">Nicolas Liampotis</a> (ICCS)
 * @since 0.4.0
 */
public class TestCtxDBConfigurator {

    private static final String CONFIG_FILE_LOCATION_SLAVE = "pssconfig.properties.SLAVE";

    private static final String CONFIG_FILE_LOCATION_MASTER = "pssconfig.properties.MASTER";

    private static final String CONFIG_FILE_LOCATION_MASTER_UPDATE = "pssconfig.properties.MASTER-update";

    private static CtxDBConfigurator config;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDefaultProps() throws ContextDBException {
        config = new CtxDBConfigurator();
        config.validate();
    }

    @Test
    public void testSlaveProps() throws ContextDBException {
        config = new CtxDBConfigurator(CONFIG_FILE_LOCATION_SLAVE);
        config.validate();
    }

    @Test
    public void testMasterProps() throws ContextDBException {
        config = new CtxDBConfigurator(CONFIG_FILE_LOCATION_MASTER);
        config.validate();
        assertTrue(config.getMasterBrokerId().equalsIgnoreCase("true"));
        config.setLocalBrokerId("abcd");
        assertEquals("abcd", config.getLocalBrokerId());
        config.setMasterBrokerId("abcd");
        assertEquals("abcd", config.getMasterBrokerId());
        assertEquals(DeviceRole.MASTER, config.getDeviceRole());
        config.save(CONFIG_FILE_LOCATION_MASTER_UPDATE);
        final CtxDBConfigurator updatedConfig = new CtxDBConfigurator(CONFIG_FILE_LOCATION_MASTER_UPDATE);
        updatedConfig.validate();
        assertEquals("abcd", updatedConfig.getLocalBrokerId());
        assertEquals("abcd", updatedConfig.getMasterBrokerId());
        assertEquals(DeviceRole.MASTER, updatedConfig.getDeviceRole());
    }
}
