package de.hu_berlin.sam.mmunit.suggestion.execution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import org.junit.Test;
import de.hu_berlin.sam.mmunit.error.AttributeMissing;
import de.hu_berlin.sam.mmunit.error.ClassMissing;
import de.hu_berlin.sam.mmunit.error.MMUnitError;
import de.hu_berlin.sam.mmunit.execution.MMTest;
import de.hu_berlin.sam.mmunit.suggestion.suggestionspace.ResultTestSuite;

public class TestSpaceTests {

    /**
	 * Helper for MM-Testexecution with check for suggestion generation.
	 * 
	 * @param metamodel
	 *            the metamodel location
	 * @param testSpec
	 *            the testspecification location
	 * @param expectedError
	 *            the expected error from the mmunit-error metamodel
	 */
    private void testHelper(String metamodel, String testSpec, Class<? extends MMUnitError> expectedError) {
        MMTest test = new MMTest(metamodel, testSpec);
        boolean failed = !test.execute();
        assertFalse("MMTest should not pass", !failed);
        assertTrue("Test was not registered", MMTest.getExecutionLog().contains(test));
    }

    private void classwithAttributeMissing() {
        testHelper("src/de/hu_berlin/sam/mmunit/suggestion/execution/multipleMM.ecore", "src/de/hu_berlin/sam/mmunit/suggestion/execution/classwithAttributeMissing.mmunit", ClassMissing.class);
    }

    private void multiAttMissing() {
        testHelper("src/de/hu_berlin/sam/mmunit/suggestion/execution/multipleMM.ecore", "src/de/hu_berlin/sam/mmunit/suggestion/execution/multiAttMissing.mmunit", AttributeMissing.class);
    }

    private void twoInsOneClassMissing() {
        testHelper("src/de/hu_berlin/sam/mmunit/suggestion/execution/multipleMM.ecore", "src/de/hu_berlin/sam/mmunit/suggestion/execution/twoInstancesOneclassMissing.mmunit", AttributeMissing.class);
    }

    @Test
    public void oneMMTestDepth1() {
        MMTest.clearExecutionLog();
        multiAttMissing();
        ResultTestSuite initialTestSuite = TestSpaceHelper.getInstance().createInitialResultTestSuite(1);
        TestSpaceHelper.getInstance().createTestSpaceFromInitialSuite(initialTestSuite);
        assertTrue("no suitesAfterAdaption were generated", !initialTestSuite.getSuitesAfterAdaptation().isEmpty());
        assertTrue("the link to parentSuite was not set", initialTestSuite.getSuitesAfterAdaptation().get(0).getSuiteBeforeAdaptation().equals(initialTestSuite));
        assertTrue("buildApaptation was not set", initialTestSuite.getSuitesAfterAdaptation().get(0).getBuildAdaptation().equals(initialTestSuite.getResults().get(0).getSuggestionList().get(0)));
        assertTrue("more than one suite after adaptation was created: " + initialTestSuite.getSuitesAfterAdaptation().size(), initialTestSuite.getSuitesAfterAdaptation().size() == 2);
        assertTrue("suites with depth = 2 were generated", initialTestSuite.getSuitesAfterAdaptation().get(0).getSuitesAfterAdaptation().isEmpty());
        TestSpaceHelper.getInstance().collectTestSpacePaths(initialTestSuite);
        assertTrue("No Paths were generated", !TestSpaceHelper.getInstance().getTestSpacePaths().isEmpty());
        assertTrue("Wrong number of Paths were returned:" + TestSpaceHelper.getInstance().getTestSpacePaths().size() + ".", TestSpaceHelper.getInstance().getTestSpacePaths().size() == 2);
    }

    @Test
    public void oneMMTestDepth3() {
        MMTest.clearExecutionLog();
        multiAttMissing();
        ResultTestSuite initialTestSuite = TestSpaceHelper.getInstance().createInitialResultTestSuite(5);
        TestSpaceHelper.getInstance().createTestSpaceFromInitialSuite(initialTestSuite);
        assertTrue("3 Adaptations didn't fix 3 missing Attributes", !initialTestSuite.getSuitesAfterAdaptation().get(0).getSuitesAfterAdaptation().get(0).getSuitesAfterAdaptation().get(0).getResults().get(0).isFailed());
        TestSpaceHelper.getInstance().collectTestSpacePaths(initialTestSuite);
        assertTrue("No Paths were generated", !TestSpaceHelper.getInstance().getTestSpacePaths().isEmpty());
        ArrayList<ArrayList<ResultTestSuite>> paths = TestSpaceHelper.getInstance().getTestSpacePaths();
        System.out.println(paths);
        assertTrue("Wrong number of Paths were returned:" + TestSpaceHelper.getInstance().getTestSpacePaths().size() + ".", TestSpaceHelper.getInstance().getTestSpacePaths().size() == 8);
    }

    @Test
    public void oneMMTestDepth2And2DifferentElements() {
        MMTest.clearExecutionLog();
        classwithAttributeMissing();
        ResultTestSuite initialTestSuite = TestSpaceHelper.getInstance().createInitialResultTestSuite(2);
        TestSpaceHelper.getInstance().createTestSpaceFromInitialSuite(initialTestSuite);
        assertTrue("2 Adaptations didn't fix 2 Missing Elements", !initialTestSuite.getSuitesAfterAdaptation().get(0).getSuitesAfterAdaptation().get(0).getResults().get(0).isFailed());
        TestSpaceHelper.getInstance().collectTestSpacePaths(initialTestSuite);
        assertTrue("No Paths were generated", !TestSpaceHelper.getInstance().getTestSpacePaths().isEmpty());
        ArrayList<ArrayList<ResultTestSuite>> paths = TestSpaceHelper.getInstance().getTestSpacePaths();
        System.out.println(paths);
        assertTrue("Wrong number of Paths were returned:" + TestSpaceHelper.getInstance().getTestSpacePaths().size() + ".", TestSpaceHelper.getInstance().getTestSpacePaths().size() == 11);
    }

    @Test
    public void twoMMTestsDepth4() {
        MMTest.clearExecutionLog();
        multiAttMissing();
        classwithAttributeMissing();
        ResultTestSuite initialTestSuite = TestSpaceHelper.getInstance().createInitialResultTestSuite(5);
        TestSpaceHelper.getInstance().createTestSpaceFromInitialSuite(initialTestSuite);
        TestSpaceHelper.getInstance().collectTestSpacePaths(initialTestSuite);
        assertTrue("No Paths were generated", !TestSpaceHelper.getInstance().getTestSpacePaths().isEmpty());
        ArrayList<ArrayList<ResultTestSuite>> paths = TestSpaceHelper.getInstance().getTestSpacePaths();
        assertTrue("Wrong number of Paths were returned:" + TestSpaceHelper.getInstance().getTestSpacePaths().size() + ".", TestSpaceHelper.getInstance().getTestSpacePaths().size() == 145);
    }

    @Test
    public void twoInstanceOneClassMissing() {
        MMTest.clearExecutionLog();
        twoInsOneClassMissing();
        ResultTestSuite initialTestSuite = TestSpaceHelper.getInstance().createInitialResultTestSuite(5);
        TestSpaceHelper.getInstance().createTestSpaceFromInitialSuite(initialTestSuite);
        TestSpaceHelper.getInstance().collectTestSpacePaths(initialTestSuite);
        ArrayList<ArrayList<ResultTestSuite>> paths = TestSpaceHelper.getInstance().getTestSpacePaths();
        System.out.println(paths);
        assertTrue("No Paths were generated", !TestSpaceHelper.getInstance().getTestSpacePaths().isEmpty());
        assertTrue("Wrong number of Paths were returned:" + TestSpaceHelper.getInstance().getTestSpacePaths().size() + ".", TestSpaceHelper.getInstance().getTestSpacePaths().size() == 4);
    }
}
