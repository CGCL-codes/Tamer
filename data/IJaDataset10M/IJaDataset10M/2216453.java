package com.hofstetter.diplthesis.ctb.ctb.util;

import com.hofstetter.diplthesis.ctb.ctb.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.hofstetter.diplthesis.ctb.ctb.CtbPackage
 * @generated
 */
public class CtbAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static CtbPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CtbAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = CtbPackage.eINSTANCE;
        }
    }

    /**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject) object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
	 * The switch the delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected CtbSwitch<Adapter> modelSwitch = new CtbSwitch<Adapter>() {

        @Override
        public Adapter caseDiagram(Diagram object) {
            return createDiagramAdapter();
        }

        @Override
        public Adapter caseComponent(Component object) {
            return createComponentAdapter();
        }

        @Override
        public Adapter caseConnection(Connection object) {
            return createConnectionAdapter();
        }

        @Override
        public Adapter casePhysical_OSComponent(Physical_OSComponent object) {
            return createPhysical_OSComponentAdapter();
        }

        @Override
        public Adapter caseExecutionEnvironment(ExecutionEnvironment object) {
            return createExecutionEnvironmentAdapter();
        }

        @Override
        public Adapter caseRuntimeComponent(RuntimeComponent object) {
            return createRuntimeComponentAdapter();
        }

        @Override
        public Adapter caseFunctionalComponent(FunctionalComponent object) {
            return createFunctionalComponentAdapter();
        }

        @Override
        public Adapter caseServiceComponent(ServiceComponent object) {
            return createServiceComponentAdapter();
        }

        @Override
        public Adapter caseCommunicationComponent(CommunicationComponent object) {
            return createCommunicationComponentAdapter();
        }

        @Override
        public Adapter casePattern(Pattern object) {
            return createPatternAdapter();
        }

        @Override
        public Adapter casePatternNode(PatternNode object) {
            return createPatternNodeAdapter();
        }

        @Override
        public Adapter casePatternInformation(PatternInformation object) {
            return createPatternInformationAdapter();
        }

        @Override
        public Adapter caseRuntimeConnection(RuntimeConnection object) {
            return createRuntimeConnectionAdapter();
        }

        @Override
        public Adapter caseLocalRuntimeConnection(LocalRuntimeConnection object) {
            return createLocalRuntimeConnectionAdapter();
        }

        @Override
        public Adapter caseRemoteRuntimeConnection(RemoteRuntimeConnection object) {
            return createRemoteRuntimeConnectionAdapter();
        }

        @Override
        public Adapter casePatternNode_InfoConnection(PatternNode_InfoConnection object) {
            return createPatternNode_InfoConnectionAdapter();
        }

        @Override
        public Adapter casePatternNodeConnection(PatternNodeConnection object) {
            return createPatternNodeConnectionAdapter();
        }

        @Override
        public Adapter caseTestPattern(TestPattern object) {
            return createTestPatternAdapter();
        }

        @Override
        public Adapter caseTestComponent(TestComponent object) {
            return createTestComponentAdapter();
        }

        @Override
        public Adapter defaultCase(EObject object) {
            return createEObjectAdapter();
        }
    };

    /**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject) target);
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.Diagram <em>Diagram</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.Diagram
	 * @generated
	 */
    public Adapter createDiagramAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.Component <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.Component
	 * @generated
	 */
    public Adapter createComponentAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.Connection <em>Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.Connection
	 * @generated
	 */
    public Adapter createConnectionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.Physical_OSComponent <em>Physical OS Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.Physical_OSComponent
	 * @generated
	 */
    public Adapter createPhysical_OSComponentAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.ExecutionEnvironment <em>Execution Environment</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.ExecutionEnvironment
	 * @generated
	 */
    public Adapter createExecutionEnvironmentAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.RuntimeComponent <em>Runtime Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.RuntimeComponent
	 * @generated
	 */
    public Adapter createRuntimeComponentAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.FunctionalComponent <em>Functional Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.FunctionalComponent
	 * @generated
	 */
    public Adapter createFunctionalComponentAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.ServiceComponent <em>Service Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.ServiceComponent
	 * @generated
	 */
    public Adapter createServiceComponentAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.CommunicationComponent <em>Communication Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.CommunicationComponent
	 * @generated
	 */
    public Adapter createCommunicationComponentAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.Pattern <em>Pattern</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.Pattern
	 * @generated
	 */
    public Adapter createPatternAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.PatternNode <em>Pattern Node</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.PatternNode
	 * @generated
	 */
    public Adapter createPatternNodeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.PatternInformation <em>Pattern Information</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.PatternInformation
	 * @generated
	 */
    public Adapter createPatternInformationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.RuntimeConnection <em>Runtime Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.RuntimeConnection
	 * @generated
	 */
    public Adapter createRuntimeConnectionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.LocalRuntimeConnection <em>Local Runtime Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.LocalRuntimeConnection
	 * @generated
	 */
    public Adapter createLocalRuntimeConnectionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.RemoteRuntimeConnection <em>Remote Runtime Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.RemoteRuntimeConnection
	 * @generated
	 */
    public Adapter createRemoteRuntimeConnectionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.PatternNode_InfoConnection <em>Pattern Node Info Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.PatternNode_InfoConnection
	 * @generated
	 */
    public Adapter createPatternNode_InfoConnectionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.PatternNodeConnection <em>Pattern Node Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.PatternNodeConnection
	 * @generated
	 */
    public Adapter createPatternNodeConnectionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.TestPattern <em>Test Pattern</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.TestPattern
	 * @generated
	 */
    public Adapter createTestPatternAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.hofstetter.diplthesis.ctb.ctb.TestComponent <em>Test Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.hofstetter.diplthesis.ctb.ctb.TestComponent
	 * @generated
	 */
    public Adapter createTestComponentAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
    public Adapter createEObjectAdapter() {
        return null;
    }
}
