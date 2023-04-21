package com.sun.midp.automation;

import com.sun.midp.midletsuite.*;
import com.sun.midp.midlet.MIDletSuite;
import java.util.*;
import java.io.*;

/**
 * AutoSuiteDescriptor implementation for internal suite
 */
final class AutoInternalSuiteDescriptorImpl extends AutoSuiteDescriptorImpl {

    /** Name of the suite's MIDlet class */
    private String midletClassName;

    /** Suite ID for all internal suites */
    static final int INTERNAL_SUITE_ID = MIDletSuite.INTERNAL_SUITE_ID;

    /**
     * Constructor
     *
     * @param midletClassName name of the suite's MIDlet class
     * @param midletSuite internal MIDlet suite representation
     */
    AutoInternalSuiteDescriptorImpl(String midletClassName, MIDletSuiteImpl midletSuite) {
        super(midletSuite);
        this.midletClassName = midletClassName;
        if (suiteName == null) {
            suiteName = midletClassName;
        }
        if (totalMIDlets > 1) {
            totalMIDlets = 1;
        }
    }

    /**
     * Tests if this suite is external
     *
     * @return true, if this suite is external
     */
    boolean isExternalSuite() {
        return false;
    }

    /**
     * Gets suite ID
     *
     * @return suite ID as String
     */
    int getSuiteID() {
        return INTERNAL_SUITE_ID;
    }

    /**
     * Updates list of suite's MIDlets
     */
    void updateMIDletsList() {
        suiteMIDlets = new Vector(totalMIDlets);
        AutoMIDletDescriptorImpl midlet = null;
        midlet = new AutoMIDletDescriptorImpl(this, suiteName, midletClassName);
        suiteMIDlets.addElement(midlet);
    }
}
