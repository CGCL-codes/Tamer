package org.hl7.v3;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>POCDMT000040 Organization Part Of</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getRealmCode <em>Realm Code</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getTypeId <em>Type Id</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getTemplateId <em>Template Id</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getId <em>Id</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getCode <em>Code</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getStatusCode <em>Status Code</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getEffectiveTime <em>Effective Time</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getWholeOrganization <em>Whole Organization</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getClassCode <em>Class Code</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getNullFlavor <em>Null Flavor</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.hl7.v3.V3Package#getPOCDMT000040OrganizationPartOf()
 * @model extendedMetaData="name='POCD_MT000040.OrganizationPartOf' kind='elementOnly'"
 * @generated
 */
public interface POCDMT000040OrganizationPartOf extends EObject {

    /**
	 * Returns the value of the '<em><b>Realm Code</b></em>' containment reference list.
	 * The list contents are of type {@link org.hl7.v3.CS1}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Realm Code</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Realm Code</em>' containment reference list.
	 * @see org.hl7.v3.V3Package#getPOCDMT000040OrganizationPartOf_RealmCode()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='realmCode' namespace='##targetNamespace'"
	 * @generated
	 */
    EList<CS1> getRealmCode();

    /**
	 * Returns the value of the '<em><b>Type Id</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Id</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type Id</em>' containment reference.
	 * @see #setTypeId(POCDMT000040InfrastructureRootTypeId)
	 * @see org.hl7.v3.V3Package#getPOCDMT000040OrganizationPartOf_TypeId()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='typeId' namespace='##targetNamespace'"
	 * @generated
	 */
    POCDMT000040InfrastructureRootTypeId getTypeId();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getTypeId <em>Type Id</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Id</em>' containment reference.
	 * @see #getTypeId()
	 * @generated
	 */
    void setTypeId(POCDMT000040InfrastructureRootTypeId value);

    /**
	 * Returns the value of the '<em><b>Template Id</b></em>' containment reference list.
	 * The list contents are of type {@link org.hl7.v3.II}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Template Id</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Template Id</em>' containment reference list.
	 * @see org.hl7.v3.V3Package#getPOCDMT000040OrganizationPartOf_TemplateId()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='templateId' namespace='##targetNamespace'"
	 * @generated
	 */
    EList<II> getTemplateId();

    /**
	 * Returns the value of the '<em><b>Id</b></em>' containment reference list.
	 * The list contents are of type {@link org.hl7.v3.II}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' containment reference list.
	 * @see org.hl7.v3.V3Package#getPOCDMT000040OrganizationPartOf_Id()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='id' namespace='##targetNamespace'"
	 * @generated
	 */
    EList<II> getId();

    /**
	 * Returns the value of the '<em><b>Code</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Code</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Code</em>' containment reference.
	 * @see #setCode(CE)
	 * @see org.hl7.v3.V3Package#getPOCDMT000040OrganizationPartOf_Code()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='code' namespace='##targetNamespace'"
	 * @generated
	 */
    CE getCode();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getCode <em>Code</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Code</em>' containment reference.
	 * @see #getCode()
	 * @generated
	 */
    void setCode(CE value);

    /**
	 * Returns the value of the '<em><b>Status Code</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Status Code</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Status Code</em>' containment reference.
	 * @see #setStatusCode(CS1)
	 * @see org.hl7.v3.V3Package#getPOCDMT000040OrganizationPartOf_StatusCode()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='statusCode' namespace='##targetNamespace'"
	 * @generated
	 */
    CS1 getStatusCode();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getStatusCode <em>Status Code</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Status Code</em>' containment reference.
	 * @see #getStatusCode()
	 * @generated
	 */
    void setStatusCode(CS1 value);

    /**
	 * Returns the value of the '<em><b>Effective Time</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Effective Time</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Effective Time</em>' containment reference.
	 * @see #setEffectiveTime(IVLTS)
	 * @see org.hl7.v3.V3Package#getPOCDMT000040OrganizationPartOf_EffectiveTime()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='effectiveTime' namespace='##targetNamespace'"
	 * @generated
	 */
    IVLTS getEffectiveTime();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getEffectiveTime <em>Effective Time</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Effective Time</em>' containment reference.
	 * @see #getEffectiveTime()
	 * @generated
	 */
    void setEffectiveTime(IVLTS value);

    /**
	 * Returns the value of the '<em><b>Whole Organization</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Whole Organization</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Whole Organization</em>' containment reference.
	 * @see #setWholeOrganization(POCDMT000040Organization)
	 * @see org.hl7.v3.V3Package#getPOCDMT000040OrganizationPartOf_WholeOrganization()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='wholeOrganization' namespace='##targetNamespace'"
	 * @generated
	 */
    POCDMT000040Organization getWholeOrganization();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getWholeOrganization <em>Whole Organization</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Whole Organization</em>' containment reference.
	 * @see #getWholeOrganization()
	 * @generated
	 */
    void setWholeOrganization(POCDMT000040Organization value);

    /**
	 * Returns the value of the '<em><b>Class Code</b></em>' attribute.
	 * The default value is <code>"PART"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Class Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Class Code</em>' attribute.
	 * @see #isSetClassCode()
	 * @see #unsetClassCode()
	 * @see #setClassCode(Object)
	 * @see org.hl7.v3.V3Package#getPOCDMT000040OrganizationPartOf_ClassCode()
	 * @model default="PART" unsettable="true" dataType="org.hl7.v3.RoleClass"
	 *        extendedMetaData="kind='attribute' name='classCode'"
	 * @generated
	 */
    Object getClassCode();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getClassCode <em>Class Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Class Code</em>' attribute.
	 * @see #isSetClassCode()
	 * @see #unsetClassCode()
	 * @see #getClassCode()
	 * @generated
	 */
    void setClassCode(Object value);

    /**
	 * Unsets the value of the '{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getClassCode <em>Class Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetClassCode()
	 * @see #getClassCode()
	 * @see #setClassCode(Object)
	 * @generated
	 */
    void unsetClassCode();

    /**
	 * Returns whether the value of the '{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getClassCode <em>Class Code</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Class Code</em>' attribute is set.
	 * @see #unsetClassCode()
	 * @see #getClassCode()
	 * @see #setClassCode(Object)
	 * @generated
	 */
    boolean isSetClassCode();

    /**
	 * Returns the value of the '<em><b>Null Flavor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Null Flavor</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Null Flavor</em>' attribute.
	 * @see #setNullFlavor(Enumerator)
	 * @see org.hl7.v3.V3Package#getPOCDMT000040OrganizationPartOf_NullFlavor()
	 * @model dataType="org.hl7.v3.NullFlavor"
	 *        extendedMetaData="kind='attribute' name='nullFlavor'"
	 * @generated
	 */
    Enumerator getNullFlavor();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040OrganizationPartOf#getNullFlavor <em>Null Flavor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Null Flavor</em>' attribute.
	 * @see #getNullFlavor()
	 * @generated
	 */
    void setNullFlavor(Enumerator value);
}
