package org.xtext.cg2009.entities;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reference Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.xtext.cg2009.entities.ReferenceProperty#isMany <em>Many</em>}</li>
 *   <li>{@link org.xtext.cg2009.entities.ReferenceProperty#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.xtext.cg2009.entities.EntitiesPackage#getReferenceProperty()
 * @model
 * @generated
 */
public interface ReferenceProperty extends Property {

    /**
   * Returns the value of the '<em><b>Many</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Many</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Many</em>' attribute.
   * @see #setMany(boolean)
   * @see org.xtext.cg2009.entities.EntitiesPackage#getReferenceProperty_Many()
   * @model
   * @generated
   */
    boolean isMany();

    /**
   * Sets the value of the '{@link org.xtext.cg2009.entities.ReferenceProperty#isMany <em>Many</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Many</em>' attribute.
   * @see #isMany()
   * @generated
   */
    void setMany(boolean value);

    /**
   * Returns the value of the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' reference.
   * @see #setType(Entity)
   * @see org.xtext.cg2009.entities.EntitiesPackage#getReferenceProperty_Type()
   * @model
   * @generated
   */
    Entity getType();

    /**
   * Sets the value of the '{@link org.xtext.cg2009.entities.ReferenceProperty#getType <em>Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' reference.
   * @see #getType()
   * @generated
   */
    void setType(Entity value);
}
