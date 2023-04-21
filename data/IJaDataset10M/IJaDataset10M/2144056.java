package org.archive;

import org.archive.util.TestUtils;
import junit.framework.Test;

/**
 * @author pjack
 *
 */
public class TestAll {

    public static Test suite() throws Exception {
        return TestUtils.makePackageSuite(TestAll.class);
    }
}
