package org.hl7.v3;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>POCDMT000040 External Procedure</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.hl7.v3.POCDMT000040ExternalProcedure#getRealmCode <em>Realm Code</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040ExternalProcedure#getTypeId <em>Type Id</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040ExternalProcedure#getTemplateId <em>Template Id</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040ExternalProcedure#getId <em>Id</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040ExternalProcedure#getCode <em>Code</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040ExternalProcedure#getText <em>Text</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040ExternalProcedure#getClassCode <em>Class Code</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040ExternalProcedure#getMoodCode <em>Mood Code</em>}</li>
 *   <li>{@link org.hl7.v3.POCDMT000040ExternalProcedure#getNullFlavor <em>Null Flavor</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.hl7.v3.V3Package#getPOCDMT000040ExternalProcedure()
 * @model extendedMetaData="name='POCD_MT000040.ExternalProcedure' kind='elementOnly'"
 * @generated
 */
public interface POCDMT000040ExternalProcedure extends EObject {

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
	 * @see org.hl7.v3.V3Package#getPOCDMT000040ExternalProcedure_RealmCode()
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
	 * @see org.hl7.v3.V3Package#getPOCDMT000040ExternalProcedure_TypeId()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='typeId' namespace='##targetNamespace'"
	 * @generated
	 */
    POCDMT000040InfrastructureRootTypeId getTypeId();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040ExternalProcedure#getTypeId <em>Type Id</em>}' containment reference.
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
	 * @see org.hl7.v3.V3Package#getPOCDMT000040ExternalProcedure_TemplateId()
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
	 * @see org.hl7.v3.V3Package#getPOCDMT000040ExternalProcedure_Id()
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
	 * @see #setCode(CD)
	 * @see org.hl7.v3.V3Package#getPOCDMT000040ExternalProcedure_Code()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='code' namespace='##targetNamespace'"
	 * @generated
	 */
    CD getCode();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040ExternalProcedure#getCode <em>Code</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Code</em>' containment reference.
	 * @see #getCode()
	 * @generated
	 */
    void setCode(CD value);

    /**
	 * Returns the value of the '<em><b>Text</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Text</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Text</em>' containment reference.
	 * @see #setText(ED)
	 * @see org.hl7.v3.V3Package#getPOCDMT000040ExternalProcedure_Text()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='text' namespace='##targetNamespace'"
	 * @generated
	 */
    ED getText();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040ExternalProcedure#getText <em>Text</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Text</em>' containment reference.
	 * @see #getText()
	 * @generated
	 */
    void setText(ED value);

    /**
	 * Returns the value of the '<em><b>Class Code</b></em>' attribute.
	 * The default value is <code>"PROC"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Class Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Class Code</em>' attribute.
	 * @see #isSetClassCode()
	 * @see #unsetClassCode()
	 * @see #setClassCode(Enumerator)
	 * @see org.hl7.v3.V3Package#getPOCDMT000040ExternalProcedure_ClassCode()
	 * @model default="PROC" unsettable="true" dataType="org.hl7.v3.ActClass"
	 *        extendedMetaData="kind='attribute' name='classCode'"
	 * @generated
	 */
    Enumerator getClassCode();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040ExternalProcedure#getClassCode <em>Class Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Class Code</em>' attribute.
	 * @see #isSetClassCode()
	 * @see #unsetClassCode()
	 * @see #getClassCode()
	 * @generated
	 */
    void setClassCode(Enumerator value);

    /**
	 * Unsets the value of the '{@link org.hl7.v3.POCDMT000040ExternalProcedure#getClassCode <em>Class Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetClassCode()
	 * @see #getClassCode()
	 * @see #setClassCode(Enumerator)
	 * @generated
	 */
    void unsetClassCode();

    /**
	 * Returns whether the value of the '{@link org.hl7.v3.POCDMT000040ExternalProcedure#getClassCode <em>Class Code</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Class Code</em>' attribute is set.
	 * @see #unsetClassCode()
	 * @see #getClassCode()
	 * @see #setClassCode(Enumerator)
	 * @generated
	 */
    boolean isSetClassCode();

    /**
	 * Returns the value of the '<em><b>Mood Code</b></em>' attribute.
	 * The default value is <code>"EVN"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mood Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mood Code</em>' attribute.
	 * @see #isSetMoodCode()
	 * @see #unsetMoodCode()
	 * @see #setMoodCode(Enumerator)
	 * @see org.hl7.v3.V3Package#getPOCDMT000040ExternalProcedure_MoodCode()
	 * @model default="EVN" unsettable="true" dataType="org.hl7.v3.ActMood"
	 *        extendedMetaData="kind='attribute' name='moodCode'"
	 * @generated
	 */
    Enumerator getMoodCode();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040ExternalProcedure#getMoodCode <em>Mood Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mood Code</em>' attribute.
	 * @see #isSetMoodCode()
	 * @see #unsetMoodCode()
	 * @see #getMoodCode()
	 * @generated
	 */
    void setMoodCode(Enumerator value);

    /**
	 * Unsets the value of the '{@link org.hl7.v3.POCDMT000040ExternalProcedure#getMoodCode <em>Mood Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMoodCode()
	 * @see #getMoodCode()
	 * @see #setMoodCode(Enumerator)
	 * @generated
	 */
    void unsetMoodCode();

    /**
	 * Returns whether the value of the '{@link org.hl7.v3.POCDMT000040ExternalProcedure#getMoodCode <em>Mood Code</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Mood Code</em>' attribute is set.
	 * @see #unsetMoodCode()
	 * @see #getMoodCode()
	 * @see #setMoodCode(Enumerator)
	 * @generated
	 */
    boolean isSetMoodCode();

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
	 * @see org.hl7.v3.V3Package#getPOCDMT000040ExternalProcedure_NullFlavor()
	 * @model dataType="org.hl7.v3.NullFlavor"
	 *        extendedMetaData="kind='attribute' name='nullFlavor'"
	 * @generated
	 */
    Enumerator getNullFlavor();

    /**
	 * Sets the value of the '{@link org.hl7.v3.POCDMT000040ExternalProcedure#getNullFlavor <em>Null Flavor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Null Flavor</em>' attribute.
	 * @see #getNullFlavor()
	 * @generated
	 */
    void setNullFlavor(Enumerator value);
}
