package com.ibm.wala.dila.tests.data.unittestcases;

import junit.textui.TestRunner;

/**
 * @author Jan Wloka
 * @version $Id: InheritanceTestA2.java,v 1.2 2008/10/08 21:21:09 jwloka Exp $
 */
public class InheritanceTestA2 extends InheritanceTestA1 {

    public static void main(String[] args) {
        TestRunner.run(InheritanceTestA2.class);
    }

    public void test2() throws Exception {
        check("2", "C2", "CC2");
    }
}
