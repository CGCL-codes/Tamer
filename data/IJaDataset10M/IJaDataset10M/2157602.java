package com.jeantessier.commandline;

import junit.framework.*;

public class TestAtLeastParameterStrategy extends TestCase {

    private ParameterStrategy strategy;

    protected void setUp() throws Exception {
        super.setUp();
        strategy = new AtLeastParameterStrategy(2);
    }

    public void testAccept() throws CommandLineException {
        assertEquals(1, strategy.accept("value"));
    }

    public void testValidate() throws CommandLineException {
        try {
            strategy.validate();
            fail("Strategy accepted too many values");
        } catch (CommandLineException e) {
        }
        strategy.accept("value1");
        try {
            strategy.validate();
            fail("Strategy accepted too many values");
        } catch (CommandLineException e) {
        }
        strategy.accept("value2");
        strategy.validate();
        strategy.accept("value3");
        strategy.validate();
    }
}
