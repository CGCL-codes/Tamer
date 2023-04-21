package junitconverter.stages;

import junitconverter.Visibility;
import junitconverter.testcase.TestCaseClass;

/**
 * Increases the visibility of the organization methods (setUp and tearDown)
 * if needed.
 *
 * @author abyx
 */
public class VisibilityAdaptionStage extends AbstractTestConversionStage {

    /**
	 * @see junitconverter.stages.TestConversionStage#convertClass(junitconverter.testcase.TestCaseClass)
	 */
    public void convertClass(TestCaseClass testCase) {
        if (testCase.getSetUpMethod() != null) {
            codeEditor.changeVisiblity(testCase, testCase.getSetUpMethod(), Visibility.PUBLIC);
        }
        if (testCase.getTearDownMethod() != null) {
            codeEditor.changeVisiblity(testCase, testCase.getTearDownMethod(), Visibility.PUBLIC);
        }
    }
}
