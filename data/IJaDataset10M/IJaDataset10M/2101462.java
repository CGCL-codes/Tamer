package org.powermock.modules.junit3;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.powermock.modules.junit3.privateandfinal.StupidPrivateFinalTest;
import org.powermock.modules.junit3.singleton.StupidSingletonTest;
import org.powermock.modules.junit3.suppressconstructor.SuppressConstructorHierarchyDemoTest;

public class AllJUnit3Tests extends TestCase {

    @SuppressWarnings("unchecked")
    public static TestSuite suite() throws Exception {
        return new PowerMockSuite(StupidPrivateFinalTest.class, StupidSingletonTest.class, SuppressConstructorHierarchyDemoTest.class);
    }
}
