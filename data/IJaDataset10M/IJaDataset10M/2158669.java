package org.stars.dao;

import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;

public abstract class StarsTest {

    protected long start;

    protected Log log;

    @Before
    public void setUp() {
        File base = new File("src/test/resources/log4j.properties");
        System.out.println(base.getAbsolutePath());
        PropertyConfigurator.configure(base.getAbsolutePath());
        log = LogFactory.getLog(getClass());
        log.info("Log system is up");
        start = System.currentTimeMillis();
    }

    @After
    public void after() {
        long end = System.currentTimeMillis();
        System.out.println("Esecuzione terminata in " + (end - start) + " ms.");
    }
}
