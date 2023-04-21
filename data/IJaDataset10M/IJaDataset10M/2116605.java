package org.ikasan.common.security.algo;

import junit.framework.JUnit4TestAdapter;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * This test class supports the XStream converter for <code>PBEConverterTest</code>.
 *
 * @author Ikasan Development Team
 */
public class PBEConverterTest {

    /**
     * The logger instance.
     */
    private static Logger logger = Logger.getLogger(PBEConverterTest.class);

    /** The cipher */
    private String cipher;

    /** The password/phrase */
    private String pass;

    /** The iteration count */
    private int iterationCount;

    /** The salt/seed */
    private String salt;

    /**
     * Setup runs before each test
     */
    @Before
    public void setUp() {
        cipher = "myCipher";
        pass = "myPass";
        iterationCount = 21;
        salt = "mySalt";
    }

    /**
     * Test fully populated PBE
     */
    @Test
    public void PBEConverter() {
        String expectedXML = "<PBE cipher=\"" + cipher + "\" iterationCount=\"" + iterationCount + "\"" + " pass=\"" + pass + "\"" + " salt=\"" + salt + "\"/>";
        PBE algorithm = new PBE();
        algorithm.setCipher(cipher);
        algorithm.setPass(pass);
        algorithm.setIterationCount(21);
        algorithm.setSalt(salt);
        String generatedXML = algorithm.toXML();
        Assert.assertEquals(expectedXML, generatedXML);
        Algorithm algorithm2 = algorithm.fromXML(generatedXML);
        Assert.assertTrue(algorithm.equals(algorithm2));
    }

    /**
     * Test partially populated PBE (one of the attributes being 'null').
     */
    @Test
    public void PartialPBEConverter() {
        salt = null;
        String expectedXML = "<PBE cipher=\"" + cipher + "\" iterationCount=\"" + iterationCount + "\"" + " pass=\"" + pass + "\"/>";
        PBE algorithm = new PBE();
        algorithm.setCipher(cipher);
        algorithm.setPass(pass);
        algorithm.setIterationCount(21);
        algorithm.setSalt(salt);
        String generatedXML = algorithm.toXML();
        Assert.assertEquals(expectedXML, generatedXML);
        Algorithm algorithm2 = algorithm.fromXML(generatedXML);
        Assert.assertTrue(algorithm.equals(algorithm2));
    }

    /**
     * Teardown after each test
     */
    @After
    public void tearDown() {
        logger.info("tearDown");
    }

    /**
     * The suite is this class
     * @return JUnit Test class
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PBEConverterTest.class);
    }
}
