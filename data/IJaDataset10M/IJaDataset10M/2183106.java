package org.moltools.apps.probemaker.modules;

/**
 * A representation of a test. Each AnalysisModule defines a number of tests. 
 * The user interface allow setting which of these tests to perform. This
 * is done by calling the setPerform method of the corresponding Test object.
 * The Test object does not implement the test itself, but represents a test 
 * for user interface purposes. 
 * @author Johan Stenberg
 * @version 1.0
 */
public class TestDescriptor {

    boolean perform;

    String brief;

    /**Create a new Test with the specified description*/
    public TestDescriptor(String description) {
        perform = true;
        brief = description;
    }

    /**Return a brief description of the test*/
    public String getBrief() {
        return brief;
    }

    /**Return whether or not to perform the test represented by this object*/
    public boolean perform() {
        return perform;
    }

    /**Set whether or not to perform the test represented by this object*/
    public void setPerform(boolean perform) {
        this.perform = perform;
    }
}
