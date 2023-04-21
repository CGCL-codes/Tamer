package ufrgs.inf.delphos.extraction.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.jdt.core.IMemberValuePair;
import ufrgs.inf.delphos.extraction.model.*;
import ufrgs.inf.delphos.identification.knowledgebase.Tag;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DelphosSystemModelFactoryImpl extends EFactoryImpl implements DelphosSystemModelFactory {

    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static DelphosSystemModelFactory init() {
        try {
            DelphosSystemModelFactory theDelphosSystemModelFactory = (DelphosSystemModelFactory) EPackage.Registry.INSTANCE.getEFactory("http:///ufrgs/inf/delphos/extraction/model");
            if (theDelphosSystemModelFactory != null) {
                return theDelphosSystemModelFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new DelphosSystemModelFactoryImpl();
    }

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DelphosSystemModelFactoryImpl() {
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
            case DelphosSystemModelPackage.DCOMPILATION_UNIT:
                return createDCompilationUnit();
            case DelphosSystemModelPackage.DANNOTATION:
                return createDAnnotation();
            case DelphosSystemModelPackage.DPOLICY:
                return createDPolicy();
            case DelphosSystemModelPackage.DSYSTEM:
                return createDSystem();
            case DelphosSystemModelPackage.DPACKAGE:
                return createDPackage();
            case DelphosSystemModelPackage.DRELATION:
                return createDRelation();
            case DelphosSystemModelPackage.DCONFIG:
                return createDConfig();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch(eDataType.getClassifierID()) {
            case DelphosSystemModelPackage.DRELATION_TYPE:
                return createDRelationTypeFromString(eDataType, initialValue);
            case DelphosSystemModelPackage.DANNOTATION_TYPE:
                return createDAnnotationTypeFromString(eDataType, initialValue);
            case DelphosSystemModelPackage.IMEMBER_VALUE_PAIR:
                return createIMemberValuePairFromString(eDataType, initialValue);
            case DelphosSystemModelPackage.TAG:
                return createTagFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch(eDataType.getClassifierID()) {
            case DelphosSystemModelPackage.DRELATION_TYPE:
                return convertDRelationTypeToString(eDataType, instanceValue);
            case DelphosSystemModelPackage.DANNOTATION_TYPE:
                return convertDAnnotationTypeToString(eDataType, instanceValue);
            case DelphosSystemModelPackage.IMEMBER_VALUE_PAIR:
                return convertIMemberValuePairToString(eDataType, instanceValue);
            case DelphosSystemModelPackage.TAG:
                return convertTagToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DCompilationUnit createDCompilationUnit() {
        DCompilationUnitImpl dCompilationUnit = new DCompilationUnitImpl();
        return dCompilationUnit;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DAnnotation createDAnnotation() {
        DAnnotationImpl dAnnotation = new DAnnotationImpl();
        return dAnnotation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DPolicy createDPolicy() {
        DPolicyImpl dPolicy = new DPolicyImpl();
        return dPolicy;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DSystem createDSystem() {
        DSystemImpl dSystem = new DSystemImpl();
        return dSystem;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DPackage createDPackage() {
        DPackageImpl dPackage = new DPackageImpl();
        return dPackage;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DRelation createDRelation() {
        DRelationImpl dRelation = new DRelationImpl();
        return dRelation;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DConfig createDConfig() {
        DConfigImpl dConfig = new DConfigImpl();
        return dConfig;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DRelationType createDRelationTypeFromString(EDataType eDataType, String initialValue) {
        DRelationType result = DRelationType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertDRelationTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DAnnotationType createDAnnotationTypeFromString(EDataType eDataType, String initialValue) {
        DAnnotationType result = DAnnotationType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertDAnnotationTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public IMemberValuePair createIMemberValuePairFromString(EDataType eDataType, String initialValue) {
        return (IMemberValuePair) super.createFromString(eDataType, initialValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertIMemberValuePairToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Tag createTagFromString(EDataType eDataType, String initialValue) {
        return (Tag) super.createFromString(eDataType, initialValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertTagToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DelphosSystemModelPackage getDelphosSystemModelPackage() {
        return (DelphosSystemModelPackage) getEPackage();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    @Deprecated
    public static DelphosSystemModelPackage getPackage() {
        return DelphosSystemModelPackage.eINSTANCE;
    }
}
