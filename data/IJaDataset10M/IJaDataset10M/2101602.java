package com.idna.gav.service.impl;

import org.dom4j.Document;
import org.junit.Ignore;
import org.junit.Test;
import com.idna.gav.service.GavBusinessDelegate;
import com.idna.common.utils.XmlUtility;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Author: vlad.shiligin
 * Date: 23-Sep-2008
 */
@Ignore
public class GavBusinessDelegateTest extends AbstractDependencyInjectionSpringContextTests {

    private GavBusinessDelegate gavBusinessDelegate;

    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] { "classpath:/applicationContext.xml" };
    }

    @Test
    public void testGetGavResponce() throws Exception {
        Document gavRequestDoc = XmlUtility.parseFromFileDom4jDocument(GavBusinessDelegateImplItalyTest.class, "TestRequestXMLCanada.xml");
        Document expectedGavResponce = XmlUtility.parseFromFileDom4jDocument(GavBusinessDelegateImplItalyTest.class, "gavResponseExpectedXMLCanada.xml");
        String result = gavBusinessDelegate.doSearch(gavRequestDoc.asXML(), "", "");
        Document gavResponseDoc = XmlUtility.parseFromStringDom4jDocument(result);
        assertEquals(expectedGavResponce.asXML(), gavResponseDoc.asXML());
    }

    public void setGavBusinessDelegate(GavBusinessDelegate gavBusinessDelegate) {
        this.gavBusinessDelegate = gavBusinessDelegate;
    }
}
