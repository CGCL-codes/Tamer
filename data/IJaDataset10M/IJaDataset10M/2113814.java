package org.databene.commons.web;

import java.net.URL;
import org.databene.commons.DatabeneTestUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests the {@link DownloadCache}.<br/><br/>
 * Created: 15.08.2010 10:34:15
 * @since 0.5.4
 * @author Volker Bergmann
 */
public class DownloadCacheTest {

    Logger LOGGER = LoggerFactory.getLogger(DownloadCacheTest.class);

    @Test
    public void test() throws Exception {
        if (!DatabeneTestUtil.isOnline()) {
            LOGGER.warn("Skipping " + getClass().getName() + " since we're offline");
            return;
        }
        DownloadCache cache = new DownloadCache();
        cache.get(new URL("http://bergmann-it.de"));
        cache.get(new URL("http://bergmann-it.de"));
    }
}
