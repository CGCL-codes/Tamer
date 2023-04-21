package com.plexobject.docusearch.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.plexobject.docusearch.docs.DocumentMultiMapHelper;
import com.plexobject.docusearch.docs.DocumentPropertiesHelper;

public class RegionLookupTest {

    private RegionLookup regionLookup;

    @Before
    public void setUp() throws Exception {
        regionLookup = new RegionLookup();
        regionLookup.setDocumentMultiMapHelper(new DocumentMultiMapHelper());
        regionLookup.setDocumentPropertiesHelper(new DocumentPropertiesHelper());
        regionLookup.afterPropertiesSet();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testGetContinentNameByCode() {
        Assert.assertEquals("Africa", regionLookup.getContinentNameByCode("AF"));
        Assert.assertEquals("Europe", regionLookup.getContinentNameByCode("EU"));
    }

    @Test
    public final void testGetContinentNames() {
        Assert.assertEquals(7, regionLookup.getContinentNames().size());
    }

    @Test
    public final void testGetCountryCodesByContinentCode() {
        Assert.assertTrue(regionLookup.getCountryCodesByContinentCode("OC").contains("AU"));
    }
}
