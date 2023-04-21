package org.dcm4chee.xero.controller;

import org.dcm4chee.xero.metadata.MetaDataBean;
import org.dcm4chee.xero.metadata.StaticMetaData;
import org.dcm4chee.xero.test.JSTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Tests the JavaScript model
 * 
 * @author Bill Wallace
 * 
 */
public class XeroJSControllerTest {

    static final Logger log = LoggerFactory.getLogger(XeroJSControllerTest.class);

    static MetaDataBean mdb = StaticMetaData.getMetaData("xero.metadata");

    static MetaDataBean stat = mdb.getChild("static");

    static final JSTemplate jst = new JSTemplate(stat, "rhinoAccess", "xeroTest", "xeroControllerTests", "xeroControllerTests/domUtilsTest", "xeroController");

    @Test
    public void doUtilsTest() {
        jst.runTest("controllerTest", false);
    }
}
