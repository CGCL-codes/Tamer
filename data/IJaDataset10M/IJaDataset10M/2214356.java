package com.wle.server.engine;

import java.io.File;
import junit.framework.TestCase;

/**
 * @author wew
 *
 */
public class ErrorCodeTest extends TestCase {

    boolean success = false;

    static final String TEST_DIRECTORY = "../sandbox/";

    static final String TEST_FILE = "MyDoc.tex";

    /**
	 * @param name
	 */
    public ErrorCodeTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Test method for {@link com.wle.server.engine.ErrorCode#isSuccess(int)}.
	 */
    public void testIsSuccess() {
        java.util.Map<String, String> env = System.getenv();
        for (String key : env.keySet()) {
            System.out.println(key + ": " + env.get(key));
        }
        for (Object key : System.getProperties().keySet()) {
            System.out.println(key.toString() + ": " + System.getProperty((String) key));
        }
        File f = new File(TEST_DIRECTORY + TEST_FILE);
        assertTrue(f.exists());
        assertTrue(f.canRead());
        success = ErrorCode.isSuccessful(ErrorCode.SUCCESS);
        assertTrue(success);
        success = ErrorCode.isSuccessful(ErrorCode.FAIL);
        assertFalse(success);
    }

    /**
	 * Test method for {@link com.wle.server.engine.ErrorCode#isFailure(int)}.
	 */
    public void testIsFailure() {
        success = ErrorCode.isFailure(ErrorCode.SUCCESS);
        assertFalse(success);
        success = ErrorCode.isFailure(ErrorCode.FAIL);
        assertTrue(success);
        success = ErrorCode.isFailure(ErrorCode.DIRECTORY_NOT_EXIST);
        assertTrue(success);
        success = ErrorCode.isFailure(ErrorCode.FILE_NOT_EXIST);
        assertTrue(success);
    }
}
