package tudresden.ocl20.pivot.tools.codegen.ocl2java.test.aspectj.expressions;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import testpackage.Class1;

/**
 * <p>
 * Tests the generated code for OCL Expressions.
 * </p>
 * 
 * @author Claas Wilke
 */
public class TestTupleLiteralExp {

    /**
	 * <p>
	 * Tests the generated code for an OCL Expression
	 * </p>
	 */
    @Test
    public void testTupleLiteralExp01() {
        Class1 class1;
        class1 = new Class1();
        java.util.HashMap<String, Object> expectedTuple;
        expectedTuple = new java.util.HashMap<String, Object>();
        expectedTuple.put("a", new Integer(42));
        expectedTuple.put("b", "some");
        assertEquals(expectedTuple, class1.testTupleLiteralExp01());
    }
}
