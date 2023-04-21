package org.sopera.administration.impl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.sopera.administration.AdminFacade;
import org.sopera.administration.AdminFacadeFactory;
import org.sopera.configuration.CfgFacade;

public class AFTestCase {

    protected static String ROOT_PATH;

    protected static AdminFacade facade;

    protected static CfgFacade configFacade;

    static {
        ROOT_PATH = System.getProperty("org.sopera.FacadeRootPath", "c:\\SOPERA\\AdminTool");
        System.err.println("org.sopera.FacadeRootPath = '" + ROOT_PATH + "'");
        System.err.println("org.sopware.sbb.home = '" + System.getProperty("org.sopware.sbb.home") + "'");
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        facade = AdminFacadeFactory.getAdminFacadeInstance(ROOT_PATH, "SOPAdministrator", "secret");
        configFacade = AdminFacadeFactory.getConfigFacadeInstance(ROOT_PATH, "SOPAdministrator", "secret");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        facade.release();
        configFacade.release();
    }

    public AFTestCase() {
        super();
    }
}
