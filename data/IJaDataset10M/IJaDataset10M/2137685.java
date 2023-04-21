package com.luxoft.fitpro.core.tester;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.luxoft.fitpro.core.testcase.Suite;
import com.luxoft.fitpro.core.testcase.TestFile;
import com.luxoft.fitpro.core.testresult.FitResult;
import com.luxoft.fitpro.core.testresult.SuiteFitResult;
import fit.Counts;

public class RunnerVisitorTestNoSetupTeardown {

    public static final String SUITE_LOCATION = "location";

    private Suite suite;

    private Suite subSuite;

    private TestFile testFile1;

    private TestFile testFile2;

    private TestFile testFile3;

    private FitProvider fakeFitProvider;

    private RunnerVisitor runVisitor1;

    @Before
    public void setUp() {
        fakeFitProvider = new FakeFitProvider();
        runVisitor1 = new RunnerVisitor(fakeFitProvider);
        suite = new Suite(SUITE_LOCATION);
        subSuite = new Suite("subSuite");
        testFile1 = new TestFile("file1");
        testFile2 = new TestFile("file2");
        testFile3 = new TestFile("file3");
        suite.add(testFile1);
        subSuite.add(testFile2);
        suite.add(subSuite);
        suite.add(testFile3);
        suite.accept(runVisitor1);
    }

    @Test
    public void testFakeProvider() {
        Assert.assertEquals(new Counts(1, 2, 3, 4), fakeFitProvider.runFIT(testFile1).getCounts());
    }

    @Test
    public void testVisitor() {
        FitResult result = runVisitor1.getResult();
        Assert.assertTrue(result instanceof SuiteFitResult);
        Assert.assertEquals(new Counts(3, 6, 9, 12), result.getCounts());
    }

    @Test
    public void testNumberOfResultsForSuitWithoutSetupTeardown() {
        Assert.assertEquals(3, runVisitor1.getResult().getChildrenResult().size());
    }

    @Test
    public void testVisitorForSuite() {
        Assert.assertEquals(3, runVisitor1.getResult().getChildrenResult().size());
    }

    @Test
    public void testCanRunSingleTest() throws Exception {
        RunnerVisitor runVisitor = new RunnerVisitor(fakeFitProvider);
        testFile1.accept(runVisitor);
        Assert.assertEquals(new Counts(1, 2, 3, 4), runVisitor.getResult().getCounts());
    }
}
