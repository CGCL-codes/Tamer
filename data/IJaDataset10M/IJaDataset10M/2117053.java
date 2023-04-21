package dataflowScheme.impl;

import dataflowScheme.Block;
import dataflowScheme.Connection;
import dataflowScheme.ConnectionType;
import dataflowScheme.DataConnection;
import dataflowScheme.DataPort;
import dataflowScheme.DataflowSchemeFactory;
import dataflowScheme.DataflowSchemePackage;
import dataflowScheme.Port;
import dataflowScheme.PortSet;
import dataflowScheme.Stopper;
import dataflowScheme.Storage;
import dataflowScheme.SynchroConnection;
import dataflowScheme.SynchroPort;
import dataflowScheme.Task;
import dataflowScheme.Trigger;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DataflowSchemePackageImpl extends EPackageImpl implements DataflowSchemePackage {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass peEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass storageEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass taskEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass connectionEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass synchroConnectionEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass dataConnectionEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass portEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass dataPortEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass synchroPortEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass portSetEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass blockEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass triggerEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EClass stopperEClass = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EEnum connectionTypeEEnum = null;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private EDataType sourceTypeEDataType = null;

    /**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see dataflowScheme.DataflowSchemePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
    private DataflowSchemePackageImpl() {
        super(eNS_URI, DataflowSchemeFactory.eINSTANCE);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private static boolean isInited = false;

    /**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
    public static DataflowSchemePackage init() {
        if (isInited) return (DataflowSchemePackage) EPackage.Registry.INSTANCE.getEPackage(DataflowSchemePackage.eNS_URI);
        DataflowSchemePackageImpl theDataflowSchemePackage = (DataflowSchemePackageImpl) (EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof DataflowSchemePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new DataflowSchemePackageImpl());
        isInited = true;
        theDataflowSchemePackage.createPackageContents();
        theDataflowSchemePackage.initializePackageContents();
        theDataflowSchemePackage.freeze();
        return theDataflowSchemePackage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getPE() {
        return peEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getPE_PortSet() {
        return (EReference) peEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getPE_Name() {
        return (EAttribute) peEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getPE_Source() {
        return (EAttribute) peEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getStorage() {
        return storageEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTask() {
        return taskEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getConnection() {
        return connectionEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSynchroConnection() {
        return synchroConnectionEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSynchroConnection_OutPort() {
        return (EReference) synchroConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getSynchroConnection_InPort() {
        return (EReference) synchroConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getDataConnection() {
        return dataConnectionEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDataConnection_OutPort() {
        return (EReference) dataConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getDataConnection_InPort() {
        return (EReference) dataConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getPort() {
        return portEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getPort_Type() {
        return (EAttribute) portEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getPort_Name() {
        return (EAttribute) portEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getDataPort() {
        return dataPortEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getSynchroPort() {
        return synchroPortEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getPortSet() {
        return portSetEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getPortSet_Type() {
        return (EAttribute) portSetEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getPortSet_Port() {
        return (EReference) portSetEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EAttribute getPortSet_Id() {
        return (EAttribute) portSetEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getBlock() {
        return blockEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getBlock_Entity() {
        return (EReference) blockEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getBlock_Connection() {
        return (EReference) blockEClass.getEStructuralFeatures().get(1);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getBlock_Trigger() {
        return (EReference) blockEClass.getEStructuralFeatures().get(2);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getBlock_Stopper() {
        return (EReference) blockEClass.getEStructuralFeatures().get(3);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getTrigger() {
        return triggerEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getTrigger_Ports() {
        return (EReference) triggerEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EClass getStopper() {
        return stopperEClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EReference getStopper_Ports() {
        return (EReference) stopperEClass.getEStructuralFeatures().get(0);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EEnum getConnectionType() {
        return connectionTypeEEnum;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EDataType getSourceType() {
        return sourceTypeEDataType;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DataflowSchemeFactory getDataflowSchemeFactory() {
        return (DataflowSchemeFactory) getEFactoryInstance();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private boolean isCreated = false;

    /**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;
        peEClass = createEClass(PE);
        createEReference(peEClass, PE__PORT_SET);
        createEAttribute(peEClass, PE__NAME);
        createEAttribute(peEClass, PE__SOURCE);
        storageEClass = createEClass(STORAGE);
        taskEClass = createEClass(TASK);
        connectionEClass = createEClass(CONNECTION);
        synchroConnectionEClass = createEClass(SYNCHRO_CONNECTION);
        createEReference(synchroConnectionEClass, SYNCHRO_CONNECTION__OUT_PORT);
        createEReference(synchroConnectionEClass, SYNCHRO_CONNECTION__IN_PORT);
        dataConnectionEClass = createEClass(DATA_CONNECTION);
        createEReference(dataConnectionEClass, DATA_CONNECTION__OUT_PORT);
        createEReference(dataConnectionEClass, DATA_CONNECTION__IN_PORT);
        portEClass = createEClass(PORT);
        createEAttribute(portEClass, PORT__TYPE);
        createEAttribute(portEClass, PORT__NAME);
        dataPortEClass = createEClass(DATA_PORT);
        synchroPortEClass = createEClass(SYNCHRO_PORT);
        portSetEClass = createEClass(PORT_SET);
        createEAttribute(portSetEClass, PORT_SET__TYPE);
        createEReference(portSetEClass, PORT_SET__PORT);
        createEAttribute(portSetEClass, PORT_SET__ID);
        blockEClass = createEClass(BLOCK);
        createEReference(blockEClass, BLOCK__ENTITY);
        createEReference(blockEClass, BLOCK__CONNECTION);
        createEReference(blockEClass, BLOCK__TRIGGER);
        createEReference(blockEClass, BLOCK__STOPPER);
        triggerEClass = createEClass(TRIGGER);
        createEReference(triggerEClass, TRIGGER__PORTS);
        stopperEClass = createEClass(STOPPER);
        createEReference(stopperEClass, STOPPER__PORTS);
        connectionTypeEEnum = createEEnum(CONNECTION_TYPE);
        sourceTypeEDataType = createEDataType(SOURCE_TYPE);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    private boolean isInitialized = false;

    /**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);
        storageEClass.getESuperTypes().add(this.getPE());
        taskEClass.getESuperTypes().add(this.getPE());
        synchroConnectionEClass.getESuperTypes().add(this.getConnection());
        dataConnectionEClass.getESuperTypes().add(this.getConnection());
        dataPortEClass.getESuperTypes().add(this.getPort());
        synchroPortEClass.getESuperTypes().add(this.getPort());
        initEClass(peEClass, dataflowScheme.PE.class, "PE", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getPE_PortSet(), this.getPortSet(), null, "portSet", null, 0, -1, dataflowScheme.PE.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPE_Name(), ecorePackage.getEString(), "name", null, 1, 1, dataflowScheme.PE.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPE_Source(), this.getSourceType(), "source", null, 0, 1, dataflowScheme.PE.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(storageEClass, Storage.class, "Storage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(taskEClass, Task.class, "Task", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(connectionEClass, Connection.class, "Connection", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(synchroConnectionEClass, SynchroConnection.class, "SynchroConnection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSynchroConnection_OutPort(), this.getSynchroPort(), null, "outPort", null, 0, 1, SynchroConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getSynchroConnection_InPort(), this.getSynchroPort(), null, "inPort", null, 0, 1, SynchroConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(dataConnectionEClass, DataConnection.class, "DataConnection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDataConnection_OutPort(), this.getDataPort(), null, "outPort", null, 0, 1, DataConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDataConnection_InPort(), this.getDataPort(), null, "inPort", null, 0, 1, DataConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(portEClass, Port.class, "Port", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getPort_Type(), this.getConnectionType(), "type", null, 0, 1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPort_Name(), ecorePackage.getEString(), "name", "<<name>>", 1, 1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(dataPortEClass, DataPort.class, "DataPort", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(synchroPortEClass, SynchroPort.class, "SynchroPort", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEClass(portSetEClass, PortSet.class, "PortSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getPortSet_Type(), this.getConnectionType(), "type", null, 0, 1, PortSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getPortSet_Port(), this.getPort(), null, "port", null, 1, -1, PortSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getPortSet_Id(), ecorePackage.getEInt(), "id", "0", 1, 1, PortSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(blockEClass, Block.class, "Block", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getBlock_Entity(), this.getPE(), null, "entity", null, 0, -1, Block.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBlock_Connection(), this.getConnection(), null, "connection", null, 0, -1, Block.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBlock_Trigger(), this.getTrigger(), null, "trigger", null, 1, 1, Block.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBlock_Stopper(), this.getStopper(), null, "stopper", null, 1, -1, Block.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(triggerEClass, Trigger.class, "Trigger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTrigger_Ports(), this.getSynchroPort(), null, "ports", null, 0, -1, Trigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEClass(stopperEClass, Stopper.class, "Stopper", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getStopper_Ports(), this.getSynchroPort(), null, "ports", null, 0, -1, Stopper.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEEnum(connectionTypeEEnum, ConnectionType.class, "ConnectionType");
        addEEnumLiteral(connectionTypeEEnum, ConnectionType.IN);
        addEEnumLiteral(connectionTypeEEnum, ConnectionType.OUT);
        initEDataType(sourceTypeEDataType, String.class, "SourceType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        createResource(eNS_URI);
    }
}
