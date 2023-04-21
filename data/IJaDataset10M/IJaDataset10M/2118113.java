package org.hl7.v3;

import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Struc Doc Thead</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.hl7.v3.StrucDocThead#getGroup <em>Group</em>}</li>
 *   <li>{@link org.hl7.v3.StrucDocThead#getTr <em>Tr</em>}</li>
 *   <li>{@link org.hl7.v3.StrucDocThead#getAlign <em>Align</em>}</li>
 *   <li>{@link org.hl7.v3.StrucDocThead#getChar <em>Char</em>}</li>
 *   <li>{@link org.hl7.v3.StrucDocThead#getCharoff <em>Charoff</em>}</li>
 *   <li>{@link org.hl7.v3.StrucDocThead#getID <em>ID</em>}</li>
 *   <li>{@link org.hl7.v3.StrucDocThead#getLanguage <em>Language</em>}</li>
 *   <li>{@link org.hl7.v3.StrucDocThead#getStyleCode <em>Style Code</em>}</li>
 *   <li>{@link org.hl7.v3.StrucDocThead#getValign <em>Valign</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.hl7.v3.V3Package#getStrucDocThead()
 * @model extendedMetaData="name='StrucDoc.Thead' kind='elementOnly'"
 * @generated
 */
public interface StrucDocThead extends EObject {

    /**
	 * Returns the value of the '<em><b>Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' attribute list.
	 * @see org.hl7.v3.V3Package#getStrucDocThead_Group()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:0'"
	 * @generated
	 */
    FeatureMap getGroup();

    /**
	 * Returns the value of the '<em><b>Tr</b></em>' containment reference list.
	 * The list contents are of type {@link org.hl7.v3.StrucDocTr}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tr</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tr</em>' containment reference list.
	 * @see org.hl7.v3.V3Package#getStrucDocThead_Tr()
	 * @model containment="true" required="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='tr' namespace='##targetNamespace' group='#group:0'"
	 * @generated
	 */
    EList<StrucDocTr> getTr();

    /**
	 * Returns the value of the '<em><b>Align</b></em>' attribute.
	 * The default value is <code>"left"</code>.
	 * The literals are from the enumeration {@link org.hl7.v3.AlignType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Align</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Align</em>' attribute.
	 * @see org.hl7.v3.AlignType
	 * @see #isSetAlign()
	 * @see #unsetAlign()
	 * @see #setAlign(AlignType)
	 * @see org.hl7.v3.V3Package#getStrucDocThead_Align()
	 * @model default="left" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='align'"
	 * @generated
	 */
    AlignType getAlign();

    /**
	 * Sets the value of the '{@link org.hl7.v3.StrucDocThead#getAlign <em>Align</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Align</em>' attribute.
	 * @see org.hl7.v3.AlignType
	 * @see #isSetAlign()
	 * @see #unsetAlign()
	 * @see #getAlign()
	 * @generated
	 */
    void setAlign(AlignType value);

    /**
	 * Unsets the value of the '{@link org.hl7.v3.StrucDocThead#getAlign <em>Align</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAlign()
	 * @see #getAlign()
	 * @see #setAlign(AlignType)
	 * @generated
	 */
    void unsetAlign();

    /**
	 * Returns whether the value of the '{@link org.hl7.v3.StrucDocThead#getAlign <em>Align</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Align</em>' attribute is set.
	 * @see #unsetAlign()
	 * @see #getAlign()
	 * @see #setAlign(AlignType)
	 * @generated
	 */
    boolean isSetAlign();

    /**
	 * Returns the value of the '<em><b>Char</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Char</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Char</em>' attribute.
	 * @see #setChar(String)
	 * @see org.hl7.v3.V3Package#getStrucDocThead_Char()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='char'"
	 * @generated
	 */
    String getChar();

    /**
	 * Sets the value of the '{@link org.hl7.v3.StrucDocThead#getChar <em>Char</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Char</em>' attribute.
	 * @see #getChar()
	 * @generated
	 */
    void setChar(String value);

    /**
	 * Returns the value of the '<em><b>Charoff</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charoff</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charoff</em>' attribute.
	 * @see #setCharoff(String)
	 * @see org.hl7.v3.V3Package#getStrucDocThead_Charoff()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='charoff'"
	 * @generated
	 */
    String getCharoff();

    /**
	 * Sets the value of the '{@link org.hl7.v3.StrucDocThead#getCharoff <em>Charoff</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charoff</em>' attribute.
	 * @see #getCharoff()
	 * @generated
	 */
    void setCharoff(String value);

    /**
	 * Returns the value of the '<em><b>ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>ID</em>' attribute.
	 * @see #setID(String)
	 * @see org.hl7.v3.V3Package#getStrucDocThead_ID()
	 * @model id="true" dataType="org.eclipse.emf.ecore.xml.type.ID"
	 *        extendedMetaData="kind='attribute' name='ID'"
	 * @generated
	 */
    String getID();

    /**
	 * Sets the value of the '{@link org.hl7.v3.StrucDocThead#getID <em>ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>ID</em>' attribute.
	 * @see #getID()
	 * @generated
	 */
    void setID(String value);

    /**
	 * Returns the value of the '<em><b>Language</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Language</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Language</em>' attribute.
	 * @see #setLanguage(String)
	 * @see org.hl7.v3.V3Package#getStrucDocThead_Language()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.NMTOKEN"
	 *        extendedMetaData="kind='attribute' name='language'"
	 * @generated
	 */
    String getLanguage();

    /**
	 * Sets the value of the '{@link org.hl7.v3.StrucDocThead#getLanguage <em>Language</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Language</em>' attribute.
	 * @see #getLanguage()
	 * @generated
	 */
    void setLanguage(String value);

    /**
	 * Returns the value of the '<em><b>Style Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Style Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Style Code</em>' attribute.
	 * @see #setStyleCode(List)
	 * @see org.hl7.v3.V3Package#getStrucDocThead_StyleCode()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.NMTOKENS" many="false"
	 *        extendedMetaData="kind='attribute' name='styleCode'"
	 * @generated
	 */
    List<String> getStyleCode();

    /**
	 * Sets the value of the '{@link org.hl7.v3.StrucDocThead#getStyleCode <em>Style Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Style Code</em>' attribute.
	 * @see #getStyleCode()
	 * @generated
	 */
    void setStyleCode(List<String> value);

    /**
	 * Returns the value of the '<em><b>Valign</b></em>' attribute.
	 * The default value is <code>"top"</code>.
	 * The literals are from the enumeration {@link org.hl7.v3.ValignType1}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Valign</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Valign</em>' attribute.
	 * @see org.hl7.v3.ValignType1
	 * @see #isSetValign()
	 * @see #unsetValign()
	 * @see #setValign(ValignType1)
	 * @see org.hl7.v3.V3Package#getStrucDocThead_Valign()
	 * @model default="top" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='valign'"
	 * @generated
	 */
    ValignType1 getValign();

    /**
	 * Sets the value of the '{@link org.hl7.v3.StrucDocThead#getValign <em>Valign</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Valign</em>' attribute.
	 * @see org.hl7.v3.ValignType1
	 * @see #isSetValign()
	 * @see #unsetValign()
	 * @see #getValign()
	 * @generated
	 */
    void setValign(ValignType1 value);

    /**
	 * Unsets the value of the '{@link org.hl7.v3.StrucDocThead#getValign <em>Valign</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetValign()
	 * @see #getValign()
	 * @see #setValign(ValignType1)
	 * @generated
	 */
    void unsetValign();

    /**
	 * Returns whether the value of the '{@link org.hl7.v3.StrucDocThead#getValign <em>Valign</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Valign</em>' attribute is set.
	 * @see #unsetValign()
	 * @see #getValign()
	 * @see #setValign(ValignType1)
	 * @generated
	 */
    boolean isSetValign();
}
