package fr.inria.uml4tst.papyrus.ocl4tst.ocl.tests;

import fr.inria.uml4tst.papyrus.ocl4tst.ocl.OclFactory;
import fr.inria.uml4tst.papyrus.ocl4tst.ocl.PostConditionDeclarationCS;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Post Condition Declaration CS</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class PostConditionDeclarationCSTest extends PrePostOrBodyDeclarationCSTest {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(PostConditionDeclarationCSTest.class);
    }

    /**
	 * Constructs a new Post Condition Declaration CS test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PostConditionDeclarationCSTest(String name) {
        super(name);
    }

    /**
	 * Returns the fixture for this Post Condition Declaration CS test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected PostConditionDeclarationCS getFixture() {
        return (PostConditionDeclarationCS) fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    @Override
    protected void setUp() throws Exception {
        setFixture(OclFactory.eINSTANCE.createPostConditionDeclarationCS());
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
