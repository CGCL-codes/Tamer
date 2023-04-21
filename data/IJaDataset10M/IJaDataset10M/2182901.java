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
public class TestPropertyCallExp {

    /**
	 * <p>
	 * Tests the generated code for an OCL Expression
	 * </p>
	 */
    @Test
    public void testPropertyCallExp01() {
        Class1 class1;
        class1 = new Class1();
        assertEquals(class1.anInteger01, class1.testPropertyCallExp01());
    }

    /**
	 * <p>
	 * Tests the generated code for an OCL Expression
	 * </p>
	 */
    @Test
    public void testPropertyCallExp02() {
        Class1 class1;
        class1 = new Class1();
        assertEquals(Class1.aStaticInteger01, class1.testPropertyCallExp02());
    }
}
