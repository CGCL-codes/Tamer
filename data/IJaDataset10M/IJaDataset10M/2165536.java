package com.ctb.util;

import com.ctb.*;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.ctb.CtbPackage
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
        public Adapter casePhysicalComponent(PhysicalComponent object) {
            return createPhysicalComponentAdapter();
        }

        @Override
        public Adapter casePhysicalConnection(PhysicalConnection object) {
            return createPhysicalConnectionAdapter();
        }

        @Override
        public Adapter casePhysicalConnector(PhysicalConnector object) {
            return createPhysicalConnectorAdapter();
        }

        @Override
        public Adapter caseDefaultConnection(DefaultConnection object) {
            return createDefaultConnectionAdapter();
        }

        @Override
        public Adapter caseComponent(Component object) {
            return createComponentAdapter();
        }

        @Override
        public Adapter caseSmartCard(SmartCard object) {
            return createSmartCardAdapter();
        }

        @Override
        public Adapter caseMobileDevice(MobileDevice object) {
            return createMobileDeviceAdapter();
        }

        @Override
        public Adapter caseWorkstation(Workstation object) {
            return createWorkstationAdapter();
        }

        @Override
        public Adapter caseEnterpriseServer(EnterpriseServer object) {
            return createEnterpriseServerAdapter();
        }

        @Override
        public Adapter caseOfficeServer(OfficeServer object) {
            return createOfficeServerAdapter();
        }

        @Override
        public Adapter caseInfrastructureComponent(InfrastructureComponent object) {
            return createInfrastructureComponentAdapter();
        }

        @Override
        public Adapter caseWire(Wire object) {
            return createWireAdapter();
        }

        @Override
        public Adapter caseWireless(Wireless object) {
            return createWirelessAdapter();
        }

        @Override
        public Adapter caseShortrange(Shortrange object) {
            return createShortrangeAdapter();
        }

        @Override
        public Adapter caseMobileCellNetwork(MobileCellNetwork object) {
            return createMobileCellNetworkAdapter();
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
	 * Creates a new adapter for an object of class '{@link com.ctb.Diagram <em>Diagram</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.ctb.Diagram
	 * @generated
	 */
    public Adapter createDiagramAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.ctb.PhysicalComponent <em>Physical Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.ctb.PhysicalComponent
	 * @generated
	 */
    public Adapter createPhysicalComponentAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.ctb.PhysicalConnection <em>Physical Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.ctb.PhysicalConnection
	 * @generated
	 */
    public Adapter createPhysicalConnectionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.ctb.PhysicalConnector <em>Physical Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.ctb.PhysicalConnector
	 * @generated
	 */
    public Adapter createPhysicalConnectorAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.ctb.DefaultConnection <em>Default Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.ctb.DefaultConnection
	 * @generated
	 */
    public Adapter createDefaultConnectionAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.ctb.Component <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.ctb.Component
	 * @generated
	 */
    public Adapter createComponentAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.ctb.SmartCard <em>Smart Card</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.ctb.SmartCard
	 * @generated
	 */
    public Adapter createSmartCardAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.ctb.MobileDevice <em>Mobile Device</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.ctb.MobileDevice
	 * @generated
	 */
    public Adapter createMobileDeviceAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.ctb.Workstation <em>Workstation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.ctb.Workstation
	 * @generated
	 */
    public Adapter createWorkstationAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.ctb.EnterpriseServer <em>Enterprise Server</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.ctb.EnterpriseServer
	 * @generated
	 */
    public Adapter createEnterpriseServerAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.ctb.OfficeServer <em>Office Server</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.ctb.OfficeServer
	 * @generated
	 */
    public Adapter createOfficeServerAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.ctb.InfrastructureComponent <em>Infrastructure Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.ctb.InfrastructureComponent
	 * @generated
	 */
    public Adapter createInfrastructureComponentAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.ctb.Wire <em>Wire</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.ctb.Wire
	 * @generated
	 */
    public Adapter createWireAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.ctb.Wireless <em>Wireless</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.ctb.Wireless
	 * @generated
	 */
    public Adapter createWirelessAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.ctb.Shortrange <em>Shortrange</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.ctb.Shortrange
	 * @generated
	 */
    public Adapter createShortrangeAdapter() {
        return null;
    }

    /**
	 * Creates a new adapter for an object of class '{@link com.ctb.MobileCellNetwork <em>Mobile Cell Network</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.ctb.MobileCellNetwork
	 * @generated
	 */
    public Adapter createMobileCellNetworkAdapter() {
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
