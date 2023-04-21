package com.google.visualization.datasource.base;

import junit.framework.TestCase;

/**
 * Unit test for StatusType.
 *
 * @author Nimrod T.
 */
public class StatusTypeTest extends TestCase {

    public void testBasic() {
        StatusType statusType = StatusType.OK;
        assertEquals("ok", statusType.lowerCaseString());
    }
}
