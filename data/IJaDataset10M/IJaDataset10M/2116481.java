package tudresden.ocl20.pivot.tools.codegen.ocl2java.test.aspectj.expressions;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import testpackage.Class1;
import testpackage.Enumeration1;

/**
 * <p>
 * Tests the generated code for OCL Expressions.
 * </p>
 * 
 * @author Claas Wilke
 */
public class TestEnumerationLiteralExp {

    /**
	 * <p>
	 * Tests the generated code for an OCL Expression
	 * </p>
	 */
    @Test
    public void testEnumerationLiteralExp01() {
        Class1 class1;
        class1 = new Class1();
        assertEquals(Enumeration1.literal1, class1.testEnumerationLiteralExp01());
    }
}
