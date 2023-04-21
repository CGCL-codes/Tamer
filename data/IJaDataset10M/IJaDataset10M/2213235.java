package org.happy.commons.ver1x1.patterns.version;

import static org.junit.Assert.*;
import java.util.Set;
import org.happy.commons.patterns.version.VersionFactory_1x0;
import org.happy.commons.patterns.version.Version_1x0;
import org.junit.Test;

public class VersionFactory_1x0Test {

    @Test
    public void testGetAPI() {
        VersionFactory_1x0<Long> factory = new VersionFactory_1x0<Long>() {

            public <V extends Version_1x0<Long>> V getAPI(Long version) {
                return null;
            }

            public <LV extends Set<? extends Version_1x0<Long>>> Set<LV> getAllVersions() {
                return null;
            }
        };
        assertNotNull(factory);
    }
}
