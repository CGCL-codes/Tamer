package com.volantis.shared.net.http;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link HttpStatusCode}.
 */
public class HttpStatusCodeTestCase extends TestCaseAbstract {

    /**
     * Ensure that it creates codes properly.
     */
    public void test() throws Exception {
        HttpStatusCode code;
        code = HttpStatusCode.getStatusCode(200);
        assertSame(code, HttpStatusCode.OK);
        code = HttpStatusCode.getStatusCode(404);
        assertSame(code, HttpStatusCode.NOT_FOUND);
        code = HttpStatusCode.getStatusCode(900);
        assertSame(code, HttpStatusCode.RESPONSE_TIMED_OUT);
    }
}
