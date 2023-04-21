package de.fraunhofer.isst.eastadl.functionmodeling.util;

import de.fraunhofer.isst.eastadl.autosar.Identifiable;
import de.fraunhofer.isst.eastadl.elements.Context;
import de.fraunhofer.isst.eastadl.elements.EAElement;
import de.fraunhofer.isst.eastadl.elements.EAPackageableElement;
import de.fraunhofer.isst.eastadl.functionmodeling.*;
import de.fraunhofer.isst.eastadl.userattributes.UserAttributeableElement;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see de.fraunhofer.isst.eastadl.functionmodeling.FunctionmodelingPackage
 * @generated
 */
public class FunctionmodelingAdapterFactory extends AdapterFactoryImpl {

    /**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static FunctionmodelingPackage modelPackage;

    /**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public FunctionmodelingAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = FunctionmodelingPackage.eINSTANCE;
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
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected FunctionmodelingSwitch<Adapter> modelSwitch = new FunctionmodelingSwitch<Adapter>() {

        @Override
        public Adapter caseAllocateableElement(AllocateableElement object) {
            return createAllocateableElementAdapter();
        }

        @Override
        public Adapter caseAllocation(Allocation object) {
            return createAllocationAdapter();
        }

        @Override
        public Adapter caseAnalysisFunctionPrototype(AnalysisFunctionPrototype object) {
            return createAnalysisFunctionPrototypeAdapter();
        }

        @Override
        public Adapter caseAnalysisFunctionType(AnalysisFunctionType object) {
            return createAnalysisFunctionTypeAdapter();
        }

        @Override
        public Adapter caseBasicSoftwareFunctionType(BasicSoftwareFunctionType object) {
            return createBasicSoftwareFunctionTypeAdapter();
        }

        @Override
        public Adapter caseDesignFunctionPrototype(DesignFunctionPrototype object) {
            return createDesignFunctionPrototypeAdapter();
        }

        @Override
        public Adapter caseDesignFunctionType(DesignFunctionType object) {
            return createDesignFunctionTypeAdapter();
        }

        @Override
        public Adapter caseFunctionAllocation(FunctionAllocation object) {
            return createFunctionAllocationAdapter();
        }

        @Override
        public Adapter caseFunctionClientServerInterface(FunctionClientServerInterface object) {
            return createFunctionClientServerInterfaceAdapter();
        }

        @Override
        public Adapter caseFunctionClientServerPort(FunctionClientServerPort object) {
            return createFunctionClientServerPortAdapter();
        }

        @Override
        public Adapter caseFunctionConnector(FunctionConnector object) {
            return createFunctionConnectorAdapter();
        }

        @Override
        public Adapter caseFunctionFlowPort(FunctionFlowPort object) {
            return createFunctionFlowPortAdapter();
        }

        @Override
        public Adapter caseFunctionPort(FunctionPort object) {
            return createFunctionPortAdapter();
        }

        @Override
        public Adapter caseFunctionPowerPort(FunctionPowerPort object) {
            return createFunctionPowerPortAdapter();
        }

        @Override
        public Adapter caseFunctionPrototype(FunctionPrototype object) {
            return createFunctionPrototypeAdapter();
        }

        @Override
        public Adapter caseFunctionType(FunctionType object) {
            return createFunctionTypeAdapter();
        }

        @Override
        public Adapter caseFunctionalDevice(FunctionalDevice object) {
            return createFunctionalDeviceAdapter();
        }

        @Override
        public Adapter caseHardwareFunctionType(HardwareFunctionType object) {
            return createHardwareFunctionTypeAdapter();
        }

        @Override
        public Adapter caseLocalDeviceManager(LocalDeviceManager object) {
            return createLocalDeviceManagerAdapter();
        }

        @Override
        public Adapter caseOperation(Operation object) {
            return createOperationAdapter();
        }

        @Override
        public Adapter casePortGroup(PortGroup object) {
            return createPortGroupAdapter();
        }

        @Override
        public Adapter caseFunctionPortInstanceRef(FunctionPortInstanceRef object) {
            return createFunctionPortInstanceRefAdapter();
        }

        @Override
        public Adapter caseAllocateableElementInstanceRef(AllocateableElementInstanceRef object) {
            return createAllocateableElementInstanceRefAdapter();
        }

        @Override
        public Adapter caseIdentifiable(Identifiable object) {
            return createIdentifiableAdapter();
        }

        @Override
        public Adapter caseUserAttributeableElement(UserAttributeableElement object) {
            return createUserAttributeableElementAdapter();
        }

        @Override
        public Adapter caseEAElement(EAElement object) {
            return createEAElementAdapter();
        }

        @Override
        public Adapter caseEAPackageableElement(EAPackageableElement object) {
            return createEAPackageableElementAdapter();
        }

        @Override
        public Adapter caseContext(Context object) {
            return createContextAdapter();
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
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.AllocateableElement <em>Allocateable Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.AllocateableElement
	 * @generated
	 */
    public Adapter createAllocateableElementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.Allocation <em>Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.Allocation
	 * @generated
	 */
    public Adapter createAllocationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.AnalysisFunctionPrototype <em>Analysis Function Prototype</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.AnalysisFunctionPrototype
	 * @generated
	 */
    public Adapter createAnalysisFunctionPrototypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.AnalysisFunctionType <em>Analysis Function Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.AnalysisFunctionType
	 * @generated
	 */
    public Adapter createAnalysisFunctionTypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.BasicSoftwareFunctionType <em>Basic Software Function Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.BasicSoftwareFunctionType
	 * @generated
	 */
    public Adapter createBasicSoftwareFunctionTypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.DesignFunctionPrototype <em>Design Function Prototype</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.DesignFunctionPrototype
	 * @generated
	 */
    public Adapter createDesignFunctionPrototypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.DesignFunctionType <em>Design Function Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.DesignFunctionType
	 * @generated
	 */
    public Adapter createDesignFunctionTypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.FunctionAllocation <em>Function Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.FunctionAllocation
	 * @generated
	 */
    public Adapter createFunctionAllocationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.FunctionClientServerInterface <em>Function Client Server Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.FunctionClientServerInterface
	 * @generated
	 */
    public Adapter createFunctionClientServerInterfaceAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.FunctionClientServerPort <em>Function Client Server Port</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.FunctionClientServerPort
	 * @generated
	 */
    public Adapter createFunctionClientServerPortAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.FunctionConnector <em>Function Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.FunctionConnector
	 * @generated
	 */
    public Adapter createFunctionConnectorAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.FunctionFlowPort <em>Function Flow Port</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.FunctionFlowPort
	 * @generated
	 */
    public Adapter createFunctionFlowPortAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.FunctionPort <em>Function Port</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.FunctionPort
	 * @generated
	 */
    public Adapter createFunctionPortAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.FunctionPowerPort <em>Function Power Port</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.FunctionPowerPort
	 * @generated
	 */
    public Adapter createFunctionPowerPortAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.FunctionPrototype <em>Function Prototype</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.FunctionPrototype
	 * @generated
	 */
    public Adapter createFunctionPrototypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.FunctionType <em>Function Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.FunctionType
	 * @generated
	 */
    public Adapter createFunctionTypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.FunctionalDevice <em>Functional Device</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.FunctionalDevice
	 * @generated
	 */
    public Adapter createFunctionalDeviceAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.HardwareFunctionType <em>Hardware Function Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.HardwareFunctionType
	 * @generated
	 */
    public Adapter createHardwareFunctionTypeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.LocalDeviceManager <em>Local Device Manager</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.LocalDeviceManager
	 * @generated
	 */
    public Adapter createLocalDeviceManagerAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.Operation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.Operation
	 * @generated
	 */
    public Adapter createOperationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.PortGroup <em>Port Group</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.PortGroup
	 * @generated
	 */
    public Adapter createPortGroupAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.FunctionPortInstanceRef <em>Function Port Instance Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.FunctionPortInstanceRef
	 * @generated
	 */
    public Adapter createFunctionPortInstanceRefAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.functionmodeling.AllocateableElementInstanceRef <em>Allocateable Element Instance Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.functionmodeling.AllocateableElementInstanceRef
	 * @generated
	 */
    public Adapter createAllocateableElementInstanceRefAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.autosar.Identifiable <em>Identifiable</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.autosar.Identifiable
	 * @generated
	 */
    public Adapter createIdentifiableAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.userattributes.UserAttributeableElement <em>User Attributeable Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.userattributes.UserAttributeableElement
	 * @generated
	 */
    public Adapter createUserAttributeableElementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.elements.EAElement <em>EA Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.elements.EAElement
	 * @generated
	 */
    public Adapter createEAElementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.elements.EAPackageableElement <em>EA Packageable Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.elements.EAPackageableElement
	 * @generated
	 */
    public Adapter createEAPackageableElementAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link de.fraunhofer.isst.eastadl.elements.Context <em>Context</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.fraunhofer.isst.eastadl.elements.Context
	 * @generated
	 */
    public Adapter createContextAdapter() {
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
