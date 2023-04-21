package iec61970.topology.impl;

import iec61970.topology.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TopologyFactoryImpl extends EFactoryImpl implements TopologyFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static TopologyFactory init() {
        try {
            TopologyFactory theTopologyFactory = (TopologyFactory) EPackage.Registry.INSTANCE.getEFactory("http:///iec61970/topology.ecore");
            if (theTopologyFactory != null) {
                return theTopologyFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new TopologyFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TopologyFactoryImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public EObject create(EClass eClass) {
        switch(eClass.getClassifierID()) {
            case TopologyPackage.CONNECTIVITY_NODE:
                return createConnectivityNode();
            case TopologyPackage.TOPOLOGICAL_ISLAND:
                return createTopologicalIsland();
            case TopologyPackage.TOPOLOGICAL_NODE:
                return createTopologicalNode();
            case TopologyPackage.TOPOLOGY_VERSION:
                return createTopologyVersion();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ConnectivityNode createConnectivityNode() {
        ConnectivityNodeImpl connectivityNode = new ConnectivityNodeImpl();
        return connectivityNode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TopologicalIsland createTopologicalIsland() {
        TopologicalIslandImpl topologicalIsland = new TopologicalIslandImpl();
        return topologicalIsland;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TopologicalNode createTopologicalNode() {
        TopologicalNodeImpl topologicalNode = new TopologicalNodeImpl();
        return topologicalNode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TopologyVersion createTopologyVersion() {
        TopologyVersionImpl topologyVersion = new TopologyVersionImpl();
        return topologyVersion;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public TopologyPackage getTopologyPackage() {
        return (TopologyPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static TopologyPackage getPackage() {
        return TopologyPackage.eINSTANCE;
    }
}
