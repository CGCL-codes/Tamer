package com.trendsoft.eyetest.cases;

import com.trendsoft.eyetest.EyeTestSkeleton;

/**
 * @author vgagin
 *
 * TODO Add JavaDoc
 */
public class EyeTestCase_UnableToPopOperandOffAnEmptyStack extends EyeTestSkeleton {

    private static final String CLASS_NAME = "com.trendsoft.eyetest.classes.EyeTest_UnableToPopOperandOffAnEmptyStack";

    /**
     * Constructor for EyeTestEmptyClass.
     * @param name
     */
    public EyeTestCase_UnableToPopOperandOffAnEmptyStack(String name) {
        super(name);
    }

    public void test() throws Exception {
        Class<?> c = loadClass(CLASS_NAME);
        Object instance = c.newInstance();
    }
}
