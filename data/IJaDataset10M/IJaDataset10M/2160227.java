package tefkat.model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Condition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tefkat.model.Condition#getArg <em>Arg</em>}</li>
 *   <li>{@link tefkat.model.Condition#getRelation <em>Relation</em>}</li>
 * </ul>
 * </p>
 *
 * @see tefkat.model.TefkatPackage#getCondition()
 * @model
 * @generated
 */
public interface Condition extends SimpleTerm {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String copyright = "Copyright michael lawley Pty Ltd 2003-2007";

    /**
     * Returns the value of the '<em><b>Arg</b></em>' containment reference list.
     * The list contents are of type {@link tefkat.model.Expression}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Arg</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Arg</em>' containment reference list.
     * @see tefkat.model.TefkatPackage#getCondition_Arg()
     * @model type="tefkat.model.Expression" containment="true"
     * @generated
     */
    EList getArg();

    /**
     * Returns the value of the '<em><b>Relation</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Relation</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Relation</em>' attribute.
     * @see #setRelation(String)
     * @see tefkat.model.TefkatPackage#getCondition_Relation()
     * @model
     * @generated
     */
    String getRelation();

    /**
     * Sets the value of the '{@link tefkat.model.Condition#getRelation <em>Relation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Relation</em>' attribute.
     * @see #getRelation()
     * @generated
     */
    void setRelation(String value);
}
