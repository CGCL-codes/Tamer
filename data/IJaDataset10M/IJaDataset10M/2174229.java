package com.sun.midp.automation;

import com.sun.midp.midletsuite.*;
import java.util.*;

/**
 *  AutoSuiteDescriptor implementation for external suite
 */
class AutoExternalSuiteDescriptorImpl extends AutoSuiteDescriptorImpl {

    /** suite ID */
    private int suiteID;

    /**
     * Constructor
     *
     * @param suiteID suite ID
     * @param midletSuite internal MIDlet suite representation
     */
    AutoExternalSuiteDescriptorImpl(int suiteID, MIDletSuiteImpl midletSuite) {
        super(midletSuite);
        this.suiteID = suiteID;
        if (suiteName == null) {
            suiteName = String.valueOf(suiteID);
        }
    }

    /**
     * Tests if this suite is external
     *
     * @return true, if this suite is external
     */
    boolean isExternalSuite() {
        return true;
    }

    /**
     * Gets suite ID
     *
     * @return suite ID as String
     */
    int getSuiteID() {
        return suiteID;
    }

    /**
     * Updates list of suite's MIDlets
     */
    void updateMIDletsList() {
        suiteMIDlets = new Vector(totalMIDlets);
        for (int i = 1; i <= totalMIDlets; i++) {
            AutoMIDletDescriptorImpl midlet = null;
            String midletAttr = midletSuite.getProperty("MIDlet-" + i);
            midlet = new AutoMIDletDescriptorImpl(this, midletAttr);
            suiteMIDlets.addElement(midlet);
        }
    }
}
