package drarch.diagram.ucmModel.impl;

import drarch.diagram.cModel.Component;
import drarch.diagram.ucmModel.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EFactoryImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class UcmModelFactoryImpl extends EFactoryImpl implements UcmModelFactory {

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public UcmModelFactoryImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EObject create(EClass eClass) {
        switch(eClass.getClassifierID()) {
            case UcmModelPackage.COMPONENT_ROLE:
                return createComponentRole();
            case UcmModelPackage.PATH:
                return createPath();
            case UcmModelPackage.PATH_NODE:
                return createPathNode();
            case UcmModelPackage.UCM_DIAGRAM:
                return createUCMDiagram();
            case UcmModelPackage.UCM_MODEL:
                return createUCMModel();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch(eDataType.getClassifierID()) {
            case UcmModelPackage.COMPONENT:
                return createComponentFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch(eDataType.getClassifierID()) {
            case UcmModelPackage.COMPONENT:
                return convertComponentToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ComponentRole createComponentRole() {
        ComponentRoleImpl componentRole = new ComponentRoleImpl();
        return componentRole;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Path createPath() {
        PathImpl path = new PathImpl();
        return path;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PathNode createPathNode() {
        PathNodeImpl pathNode = new PathNodeImpl();
        return pathNode;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public UCMDiagram createUCMDiagram() {
        UCMDiagramImpl ucmDiagram = new UCMDiagramImpl();
        return ucmDiagram;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public UCMModel createUCMModel() {
        UCMModelImpl ucmModel = new UCMModelImpl();
        return ucmModel;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Component createComponentFromString(EDataType eDataType, String initialValue) {
        return (Component) super.createFromString(eDataType, initialValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertComponentToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public UcmModelPackage getUcmModelPackage() {
        return (UcmModelPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    public static UcmModelPackage getPackage() {
        return UcmModelPackage.eINSTANCE;
    }
}
