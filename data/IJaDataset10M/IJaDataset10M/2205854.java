package com.chrisgammage.wjt.test_cases;

import com.chrisgammage.wjt.WJT;
import com.chrisgammage.wjt.errors.WJTError;
import junit.framework.TestCase;

public class ClassTestTest extends TestCase {

    private final String sourceFolder = "com.chrisgammage.wjt.class_test";

    private final String outputFolder = "target\\ClassTest";

    /**
	 * Tests Class Structure.
	 */
    public final void testClassTest() {
        System.out.println("Testing ClassTest");
        try {
            WJT.compile(sourceFolder, "ClassTest", outputFolder);
        } catch (WJTError wjtException) {
            System.out.println("Caught WJT exception " + wjtException);
        }
    }
}
