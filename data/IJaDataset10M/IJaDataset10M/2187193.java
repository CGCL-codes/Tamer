package SBVR;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Body Of Shared Meanings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link SBVR.BodyOfSharedMeanings#getConcept <em>Concept</em>}</li>
 *   <li>{@link SBVR.BodyOfSharedMeanings#getProposition <em>Proposition</em>}</li>
 * </ul>
 * </p>
 *
 * @see SBVR.SBVRPackage#getBodyOfSharedMeanings()
 * @model
 * @generated
 */
public interface BodyOfSharedMeanings extends Set {

    /**
	 * Returns the value of the '<em><b>Concept</b></em>' containment reference list.
	 * The list contents are of type {@link SBVR.Concept}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Concept</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Concept</em>' containment reference list.
	 * @see SBVR.SBVRPackage#getBodyOfSharedMeanings_Concept()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
    EList<Concept> getConcept();

    /**
	 * Returns the value of the '<em><b>Proposition</b></em>' containment reference list.
	 * The list contents are of type {@link SBVR.Proposition}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Proposition</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Proposition</em>' containment reference list.
	 * @see SBVR.SBVRPackage#getBodyOfSharedMeanings_Proposition()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
    EList<Proposition> getProposition();
}
