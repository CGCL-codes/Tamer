package com.varaneckas.hawkscope.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import com.varaneckas.hawkscope.Version;

public class VersionTest {

    private static final Log log = LogFactory.getLog(VersionTest.class);

    @Test
    public void testAnything() throws Exception {
        Version.checkForUpdate();
        boolean update = Version.isUpdateAvailable();
        log.debug("Update available: " + update);
    }
}
