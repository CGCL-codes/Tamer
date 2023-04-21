package net.derquinse.common.test;

import java.util.UUID;
import org.testng.annotations.Test;

/**
 * Tests for SerializabilityTests
 * @author Andres Rodriguez
 */
public class SerializabilityTestsTest {

    /**
	 * String.
	 */
    @Test
    public void string() {
        SerializabilityTests.check(UUID.randomUUID().toString());
    }

    /**
	 * UUID.
	 */
    @Test
    public void uuid() {
        SerializabilityTests.check(UUID.randomUUID());
    }
}
