package org.personalsmartspace.pss_sm_service.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestServiceLifeTimes {

    private static final String TEST_ID_1 = "7698899878";

    private static final String TEST_ID_2 = "87897879";

    private static final String TEST_ID_3 = "76556587676878";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testClass() throws Exception {
        ServiceLifeTimes life = ServiceLifeTimes.getInstance();
        assertNotNull(life);
        long startTime_1 = System.currentTimeMillis();
        life.addSession(TEST_ID_1, startTime_1);
        assertEquals(startTime_1, life.getSessionStartTime(TEST_ID_1));
        long startTime_2 = System.currentTimeMillis();
        life.addSession(TEST_ID_2, startTime_2);
        assertEquals(startTime_2, life.getSessionStartTime(TEST_ID_2));
        long startTime_3 = System.currentTimeMillis();
        life.addSession(TEST_ID_3, startTime_3);
        assertEquals(startTime_3, life.getSessionStartTime(TEST_ID_3));
    }
}
