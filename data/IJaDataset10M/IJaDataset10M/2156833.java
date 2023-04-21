package Slee11;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sbb Usage Parameters Interface Name Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link Slee11.SbbUsageParametersInterfaceNameType#getValue <em>Value</em>}</li>
 *   <li>{@link Slee11.SbbUsageParametersInterfaceNameType#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see Slee11.Slee11Package#getSbbUsageParametersInterfaceNameType()
 * @model extendedMetaData="name='sbb-usage-parameters-interface-name_._type' kind='simple'"
 * @generated
 */
public interface SbbUsageParametersInterfaceNameType extends EObject {

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
	 * @see Slee11.Slee11Package#getSbbUsageParametersInterfaceNameType_Value()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="name=':0' kind='simple'"
	 * @generated
	 */
    String getValue();

    /**
	 * Sets the value of the '{@link Slee11.SbbUsageParametersInterfaceNameType#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
    void setValue(String value);

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
	 * @see Slee11.Slee11Package#getSbbUsageParametersInterfaceNameType_Id()
	 * @model id="true" dataType="org.eclipse.emf.ecore.xml.type.ID"
	 *        extendedMetaData="kind='attribute' name='id' namespace='##targetNamespace'"
	 * @generated
	 */
    String getId();

    /**
	 * Sets the value of the '{@link Slee11.SbbUsageParametersInterfaceNameType#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
    void setId(String value);
}
