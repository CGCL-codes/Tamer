package fd2.tests;

import fd2.Fd2Factory;
import fd2.FeatureNode;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Feature Node</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class FeatureNodeTest extends BaseFeatureNodeTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(FeatureNodeTest.class);
    }

    /**
	 * Constructs a new Feature Node test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FeatureNodeTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Feature Node test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected FeatureNode getFixture() {
        return (FeatureNode) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(Fd2Factory.eINSTANCE.createFeatureNode());
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
    @Override
    protected void tearDown() throws Exception {
        setFixture(null);
    }
}
