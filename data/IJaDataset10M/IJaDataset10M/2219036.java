package vogar.target.junit3;

import junit.framework.TestCase;

public class FailTest extends TestCase {

    public void testSuccess() {
    }

    public void testFail() {
        fail("failed.");
    }

    public void testThrowException() {
        throw new RuntimeException("exceptrion");
    }
}
