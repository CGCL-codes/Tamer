package net.jwde;

import junit.framework.TestCase;

public class JWDEInputConfigTest extends TestCase {

    private JWDEInputConfig jwdeConfig1;

    public void setUp() {
        String inputType1;
        String outputURLName1;
        String inputURLName1;
        String transformURLName1;
        String mappingURLName1;
        inputType1 = "oscMax";
        inputURLName1 = "file:test/result/input/input.html";
        outputURLName1 = "test/result/output/output.xml";
        transformURLName1 = "file:config/extractor/transform.xsl";
        mappingURLName1 = "file:config/processor/jwdeMapping_castor.xml";
        jwdeConfig1 = new JWDEInputConfig(inputType1, inputURLName1, transformURLName1, mappingURLName1, outputURLName1);
    }

    public void testGetOutputURLName() {
        assertEquals("test/result/output/output.xml", jwdeConfig1.getOutputURLName());
    }

    public void testSetOutputURLName() {
        jwdeConfig1.setOutputURLName("test/result/output/stock2.xml");
        assertEquals("test/result/output/stock2.xml", jwdeConfig1.getOutputURLName());
    }

    public void testGetInputType() {
        assertEquals("oscMax", jwdeConfig1.getInputType());
    }

    public void testSetInputType() {
        jwdeConfig1.setInputType("osCommerce");
        assertEquals("osCommerce", jwdeConfig1.getInputType());
    }

    public void testGetInputURLName() {
        assertEquals("file:test/result/input/input.html", jwdeConfig1.getInputURLName());
    }

    public void testSetInputURLName() {
        jwdeConfig1.setInputURLName("file:test/result/input/input1.cvs");
        assertEquals("file:test/result/input/input1.cvs", jwdeConfig1.getInputURLName());
    }

    public void testGetMappingURLName() {
        assertEquals("file:config/processor/jwdeMapping_castor.xml", jwdeConfig1.getMappingURLName());
    }

    public void testSetMappingURLName() {
        jwdeConfig1.setMappingURLName("file:config/processor/mapping.xml");
        assertEquals("file:config/processor/mapping.xml", jwdeConfig1.getMappingURLName());
    }

    public void testGetTransformURLName() {
        assertEquals("file:config/extractor/transform.xsl", jwdeConfig1.getTransformURLName());
    }

    public void testSetTransformURLName() {
        jwdeConfig1.setTransformURLName("file:config/extractor/trans.xsl");
        assertEquals("file:config/extractor/trans.xsl", jwdeConfig1.getTransformURLName());
    }
}
