package com.test.utils.dao;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DBUtilsTest {

    @Before
    public void init() {
    }

    @Test
    public void testGenerateDBIdentifier() {
        assertTrue("my_mothers_name".equals(DBUtils.generateDBIdentifierFromName("My Mothers Name")));
        assertTrue("my_mothers_name".equals(DBUtils.generateDBIdentifierFromName(" my    motHers         nAMe   ")));
    }
}
