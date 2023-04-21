package com.google.inject.testing.guiceberry;

import com.google.guiceberry.GuiceBerryJunit3Test;
import com.google.inject.testing.guiceberry.controllable.IcMasterTest;
import com.google.inject.testing.guiceberry.controllable.InterceptingBindingsBuilderTest;
import com.google.inject.testing.guiceberry.util.MutableSingletonScopeTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for com.google.inject.testing.guiceberry.junit3");
        suite.addTestSuite(GuiceBerryJunit3Test.class);
        suite.addTestSuite(IcMasterTest.class);
        suite.addTestSuite(InterceptingBindingsBuilderTest.class);
        suite.addTestSuite(MutableSingletonScopeTest.class);
        return suite;
    }
}
