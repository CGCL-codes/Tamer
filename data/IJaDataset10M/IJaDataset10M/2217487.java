package org.archive.processors.util;

import org.archive.util.TestUtils;
import junit.framework.TestCase;

/**
 * @author pjack
 *
 */
public class CrawlServerTest extends TestCase {

    public void testSerialization() throws Exception {
        TestUtils.testSerialization(new CrawlServer("hi"));
    }
}
