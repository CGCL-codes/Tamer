package org.jzonic.yawiki;

import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Administrator
 */
public class MyDiffTest extends TestCase {

    public MyDiffTest(java.lang.String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(MyDiffTest.class);
        return suite;
    }

    public void testDiffOne() {
        ArrayList origContent = new ArrayList();
        origContent.add("12");
        origContent.add("23");
        origContent.add("34");
        origContent.add("45");
        ArrayList oldContent = new ArrayList();
        oldContent.add("23");
        oldContent.add("34");
        oldContent.add("61");
        oldContent.add("65");
        MyDiff diff = new MyDiff(origContent, oldContent);
        diff.runDiff();
    }

    public void testDiffTwo() {
        ArrayList origContent = new ArrayList();
        origContent.add("12");
        origContent.add("23");
        origContent.add("34");
        origContent.add("45");
        ArrayList oldContent = new ArrayList();
        oldContent.add("23");
        oldContent.add("41");
        oldContent.add("34");
        oldContent.add("61");
        oldContent.add("65");
        MyDiff diff = new MyDiff(origContent, oldContent);
        diff.runDiff();
    }
}
