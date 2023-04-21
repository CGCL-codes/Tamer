package org.personalsmartspace.pss_sm_api.impl;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestIPojoParser {

    private static final String NON_IPOJO_STRING = "i-Components: component { $classname=\"org.personalsmartspace.psw3p";

    private static final String NON_COMPOSITE_STRING = "iPOJO-Components: component { $classname=\"org.personalsmartspace.psw3p" + ".pris1.PrinterService1Impl\" provides { }manipulation { field { $name=" + "\"printerName\" $type=\"java.lang.String\" }method { $name=\"$init\" }metho" + "d { $arguments=\"{java.io.File}\" $name=\"print\" $return=\"java.lang.Stri" + "ng\" }method { $name=\"whatPrinter\" $return=\"java.lang.String\" }interfa" + "ce { $name=\"org.personalsmartspace.psw3p.api.pris.IPrinterService\" }}}";

    private static final String COMPOSITE_STRING = "iPOJO-Components: component { $classname=\"org.personalsmartspace.psw3p}composite {";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testComposite() throws Exception {
        assertTrue(IPojoParser.isServiceComposite(COMPOSITE_STRING));
        assertFalse(IPojoParser.isServiceComposite(NON_COMPOSITE_STRING));
    }

    @Test
    public void testiPojo() throws Exception {
        assertTrue(IPojoParser.isServiceIPojo(COMPOSITE_STRING));
        assertTrue(IPojoParser.isServiceIPojo(NON_COMPOSITE_STRING));
        assertFalse(IPojoParser.isServiceIPojo(NON_IPOJO_STRING));
    }
}
