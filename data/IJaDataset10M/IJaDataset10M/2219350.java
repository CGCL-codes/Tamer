package abbot.script;

import java.util.HashMap;
import abbot.DynamicLoadingConstants;
import abbot.finder.*;
import abbot.finder.AWTHierarchy;
import junit.framework.TestCase;
import junit.extensions.abbot.TestHelper;

/** ScriptFixture must only restore system state on terminate;
 * SystemState needs to be static, or preserved across ScriptFixture invocations
 * @author twall
 *
 */
public class FixtureTest extends TestCase {

    private Script script;

    private TestFixture fixture;

    private Script script2;

    private TestFixture fixture2;

    private Script scriptDup;

    private TestFixture fixtureDup;

    private StepRunner runner;

    protected void setUp() {
        script = new Script(new AWTHierarchy());
        script.addStep(fixture = new TestFixture(script, "1"));
        scriptDup = new Script(new AWTHierarchy());
        scriptDup.addStep(fixtureDup = new TestFixture(scriptDup, "1"));
        script2 = new Script(new AWTHierarchy());
        script2.addStep(fixture2 = new TestFixture(script2, "2"));
        runner = new StepRunner();
    }

    protected void tearDown() {
        fixture.terminate();
        fixtureDup.terminate();
        fixture2.terminate();
    }

    public void testXML() {
        String EXPECTED = "<fixture filename=\"" + fixture.getFilename() + "\" />";
        assertEquals("Wrong XML prefix", EXPECTED, Step.toXMLString(fixture));
    }

    public void testFixturesEquivalent() {
        assertTrue("Fixtures should be equivalent", fixture.equivalent(fixtureDup));
        assertTrue("Fixtures should not be equivalent", !fixture.equivalent(fixture2));
    }

    public void testLaunchWithNoExtantFixture() throws Throwable {
        UIContext context = script.getUIContext();
        context.launch(runner);
        assertTrue("Script context should indicate launched", context.isLaunched());
        assertTrue("Fixture should have been run", fixture.wasRun);
        context.terminate();
        assertFalse("Context should not be launched after terminate", context.isLaunched());
    }

    public void testRunWithNoExtantFixture() throws Throwable {
        UIContext context = script.getUIContext();
        runner.run(script);
        assertTrue("Context should indicate launched", context.isLaunched());
        assertTrue("Fixture run should have run", fixture.wasRun);
        context.terminate();
        assertFalse("Context should not be launched after terminate", context.isLaunched());
    }

    public void testLaunchWithExtantSameFixture() throws Throwable {
        runner.launch(script);
        fixture.wasRun = false;
        runner.launch(scriptDup);
        assertFalse("Same fixture should not be launched twice", fixtureDup.wasRun);
        assertTrue("Script 1 should be considered launched", script.getUIContext().isLaunched());
        assertTrue("Script 2 should be considered launched", scriptDup.getUIContext().isLaunched());
    }

    public void testRunWithExtantSameFixture() throws Throwable {
        runner.run(script);
        fixture.wasRun = false;
        runner.run(scriptDup);
        assertFalse("Fixture should not be run twice", fixture.wasRun);
        assertTrue("Script should still be considered launched", script.getUIContext().isLaunched());
        assertTrue("Script should still be considered launched", scriptDup.getUIContext().isLaunched());
    }

    public void testLaunchWithExtantDifferentFixture() throws Throwable {
        script.getUIContext().launch(runner);
        script2.getUIContext().launch(runner);
        assertFalse("First script should have terminated on launch of second", script.getUIContext().isLaunched());
        assertTrue("Second script should indicate launched", script2.getUIContext().isLaunched());
        assertTrue("Second fixture should have run", fixture2.wasRun);
    }

    public void testRunWithExtantDifferentFixture() throws Throwable {
        runner.run(script);
        runner.run(script2);
        assertFalse("First script should have terminated on launch of second", script.getUIContext().isLaunched());
        assertTrue("Second script should indicate launched", script2.getUIContext().isLaunched());
        assertTrue("Second fixture should have run", fixture2.wasRun);
    }

    public void testPreserveHierarchyDuringContext() throws Throwable {
        Hierarchy runnerHierarchy = runner.getHierarchy();
        assertTrue("Wrong runner default Hierarchy before run: " + runnerHierarchy, runnerHierarchy instanceof TestHierarchy);
        runner.run(script);
        Hierarchy scriptHierarchy = script.getHierarchy();
        assertEquals("Runner should set the script Hierarchy", runnerHierarchy, scriptHierarchy);
        UIContext context = script.getUIContext();
        Hierarchy fixtureHierarchy = context.getHierarchy();
        assertEquals("Runner and fixture (" + context + ") should share a Hierarchy", runnerHierarchy, fixtureHierarchy);
        assertEquals("Runner Hierarchy should not change", runnerHierarchy, runner.getHierarchy());
        StepRunner runner2 = new StepRunner();
        runner2.run(scriptDup);
        assertEquals("New runner should get Hierarchy from fixture", fixtureHierarchy, runner2.getHierarchy());
        assertEquals("New runner should set the new script's Hierarchy", fixtureHierarchy, scriptDup.getHierarchy());
    }

    public void testParseScriptWithFixture() throws Throwable {
        String SCRIPT = "<AWTTestScript>" + "<fixture filename=\"" + fixture.getFilename() + "\"/>" + "</AWTTestScript>";
        script.load(ScriptTest.stringToReader(SCRIPT));
        assertEquals("Wrong number of steps", 1, script.steps().size());
        assertTrue("First step should be a script", script.steps().get(0) instanceof Script);
        assertTrue("First step should be a fixture", ((Script) script.steps().get(0)) instanceof Fixture);
    }

    private class TestFixture extends Fixture implements DynamicLoadingConstants {

        public boolean wasRun;

        public TestFixture(Script parent, String id) {
            super(parent, new HashMap());
            setDescription("TestFixture " + id);
            addStep(new Launch(this, id, DYNAMIC_CLASSNAME, "main", new String[] { "[]" }, DYNAMIC_CLASSPATH, false));
            addStep(new Comment(this, "comment: " + id) {

                protected void runStep() {
                    wasRun = true;
                }
            });
        }
    }

    public static void main(String[] args) {
        TestHelper.runTests(args, FixtureTest.class);
    }
}
