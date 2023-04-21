package tefkat.model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Compound Term</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tefkat.model.CompoundTerm#getTerm <em>Term</em>}</li>
 * </ul>
 * </p>
 *
 * @see tefkat.model.TefkatPackage#getCompoundTerm()
 * @model abstract="true"
 * @generated
 */
public interface CompoundTerm extends SourceTerm {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String copyright = "Copyright michael lawley Pty Ltd 2003-2007";

    /**
     * Returns the value of the '<em><b>Term</b></em>' containment reference list.
     * The list contents are of type {@link tefkat.model.Term}.
     * It is bidirectional and its opposite is '{@link tefkat.model.Term#getCompoundTerm <em>Compound Term</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Term</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Term</em>' containment reference list.
     * @see tefkat.model.TefkatPackage#getCompoundTerm_Term()
     * @see tefkat.model.Term#getCompoundTerm
     * @model type="tefkat.model.Term" opposite="compoundTerm" containment="true"
     * @generated
     */
    EList getTerm();
}
