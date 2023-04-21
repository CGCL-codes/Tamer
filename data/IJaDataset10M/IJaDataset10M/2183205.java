package tudresden.ocl20.pivot.tools.codegen.ocl2java.test.aspectj.constraintkinds;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import testpackage.Class1;

/**
 * <p>
 * Tests the generated code for a <code>Constraint</code> of the
 * <code>ConstraintKind.DEF</code>.
 * </p>
 * 
 * @author Claas Wilke
 */
public class TestDef {

    /**
	 * <p>
	 * Tests the generated code for a <code>Constraint</code> of the
	 * <code>ConstraintKind.DEF</code>.
	 * </p>
	 */
    @Test
    public void testDef01() {
        Class1 class1 = new Class1();
        assertEquals(new Integer(42), class1.defProperty01);
    }

    /**
	 * <p>
	 * Tests the generated code for a <code>Constraint</code> of the
	 * <code>ConstraintKind.DEF</code>.
	 * </p>
	 */
    @Test
    public void testDef01_Getter() {
        Class1 class1 = new Class1();
        assertEquals(new Integer(42), class1.getDefProperty01());
    }

    /**
	 * <p>
	 * Tests the generated code for a <code>Constraint</code> of the
	 * <code>ConstraintKind.DEF</code>.
	 * </p>
	 */
    @Test
    public void testDef02() {
        Class1 class1 = new Class1();
        assertEquals(new Integer(42), class1.defOperation01());
    }

    /**
	 * <p>
	 * Tests the generated code for a <code>Constraint</code> of the
	 * <code>ConstraintKind.DEF</code>.
	 * </p>
	 */
    @Test
    public void testDef03() {
        Class1 class1 = new Class1();
        assertEquals(new Integer(-42), class1.defOperation02(new Integer(42)));
    }

    /**
	 * <p>
	 * Tests the generated code for a <code>Constraint</code> of the
	 * <code>ConstraintKind.DEF</code>.
	 * </p>
	 */
    @Test
    public void testStaticDef01() {
        assertEquals(new Integer(42), Class1.staticDefProperty01);
    }

    /**
	 * <p>
	 * Tests the generated code for a <code>Constraint</code> of the
	 * <code>ConstraintKind.DEF</code>.
	 * </p>
	 */
    @Test
    public void testStaticDef01_Getter() {
        assertEquals(new Integer(42), Class1.getStaticDefProperty01());
    }

    /**
	 * <p>
	 * Tests the generated code for a <code>Constraint</code> of the
	 * <code>ConstraintKind.DEF</code>.
	 * </p>
	 */
    @Test
    public void testStaticDef02() {
        assertEquals(new Integer(42), Class1.staticDefOperation01());
    }
}
