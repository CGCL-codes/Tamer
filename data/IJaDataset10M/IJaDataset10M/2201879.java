package org.hl7.v3;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>POCDMT000040 Device</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.hl7.v3.POCDMT000040Device#getRealmCode <em>Realm Code</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040Device#getTypeId <em>Type Id</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040Device#getTemplateId <em>Template Id</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040Device#getCode <em>Code</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040Device#getManufacturerModelName <em>Manufacturer Model Name</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040Device#getSoftwareName <em>Software Name</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040Device#getClassCode <em>Class Code</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040Device#getDeterminerCode <em>Determiner Code</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040Device#getNullFlavor <em>Null Flavor</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.hl7.v3.V3Package#getPOCDMT000040Device()
 * @model extendedMetaData="name='POCD_MT000040.Device' kind='elementOnly'"
 * @generated
 */
public interface POCDMT000040Device extends EObject {

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
	 * @see org.hl7.v3.V3Package#getPOCDMT000040Device_RealmCode()
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
	 * @see org.hl7.v3.V3Package#getPOCDMT000040Device_TypeId()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='typeId' namespace='##targetNamespace'"
	 * @generated
	 */
    POCDMT000040InfrastructureRootTypeId getTypeId();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040Device#getTypeId <em>Type Id</em>}' containment reference.
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
	 * @see org.hl7.v3.V3Package#getPOCDMT000040Device_TemplateId()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='templateId' namespace='##targetNamespace'"
	 * @generated
	 */
    EList<II> getTemplateId();

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
	 * @see org.hl7.v3.V3Package#getPOCDMT000040Device_Code()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='code' namespace='##targetNamespace'"
	 * @generated
	 */
    CE getCode();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040Device#getCode <em>Code</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Code</em>' containment reference.
	 * @see #getCode()
	 * @generated
	 */
    void setCode(CE value);

    /**
	 * Returns the value of the '<em><b>Manufacturer Model Name</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Manufacturer Model Name</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Manufacturer Model Name</em>' containment reference.
	 * @see #setManufacturerModelName(SC)
	 * @see org.hl7.v3.V3Package#getPOCDMT000040Device_ManufacturerModelName()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='manufacturerModelName' namespace='##targetNamespace'"
	 * @generated
	 */
    SC getManufacturerModelName();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040Device#getManufacturerModelName <em>Manufacturer Model Name</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Manufacturer Model Name</em>' containment reference.
	 * @see #getManufacturerModelName()
	 * @generated
	 */
    void setManufacturerModelName(SC value);

    /**
	 * Returns the value of the '<em><b>Software Name</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Software Name</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Software Name</em>' containment reference.
	 * @see #setSoftwareName(SC)
	 * @see org.hl7.v3.V3Package#getPOCDMT000040Device_SoftwareName()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='softwareName' namespace='##targetNamespace'"
	 * @generated
	 */
    SC getSoftwareName();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040Device#getSoftwareName <em>Software Name</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Software Name</em>' containment reference.
	 * @see #getSoftwareName()
	 * @generated
	 */
    void setSoftwareName(SC value);

    /**
	 * Returns the value of the '<em><b>Class Code</b></em>' attribute.
	 * The default value is <code>"DEV"</code>.
	 * The literals are from the enumeration {@link org.hl7.v3.EntityClassDevice}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Class Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Class Code</em>' attribute.
	 * @see org.hl7.v3.EntityClassDevice
	 * @see #isSetClassCode()
	 * @see #unsetClassCode()
	 * @see #setClassCode(EntityClassDevice)
	 * @see org.hl7.v3.V3Package#getPOCDMT000040Device_ClassCode()
	 * @model default="DEV" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='classCode'"
	 * @generated
	 */
    EntityClassDevice getClassCode();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040Device#getClassCode <em>Class Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Class Code</em>' attribute.
	 * @see org.hl7.v3.EntityClassDevice
	 * @see #isSetClassCode()
	 * @see #unsetClassCode()
	 * @see #getClassCode()
	 * @generated
	 */
    void setClassCode(EntityClassDevice value);

    /**
	 * Unsets the value of the '{@link org.hl7.v3.POCDMT000040Device#getClassCode <em>Class Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetClassCode()
	 * @see #getClassCode()
	 * @see #setClassCode(EntityClassDevice)
	 * @generated
	 */
    void unsetClassCode();

    /**
	 * Returns whether the value of the '{@link org.hl7.v3.POCDMT000040Device#getClassCode <em>Class Code</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Class Code</em>' attribute is set.
	 * @see #unsetClassCode()
	 * @see #getClassCode()
	 * @see #setClassCode(EntityClassDevice)
	 * @generated
	 */
    boolean isSetClassCode();

    /**
	 * Returns the value of the '<em><b>Determiner Code</b></em>' attribute.
	 * The default value is <code>"INSTANCE"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Determiner Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Determiner Code</em>' attribute.
	 * @see #isSetDeterminerCode()
	 * @see #unsetDeterminerCode()
	 * @see #setDeterminerCode(Enumerator)
	 * @see org.hl7.v3.V3Package#getPOCDMT000040Device_DeterminerCode()
	 * @model default="INSTANCE" unsettable="true" dataType="org.hl7.v3.EntityDeterminer"
	 *        extendedMetaData="kind='attribute' name='determinerCode'"
	 * @generated
	 */
    Enumerator getDeterminerCode();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040Device#getDeterminerCode <em>Determiner Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Determiner Code</em>' attribute.
	 * @see #isSetDeterminerCode()
	 * @see #unsetDeterminerCode()
	 * @see #getDeterminerCode()
	 * @generated
	 */
    void setDeterminerCode(Enumerator value);

    /**
	 * Unsets the value of the '{@link org.hl7.v3.POCDMT000040Device#getDeterminerCode <em>Determiner Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDeterminerCode()
	 * @see #getDeterminerCode()
	 * @see #setDeterminerCode(Enumerator)
	 * @generated
	 */
    void unsetDeterminerCode();

    /**
	 * Returns whether the value of the '{@link org.hl7.v3.POCDMT000040Device#getDeterminerCode <em>Determiner Code</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Determiner Code</em>' attribute is set.
	 * @see #unsetDeterminerCode()
	 * @see #getDeterminerCode()
	 * @see #setDeterminerCode(Enumerator)
	 * @generated
	 */
    boolean isSetDeterminerCode();

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
	 * @see org.hl7.v3.V3Package#getPOCDMT000040Device_NullFlavor()
	 * @model dataType="org.hl7.v3.NullFlavor"
	 *        extendedMetaData="kind='attribute' name='nullFlavor'"
	 * @generated
	 */
    Enumerator getNullFlavor();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040Device#getNullFlavor <em>Null Flavor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Null Flavor</em>' attribute.
	 * @see #getNullFlavor()
	 * @generated
	 */
    void setNullFlavor(Enumerator value);
}
