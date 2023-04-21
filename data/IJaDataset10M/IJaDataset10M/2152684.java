package com.siberhus.commons.xml;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

public class DomElementFinderTest {

    static enum LogVendor {

        jdk, log4j
    }

    static DomElementFinder finder;

    static final String XML_DATA = "<?xml version='1.0' ?>\n" + "<config>" + "<logs>" + "<log for-mode='prod' vendor='log4j' config='log4j-prod.properties'>" + "<vendor value='jdk'/>" + "<config>log-prod.properties</config>" + "</log>" + "<log for-mode='test' vendor='jdk' config='log-test.properties'>" + "<vendor value='log4j'/>" + "<config value='log4j-test.properties'></config>" + "</log>" + "<log for-mode='dev'>" + "<vendor value='log4j'/>" + "<config value='log4j-dev.properties'></config>" + "</log>" + "</logs>" + "<jobs>" + "<job-properties>" + "<property for-mode='all' name='type' value='object'/>" + "<property for-mode='all' for-job='animal' name='type' value='animal'/>" + "<property for-mode='all' for-job='animal.dog' name='name' value='bie'/>" + "</job-properties>" + "</jobs>" + "</config>";

    @BeforeClass
    public static void initFinder() throws SAXException, IOException, ParserConfigurationException {
        finder = DomElementFinder.newInstance(XML_DATA);
    }

    @Test
    public void testGettingAttributeOrElementValue() {
        Assert.assertEquals("log4j-prod.properties", finder.getAttributeOrElementValue("logs/log[@for-mode='prod']", "config"));
        Assert.assertEquals("log-test.properties", finder.getAttributeOrElementValue("logs/log[@for-mode='test']", "config"));
        Assert.assertEquals("log4j-dev.properties", finder.getAttributeOrElementValue("logs/log[@for-mode='dev']", "config"));
        Assert.assertEquals(LogVendor.log4j, finder.getAttributeOrElementValue(LogVendor.class, "logs/log", "vendor"));
        Assert.assertEquals(LogVendor.jdk, finder.getAttributeOrElementValue(LogVendor.class, "logs/log[@for-mode='test']", "vendor"));
        Assert.assertNull(finder.getAttributeOrElementValue(LogVendor.class, "logs/log", "notExists"));
    }

    @Test
    public void test() {
    }
}
