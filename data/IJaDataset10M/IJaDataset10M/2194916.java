package edu.asu.vogon.digitalHPS;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ITerm Parts</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link edu.asu.vogon.digitalHPS.ITermParts#getReferencedSource <em>Referenced Source</em>}</li>
 *   <li>{@link edu.asu.vogon.digitalHPS.ITermParts#getTermParts <em>Term Parts</em>}</li>
 * </ul>
 * </p>
 *
 * @see edu.asu.vogon.digitalHPS.DigitalHPSPackage#getITermParts()
 * @model
 * @generated
 */
public interface ITermParts extends IElement {

    /**
	 * Returns the value of the '<em><b>Referenced Source</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Referenced Source</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Referenced Source</em>' containment reference.
	 * @see #setReferencedSource(ISourceReference)
	 * @see edu.asu.vogon.digitalHPS.DigitalHPSPackage#getITermParts_ReferencedSource()
	 * @model containment="true"
	 * @generated
	 */
    ISourceReference getReferencedSource();

    /**
	 * Sets the value of the '{@link edu.asu.vogon.digitalHPS.ITermParts#getReferencedSource <em>Referenced Source</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referenced Source</em>' containment reference.
	 * @see #getReferencedSource()
	 * @generated
	 */
    void setReferencedSource(ISourceReference value);

    /**
	 * Returns the value of the '<em><b>Term Parts</b></em>' containment reference list.
	 * The list contents are of type {@link edu.asu.vogon.digitalHPS.ITermPart}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Term Parts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Term Parts</em>' containment reference list.
	 * @see edu.asu.vogon.digitalHPS.DigitalHPSPackage#getITermParts_TermParts()
	 * @model containment="true"
	 * @generated
	 */
    EList<ITermPart> getTermParts();
}
