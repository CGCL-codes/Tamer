package net.sf.etl.parsers.grammar.tests;

import junit.textui.TestRunner;
import net.sf.etl.parsers.grammar.GrammarFactory;
import net.sf.etl.parsers.grammar.IdentifierOp;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Identifier Op</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class IdentifierOpTest extends TokenRefOpTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(IdentifierOpTest.class);
    }

    /**
	 * Constructs a new Identifier Op test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IdentifierOpTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Identifier Op test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private IdentifierOp getFixture() {
        return (IdentifierOp) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    protected void setUp() throws Exception {
        setFixture(GrammarFactory.eINSTANCE.createIdentifierOp());
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
