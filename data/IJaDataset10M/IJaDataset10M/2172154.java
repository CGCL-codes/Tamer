package com.javector.ser.util;

import junit.framework.TestCase;
import junit.framework.Assert;
import org.w3c.dom.Document;
import org.apache.xpath.XPathAPI;
import com.javector.adaptive.framework.util.SOAUtil;

/**
 * Created by IntelliJ IDEA.
 * Auther: Rohit Agarwal
 * Date: May 20, 2006
 * Time: 11:58:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestXpath extends TestCase {

    public void testXPathEvaluator() throws Exception {
        String xmlString = "rohit-agarwal";
        Document document = SOAUtil.createDummyDocumentForString(xmlString);
        String xPath = "substring-before(.,'-')";
        String value = XPathAPI.eval(document, xPath).toString();
        Assert.assertEquals("rohit", value);
    }
}
