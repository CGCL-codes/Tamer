package se.vgregion.webbisar.svc.sitemap.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import se.vgregion.sitemap.model.SitemapCache;
import se.vgregion.sitemap.model.SitemapEntry;
import se.vgregion.webbisar.svc.WebbisServiceMock;

/**
 * @author anders.bergkvist@omegapoint.se
 * 
 */
public class WebbisExtraInfoSitemapCacheLoaderTest {

    private WebbisExtraInfoSitemapCacheLoader sitemapCacheLoader;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        WebbisServiceMock webbisService = new WebbisServiceMock();
        WebbisSitemapEntryLoader sitemapEntryLoader = new WebbisSitemapEntryLoader(webbisService);
        sitemapCacheLoader = new WebbisExtraInfoSitemapCacheLoader(sitemapEntryLoader, "http://base.com", "http://base.com/images");
    }

    /**
     * Test method for
     * {@link se.vgregion.webbisar.svc.sitemap.impl.WebbisExtraInfoSitemapCacheLoader#populateSitemapEntryCache(se.vgregion.sitemap.model.SitemapCache)}
     * .
     */
    @Test
    public final void testPopulateSitemapEntryCacheSitemapCache() {
        SitemapCache cache = new SitemapCache();
        sitemapCacheLoader.populateSitemapEntryCache(cache);
        assertEquals(1, cache.getEntries().size());
        SitemapEntry entry = cache.getEntries().get(0);
        assertEquals("http://base.com?webbisId=1", entry.getLocation());
        assertEquals("2010-01-11T01:00:00+01:00", entry.getLastModified());
        assertEquals("daily", entry.getChangeFrequency());
        for (SitemapEntry.ExtraInformation extraInformation : entry) {
            if ("parent1".equals(extraInformation.getName())) {
                assertEquals("Gunnar Bohlin", extraInformation.getValue());
            } else if ("name".equals(extraInformation.getName()) || "weight".equals(extraInformation.getName()) || "length".equals(extraInformation.getName()) || "hostpital".equals(extraInformation.getName()) || "locality".equals(extraInformation.getName()) || "parent2".equals(extraInformation.getName()) || "birthdate".equals(extraInformation.getName()) || "imageLink".equals(extraInformation.getName())) {
            } else {
                fail("Unexpected extra information found");
            }
        }
    }
}
