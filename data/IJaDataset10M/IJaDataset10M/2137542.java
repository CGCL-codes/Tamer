package org.hl7.v3;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>URL1</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             A telecommunications address  specified according to
 *             Internet standard RFC 1738
 *             [http://www.ietf.org/rfc/rfc1738.txt]. The
 *             URL specifies the protocol and the contact point defined
 *             by that protocol for the resource.  Notable uses of the
 *             telecommunication address data type are for telephone and
 *             telefax numbers, e-mail addresses, Hypertext references,
 *             FTP references, etc.
 *          
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.hl7.v3.URL1#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.hl7.v3.V3Package#getURL1()
 * @model abstract="true"
 *        extendedMetaData="name='URL' kind='empty'"
 * @generated
 */
public interface URL1 extends ANY {

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
	 * @see org.hl7.v3.V3Package#getURL1_Value()
	 * @model dataType="org.hl7.v3.Url"
	 *        extendedMetaData="kind='attribute' name='value'"
	 * @generated
	 */
    String getValue();

    /**
	 * Sets the value of the '{@link org.hl7.v3.URL1#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
    void setValue(String value);
}
