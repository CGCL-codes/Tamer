package com.sitechasia.webx.core.dao.jdbc.hsqldb;

import junit.framework.TestCase;

public class HsqldbPageSqlProviderTest extends TestCase {

    HsqldbPageSqlProvider hsqlpage;

    public void setUp() {
        hsqlpage = new HsqldbPageSqlProvider();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testgetPageSql() {
        assertEquals("SELECT LIMIT 2 5 top 10 from user", hsqlpage.getPageSql("select top 10 from user", 2, 5));
        assertEquals("select top 10 from user", hsqlpage.getPageSql("select top 10 from user", -2, 0));
    }
}
