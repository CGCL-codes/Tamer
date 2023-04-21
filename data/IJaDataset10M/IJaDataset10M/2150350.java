package com.luxoft.fitpro.core.testresult;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import com.luxoft.fitpro.core.testcase.Location;
import com.luxoft.fitpro.core.testcase.Suite;
import com.luxoft.fitpro.core.testcase.TestFile;
import com.luxoft.fitpro.core.tester.FakeFitProvider;
import com.luxoft.fitpro.core.tester.RunnerVisitor;
import fit.Counts;

public class ResultCollectorTest {

    private static final Location SUBSUITE_LOCATION = Location.createLocation("c:/subSuite.xml");

    private static final Location SUBSUBSUITE_LOCATION = Location.createLocation("c:/subSubSuite.xml");

    public static final Location SUITE_LOCATION = Location.createLocation("c:/suite.xml");

    private Suite suite;

    private Suite subsuite;

    private Suite subsubsuite;

    private TestFile testFile1;

    private TestFile testFile2;

    private TestFile setupFile;

    private TestFile teardownFile;

    private FakeFitProvider fakeFitProvider;

    RunnerVisitor runVisitor;

    @Before
    public void setUp() throws Exception {
        fakeFitProvider = new FakeFitProvider();
        runVisitor = new RunnerVisitor(fakeFitProvider);
        suite = new Suite(SUITE_LOCATION);
        subsuite = new Suite(SUBSUITE_LOCATION);
        subsubsuite = new Suite(SUBSUBSUITE_LOCATION);
        testFile1 = new TestFile(Location.createLocation("c:/file1"));
        testFile2 = new TestFile(Location.createLocation("c:/file2"));
        setupFile = new TestFile(Location.createLocation("c:/setup"));
        teardownFile = new TestFile(Location.createLocation("c:/teardown"));
        suite.setSetup(setupFile);
        suite.add(testFile2);
        suite.setTeardown(teardownFile);
        suite.add(subsuite);
        subsuite.add(testFile1);
        subsuite.add(subsubsuite);
        suite.accept(runVisitor);
    }

    @Test
    public void testGetResult() {
        FitResult expectedResult = new SuiteFitResult(suite.getLogicalName(), suite.getDescription());
        FitResult fileResult1 = new FileFitResult(new Counts(1, 2, 3, 4));
        FitResult subsuiteResult = new SuiteFitResult(subsuite.getLogicalName(), subsuite.getDescription());
        subsuiteResult.addResult(new FileFitResult(new Counts(1, 2, 3, 4)));
        subsuiteResult.addResult(new SuiteFitResult(subsubsuite.getLogicalName(), subsubsuite.getDescription()));
        fileResult1.setSetupResult(new FileFitResult(new Counts(1, 2, 3, 4)));
        fileResult1.setTeardownResult(new FileFitResult(new Counts(1, 2, 3, 4)));
        expectedResult.addResult(fileResult1);
        expectedResult.addResult(subsuiteResult);
        suite.accept(runVisitor);
        Assert.assertTrue(compareResults(expectedResult, runVisitor.getResult()));
    }

    private boolean compareResults(FitResult result1, FitResult result2) {
        return result1.getHtmlResult().equals(result2.getHtmlResult());
    }
}
