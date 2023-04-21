package siseor;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Metadata</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link siseor.Metadata#getOperation <em>Operation</em>}</li>
 *   <li>{@link siseor.Metadata#getContent <em>Content</em>}</li>
 * </ul>
 * </p>
 *
 * @see siseor.SiseorPackage#getMetadata()
 * @model abstract="true"
 * @generated
 */
public interface Metadata extends EObject {

    /**
	 * Returns the value of the '<em><b>Operation</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link siseor.Operation#getMetadata <em>Metadata</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operation</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operation</em>' container reference.
	 * @see #setOperation(Operation)
	 * @see siseor.SiseorPackage#getMetadata_Operation()
	 * @see siseor.Operation#getMetadata
	 * @model opposite="metadata" resolveProxies="false" required="true" transient="false"
	 * @generated
	 */
    Operation getOperation();

    /**
	 * Sets the value of the '{@link siseor.Metadata#getOperation <em>Operation</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operation</em>' container reference.
	 * @see #getOperation()
	 * @generated
	 */
    void setOperation(Operation value);

    /**
	 * Returns the value of the '<em><b>Content</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Content</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Content</em>' attribute.
	 * @see #setContent(String)
	 * @see siseor.SiseorPackage#getMetadata_Content()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
    String getContent();

    /**
	 * Sets the value of the '{@link siseor.Metadata#getContent <em>Content</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Content</em>' attribute.
	 * @see #getContent()
	 * @generated
	 */
    void setContent(String value);
}
