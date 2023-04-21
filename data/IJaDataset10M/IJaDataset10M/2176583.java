package tudresden.ocl20.pivot.ocl2parser.test.expressions;

import org.junit.Test;
import tudresden.ocl20.pivot.ocl2parser.test.TestPerformer;

/**
 * <p>
 * Contains test cases that check that boolean literals are parsed
 * appropriately.
 * </p>
 * 
 * @author Claas Wilke
 */
public class TestBooleanLiterals {

    /**
	 * <p>
	 * A test case to check that the boolean literal is parsed appropriately.
	 * </p>
	 */
    @Test
    public void testBooleanPositive01() throws Exception {
        TestPerformer testPerformer;
        String modelFileName;
        String oclFileName;
        oclFileName = "expressions/literals/booleanPositive01.ocl";
        modelFileName = "testmodel.uml";
        testPerformer = TestPerformer.getInstance(AllExpressionTests.META_MODEL_ID, AllExpressionTests.MODEL_BUNDLE, AllExpressionTests.MODEL_DIRECTORY);
        testPerformer.setModel(modelFileName);
        testPerformer.parseFile(oclFileName);
    }

    /**
	 * <p>
	 * A test case to check that the boolean literal is parsed appropriately.
	 * </p>
	 */
    @Test
    public void testBooleanPositive02() throws Exception {
        TestPerformer testPerformer;
        String modelFileName;
        String oclFileName;
        oclFileName = "expressions/literals/booleanPositive02.ocl";
        modelFileName = "testmodel.uml";
        testPerformer = TestPerformer.getInstance(AllExpressionTests.META_MODEL_ID, AllExpressionTests.MODEL_BUNDLE, AllExpressionTests.MODEL_DIRECTORY);
        testPerformer.setModel(modelFileName);
        testPerformer.parseFile(oclFileName);
    }
}
