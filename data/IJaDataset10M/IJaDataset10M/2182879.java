package jp.ekasi.pms.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link jp.ekasi.pms.model.Resource#getUid <em>Uid</em>}</li>
 *   <li>{@link jp.ekasi.pms.model.Resource#getName <em>Name</em>}</li>
 *   <li>{@link jp.ekasi.pms.model.Resource#getNotes <em>Notes</em>}</li>
 * </ul>
 * </p>
 *
 * @see jp.ekasi.pms.model.ModelPackage#getResource()
 * @model
 * @generated
 */
public interface Resource extends EObject {

    /**
	 * Returns the value of the '<em><b>Uid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uid</em>' attribute.
	 * @see #setUid(int)
	 * @see jp.ekasi.pms.model.ModelPackage#getResource_Uid()
	 * @model id="true" required="true"
	 * @generated
	 */
    int getUid();

    /**
	 * Sets the value of the '{@link jp.ekasi.pms.model.Resource#getUid <em>Uid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uid</em>' attribute.
	 * @see #getUid()
	 * @generated
	 */
    void setUid(int value);

    /**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see jp.ekasi.pms.model.ModelPackage#getResource_Name()
	 * @model
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link jp.ekasi.pms.model.Resource#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notes</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notes</em>' attribute.
	 * @see #setNotes(String)
	 * @see jp.ekasi.pms.model.ModelPackage#getResource_Notes()
	 * @model
	 * @generated
	 */
    String getNotes();

    /**
	 * Sets the value of the '{@link jp.ekasi.pms.model.Resource#getNotes <em>Notes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notes</em>' attribute.
	 * @see #getNotes()
	 * @generated
	 */
    void setNotes(String value);
}
