package junitconverter.stages;

import junit.framework.TestCase;
import junitconverter.testcase.TestCaseClass;
import org.junit.After;
import org.junit.Before;

/**
 * This stage is responsible for adding the {@link Before} and {@link After}
 * annotations to the {@link TestCase#setUp} and {@link TestCase#tearDown}
 * methods respectfully.
 *
 * @author abyx
 */
public class PreperationMethodsAnnotationStage extends AbstractTestConversionStage {

    /**
	 * @see junitconverter.stages.TestConversionStage#convertClass(junitconverter.testcase.TestCaseClass)
	 */
    public void convertClass(TestCaseClass testCase) {
        if (testCase.getSetUpMethod() != null) {
            codeEditor.annotateMethod(testCase, testCase.getSetUpMethod(), Before.class);
        }
        if (testCase.getTearDownMethod() != null) {
            codeEditor.annotateMethod(testCase, testCase.getTearDownMethod(), After.class);
        }
    }
}
