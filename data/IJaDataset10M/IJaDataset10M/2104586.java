package net.sf.etl.parsers.grammar.tests;

import junit.textui.TestRunner;
import net.sf.etl.parsers.grammar.GrammarFactory;
import net.sf.etl.parsers.grammar.ModifierOp;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Modifier Op</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class ModifierOpTest extends TokenRefOpTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(ModifierOpTest.class);
    }

    /**
	 * Constructs a new Modifier Op test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ModifierOpTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Modifier Op test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private ModifierOp getFixture() {
        return (ModifierOp) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    protected void setUp() throws Exception {
        setFixture(GrammarFactory.eINSTANCE.createModifierOp());
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
    protected void tearDown() throws Exception {
        setFixture(null);
    }
}
