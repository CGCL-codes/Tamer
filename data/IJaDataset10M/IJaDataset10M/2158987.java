package net.services.servicebus.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class DateUtilTest {

    public DateUtilTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDateUtilOffsetLength() throws Exception {
        System.out.println(DateUtil.getDatetimeWithOffset(30));
        assertEquals("yyyy-MM-dd'T'HH:mm:ss".length() - 2, DateUtil.getDatetimeWithOffset(30).length());
    }
}
