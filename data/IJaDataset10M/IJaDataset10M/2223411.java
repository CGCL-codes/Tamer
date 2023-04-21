package org.rapla.plugin.abstractcalendar.tests;

import java.util.Calendar;
import java.util.Locale;
import org.junit.Test;
import org.rapla.ServletTestBase;
import org.rapla.components.util.SerializableDateTimeFormat;
import org.rapla.facade.ClientFacade;
import org.rapla.plugin.abstractcalendar.AbstractHTMLCalendarPage;
import org.rapla.plugin.mobile.tests.internal.NullHttpServletRequest;
import org.rapla.server.ServerService;

public class AbstractHTMLCalendarPageTest extends ServletTestBase {

    public AbstractHTMLCalendarPageTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testIsMobileTrue() throws Exception {
        NullHttpServletRequest request = new NullHttpServletRequest();
        request.setHeader("ua-pixels", "1337");
        assertTrue(AbstractHTMLCalendarPage.isMobile(request));
    }

    @Test
    public void testIsMobileFalse() throws Exception {
        NullHttpServletRequest request = new NullHttpServletRequest();
        assertFalse(AbstractHTMLCalendarPage.isMobile(request));
    }
}
