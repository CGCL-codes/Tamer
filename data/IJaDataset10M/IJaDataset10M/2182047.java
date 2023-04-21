package com.wle.server.engine.exception;

import com.wle.server.engine.ErrorCode;
import junit.framework.TestCase;

public class EngineExceptionTest extends TestCase {

    public EngineExceptionTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testEngineExceptionString() {
        String message = "Hello";
        EngineException e = new EngineException(message);
        assertEquals(message, e.getMessage());
        assertEquals(ErrorCode.FAIL, e.getCode());
    }

    public void testEngineExceptionStringInt() {
        String message = "Hello";
        int code = ErrorCode.DIRECTORY_NOT_EXIST;
        EngineException e = new EngineException(message, code);
        assertEquals(message, e.getMessage());
        assertEquals(code, e.getCode());
    }

    public void testSessionNotFoundException() {
        long sessionID = 1234;
        SessionNotFoundException e = new SessionNotFoundException(sessionID);
        assertTrue(e instanceof EngineException);
        assertEquals(sessionID, e.getSessionId());
        assertEquals(ErrorCode.SESSION_NOT_FOUND, e.getCode());
    }
}
