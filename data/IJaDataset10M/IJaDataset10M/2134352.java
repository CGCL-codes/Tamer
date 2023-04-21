package org.shaitu.wakeremote4j;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HostDAOTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetHosts() {
        HostBean bean = new HostBean();
        bean.setName("home");
        bean.setMac("aa:bb:cc:dd:ee:ff");
        HostDAO.createHost(bean);
        assertEquals(1, HostDAO.getHosts().size());
    }

    @Test
    public void testGetHost() {
        HostBean bean = HostDAO.getHost("home");
        assertEquals("aa:bb:cc:dd:ee:ff", bean.getMac());
    }
}
