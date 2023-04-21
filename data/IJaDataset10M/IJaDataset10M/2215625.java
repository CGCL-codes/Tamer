package eu.medeia.ecore.bm;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>INamed Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link eu.medeia.ecore.bm.INamedElement#getID <em>ID</em>}</li>
 *   <li>{@link eu.medeia.ecore.bm.INamedElement#getName <em>Name</em>}</li>
 *   <li>{@link eu.medeia.ecore.bm.INamedElement#getComment <em>Comment</em>}</li>
 *   <li>{@link eu.medeia.ecore.bm.INamedElement#getAdditionalInformation <em>Additional Information</em>}</li>
 * </ul>
 * </p>
 *
 * @see eu.medeia.ecore.bm.BmPackage#getINamedElement()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface INamedElement extends EObject {

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
	 * @see eu.medeia.ecore.bm.BmPackage#getINamedElement_ID()
	 * @model
	 * @generated
	 */
    String getID();

    /**
	 * Sets the value of the '{@link eu.medeia.ecore.bm.INamedElement#getID <em>ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>ID</em>' attribute.
	 * @see #getID()
	 * @generated
	 */
    void setID(String value);

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
	 * @see eu.medeia.ecore.bm.BmPackage#getINamedElement_Name()
	 * @model
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link eu.medeia.ecore.bm.INamedElement#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Comment</em>' attribute.
	 * @see #setComment(String)
	 * @see eu.medeia.ecore.bm.BmPackage#getINamedElement_Comment()
	 * @model
	 * @generated
	 */
    String getComment();

    /**
	 * Sets the value of the '{@link eu.medeia.ecore.bm.INamedElement#getComment <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Comment</em>' attribute.
	 * @see #getComment()
	 * @generated
	 */
    void setComment(String value);

    /**
	 * Returns the value of the '<em><b>Additional Information</b></em>' containment reference list.
	 * The list contents are of type {@link eu.medeia.ecore.bm.AdditionalModelInformation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Additional Information</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Additional Information</em>' containment reference list.
	 * @see eu.medeia.ecore.bm.BmPackage#getINamedElement_AdditionalInformation()
	 * @model containment="true"
	 * @generated
	 */
    EList<AdditionalModelInformation> getAdditionalInformation();
}
