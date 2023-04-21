package hu.cubussapiens.modembed.model.components.impl;

import hu.cubussapiens.modembed.model.components.*;
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
public class ComponentsFactoryImpl extends EFactoryImpl implements ComponentsFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static ComponentsFactory init() {
        try {
            ComponentsFactory theComponentsFactory = (ComponentsFactory) EPackage.Registry.INSTANCE.getEFactory("http://cubussapiens.hu/modembed/components");
            if (theComponentsFactory != null) {
                return theComponentsFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new ComponentsFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ComponentsFactoryImpl() {
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
            case ComponentsPackage.COMPONENT_INTERFACE:
                return createComponentInterface();
            case ComponentsPackage.DATA_ELEMENT:
                return createDataElement();
            case ComponentsPackage.COMPONENT_TYPE:
                return createComponentType();
            case ComponentsPackage.USED_ROLE:
                return createUsedRole();
            case ComponentsPackage.IMPLEMENTED_ROLE:
                return createImplementedRole();
            case ComponentsPackage.COMPONENT_COMPOSITION:
                return createComponentComposition();
            case ComponentsPackage.COMPONENT_INSTANCE:
                return createComponentInstance();
            case ComponentsPackage.CONNECTION:
                return createConnection();
            case ComponentsPackage.ROLE_INSTANCE:
                return createRoleInstance();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ComponentInterface createComponentInterface() {
        ComponentInterfaceImpl componentInterface = new ComponentInterfaceImpl();
        return componentInterface;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DataElement createDataElement() {
        DataElementImpl dataElement = new DataElementImpl();
        return dataElement;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ComponentType createComponentType() {
        ComponentTypeImpl componentType = new ComponentTypeImpl();
        return componentType;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public UsedRole createUsedRole() {
        UsedRoleImpl usedRole = new UsedRoleImpl();
        return usedRole;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ImplementedRole createImplementedRole() {
        ImplementedRoleImpl implementedRole = new ImplementedRoleImpl();
        return implementedRole;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ComponentComposition createComponentComposition() {
        ComponentCompositionImpl componentComposition = new ComponentCompositionImpl();
        return componentComposition;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ComponentInstance createComponentInstance() {
        ComponentInstanceImpl componentInstance = new ComponentInstanceImpl();
        return componentInstance;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Connection createConnection() {
        ConnectionImpl connection = new ConnectionImpl();
        return connection;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public RoleInstance createRoleInstance() {
        RoleInstanceImpl roleInstance = new RoleInstanceImpl();
        return roleInstance;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ComponentsPackage getComponentsPackage() {
        return (ComponentsPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static ComponentsPackage getPackage() {
        return ComponentsPackage.eINSTANCE;
    }
}
