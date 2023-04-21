package org.openscience.cdk.interfaces;

/**
 * Interfaces for objects that create new, clean test objects to be used by
 * unit testing for the module <code>data</code>, <code>datadebug</code> and
 * <code>nonotify</code>.
 *
 * @cdk.module  test-interfaces
 */
public interface ITestObjectBuilder {

    /**
     * Returns a clean new test object.
     * @return a new test object
     */
    public IChemObject newTestObject();
}
