package Slee11;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Adaptor Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link Slee11.ResourceAdaptorType#getDescription <em>Description</em>}</li>
 *   <li>{@link Slee11.ResourceAdaptorType#getResourceAdaptorName <em>Resource Adaptor Name</em>}</li>
 *   <li>{@link Slee11.ResourceAdaptorType#getResourceAdaptorVendor <em>Resource Adaptor Vendor</em>}</li>
 *   <li>{@link Slee11.ResourceAdaptorType#getResourceAdaptorVersion <em>Resource Adaptor Version</em>}</li>
 *   <li>{@link Slee11.ResourceAdaptorType#getResourceAdaptorTypeRef <em>Resource Adaptor Type Ref</em>}</li>
 *   <li>{@link Slee11.ResourceAdaptorType#getResourceAdaptorClasses <em>Resource Adaptor Classes</em>}</li>
 *   <li>{@link Slee11.ResourceAdaptorType#getConfigProperty <em>Config Property</em>}</li>
 *   <li>{@link Slee11.ResourceAdaptorType#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see Slee11.Slee11Package#getResourceAdaptorType()
 * @model extendedMetaData="name='resource-adaptor_._type' kind='elementOnly'"
 * @generated
 */
public interface ResourceAdaptorType extends EObject {

    /**
	 * Returns the value of the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' containment reference.
	 * @see #setDescription(DescriptionType)
	 * @see Slee11.Slee11Package#getResourceAdaptorType_Description()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
	 * @generated
	 */
    DescriptionType getDescription();

    /**
	 * Sets the value of the '{@link Slee11.ResourceAdaptorType#getDescription <em>Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' containment reference.
	 * @see #getDescription()
	 * @generated
	 */
    void setDescription(DescriptionType value);

    /**
	 * Returns the value of the '<em><b>Resource Adaptor Name</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource Adaptor Name</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource Adaptor Name</em>' containment reference.
	 * @see #setResourceAdaptorName(ResourceAdaptorNameType)
	 * @see Slee11.Slee11Package#getResourceAdaptorType_ResourceAdaptorName()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='resource-adaptor-name' namespace='##targetNamespace'"
	 * @generated
	 */
    ResourceAdaptorNameType getResourceAdaptorName();

    /**
	 * Sets the value of the '{@link Slee11.ResourceAdaptorType#getResourceAdaptorName <em>Resource Adaptor Name</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource Adaptor Name</em>' containment reference.
	 * @see #getResourceAdaptorName()
	 * @generated
	 */
    void setResourceAdaptorName(ResourceAdaptorNameType value);

    /**
	 * Returns the value of the '<em><b>Resource Adaptor Vendor</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource Adaptor Vendor</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource Adaptor Vendor</em>' containment reference.
	 * @see #setResourceAdaptorVendor(ResourceAdaptorVendorType)
	 * @see Slee11.Slee11Package#getResourceAdaptorType_ResourceAdaptorVendor()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='resource-adaptor-vendor' namespace='##targetNamespace'"
	 * @generated
	 */
    ResourceAdaptorVendorType getResourceAdaptorVendor();

    /**
	 * Sets the value of the '{@link Slee11.ResourceAdaptorType#getResourceAdaptorVendor <em>Resource Adaptor Vendor</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource Adaptor Vendor</em>' containment reference.
	 * @see #getResourceAdaptorVendor()
	 * @generated
	 */
    void setResourceAdaptorVendor(ResourceAdaptorVendorType value);

    /**
	 * Returns the value of the '<em><b>Resource Adaptor Version</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource Adaptor Version</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource Adaptor Version</em>' containment reference.
	 * @see #setResourceAdaptorVersion(ResourceAdaptorVersionType)
	 * @see Slee11.Slee11Package#getResourceAdaptorType_ResourceAdaptorVersion()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='resource-adaptor-version' namespace='##targetNamespace'"
	 * @generated
	 */
    ResourceAdaptorVersionType getResourceAdaptorVersion();

    /**
	 * Sets the value of the '{@link Slee11.ResourceAdaptorType#getResourceAdaptorVersion <em>Resource Adaptor Version</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource Adaptor Version</em>' containment reference.
	 * @see #getResourceAdaptorVersion()
	 * @generated
	 */
    void setResourceAdaptorVersion(ResourceAdaptorVersionType value);

    /**
	 * Returns the value of the '<em><b>Resource Adaptor Type Ref</b></em>' containment reference list.
	 * The list contents are of type {@link Slee11.ResourceAdaptorTypeRefType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource Adaptor Type Ref</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource Adaptor Type Ref</em>' containment reference list.
	 * @see Slee11.Slee11Package#getResourceAdaptorType_ResourceAdaptorTypeRef()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='resource-adaptor-type-ref' namespace='##targetNamespace'"
	 * @generated
	 */
    EList<ResourceAdaptorTypeRefType> getResourceAdaptorTypeRef();

    /**
	 * Returns the value of the '<em><b>Resource Adaptor Classes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource Adaptor Classes</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource Adaptor Classes</em>' containment reference.
	 * @see #setResourceAdaptorClasses(ResourceAdaptorClassesType)
	 * @see Slee11.Slee11Package#getResourceAdaptorType_ResourceAdaptorClasses()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='resource-adaptor-classes' namespace='##targetNamespace'"
	 * @generated
	 */
    ResourceAdaptorClassesType getResourceAdaptorClasses();

    /**
	 * Sets the value of the '{@link Slee11.ResourceAdaptorType#getResourceAdaptorClasses <em>Resource Adaptor Classes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource Adaptor Classes</em>' containment reference.
	 * @see #getResourceAdaptorClasses()
	 * @generated
	 */
    void setResourceAdaptorClasses(ResourceAdaptorClassesType value);

    /**
	 * Returns the value of the '<em><b>Config Property</b></em>' containment reference list.
	 * The list contents are of type {@link Slee11.ConfigPropertyType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Config Property</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Config Property</em>' containment reference list.
	 * @see Slee11.Slee11Package#getResourceAdaptorType_ConfigProperty()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='config-property' namespace='##targetNamespace'"
	 * @generated
	 */
    EList<ConfigPropertyType> getConfigProperty();

    /**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see Slee11.Slee11Package#getResourceAdaptorType_Id()
	 * @model id="true" dataType="org.eclipse.emf.ecore.xml.type.ID"
	 *        extendedMetaData="kind='attribute' name='id' namespace='##targetNamespace'"
	 * @generated
	 */
    String getId();

    /**
	 * Sets the value of the '{@link Slee11.ResourceAdaptorType#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
    void setId(String value);
}
