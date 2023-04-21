package fr.inria.papyrus.uml4tst.emftext.ocl.tests;

import fr.inria.papyrus.uml4tst.emftext.ocl.CollectionLiteralExpCS;
import fr.inria.papyrus.uml4tst.emftext.ocl.OclFactory;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Collection Literal Exp CS</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class CollectionLiteralExpCSTest extends LiteralExpCSTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(CollectionLiteralExpCSTest.class);
    }

    /**
	 * Constructs a new Collection Literal Exp CS test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CollectionLiteralExpCSTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Collection Literal Exp CS test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected CollectionLiteralExpCS getFixture() {
        return (CollectionLiteralExpCS) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(OclFactory.eINSTANCE.createCollectionLiteralExpCS());
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
