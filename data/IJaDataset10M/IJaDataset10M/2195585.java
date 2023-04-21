package org.apache.tools.ant.taskdefs.optional.junit;

import junit.framework.TestCase;

/**
 */
public class VmCrash extends TestCase {

    public VmCrash(String name) {
        super(name);
    }

    public void testCrash() {
        System.exit(0);
    }
}
