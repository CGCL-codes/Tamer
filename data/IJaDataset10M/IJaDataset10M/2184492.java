package org.xtext.example.swrtj;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Trait Name</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.xtext.example.swrtj.TraitName#getTrait <em>Trait</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.xtext.example.swrtj.SwrtjPackage#getTraitName()
 * @model
 * @generated
 */
public interface TraitName extends BaseTrait {

    /**
   * Returns the value of the '<em><b>Trait</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Trait</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trait</em>' reference.
   * @see #setTrait(Trait)
   * @see org.xtext.example.swrtj.SwrtjPackage#getTraitName_Trait()
   * @model
   * @generated
   */
    Trait getTrait();

    /**
   * Sets the value of the '{@link org.xtext.example.swrtj.TraitName#getTrait <em>Trait</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trait</em>' reference.
   * @see #getTrait()
   * @generated
   */
    void setTrait(Trait value);
}
