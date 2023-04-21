package hub.sam.lang.vcl;

import hub.metrik.lang.eprovide.debuggingstate.MVariable;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Variable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hub.sam.lang.vcl.Variable#getName <em>Name</em>}</li>
 *   <li>{@link hub.sam.lang.vcl.Variable#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see hub.sam.lang.vcl.VclPackage#getVariable()
 * @model
 * @generated
 */
public interface Variable extends MVariable {

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
     * @see hub.sam.lang.vcl.VclPackage#getVariable_Name()
     * @model
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link hub.sam.lang.vcl.Variable#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #isSetValue()
     * @see #unsetValue()
     * @see #setValue(Integer)
     * @see hub.sam.lang.vcl.VclPackage#getVariable_Value()
     * @model unsettable="true"
     * @generated
     */
    Integer getValue();

    /**
     * Sets the value of the '{@link hub.sam.lang.vcl.Variable#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #isSetValue()
     * @see #unsetValue()
     * @see #getValue()
     * @generated
     */
    void setValue(Integer value);

    /**
     * Unsets the value of the '{@link hub.sam.lang.vcl.Variable#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetValue()
     * @see #getValue()
     * @see #setValue(Integer)
     * @generated
     */
    void unsetValue();

    /**
     * Returns whether the value of the '{@link hub.sam.lang.vcl.Variable#getValue <em>Value</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Value</em>' attribute is set.
     * @see #unsetValue()
     * @see #getValue()
     * @see #setValue(Integer)
     * @generated
     */
    boolean isSetValue();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model
     * @generated
     */
    String asString();
}
