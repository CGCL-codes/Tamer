package eu.medeia.caex.model.CAEXClassModelV215.util;

import java.util.List;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import eu.medeia.caex.model.CAEXClassModelV215.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see eu.medeia.caex.model.CAEXClassModelV215.CAEXClassModelV215Package
 * @generated
 */
public class CAEXClassModelV215Switch<T> {

    /**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected static CAEXClassModelV215Package modelPackage;

    /**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public CAEXClassModelV215Switch() {
        if (modelPackage == null) {
            modelPackage = CAEXClassModelV215Package.eINSTANCE;
        }
    }

    /**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
    public T doSwitch(EObject theEObject) {
        return doSwitch(theEObject.eClass(), theEObject);
    }

    /**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
    protected T doSwitch(EClass theEClass, EObject theEObject) {
        if (theEClass.eContainer() == modelPackage) {
            return doSwitch(theEClass.getClassifierID(), theEObject);
        } else {
            List<EClass> eSuperTypes = theEClass.getESuperTypes();
            return eSuperTypes.isEmpty() ? defaultCase(theEObject) : doSwitch(eSuperTypes.get(0), theEObject);
        }
    }

    /**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch(classifierID) {
            case CAEXClassModelV215Package.ATTRIBUTE_NAME_MAPPING_TYPE:
                {
                    AttributeNameMappingType attributeNameMappingType = (AttributeNameMappingType) theEObject;
                    T result = caseAttributeNameMappingType(attributeNameMappingType);
                    if (result == null) result = caseCAEXBasicObject(attributeNameMappingType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.ATTRIBUTE_TYPE:
                {
                    AttributeType attributeType = (AttributeType) theEObject;
                    T result = caseAttributeType(attributeType);
                    if (result == null) result = caseCAEXObject(attributeType);
                    if (result == null) result = caseCAEXBasicObject(attributeType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.ATTRIBUTE_VALUE_REQUIREMENT_TYPE:
                {
                    AttributeValueRequirementType attributeValueRequirementType = (AttributeValueRequirementType) theEObject;
                    T result = caseAttributeValueRequirementType(attributeValueRequirementType);
                    if (result == null) result = caseCAEXBasicObject(attributeValueRequirementType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.CAEX_BASIC_OBJECT:
                {
                    CAEXBasicObject caexBasicObject = (CAEXBasicObject) theEObject;
                    T result = caseCAEXBasicObject(caexBasicObject);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.CAEX_FILE_TYPE:
                {
                    CAEXFileType caexFileType = (CAEXFileType) theEObject;
                    T result = caseCAEXFileType(caexFileType);
                    if (result == null) result = caseCAEXBasicObject(caexFileType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.CAEX_OBJECT:
                {
                    CAEXObject caexObject = (CAEXObject) theEObject;
                    T result = caseCAEXObject(caexObject);
                    if (result == null) result = caseCAEXBasicObject(caexObject);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.COPYRIGHT_TYPE:
                {
                    CopyrightType copyrightType = (CopyrightType) theEObject;
                    T result = caseCopyrightType(copyrightType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.DESCRIPTION_TYPE:
                {
                    DescriptionType descriptionType = (DescriptionType) theEObject;
                    T result = caseDescriptionType(descriptionType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.DOCUMENT_ROOT:
                {
                    DocumentRoot documentRoot = (DocumentRoot) theEObject;
                    T result = caseDocumentRoot(documentRoot);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.EXTERNAL_INTERFACE_TYPE:
                {
                    ExternalInterfaceType externalInterfaceType = (ExternalInterfaceType) theEObject;
                    T result = caseExternalInterfaceType(externalInterfaceType);
                    if (result == null) result = caseInterfaceClassType(externalInterfaceType);
                    if (result == null) result = caseCAEXObject(externalInterfaceType);
                    if (result == null) result = caseCAEXBasicObject(externalInterfaceType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.EXTERNAL_REFERENCE_TYPE:
                {
                    ExternalReferenceType externalReferenceType = (ExternalReferenceType) theEObject;
                    T result = caseExternalReferenceType(externalReferenceType);
                    if (result == null) result = caseCAEXBasicObject(externalReferenceType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.INSTANCE_HIERARCHY_TYPE:
                {
                    InstanceHierarchyType instanceHierarchyType = (InstanceHierarchyType) theEObject;
                    T result = caseInstanceHierarchyType(instanceHierarchyType);
                    if (result == null) result = caseCAEXObject(instanceHierarchyType);
                    if (result == null) result = caseCAEXBasicObject(instanceHierarchyType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.INTERFACE_CLASS_LIB_TYPE:
                {
                    InterfaceClassLibType interfaceClassLibType = (InterfaceClassLibType) theEObject;
                    T result = caseInterfaceClassLibType(interfaceClassLibType);
                    if (result == null) result = caseCAEXObject(interfaceClassLibType);
                    if (result == null) result = caseCAEXBasicObject(interfaceClassLibType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.INTERFACE_CLASS_TYPE:
                {
                    InterfaceClassType interfaceClassType = (InterfaceClassType) theEObject;
                    T result = caseInterfaceClassType(interfaceClassType);
                    if (result == null) result = caseCAEXObject(interfaceClassType);
                    if (result == null) result = caseCAEXBasicObject(interfaceClassType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.INTERFACE_FAMILY_TYPE:
                {
                    InterfaceFamilyType interfaceFamilyType = (InterfaceFamilyType) theEObject;
                    T result = caseInterfaceFamilyType(interfaceFamilyType);
                    if (result == null) result = caseInterfaceClassType(interfaceFamilyType);
                    if (result == null) result = caseCAEXObject(interfaceFamilyType);
                    if (result == null) result = caseCAEXBasicObject(interfaceFamilyType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.INTERFACE_NAME_MAPPING_TYPE:
                {
                    InterfaceNameMappingType interfaceNameMappingType = (InterfaceNameMappingType) theEObject;
                    T result = caseInterfaceNameMappingType(interfaceNameMappingType);
                    if (result == null) result = caseCAEXBasicObject(interfaceNameMappingType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.INTERNAL_ELEMENT_TYPE:
                {
                    InternalElementType internalElementType = (InternalElementType) theEObject;
                    T result = caseInternalElementType(internalElementType);
                    if (result == null) result = caseSystemUnitClassType(internalElementType);
                    if (result == null) result = caseCAEXObject(internalElementType);
                    if (result == null) result = caseCAEXBasicObject(internalElementType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.INTERNAL_LINK_TYPE:
                {
                    InternalLinkType internalLinkType = (InternalLinkType) theEObject;
                    T result = caseInternalLinkType(internalLinkType);
                    if (result == null) result = caseCAEXObject(internalLinkType);
                    if (result == null) result = caseCAEXBasicObject(internalLinkType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.MAPPING_TYPE:
                {
                    MappingType mappingType = (MappingType) theEObject;
                    T result = caseMappingType(mappingType);
                    if (result == null) result = caseCAEXBasicObject(mappingType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.NOMINAL_SCALED_TYPE_TYPE:
                {
                    NominalScaledTypeType nominalScaledTypeType = (NominalScaledTypeType) theEObject;
                    T result = caseNominalScaledTypeType(nominalScaledTypeType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.ORDINAL_SCALED_TYPE_TYPE:
                {
                    OrdinalScaledTypeType ordinalScaledTypeType = (OrdinalScaledTypeType) theEObject;
                    T result = caseOrdinalScaledTypeType(ordinalScaledTypeType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.REF_SEMANTIC_TYPE:
                {
                    RefSemanticType refSemanticType = (RefSemanticType) theEObject;
                    T result = caseRefSemanticType(refSemanticType);
                    if (result == null) result = caseCAEXBasicObject(refSemanticType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.REVISION_TYPE:
                {
                    RevisionType revisionType = (RevisionType) theEObject;
                    T result = caseRevisionType(revisionType);
                    if (result == null) result = caseCAEXBasicObject(revisionType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.ROLE_CLASS_LIB_TYPE:
                {
                    RoleClassLibType roleClassLibType = (RoleClassLibType) theEObject;
                    T result = caseRoleClassLibType(roleClassLibType);
                    if (result == null) result = caseCAEXObject(roleClassLibType);
                    if (result == null) result = caseCAEXBasicObject(roleClassLibType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.ROLE_CLASS_TYPE:
                {
                    RoleClassType roleClassType = (RoleClassType) theEObject;
                    T result = caseRoleClassType(roleClassType);
                    if (result == null) result = caseCAEXObject(roleClassType);
                    if (result == null) result = caseCAEXBasicObject(roleClassType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.ROLE_FAMILY_TYPE:
                {
                    RoleFamilyType roleFamilyType = (RoleFamilyType) theEObject;
                    T result = caseRoleFamilyType(roleFamilyType);
                    if (result == null) result = caseRoleClassType(roleFamilyType);
                    if (result == null) result = caseCAEXObject(roleFamilyType);
                    if (result == null) result = caseCAEXBasicObject(roleFamilyType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.ROLE_REQUIREMENTS_TYPE:
                {
                    RoleRequirementsType roleRequirementsType = (RoleRequirementsType) theEObject;
                    T result = caseRoleRequirementsType(roleRequirementsType);
                    if (result == null) result = caseCAEXBasicObject(roleRequirementsType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.SUPPORTED_ROLE_CLASS_TYPE:
                {
                    SupportedRoleClassType supportedRoleClassType = (SupportedRoleClassType) theEObject;
                    T result = caseSupportedRoleClassType(supportedRoleClassType);
                    if (result == null) result = caseCAEXBasicObject(supportedRoleClassType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.SYSTEM_UNIT_CLASS_LIB_TYPE:
                {
                    SystemUnitClassLibType systemUnitClassLibType = (SystemUnitClassLibType) theEObject;
                    T result = caseSystemUnitClassLibType(systemUnitClassLibType);
                    if (result == null) result = caseCAEXObject(systemUnitClassLibType);
                    if (result == null) result = caseCAEXBasicObject(systemUnitClassLibType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.SYSTEM_UNIT_CLASS_TYPE:
                {
                    SystemUnitClassType systemUnitClassType = (SystemUnitClassType) theEObject;
                    T result = caseSystemUnitClassType(systemUnitClassType);
                    if (result == null) result = caseCAEXObject(systemUnitClassType);
                    if (result == null) result = caseCAEXBasicObject(systemUnitClassType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.SYSTEM_UNIT_FAMILY_TYPE:
                {
                    SystemUnitFamilyType systemUnitFamilyType = (SystemUnitFamilyType) theEObject;
                    T result = caseSystemUnitFamilyType(systemUnitFamilyType);
                    if (result == null) result = caseSystemUnitClassType(systemUnitFamilyType);
                    if (result == null) result = caseCAEXObject(systemUnitFamilyType);
                    if (result == null) result = caseCAEXBasicObject(systemUnitFamilyType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.UNKNOWN_TYPE_TYPE:
                {
                    UnknownTypeType unknownTypeType = (UnknownTypeType) theEObject;
                    T result = caseUnknownTypeType(unknownTypeType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            case CAEXClassModelV215Package.VERSION_TYPE:
                {
                    VersionType versionType = (VersionType) theEObject;
                    T result = caseVersionType(versionType);
                    if (result == null) result = defaultCase(theEObject);
                    return result;
                }
            default:
                return defaultCase(theEObject);
        }
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Name Mapping Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Name Mapping Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseAttributeNameMappingType(AttributeNameMappingType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseAttributeType(AttributeType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute Value Requirement Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute Value Requirement Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseAttributeValueRequirementType(AttributeValueRequirementType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>CAEX Basic Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>CAEX Basic Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseCAEXBasicObject(CAEXBasicObject object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>CAEX File Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>CAEX File Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseCAEXFileType(CAEXFileType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>CAEX Object</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>CAEX Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseCAEXObject(CAEXObject object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Copyright Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Copyright Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseCopyrightType(CopyrightType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Description Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Description Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseDescriptionType(DescriptionType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Document Root</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Document Root</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseDocumentRoot(DocumentRoot object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>External Interface Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>External Interface Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseExternalInterfaceType(ExternalInterfaceType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>External Reference Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>External Reference Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseExternalReferenceType(ExternalReferenceType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Instance Hierarchy Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Instance Hierarchy Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseInstanceHierarchyType(InstanceHierarchyType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Interface Class Lib Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Interface Class Lib Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseInterfaceClassLibType(InterfaceClassLibType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Interface Class Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Interface Class Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseInterfaceClassType(InterfaceClassType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Interface Family Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Interface Family Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseInterfaceFamilyType(InterfaceFamilyType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Interface Name Mapping Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Interface Name Mapping Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseInterfaceNameMappingType(InterfaceNameMappingType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Internal Element Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Internal Element Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseInternalElementType(InternalElementType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Internal Link Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Internal Link Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseInternalLinkType(InternalLinkType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Mapping Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Mapping Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseMappingType(MappingType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Nominal Scaled Type Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Nominal Scaled Type Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseNominalScaledTypeType(NominalScaledTypeType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Ordinal Scaled Type Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ordinal Scaled Type Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseOrdinalScaledTypeType(OrdinalScaledTypeType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Ref Semantic Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ref Semantic Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseRefSemanticType(RefSemanticType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Revision Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Revision Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseRevisionType(RevisionType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Role Class Lib Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Role Class Lib Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseRoleClassLibType(RoleClassLibType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Role Class Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Role Class Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseRoleClassType(RoleClassType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Role Family Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Role Family Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseRoleFamilyType(RoleFamilyType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Role Requirements Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Role Requirements Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseRoleRequirementsType(RoleRequirementsType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Supported Role Class Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Supported Role Class Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseSupportedRoleClassType(SupportedRoleClassType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>System Unit Class Lib Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>System Unit Class Lib Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseSystemUnitClassLibType(SystemUnitClassLibType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>System Unit Class Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>System Unit Class Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseSystemUnitClassType(SystemUnitClassType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>System Unit Family Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>System Unit Family Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseSystemUnitFamilyType(SystemUnitFamilyType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Unknown Type Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Unknown Type Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseUnknownTypeType(UnknownTypeType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>Version Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Version Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
    public T caseVersionType(VersionType object) {
        return null;
    }

    /**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
    public T defaultCase(EObject object) {
        return null;
    }
}
