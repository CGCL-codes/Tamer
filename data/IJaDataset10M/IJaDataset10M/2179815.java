package org.carabiner.infinitest;

/**
 * Implemementers of this interface must provide a default constructor so that the TestRunner can be
 * created using Class.newInstance();
 * 
 * @author <a href="mailto:benrady@gmail.com"Ben Rady</a>
 * 
 */
public interface TestRunner {

    public void runTest(String testClass);

    public void addTestStatusListener(TestStatusListener listener);

    /**
   * Try to stop the currently running test
   * 
   */
    public void killTest();
}
