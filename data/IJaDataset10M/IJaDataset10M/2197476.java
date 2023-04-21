package com.carbonfive.flash;

import org.apache.commons.logging.*;
import junit.framework.*;
import com.carbonfive.flash.test.*;

public class ASTranslatorFactoryTest extends TestCase {

    private static final Log log = LogFactory.getLog(ASTranslatorFactoryTest.class);

    public ASTranslatorFactoryTest(String s) {
        super(s);
    }

    public void setUp() throws Exception {
        System.setProperty(ASTranslatorFactory.FACTORY_PROPERTY, "");
    }

    public void tearDown() throws Exception {
        System.setProperty(ASTranslatorFactory.FACTORY_PROPERTY, "");
    }

    public void testDefaultFactory() throws Exception {
        ASTranslatorFactory factory = ASTranslatorFactory.getInstance();
        assertEquals(ASTranslatorFactory.class, factory.getClass());
    }

    public void testCustomFactory() throws Exception {
        System.setProperty(ASTranslatorFactory.FACTORY_PROPERTY, TestASTranslatorFactory.class.getName());
        ASTranslatorFactory factory = ASTranslatorFactory.getInstance();
        assertEquals(TestASTranslatorFactory.class, factory.getClass());
        System.setProperty(ASTranslatorFactory.FACTORY_PROPERTY, "");
    }

    public void testInvalidCustomFactory() throws Exception {
        System.setProperty(ASTranslatorFactory.FACTORY_PROPERTY, Object.class.getName());
        try {
            ASTranslatorFactory.getInstance();
            fail("Should have thrown ASTranslationException");
        } catch (ASTranslationException ae) {
            log.debug("Good: " + ae.getMessage());
        }
        System.setProperty(ASTranslatorFactory.FACTORY_PROPERTY, "");
    }
}
