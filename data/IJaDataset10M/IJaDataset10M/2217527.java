package com.thoughtworks.shadow.tests;

import junit.framework.TestCase;
import java.text.MessageFormat;

public class Err extends TestCase {

    public static String getTestName() {
        return MessageFormat.format("testShouldThrowException({0})", new Object[] { Err.class.getName() });
    }

    public Err() {
        super("testShouldThrowException");
    }

    public void testShouldThrowException() throws Exception {
        throw new Exception("cause a junit error");
    }
}
