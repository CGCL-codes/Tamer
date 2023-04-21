package com.cyberkinetx.ecr.country;

import com.cyberkinetx.ecr.util.DataUrlResolver;
import junit.framework.TestCase;

public class DataUrlResolverTest extends TestCase {

    public void testBADataUrl() {
        DataUrlResolver dur = new DataUrlResolver();
        assertEquals("http://www.cbbh.ba/kursna_en.xml", dur.getDataUrl("ba"));
    }
}
