package org.eclipse.epf.uma._1._0;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Content Description</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A generalized Method Element that is used to store the textual description for a Content Element.  It defines standard attributes applicable for all Content Element types.  Specific Content Element sub-types can define their own matching Content Description sub-types. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.epf.uma._1._0.ContentDescription#getMainDescription <em>Main Description</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.ContentDescription#getKeyConsiderations <em>Key Considerations</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.ContentDescription#getSection <em>Section</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.ContentDescription#getExternalId <em>External Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.epf.uma._1._0._0Package#getContentDescription()
 * @model extendedMetaData="name='ContentDescription' kind='elementOnly'"
 * @generated
 */
public interface ContentDescription extends MethodUnit {

    /**
	 * Returns the value of the '<em><b>Main Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Stores the main descriptive text for the Content Element.  All text that is not part of any of the more specific attributes shall be stored here.  If the Content Description is divided into sections using the Section class, then only the text from the 'start' of the content description to the first section will be stored here (similar to a normal document where you can place text between its beginning to its first diction heading).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Main Description</em>' attribute.
	 * @see #setMainDescription(String)
	 * @see org.eclipse.epf.uma._1._0._0Package#getContentDescription_MainDescription()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='MainDescription'"
	 * @generated
	 */
    String getMainDescription();

    /**
	 * Sets the value of the '{@link org.eclipse.epf.uma._1._0.ContentDescription#getMainDescription <em>Main Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Main Description</em>' attribute.
	 * @see #getMainDescription()
	 * @generated
	 */
    void setMainDescription(String value);

    /**
	 * Returns the value of the '<em><b>Key Considerations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Provides advise and guidance of a critical nature for the content element as well as warnings, cautions, pitfalls, dangers.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Key Considerations</em>' attribute.
	 * @see #setKeyConsiderations(String)
	 * @see org.eclipse.epf.uma._1._0._0Package#getContentDescription_KeyConsiderations()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='KeyConsiderations'"
	 * @generated
	 */
    String getKeyConsiderations();

    /**
	 * Sets the value of the '{@link org.eclipse.epf.uma._1._0.ContentDescription#getKeyConsiderations <em>Key Considerations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key Considerations</em>' attribute.
	 * @see #getKeyConsiderations()
	 * @generated
	 */
    void setKeyConsiderations(String value);

    /**
	 * Returns the value of the '<em><b>Section</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.epf.uma._1._0.Section}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Section</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Section</em>' containment reference list.
	 * @see org.eclipse.epf.uma._1._0._0Package#getContentDescription_Section()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Section'"
	 * @generated
	 */
    EList<Section> getSection();

    /**
	 * Returns the value of the '<em><b>External Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * An external visible number that is used to reference this artifact. Used like a synonym.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>External Id</em>' attribute.
	 * @see #setExternalId(String)
	 * @see org.eclipse.epf.uma._1._0._0Package#getContentDescription_ExternalId()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='externalId'"
	 * @generated
	 */
    String getExternalId();

    /**
	 * Sets the value of the '{@link org.eclipse.epf.uma._1._0.ContentDescription#getExternalId <em>External Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>External Id</em>' attribute.
	 * @see #getExternalId()
	 * @generated
	 */
    void setExternalId(String value);
}
