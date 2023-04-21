package com.plexobject.docusearch.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.plexobject.docusearch.docs.DocumentPropertiesHelper;

public class CountriesHelperTest {

    CountriesHelper countriesHelper;

    @Before
    public void setUp() throws Exception {
        countriesHelper = new CountriesHelper();
        countriesHelper.setDocumentPropertiesHelper(new DocumentPropertiesHelper());
        countriesHelper.afterPropertiesSet();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public final void testGetCountryNameByCode() {
        Assert.assertEquals("United States", countriesHelper.getCountryNameByCode("US"));
    }

    @Test
    public final void testGetCountryNames() {
        Assert.assertTrue(countriesHelper.getCountryNames().size() > 200);
    }
}
