package com.ibm.wala.dila.tests.data.unittestcases;

import junit.textui.TestRunner;

/**
 * @author Einar
 * @version $Id: ValidConstructorStringParamTest.java,v 1.2 2008/10/08 21:21:09 jwloka Exp $
 */
public class ValidConstructorStringParamTest extends ConstructorTestBase {

    public ValidConstructorStringParamTest(final String name) {
        super(name);
        add("C");
    }

    public ValidConstructorStringParamTest() {
        add("D");
    }

    public static void main(String[] args) {
        TestRunner.run(ValidConstructorStringParamTest.class);
    }
}
