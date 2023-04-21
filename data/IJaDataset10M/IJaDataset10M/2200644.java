package org.springunit.framework.tests;

import javax.sql.DataSource;
import org.junit.Test;
import org.springunit.framework.SpringUnitContext;
import org.springunit.framework.junit4.SpringUnit4TransactionalTest;

public class OneDeep4TransactionalTest extends SpringUnit4TransactionalTest {

    public OneDeep4TransactionalTest() {
        this.setDependencyCheck(false);
    }

    @Test
    public void testOne() throws Exception {
        assertEquals(1, getObject("a"));
        assertEquals(4, getObject("d"));
        assertNull(getObject("g"));
    }

    @Test
    public void testFour() throws Exception {
        assertNull(getObject("z"));
    }

    public SpringUnitContext getOneDeep4TransactionalTest() {
        return this.oneDeep4TransactionalTest;
    }

    public void setOneDeep4TransactionalTest(SpringUnitContext oneDeep4TransactionalTest) {
        this.oneDeep4TransactionalTest = oneDeep4TransactionalTest;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private SpringUnitContext oneDeep4TransactionalTest;

    private DataSource dataSource;
}
