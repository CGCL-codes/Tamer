package org.noranj.formak.shared;

import static org.junit.Assert.*;
import java.util.Date;
import java.util.Locale;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.noranj.formak.shared.utils.Formatter;

public class FormatterTest {

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
    public void testFormatDateTime() {
        String date = Formatter.formatDate(System.currentTimeMillis(), GlobalSettings.getDateFormat());
        System.out.println(date);
        if (date.length() < 10) fail("date[" + date + "] is not well formed.");
    }

    @Test
    public void testFormatAmount() {
        String amount = Formatter.formatAmount(473476);
        System.out.println(amount);
        if (amount.length() < 2) fail("amount[" + amount + "] is not well formed.");
    }

    @Test
    public void testConvertToAmount() {
        fail("Not being implemented");
    }
}
