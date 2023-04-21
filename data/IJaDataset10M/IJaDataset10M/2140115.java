package de.hackerdan.projectcreator.model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Token</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.hackerdan.projectcreator.model.Token#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.hackerdan.projectcreator.model.ProjectCreatorPackage#getToken()
 * @model annotation="http://www.eclipse.org/emf/2002/Ecore constraints='CheckReservedTokens'"
 * @generated
 */
public interface Token extends Named {

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
    * @see de.hackerdan.projectcreator.model.ProjectCreatorPackage#getToken_Value()
    * @model required="true"
    * @generated
    */
    String getValue();

    /**
    * Sets the value of the '{@link de.hackerdan.projectcreator.model.Token#getValue <em>Value</em>}' attribute.
    * <!-- begin-user-doc -->
    * <!-- end-user-doc -->
    * @param value the new value of the '<em>Value</em>' attribute.
    * @see #getValue()
    * @generated
    */
    void setValue(String value);
}
