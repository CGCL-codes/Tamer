package net.sourceforge.pebble.permalink;

import net.sourceforge.pebble.domain.Day;
import net.sourceforge.pebble.domain.Month;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.api.permalink.PermalinkProvider;

/**
 * Tests for the DefaultPermalinkProvider class.
 *
 * @author    Simon Brown
 */
public abstract class PermalinkProviderSupportTestCase extends SingleBlogTestCase {

    protected PermalinkProvider permalinkProvider;

    protected void setUp() throws Exception {
        super.setUp();
        permalinkProvider = getPermalinkProvider();
        blog.setPermalinkProvider(permalinkProvider);
    }

    /**
   * Gets a PermalinkProvider instance.
   *
   * @return  a PermalinkProvider instance
   */
    protected abstract PermalinkProvider getPermalinkProvider();

    /**
   * Tests that a monthly blog permalink can be generated.
   */
    public void testGetPermalinkForMonth() {
        Month month = blog.getBlogForMonth(2004, 01);
        assertEquals("/2004/01.html", permalinkProvider.getPermalink(month));
    }

    /**
   * Tests that a monthly blog permalink is recognised.
   */
    public void testMonthPermalink() {
        assertTrue(permalinkProvider.isMonthPermalink("/2004/01.html"));
        assertFalse(permalinkProvider.isMonthPermalink("/2004/01/01.html"));
        assertFalse(permalinkProvider.isMonthPermalink("/someotherpage.html"));
        assertFalse(permalinkProvider.isMonthPermalink(""));
        assertFalse(permalinkProvider.isMonthPermalink(null));
    }

    /**
   * Tests thet the correct monthly blog can be found from a permalink.
   */
    public void testGetMonth() {
        Month month = permalinkProvider.getMonth("/2004/07.html");
        assertEquals(2004, month.getYear().getYear());
        assertEquals(7, month.getMonth());
    }

    /**
   * Tests that a day permalink can be generated.
   */
    public void testGetPermalinkForDay() {
        Day day = blog.getBlogForDay(2004, 07, 14);
        assertEquals("/2004/07/14.html", permalinkProvider.getPermalink(day));
    }

    /**
   * Tests that a day permalink is recognised.
   */
    public void testDayPermalink() {
        assertTrue(permalinkProvider.isDayPermalink("/2004/01/01.html"));
        assertFalse(permalinkProvider.isDayPermalink("/2004/01.html"));
        assertFalse(permalinkProvider.isDayPermalink("/someotherpage.html"));
        assertFalse(permalinkProvider.isDayPermalink(""));
        assertFalse(permalinkProvider.isDayPermalink(null));
    }

    /**
   * Tests thet the correct day can be found from a permalink.
   */
    public void testGetDay() {
        Day day = permalinkProvider.getDay("/2004/07/14.html");
        assertEquals(2004, day.getMonth().getYear().getYear());
        assertEquals(7, day.getMonth().getMonth());
        assertEquals(14, day.getDay());
    }
}
