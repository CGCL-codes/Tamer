package ca.ucalgary.cpsc.ebe.fitClipseCore.tests.subLayers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ca.ucalgary.cpsc.ebe.fitClipseCore.core.data.impl.ActionPartTest;
import ca.ucalgary.cpsc.ebe.fitClipseCore.core.data.impl.Action_Test;
import ca.ucalgary.cpsc.ebe.fitClipseCore.core.refactoring.scenario.RegExUtilTest;
import ca.ucalgary.cpsc.ebe.fitClipseCore.core.refactoring.scenario.ScenarioActionTextSplitterTest;
import ca.ucalgary.cpsc.ebe.fitClipseCore.fixtureGeneration.FitActionGenerationTest;
import ca.ucalgary.cpsc.ebe.fitClipseCore.fixtureGeneration.FitColumnGenerationTest;
import ca.ucalgary.cpsc.ebe.fitClipseCore.fixtureGeneration.GPActionGenerationTest;
import ca.ucalgary.cpsc.ebe.fitClipseCore.fixtureGeneration.GPColumnGenerationTest;
import ca.ucalgary.cpsc.ebe.fitClipseCore.fixtureGeneration.GPScenarioGenerationTest;
import ca.ucalgary.cpsc.ebe.fitClipseCore.javaSourceModification.JavaPropertyTest;
import ca.ucalgary.cpsc.ebe.fitClipseCore.utils.StringUtilTest;
import ca.ucalgary.cpsc.ebe.fitClipseCore.utils.UtilFileTest;

@RunWith(value = Suite.class)
@SuiteClasses(value = { GPActionGenerationTest.class, GPColumnGenerationTest.class, FitActionGenerationTest.class, FitColumnGenerationTest.class, StringUtilTest.class, UtilFileTest.class, ActionPartTest.class, JavaPropertyTest.class, Action_Test.class, RegExUtilTest.class, GPScenarioGenerationTest.class, ScenarioActionTextSplitterTest.class })
public class JunitTestSuite {
}
