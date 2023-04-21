package eu.medeia.caex.model.CAEXClassModelV215;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.medeia.caex.model.CAEXClassModelV215.DescriptionType#getValue <em>Value</em>}</li>
 *   <li>{@link eu.medeia.caex.model.CAEXClassModelV215.DescriptionType#getChangeMode <em>Change Mode</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.medeia.caex.model.CAEXClassModelV215.CAEXClassModelV215Package#getDescriptionType()
 * @model extendedMetaData="name='Description_._type' kind='simple'"
 * @generated
 */
public interface DescriptionType extends EObject {

    /**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see eu.medeia.caex.model.CAEXClassModelV215.CAEXClassModelV215Package#getDescriptionType_Value()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="name=':0' kind='simple'"
	 * @generated
	 */
    String getValue();

    /**
	 * Sets the value of the '{@link eu.medeia.caex.model.CAEXClassModelV215.DescriptionType#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
    void setValue(String value);

    /**
	 * Returns the value of the '<em><b>Change Mode</b></em>' attribute.
	 * The default value is <code>"state"</code>.
	 * The literals are from the enumeration {@link eu.medeia.caex.model.CAEXClassModelV215.ChangeMode}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Change Mode</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Change Mode</em>' attribute.
	 * @see eu.medeia.caex.model.CAEXClassModelV215.ChangeMode
	 * @see #isSetChangeMode()
	 * @see #unsetChangeMode()
	 * @see #setChangeMode(ChangeMode)
	 * @see eu.medeia.caex.model.CAEXClassModelV215.CAEXClassModelV215Package#getDescriptionType_ChangeMode()
	 * @model default="state" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='ChangeMode' namespace='##targetNamespace'"
	 * @generated
	 */
    ChangeMode getChangeMode();

    /**
	 * Sets the value of the '{@link eu.medeia.caex.model.CAEXClassModelV215.DescriptionType#getChangeMode <em>Change Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Change Mode</em>' attribute.
	 * @see eu.medeia.caex.model.CAEXClassModelV215.ChangeMode
	 * @see #isSetChangeMode()
	 * @see #unsetChangeMode()
	 * @see #getChangeMode()
	 * @generated
	 */
    void setChangeMode(ChangeMode value);

    /**
	 * Unsets the value of the '{@link eu.medeia.caex.model.CAEXClassModelV215.DescriptionType#getChangeMode <em>Change Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetChangeMode()
	 * @see #getChangeMode()
	 * @see #setChangeMode(ChangeMode)
	 * @generated
	 */
    void unsetChangeMode();

    /**
	 * Returns whether the value of the '{@link eu.medeia.caex.model.CAEXClassModelV215.DescriptionType#getChangeMode <em>Change Mode</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Change Mode</em>' attribute is set.
	 * @see #unsetChangeMode()
	 * @see #getChangeMode()
	 * @see #setChangeMode(ChangeMode)
	 * @generated
	 */
    boolean isSetChangeMode();
}
