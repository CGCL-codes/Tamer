package org.charvolant.tmsnet.resources.ratings;

import java.util.Locale;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the network map factory.
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
public class RatingMapFactoryTest {

    private Locale oldLocale;

    /**
   * @throws java.lang.Exception
   */
    @Before
    public void setUp() throws Exception {
        this.oldLocale = Locale.getDefault();
    }

    /**
   * @throws java.lang.Exception
   */
    @After
    public void tearDown() throws Exception {
        Locale.setDefault(this.oldLocale);
    }

    private Locale getLocale(String name) {
        for (Locale locale : Locale.getAvailableLocales()) if (locale.toString().equals(name)) return locale;
        throw new IllegalStateException("Can't find locale " + name);
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.resources.networks.RatingMapFactory#RatingMapFactory()}.
   */
    @Test
    public void testGetInstance() {
        RatingMapFactory factory = RatingMapFactory.getInstance();
        Assert.assertNotNull(factory);
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.resources.networks.RatingMapFactory#getRatingMap()}.
   */
    @Test
    public void testRatingMap1() throws Exception {
        RatingMapFactory factory = RatingMapFactory.getInstance();
        RatingMap map;
        Locale.setDefault(this.getLocale("en_AU"));
        map = factory.getResource();
        Assert.assertNotNull(map);
        Assert.assertEquals("Australia TV", map.getName());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.resources.networks.RatingMapFactory#getRatingMap()}.
   */
    @Test
    public void testRatingMap2() throws Exception {
        RatingMapFactory factory = RatingMapFactory.getInstance();
        RatingMap map;
        Locale.setDefault(this.getLocale("fr_FR"));
        map = factory.getResource();
        Assert.assertNotNull(map);
        Assert.assertEquals("Default", map.getName());
    }
}
