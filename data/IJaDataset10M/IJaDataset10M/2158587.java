package bpmn.tests;

import bpmn.BpmnFactory;
import bpmn.WebService;
import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Web Service</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class WebServiceTest extends TestCase {

    /**
	 * The fixture for this Web Service test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected WebService fixture = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static void main(String[] args) {
        TestRunner.run(WebServiceTest.class);
    }

    /**
	 * Constructs a new Web Service test case with the given name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public WebServiceTest(String name) {
        super(name);
    }

    /**
	 * Sets the fixture for this Web Service test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected void setFixture(WebService fixture) {
        this.fixture = fixture;
    }

    /**
	 * Returns the fixture for this Web Service test case.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private WebService getFixture() {
        return fixture;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
    protected void setUp() throws Exception {
        setFixture(BpmnFactory.eINSTANCE.createWebService());
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
