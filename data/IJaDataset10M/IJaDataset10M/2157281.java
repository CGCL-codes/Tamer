package org.larz.dom3.dm.dm;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Site Inst1</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.larz.dom3.dm.dm.SiteInst1#getValue <em>Value</em>}</li>
 *   <li>{@link org.larz.dom3.dm.dm.SiteInst1#isName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.larz.dom3.dm.dm.DmPackage#getSiteInst1()
 * @model
 * @generated
 */
public interface SiteInst1 extends SitePattern1 {

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
   * @see org.larz.dom3.dm.dm.DmPackage#getSiteInst1_Value()
   * @model
   * @generated
   */
    String getValue();

    /**
   * Sets the value of the '{@link org.larz.dom3.dm.dm.SiteInst1#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
    void setValue(String value);

    /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(boolean)
   * @see org.larz.dom3.dm.dm.DmPackage#getSiteInst1_Name()
   * @model
   * @generated
   */
    boolean isName();

    /**
   * Sets the value of the '{@link org.larz.dom3.dm.dm.SiteInst1#isName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #isName()
   * @generated
   */
    void setName(boolean value);
}
