package com.goodcodeisbeautiful.archtea.search;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AddFieldQuerySearchFilterTest extends TestCase {

    public static Test suite() {
        return new TestSuite(AddFieldQuerySearchFilterTest.class);
    }

    AddFieldQuerySearchFilter _filter;

    protected void setUp() throws Exception {
        super.setUp();
        _filter = new AddFieldQuerySearchFilter();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        _filter = null;
    }

    public void testDoFilter() {
        assertEquals("+abc", _filter.doFilter("abc"));
        _filter.setProperty("fields", "field1,  field2");
        assertEquals("+abc +abc field1:abc field2:abc", _filter.doFilter("abc"));
        assertEquals("+abc +def field1:abc field1:def field2:abc field2:def", _filter.doFilter("abc def"));
    }
}
