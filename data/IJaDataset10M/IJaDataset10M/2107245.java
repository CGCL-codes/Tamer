package tudresden.ocl20.pivot.tools.codegen.ocl2java.test.tests.expressions;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.pivotmodel.Constraint;
import tudresden.ocl20.pivot.tools.codegen.ocl2java.test.tests.AbstractDiffTest;

/**
 * <p>
 * Contains some test cases to test the code generation {@link Constraint}s
 * containing Literal expressions.
 * </p>
 * 
 * @author Claas Wilke
 */
public class TestRealLiteralExp extends AbstractDiffTest {

    /**
	 * <p>
	 * Initializes the test cases.
	 * </p>
	 * 
	 * @throws Exception
	 */
    @BeforeClass
    public static void setUp() throws Exception {
        AbstractDiffTest.setUp();
    }

    /**
	 * <p>
	 * Tears down the test cases.
	 * </p>
	 * 
	 * @throws ModelAccessException
	 * @throws IllegalArgumentException
	 */
    @AfterClass
    public static void tearDown() throws IllegalArgumentException, ModelAccessException {
        AbstractDiffTest.tearDown();
    }

    /**
	 * <p>
	 * Tests the fragment code generation of the constraint.
	 * </p>
	 * 
	 * @throws Exception
	 */
    @Test
    public void testReal01() throws Exception {
        this.compareFragmentCodeGeneration("expressions/literals", "real01");
    }
}
