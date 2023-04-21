package com.tredart.utils;

import static org.junit.Assert.assertEquals;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * this class tests the Log4jInitialser.
 * <p>
 * note: the testDefaultPropertyManager file should be updated so that it
 * contains the current users root.
 * 
 * @author hasitha
 */
public class Testlog4jInitialiser {

    /**
     * get the appropriate logger.
     */
    private static Logger logTest = Logger.getLogger(Testlog4jInitialiser.class);

    /**
     * tests the initialiser.
     */
    @Test
    public final void testInitialser() {
        Log4jInitialiser app = new Log4jInitialiser();
        app.initialise();
        logTest.info("Log4j Configured Sucessfully");
    }

    /**
     * test the setPath() method.
     */
    @Test
    public final void testSetPath() {
        Log4jInitialiser app = new Log4jInitialiser();
        app.setPath();
        String s = "/";
        StringBuffer sb = new StringBuffer();
        sb.append("src").append(s);
        sb.append("test").append(s);
        sb.append("resources").append(s);
        sb.append("com").append(s);
        sb.append("tredart").append(s);
        sb.append("utils").append(s);
        sb.append("logger").append(s);
        sb.append("logConfigurator.xml");
        assertEquals(sb.toString(), app.getPath());
    }
}
