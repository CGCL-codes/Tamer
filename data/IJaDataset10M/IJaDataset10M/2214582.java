package de.banh.bibo.servlet.tainted;

import java.util.Random;

/**
 * @author Thomas
 * 
 */
public class TaintedGanzeZahlTest extends AbstractTaintedTest {

    @Override
    protected TaintedTestCases getTestCases() {
        TestCase[] testCases = new TestCase[1000];
        int i = 0;
        testCases[i++] = new TestCase("abc").isTainted();
        testCases[i++] = new TestCase("+def").isTainted();
        testCases[i++] = new TestCase("-abc").isTainted();
        testCases[i++] = new TestCase("1-123").isTainted();
        testCases[i++] = new TestCase("123,4").isTainted();
        testCases[i++] = new TestCase("--123123").isTainted();
        Random random = new Random();
        for (; i < testCases.length; i++) {
            testCases[i] = new TestCase(Integer.toString(random.nextInt(Integer.MAX_VALUE)));
        }
        return new TaintedTestCases(testCases);
    }

    @Override
    protected Class<? extends TaintedBase> getTestClass() {
        return TaintedGanzeZahl.class;
    }
}
