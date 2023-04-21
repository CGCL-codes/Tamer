package org.xj4.spi;

import java.util.List;
import org.junit.runners.model.FrameworkMethod;
import org.xj4.XJ4TestClass;

/**
 * An extension point for performing actions before and after a test method.  This is akin to
 * {@link org.junit.Before @Before) and {@link org.junit.After @After} methods.
 *
 * @author Jared Bunting
 */
public interface MethodWrapperSource {

    /**
   * Provides a list of test wrappers for the test method.
   * @param testClass the test class
   * @param method the method that will be invoked
   * @param target the target test object
   * @return a list of wrappers
   */
    public List<TestWrapper> generateWrapper(XJ4TestClass testClass, FrameworkMethod method, Object target);
}
